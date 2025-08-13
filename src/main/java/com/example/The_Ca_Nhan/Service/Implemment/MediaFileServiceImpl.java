package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.MediaFilesMapper;
import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import com.example.The_Ca_Nhan.Repository.MediaFileRepository;
import com.example.The_Ca_Nhan.Service.Interface.MediaFileInterface;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import com.example.The_Ca_Nhan.Util.Extract;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Profile("!test")
@Slf4j
public class MediaFileServiceImpl implements MediaFileInterface {
    private final MediaFileRepository mediaFileRepository;
    private final MediaFilesMapper mediaFilesMapper;
    private final S3Interface s3Interface;
    private final Extract extract;

    private MediaFiles getMediaFileById(int id) {
        return mediaFileRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.MEDIA_NOT_FOUND));
    }

    private void checkMediaFileAuthenticated(Users users, MediaFiles mediaFiles) {
        if (!mediaFiles.getUsers().getUserName().equals(users.getUserName())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private String uploadImageAndGetUrl(MultipartFile file) {
        return s3Interface.uploadFile(file);
    }

    private void deleteImage(String url) {
        s3Interface.deleteImage(url);
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public MediaFileResponse insertMedia(MediaFileCreateRequest request) {

        Users users = extract.getUserInFlowLogin();
        MediaFiles mediaFiles = mediaFilesMapper.toEntity(request);
        String url = uploadImageAndGetUrl(request.getImageUrl());
        mediaFiles.setLink(url);
        mediaFiles.setEntityId(request.getEntityId());
        mediaFiles.setUsers(users);

        MediaFiles mediaFilesInserted = mediaFileRepository.save(mediaFiles);
        return mediaFilesMapper.toDTO(mediaFilesInserted);


    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public MediaFileResponse updateMedia(MediaFileUpdateRequest request, int Id) {
        // lay user trong phien dang nhap
        Users users = extract.getUserInFlowLogin();
        // kiem tra mediaFile co ton tai hay khong
        MediaFiles mediaFiles = getMediaFileById(Id);
        // kiem tra media co thuoc nguoi dung khong
        checkMediaFileAuthenticated(users, mediaFiles);
        // kiem tra media co lien ket dung entity khong
        if (!mediaFiles.getEntityType().equals(request.getEntityType()) || mediaFiles.getEntityId() != request.getEntityId()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        // tien hanh cap nhat
        mediaFiles.setFileName(request.getFileName());
        mediaFiles.setUpdateDate(LocalDateTime.now());
        // xoa media
        deleteImage(mediaFiles.getLink());
        String newUrl = uploadImageAndGetUrl(request.getImageUrl());
        mediaFiles.setLink(newUrl);

        return mediaFilesMapper.toDTO(mediaFileRepository.save(mediaFiles));


    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void deleteMedia(int id) {
        MediaFiles mediaFiles = mediaFileRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MEDIA_NOT_FOUND));
        Users users = extract.getUserInFlowLogin();
        checkMediaFileAuthenticated(users, mediaFiles);
        deleteImage(mediaFiles.getLink());
        mediaFileRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<MediaFileResponse> findAll() {
        return mediaFileRepository.findAll().stream().map(mediaFilesMapper::toDTO).toList();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public MediaFileResponse findById(int id) {
        MediaFiles mediaFiles = mediaFileRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MEDIA_NOT_FOUND));
        Users users = extract.getUserInFlowLogin();
        checkMediaFileAuthenticated(users, mediaFiles);
        return mediaFilesMapper.toDTO(mediaFiles);
    }

    @Override
    public List<MediaFileResponse> findByEntity(MediaEntityType type, int entityId) {
        return mediaFileRepository.findByEntityTypeAndEntityId(type, entityId).stream()
                .map(mediaFilesMapper::toDTO)
                .toList();
    }

}
