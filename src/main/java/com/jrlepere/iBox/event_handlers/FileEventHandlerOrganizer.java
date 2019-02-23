package com.jrlepere.iBox.event_handlers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Map;

import com.jrlepere.iBox.event_handlers.file_handlers.FileHandleException;
import com.jrlepere.iBox.event_handlers.file_handlers.FileHandler;

public class FileEventHandlerOrganizer implements EventHandler {

	private Map<WatchEvent.Kind<?>, FileHandler> fileEventHandlerMap;
	
	public FileEventHandlerOrganizer(Map<WatchEvent.Kind<?>, FileHandler> fileEventHandlerMap) {
		this.fileEventHandlerMap = fileEventHandlerMap;
	}
	
	public void setFileEventHandlerMap(Map<WatchEvent.Kind<?>, FileHandler> fileEventHandlerMap) {
		this.fileEventHandlerMap = fileEventHandlerMap;
	}
	
	public void handleEvent(WatchEvent<?> event, File eventLocation) {
		WatchEvent.Kind<?> eventKind = event.kind();
		if (fileEventHandlerMap.containsKey(eventKind)) {
			try {
				fileEventHandlerMap.get(eventKind).handleFileEvent(getFullFile(event, eventLocation));
			} catch (FileHandleException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public File getFullFile(WatchEvent<?> event, File eventLocation) {
		return new File(eventLocation, ((Path) event.context()).toFile().getName());
	}

}
