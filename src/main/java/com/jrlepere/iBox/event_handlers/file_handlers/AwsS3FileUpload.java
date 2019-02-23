package com.jrlepere.iBox.event_handlers.file_handlers;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AwsS3FileUpload extends AwsS3FileHandler {
	
	public AwsS3FileUpload(AmazonS3 s3Client, String bucketName) {
		super(s3Client, bucketName);
	}

	public void handleFileEvent(File file) throws FileHandleException {
		if (file.isFile()) {
			try {
				s3Client.putObject(new PutObjectRequest(bucketName, file.getName(), file));
			} catch (Exception e) {
				String message = "Failed to upload file: "
						+ file.getName()
						+ " from AWS S3 w/ exception: "
						+ e.getMessage();
				throw new FileHandleException(message);
			}
		}
	}
	
}
