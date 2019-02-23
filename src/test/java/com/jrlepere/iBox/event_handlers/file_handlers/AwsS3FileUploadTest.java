package com.jrlepere.iBox.event_handlers.file_handlers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class AwsS3FileUploadTest {

	private AwsS3FileUpload fileUploadHandler;
	private AmazonS3 s3Client;
	
	@Before
	public void setup() {
		s3Client = mock(AmazonS3.class);
		fileUploadHandler = new AwsS3FileUpload(s3Client, "");
	}
	
	@Test
	public void testHandleFileEventPass() throws FileHandleException {
		File f = mock(File.class);
		when(f.isFile()).thenReturn(true);
		when(f.getName()).thenReturn("");
		when(s3Client.putObject(any(PutObjectRequest.class))).thenReturn(mock(PutObjectResult.class));
		fileUploadHandler.handleFileEvent(f);
		verify(s3Client, times(1)).putObject(any(PutObjectRequest.class));
	}
	
	@Test(expected = FileHandleException.class)
	public void testHandleFileEventSdkClientException() throws FileHandleException {
		File f = mock(File.class);
		when(f.isFile()).thenReturn(true);
		when(f.getName()).thenReturn("");
		when(s3Client.putObject(any(PutObjectRequest.class))).thenThrow(new SdkClientException(""));
		fileUploadHandler.handleFileEvent(f);
	}
	
	@Test(expected = FileHandleException.class)
	public void testHandleFileEventAmazonServiceException() throws FileHandleException {
		File f = mock(File.class);
		when(f.isFile()).thenReturn(true);
		when(f.getName()).thenReturn("");
		when(s3Client.putObject(any(PutObjectRequest.class))).thenThrow(new AmazonServiceException(""));
		fileUploadHandler.handleFileEvent(f);
	}
	
	@Test
	public void testHandleFileEventNotFile() throws FileHandleException {
		File f = mock(File.class);
		when(f.isFile()).thenReturn(false);
		fileUploadHandler.handleFileEvent(f);
		verify(s3Client, times(0)).putObject(any(PutObjectRequest.class));
	}
	
}
