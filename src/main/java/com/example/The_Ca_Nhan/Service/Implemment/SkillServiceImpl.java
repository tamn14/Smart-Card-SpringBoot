package com.example.The_Ca_Nhan.Service.Implemment;


import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.SkillRequest;
import com.example.The_Ca_Nhan.DTO.Response.SkillResponse;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Projects;
import com.example.The_Ca_Nhan.Entity.Skills;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.MediaFilesMapper;
import com.example.The_Ca_Nhan.Mapper.SkillMapper;
import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import com.example.The_Ca_Nhan.Repository.MediaFileRepository;
import com.example.The_Ca_Nhan.Repository.SkillRepository;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import com.example.The_Ca_Nhan.Service.Interface.SkillInterface;
import com.example.The_Ca_Nhan.Util.Extract;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillInterface {
    private final SkillRepository skillRepository ;
    private final SkillMapper skillMapper ;
    private final Extract extract ;
    private final MediaFilesMapper mediaFilesMapper ;
    private final MediaFileRepository mediaFileRepository ;
    private final S3Interface s3Interface ;
    private final UsersRepository usersRepository ;

    private Skills getSkillById(int id ) {
        return skillRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PROJECT_NOT_FOUND)) ;
    }


    private Users getUserById(String id) {
        Users users = usersRepository.findByKeycloakId(id) ;
        if( users == null) {
            throw new AppException((ErrorCode.USER_NOT_EXISTED)) ;
        }
        return users ;
    }


    private void checkAuthenticated (Users users ,Skills skills) {
        if(!(skills.getUsers().getUserName().equals(users.getUserName()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED) ;
        }
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
    public SkillResponse insertSkill(SkillRequest request) {
        // lay user trong phien dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // chuyen Skill ve entity
        Skills skills  = skillMapper.toEntity(request) ;
        skills.setUsers(users);
        Skills skillsInserted = skillRepository.save(skills) ;


        return skillMapper.toDTO(skillsInserted) ;

    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public SkillResponse updateSkill(SkillRequest request, int Id) {
        // kiem tra skill co ton tai khong
        Skills skills = getSkillById(Id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , skills);

        if(request.getName() != null) {
            skills.setName(request.getName());
        }
        if(request.getLevel() != null) {
            skills.setLevel(request.getLevel());
        }

        skillRepository.save(skills) ;
        return skillMapper.toDTO(skills) ;
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public void deleteSkill(int Id) {
        // kiem tra skill co ton tai khong
        Skills skills = getSkillById(Id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , skills);
        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.SKILL , Id ) ;
        mediaFiles.forEach(mediaFilesDelete -> {
            deleteImage(mediaFilesDelete.getLink());
            mediaFileRepository.deleteById(mediaFilesDelete.getMediaId());
        });
        skillRepository.deleteById(Id);

    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public List<SkillResponse> findAll() {
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin();
        return skillRepository.findByUsers(users).stream().map(skillMapper :: toDTO).toList() ;
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public SkillResponse findById(int id) {
        // kiem tra skill co ton tai khong
        Skills skills = getSkillById(id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , skills);
        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.SKILL , id ) ;
        return skillMapper.toDTO(skills , mediaFiles);
    }

    @Override
    public List<SkillResponse> findAllByUser(String id) {
        Users users = getUserById(id) ;
        return skillRepository.findByUsers(users).stream().map(skillMapper :: toDTO).toList() ;
    }
}
