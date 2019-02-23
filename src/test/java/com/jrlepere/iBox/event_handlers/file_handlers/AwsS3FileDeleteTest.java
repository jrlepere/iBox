package com.jrlepere.iBox.event_handlers.file_handlers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import com.amazonaws.services.s3.model.DeleteObjectRequest;

public class AwsS3FileDeleteTest {

	private AwsS3FileDelete fileDeleteHandler;
	private AmazonS3 s3Client;
	
	@Before
	public void setup() {
		s3Client = mock(AmazonS3.class);
		fileDeleteHandler = new AwsS3FileDelete(s3Client, "");
	}
	
	@Test
	public void testHandleFileEventPass() throws FileHandleException {
		File f = mock(File.class);
		when(f.getName()).thenReturn("");
		doNothing().when(s3Client).deleteObject(any(DeleteObjectRequest.class));
		fileDeleteHandler.handleFileEvent(f);
		verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
	}
	
	@Test(expected = FileHandleException.class)
	public void testHandleFileEventSdkClientException() throws FileHandleException {
		File f = mock(File.class);
		when(f.getName()).thenReturn("");
		doThrow(new SdkClientException("")).when(s3Client).deleteObject(any(DeleteObjectRequest.class));
		fileDeleteHandler.handleFileEvent(f);
	}
	
	@Test(expected = FileHandleException.class)
	public void testHandleFileEventAmazonServiceException() throws FileHandleException {
		File f = mock(File.class);
		when(f.getName()).thenReturn("");
		doThrow(new AmazonServiceException("")).when(s3Client).deleteObject(any(DeleteObjectRequest.class));
		fileDeleteHandler.handleFileEvent(f);
	}
	
}
