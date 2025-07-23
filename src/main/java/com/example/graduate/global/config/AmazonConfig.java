package com.example.graduate.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//s3에 접근하기 위한 설정을 담당하는 클래스
@Configuration //설정 클래스, Bean 등록한다.
@Getter
public class AmazonConfig {
    //AWS 자격 증명을 담는 객체
    private AWSCredentials awsCredentials;

    //application.tml에서 accessKey를 받아오기
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    //application.yml에서 secretKey를 받아오기
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    //application.yml에서 region정보를 받아오기
    @Value("${cloud.aws.region.static}")
    private String region;


    //추가하래서 일단 해봄 - by gpt
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder.letter}")
    private String letterPath;
    //여기까지


    //Bean 등록 전에 실행되는 초기화 메서드.
    //accessKey와 secretKey로 BasicAWSCredentials 객체를 생성해 필드에 저장한다.
    @PostConstruct
    public void init(){
        this.awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    //Amazons3 클라이언트를 Bean으로 등록
    //이 클라이언트 통해 파일 업로드, 삭제 등의 S3 API 호출이 가능하다.
    @Bean
    public AmazonS3 amazonS3(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    //AWSCredentialsProvider를 Bean으로 등록
    //필요시 의존성 주입하여 사용할 수 있다.
    @Bean
    public AWSCredentialsProvider awsCredentialsProvider(){
        return new AWSStaticCredentialsProvider(awsCredentials);
    }
}
