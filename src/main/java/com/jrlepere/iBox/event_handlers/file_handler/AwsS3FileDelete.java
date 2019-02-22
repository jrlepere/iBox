package com.jrlepere.iBox.event_handlers.file_handler;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

public class AwsS3FileDelete extends AwsS3FileHandler {
	
	public AwsS3FileDelete(AmazonS3 s3Client, String bucketName) {
		super(s3Client, bucketName);
	}

	public void handleFileEvent(File file) {
		s3Client.deleteObject(new DeleteObjectRequest(bucketName, file.getName()));
	}
	
}
