package com.rand.service.file;

import com.rand.exception.custom.BadRequestException;
import com.rand.exception.custom.InternerServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {


    // 최대 파일 크기 5MB 변수
    private static final long MAX_FILE_SIZE = 5* 1024 * 1024 ;
    private final S3Client s3Client;

    @Value("${s3.credentials.bucket.name}")
    private String bucket;


    public String upload(MultipartFile image) {
        //버킷이름에 공백에 생기는 경우 방지
//        bucket = bucket.trim();

        // 입력받은 이미지 파일이 빈 파일인지 검증
        if (image == null || image.isEmpty() || image.getOriginalFilename() == null) {
            //기본이미지가 아닌 이미지로 변경중 파일이 비어있거나 이름이 없습니다.
            throw new BadRequestException("ERR-FILE-03");
        }

        //파일 사이즈 검증
        validateFileSize(image);


        // 이미지 업로드 메서드 호출
        try {
            log.info("실행");
            return uploadImageToS3(image);
        } catch (IOException e) {
            // 파일 읽기 중 IO 예외 처리
            //새 파일 업로드 중 에러발생
            throw new InternerServerException("ERR-FILE-01");

        }

    }

    //파일 확장자 검증
    private void validateImageFileExtension(String filename) {

        if (filename == null || filename.isEmpty()) {
            //기본이미지가 아닌 이미지로 변경중 파일이 비어있거나 이름이 없습니다.
            throw new BadRequestException("ERR-FILE-03");
        }

        int lastDotIndex = filename.lastIndexOf(".");

        if (lastDotIndex == -1) {

            throw new BadRequestException("ERR-FILE-04");
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");

        if (!allowedExtensions.contains(extension)) {
            //파일 확장자가 없습니다
            log.info("실행 확장자");
            //지원되는 확장자가 아닙니다.(jpg,jpeg,png만 가능)
            throw new BadRequestException("ERR-FILE-05");
        }
    }

    //파일크기 서버측 검증 (한번더 ) 1차 : 클라이언트 , 2차 : 게시물서비스
    private void validateFileSize(MultipartFile file) {

        if (file.getSize() > MAX_FILE_SIZE) {
            //파일 사이즈가 너무 큽니다.(최대 5MB)
            throw new BadRequestException("ERR-FILE-06"); // 파일 크기 초과 오류 처리
        }
    }
    //실제 업로드
    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();

        if (originalFilename == null) {
            //기본이미지가 아닌 이미지로 변경중 파일이 비어있거나 이름이 없습니다.
            throw new BadRequestException("ERR-FILE-03");
        }
        //확장자 검증
        validateImageFileExtension(originalFilename);

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        byte[] bytes = image.getBytes();

        try (InputStream is = new ByteArrayInputStream(bytes)) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3FileName)
                    .contentType("image/" + extension)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
        } catch (S3Exception e) {

            //새 파일 업로드 중 에러발생
            throw new InternerServerException("ERR-FILE-01");
        } catch (SdkException e) {

            //새 파일 업로드 중 에러발생
            throw new InternerServerException("ERR-FILE-01");
        }

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(s3FileName)).toExternalForm();
    }
    //파일 삭제
    public void deleteImage(String imageAddress) {
        bucket=bucket.trim();
        String key = getKeyFromImageAddress(imageAddress);
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {

            //기존 파일 삭제 중 에러발생
            throw new InternerServerException("ERR-FILE-02");
        } catch (Exception e) {

            //기존 파일 삭제 중 에러발생
            throw new InternerServerException("ERR-FILE-02");
        }
    }
    //파일 삭제시 키값 가져오기
    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodedPath = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8.name());
            return decodedPath.startsWith("/") ? decodedPath.substring(1) : decodedPath;
        } catch (MalformedURLException e) {

            //기존 파일 삭제 중 에러발생
            throw new InternerServerException("ERR-FILE-02");
        } catch (UnsupportedEncodingException e) {

            //기존 파일 삭제 중 에러발생
            throw new InternerServerException("ERR-FILE-02");
        }
        catch (Exception e) {

            //기존 파일 삭제 중 에러발생
            throw new InternerServerException("ERR-FILE-02");
        }
    }
}
