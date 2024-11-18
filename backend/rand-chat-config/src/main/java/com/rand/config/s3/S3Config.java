package com.rand.config.s3;


//@Configuration
//public class S3Config {
//    /*AWS S3 CONFIG*/
//
//    @Value("${s3.credentials.access-key}")
//    private String accessKey;
//
//    @Value("${s3.credentials.secret-key}")
//    private String secretKey;
//
//    @Value("${s3.credentials.region}")
//    private String region;
//
//    @Bean
//    public S3Client s3Client() {
//        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
//        return S3Client.builder()
//                .region(Region.of(region))
//                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
//                .build();
//    }
//}