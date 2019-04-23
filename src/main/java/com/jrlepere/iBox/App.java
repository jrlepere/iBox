package com.jrlepere.iBox;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.s3.AmazonS3;
import com.jrlepere.iBox.event_handlers.EventHandler;
import com.jrlepere.iBox.event_handlers.FileEventHandlerOrganizer;
import com.jrlepere.iBox.event_handlers.file_handlers.AwsS3FileDelete;
import com.jrlepere.iBox.event_handlers.file_handlers.AwsS3FileUpload;
import com.jrlepere.iBox.event_handlers.file_handlers.FileHandler;
import com.jrlepere.iBox.exceptions.WatchKeyGenerationException;

public class App implements Runnable {
		
	public static void main(String[] args) throws Exception {
		
		JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(new File(args[0]))); 

		final String clientRegion = (String) obj.get("clientRegion");
		final String accessKey = (String) obj.get("accessKey");
		final String privateKey = (String) obj.get("privateKey");
		final String bucketName = (String) obj.get("bucketName");
		
		final File watchFolder = new File(args[1]); 
		
		final Kind<?>[] events = {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
		final WatchKey watchKey = generateWatchKey(watchFolder.toPath(), events);
		final AmazonS3 s3Client = AwsS3FileUpload.createS3Client(clientRegion, accessKey, privateKey);
		final EventHandler eventHandler = new FileEventHandlerOrganizer(
				new HashMap<WatchEvent.Kind<?>, FileHandler>() {{
					put(ENTRY_CREATE, new AwsS3FileUpload(s3Client, bucketName));
					put(ENTRY_MODIFY, new AwsS3FileUpload(s3Client, bucketName));
					put(ENTRY_DELETE, new AwsS3FileDelete(s3Client, bucketName));
				}}
		);
		
		FolderListener folderListener = new FolderListener(watchKey, eventHandler, watchFolder);
		
		while (true) {
			folderListener.checkForAndHandleEvents();
			folderListener.resetKey();
		}
		
    }

	public static WatchKey generateWatchKey(Path dir, Kind<?>[] events) throws WatchKeyGenerationException {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			return dir.register(watcher, events);
		} catch (IOException e) {
			throw new WatchKeyGenerationException("Could not generate the watch key w/ exception: " + e.getMessage());
		}
	}
	
	public void run() {}
}
