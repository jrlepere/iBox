package com.jrlepere.iBox.event_handlers.file_handlers;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

public class AwsS3FileDelete extends AwsS3FileHandler {
	
	public AwsS3FileDelete(AmazonS3 s3Client, String bucketName) {
		super(s3Client, bucketName);
	}

	public void handleFileEvent(File file) throws FileHandleException {
		try {
			s3Client.deleteObject(new DeleteObjectRequest(bucketName, file.getName()));
		} catch (Exception e) {
			String message = "Failed to delete file: "
					+ ((file == null) ? "NULL" : file.getName())
					+ " from AWS S3 w/ exception: "
					+ e.getMessage();
			throw new FileHandleException(message);
		}
	}
	
}
