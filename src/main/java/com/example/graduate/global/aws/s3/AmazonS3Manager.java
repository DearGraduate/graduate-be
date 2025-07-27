package com.example.graduate.global.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.graduate.global.config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
//S3에 업로드하고 URL을 리턴하는 메서드 입니다
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    private final UuidRepository uuidRepository;

    //실제 S3에 파일 업로드
    public String uploadFile(String keyName, MultipartFile file){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType()); //이미지 다운로드 되는 문제 해결 코드

        try {
            amazonS3.putObject(new PutObjectRequest(
                    amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
        } catch (IOException e){
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    public String generateLetterKeyName(MultipartFile file, Uuid uuid) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        return amazonConfig.getLetterPath() + "/" + uuid.getUuid() + extension;
    }



    // S3에 저장된 파일 삭제 메서드 - picUrl을 받아서 key 추출 후 삭제
    public void deleteFileByUrl(String fileUrl) {
        try {
            // fileUrl에서 버킷명과 key를 분리하는 로직
            String bucket = amazonConfig.getBucket();
            // fileUrl 예시: https://버킷명.s3.지역.amazonaws.com/key
            // key는 fileUrl에서 버킷 URL 부분을 제외한 나머지
            String baseUrl = "https://" + bucket + ".s3." + amazonConfig.getRegion() + ".amazonaws.com/";
            String key = fileUrl.replace(baseUrl, "");

            amazonS3.deleteObject(bucket, key);
        } catch (Exception e) {
            log.error("Failed to delete file from S3: {}", e.getMessage());
        }
    }
}
