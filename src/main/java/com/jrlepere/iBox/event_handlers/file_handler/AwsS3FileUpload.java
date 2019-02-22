package com.jrlepere.iBox.event_handlers.file_handler;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AwsS3FileUpload extends AwsS3FileHandler {
	
	public AwsS3FileUpload(AmazonS3 s3Client, String bucketName) {
		super(s3Client, bucketName);
	}

	public void handleFileEvent(File file) {
		if (file.isFile()) {
			s3Client.putObject(new PutObjectRequest(bucketName, file.getName(), file));
		}
	}
	
}
