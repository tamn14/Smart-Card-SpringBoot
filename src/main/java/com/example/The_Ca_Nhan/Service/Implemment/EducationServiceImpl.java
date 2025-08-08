package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.Request.EducationRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;
import com.example.The_Ca_Nhan.Entity.Educations;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.EducationMapper;
import com.example.The_Ca_Nhan.Mapper.MediaFilesMapper;
import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import com.example.The_Ca_Nhan.Repository.EducationRepository;
import com.example.The_Ca_Nhan.Repository.MediaFileRepository;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import com.example.The_Ca_Nhan.Service.Interface.EducationInterface;
import com.example.The_Ca_Nhan.Service.Interface.MediaFileInterface;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import com.example.The_Ca_Nhan.Util.Extract;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationInterface {
    private final EducationMapper educationMapper;
    private final EducationRepository educationRepository;
    private final Extract extract;
    private final MediaFilesMapper mediaFilesMapper;
    private final MediaFileRepository mediaFileRepository;
    private final S3Interface s3Interface;
    private final UsersRepository usersRepository ;


    private Educations getEducation(int id) {
        return educationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EDUCATION_NOT_FOUND));
    }

    private void checkAuthenticated(Users users, Educations educations) {
        if (!(educations.getUsers().getUserName().equals(users.getUserName()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private boolean checkListMediaIsNull(List<MediaFileCreateRequest> requests) {
        return requests == null || requests.isEmpty();
    }

    private String uploadImageAndGetUrl(MultipartFile file) {
        return s3Interface.uploadFile(file);
    }

    private void deleteImage(String url) {
        s3Interface.deleteImage(url);
    }

    private Users getUserById(String id) {
        Users users = usersRepository.findByKeycloakId(id) ;
        if( users == null) {
            throw new AppException((ErrorCode.USER_NOT_EXISTED)) ;
        }
        return users ;
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public EducationResponse insertEdu(EducationRequest request) {
        // lay user dang trong phien dang nhap
        Users users = extract.getUserInFlowLogin();
        // chuyen education request ve entity
        Educations educations = educationMapper.toEntity(request);
        educations.setUsers(users);
        Educations educationInserted = educationRepository.save(educations);
        return educationMapper.toDTO(educationInserted);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public EducationResponse updateEdu(EducationRequest request, int Id) {
        // kiem tra education ton tai
        Educations educations = getEducation(Id);
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin();
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users, educations);
        // set value cho education
        if (request.getSchoolName() != null) {
            educations.setSchoolName(request.getSchoolName());
        }
        if (request.getDescription() != null) {
            educations.setDescription(request.getDescription());
        }
        if (request.getDegree() != null) {
            educations.setDegree(request.getDegree());
        }
        if (request.getStartDate() != null) {
            educations.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            educations.setEndDate(request.getEndDate());
        }

        return educationMapper.toDTO(educationRepository.save(educations));

    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void deleteEdu(int eduId) {
        // kiem tra education ton tai
        Educations educations = getEducation(eduId);
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin();
        // kiem tra doan bao  Education cua chinh user do
        checkAuthenticated(users, educations);
        // delete media cua education
        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.EDUCATION, eduId);
        mediaFiles.forEach(mediaFilesDelete -> {
            deleteImage(mediaFilesDelete.getLink());
            mediaFileRepository.deleteById(mediaFilesDelete.getMediaId());
        });

        educationRepository.deleteById(eduId);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<EducationResponse> findAll() {
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin();
        return educationRepository.findByUsers(users).stream().map(educationMapper::toDTO).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public EducationResponse findById(int id) {
        // kiem tra education ton tai
        Educations educations = getEducation(id);
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin();
        // kiem tra doan bao  Education cua chinh user do
        checkAuthenticated(users, educations);
        // lay list media cua education
        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.EDUCATION, id);
        return educationMapper.toDTO(educations, mediaFiles);
    }

    // service public
    @Override
    public List<EducationResponse> findAllByUsers(String userId) {
        Users users = getUserById(userId) ;
        return educationRepository.findByUsers(users)
                .stream().map(educationMapper::toDTO).toList();
    }
}
