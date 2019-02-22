package com.jrlepere.iBox.event_handlers.file_handler;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public abstract class AwsS3FileHandler implements FileHandler {

	protected AmazonS3 s3Client;
	protected String bucketName;
	
	public AwsS3FileHandler(AmazonS3 s3Client, String bucketName) {
		this.s3Client = s3Client;
		this.bucketName = bucketName;
	}
	
	public static AmazonS3 createS3Client(String clientRegion, String accessKey, String privateKey) {
		return AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, privateKey)))
                .build();
	}
	
}
