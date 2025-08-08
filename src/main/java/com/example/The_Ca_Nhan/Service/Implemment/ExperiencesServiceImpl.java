package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.Request.ExperiencesRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Response.ExperiencesResponse;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.Entity.Educations;
import com.example.The_Ca_Nhan.Entity.Experiences;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.ExperiencesMapper;
import com.example.The_Ca_Nhan.Mapper.MediaFilesMapper;
import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import com.example.The_Ca_Nhan.Repository.ExperiencesRepository;
import com.example.The_Ca_Nhan.Repository.MediaFileRepository;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import com.example.The_Ca_Nhan.Service.Interface.ExperiencesInterface;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import com.example.The_Ca_Nhan.Util.Extract;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExperiencesServiceImpl implements ExperiencesInterface {

    private final ExperiencesMapper experiencesMapper ;
    private final ExperiencesRepository experiencesRepository ;
    private final S3Interface s3Interface ;
    private final MediaFilesMapper mediaFilesMapper ;
    private final MediaFileRepository mediaFileRepository ;
    private final Extract extract ;
    private final UsersRepository usersRepository ;


    private Experiences getExperiencesById(int id ) {
        return experiencesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXPERIENCES_NOT_FOUND));
    }

    private void checkAuthenticated (Users users , Experiences experiences) {
        if(!(experiences.getUsers().getUserName().equals(users.getUserName()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED) ;
        }
    }


    private Users getUserById(String id) {
        Users users = usersRepository.findByKeycloakId(id) ;
        if( users == null) {
            throw new AppException((ErrorCode.USER_NOT_EXISTED)) ;
        }
        return users ;
    }


    private boolean checkListMediaIsNull(List<MediaFileCreateRequest> requests) {
        return requests == null || requests.isEmpty();
    }

    private String uploadImageAndGetUrl (MultipartFile file) {
        return s3Interface.uploadFile(file);
    }

    private void deleteImage (String url) {
        s3Interface.deleteImage(url);
    }

    private MediaFiles createAndSaveMedia(MediaFileCreateRequest request, Users user, int entityId) {
        String url = uploadImageAndGetUrl(request.getImageUrl());
        MediaFiles media = mediaFilesMapper.toEntity(request);
        media.setUsers(user);
        media.setEntityId(entityId);
        media.setLink(url);
        return mediaFileRepository.save(media);
    }


    @Override
    @PreAuthorize( "hasRole('USER')")
    public ExperiencesResponse insertExp(ExperiencesRequest request) {
        // lay user dang trong phien dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // chuyen experiences ve entity
        Experiences experiences = experiencesMapper.toEntity(request) ;
        experiences.setUsers(users);
        Experiences experiencesInserted = experiencesRepository.save(experiences) ;


        return experiencesMapper.toDTO(experiencesInserted ) ;
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public ExperiencesResponse updateExp(ExperiencesRequest request, int Id) {
        // kiem tra experiences ton tai
        Experiences experiences = getExperiencesById(Id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , experiences);

        // set du lieu
        if(request.getName() != null) {
            experiences.setName(request.getName());
        }
        if(request.getPosition() != null) {
            experiences.setPosition(request.getPosition());
        }
        if(request.getDescription() != null) {
            experiences.setDescription(request.getDescription());
        }
        if(request.getStartDate() !=null) {
            experiences.setStartDate(request.getStartDate());
        }
        if(request.getEndDate() != null) {
            experiences.setEndDate(request.getEndDate());
        }
        Experiences experiencesUpdate = experiencesRepository.save(experiences) ;
        return experiencesMapper.toDTO(experiencesUpdate);
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public void deleteExp(int id) {
        // kiem tra experiences ton tai
        Experiences experiences = getExperiencesById(id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , experiences);

        // delete media cua education
        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.EXPERIENCE , id ) ;
        mediaFiles.forEach(mediaFilesDelete -> {
            deleteImage(mediaFilesDelete.getLink());
            mediaFileRepository.deleteById(mediaFilesDelete.getMediaId());
        });

        experiencesRepository.deleteById(id);
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public List<ExperiencesResponse> findAll() {
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin();
        return experiencesRepository.findByUsers(users).stream().map(experiencesMapper::toDTO).toList() ;
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public ExperiencesResponse findById(int id) {
        // kiem tra experiences ton tai
        Experiences experiences = getExperiencesById(id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , experiences);

        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.EXPERIENCE , id ) ;
        return experiencesMapper.toDTO(experiences , mediaFiles) ;
    }

    @Override
    public List<ExperiencesResponse> findAllByUser(String id) {
        Users users = getUserById(id) ;
        return experiencesRepository.findByUsers(users).stream().map(experiencesMapper::toDTO).toList() ;
    }
}
