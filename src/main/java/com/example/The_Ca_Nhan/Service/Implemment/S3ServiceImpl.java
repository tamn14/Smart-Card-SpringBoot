package com.example.The_Ca_Nhan.Service.Implemment;


import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Properties.AWSProperties;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import static java.rmi.server.LogStream.log;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Interface {
    private final S3Client s3Client;
    private final AWSProperties awsProperties ;


    @Override
    public String uploadFile(MultipartFile file) {
        try {
            log.info("===> File name: {}", file.getOriginalFilename());
            log.info("===> Content type: {}", file.getContentType());
            log.info("===> Size: {}", file.getSize());
            log.info("===> Is empty: {}", file.isEmpty());
            if (file.isEmpty() || file.getSize() == 0) {
                log.error("File upload rỗng");
                throw new AppException(ErrorCode.FILE_IMAGE_EMPTY);
            }
            String fileName = "public/" + UUID.randomUUID() + "_" + file.getOriginalFilename() ;
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsProperties.getBucket())
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();
            log.info("Đang upload lên bucket: {}", awsProperties.getBucket());
            log.info("Tên file sau khi gán UUID: {}", fileName);
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return s3Client.utilities()
                    .getUrl(b -> b.bucket(awsProperties.getBucket()).key(fileName))
                    .toString();
        }
        catch (IOException e) {
            log.error("IOException khi đọc file: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.S3_SERVICE_FAILED);
        }
    }

    @Override
    public void deleteImage(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            log.warn("Không có fileUrl để xoá -> bỏ qua");
            return;
        }

        try {
            URI uri = new URI(fileUrl);
            String key = uri.getPath().substring(1); // bỏ dấu '/'
            log.info("🔍 Đang xoá file S3 với key: {}", key);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(awsProperties.getBucket())
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            log.error(" Lỗi khi xoá file trên S3: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.S3_SERVICE_FAILED);
        }
    }




}
