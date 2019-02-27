package com.jrlepere.iBox;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.s3.AmazonS3;
import com.jrlepere.iBox.event_handlers.EventHandler;
import com.jrlepere.iBox.event_handlers.FileEventHandlerOrganizer;
import com.jrlepere.iBox.event_handlers.file_handlers.AwsS3FileDelete;
import com.jrlepere.iBox.event_handlers.file_handlers.AwsS3FileUpload;
import com.jrlepere.iBox.event_handlers.file_handlers.FileHandler;
import com.jrlepere.iBox.exceptions.WatchKeyResetException;

public class iBoxIntegrationTest {

	private static final String CONFIG_FILE_PATH = "./.config.json";
	private static final String TEMP_FILE_NAME = "temp.txt";
	private static final int SLEEP_TIME_MILLISECONDS = 2500;
	
	@Test
	public void integratinTest() throws Exception {
		
		// parse config file
		JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(new File(CONFIG_FILE_PATH))); 
		final File watchFolder = new File((String) obj.get("watchFolder"));
		final String clientRegion = (String) obj.get("clientRegion");
		final String accessKey = (String) obj.get("accessKey");
		final String privateKey = (String) obj.get("privateKey");
		final String bucketName = (String) obj.get("bucketName");
		
		// generate event handler
		final Kind<?>[] events = {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
		final WatchKey watchKey = App.generateWatchKey(watchFolder.toPath(), events);
		final AmazonS3 s3Client = AwsS3FileUpload.createS3Client(clientRegion, accessKey, privateKey);
		final EventHandler eventHandler = new FileEventHandlerOrganizer(
				new HashMap<WatchEvent.Kind<?>, FileHandler>() {{
					put(ENTRY_CREATE, new AwsS3FileUpload(s3Client, bucketName));
					put(ENTRY_MODIFY, new AwsS3FileUpload(s3Client, bucketName));
					put(ENTRY_DELETE, new AwsS3FileDelete(s3Client, bucketName));
				}}
		);
		
		// create folder listener
		FolderListener folderListener = new FolderListener(watchKey, eventHandler, watchFolder);
		
		// temporary file to create and delete
		File tempFile = new File(watchFolder, TEMP_FILE_NAME);
		
		// test create
		createFile(tempFile);
		Thread.sleep(SLEEP_TIME_MILLISECONDS);
		listen(folderListener);
		Thread.sleep(SLEEP_TIME_MILLISECONDS);
		Assert.assertTrue(s3Client.doesObjectExist(bucketName, TEMP_FILE_NAME));
		
		// test delete
		deleteFile(tempFile);
		Thread.sleep(SLEEP_TIME_MILLISECONDS);
		listen(folderListener);
		Thread.sleep(SLEEP_TIME_MILLISECONDS);
		Assert.assertFalse(s3Client.doesObjectExist(bucketName, TEMP_FILE_NAME));
		
	}
	
	private void createFile(File tempFile) throws IOException {
		FileWriter tempFileWriter = new FileWriter(tempFile);
		tempFileWriter.close();
	}
	
	private void deleteFile(File tempFile) {
		tempFile.delete();
	}
	
	private void listen(FolderListener folderListener) throws WatchKeyResetException {
		folderListener.checkForAndHandleEvents();
		folderListener.resetKey();
	}
	
}
