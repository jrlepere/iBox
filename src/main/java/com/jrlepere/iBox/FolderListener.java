package com.jrlepere.iBox;

import java.io.File;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

import com.jrlepere.iBox.event_handlers.EventHandler;
import com.jrlepere.iBox.exceptions.WatchKeyResetException;

public class FolderListener {

	private WatchKey key;
	private EventHandler eventHandler;
	private File folder;
	
	public FolderListener(WatchKey key, EventHandler eventHandler, File folder) {
		this.key = key;
		this.eventHandler = eventHandler;
		this.folder = folder;
	}
	
	public void checkForAndHandleEvents() {
		for (WatchEvent<?> event : key.pollEvents()) {
	        eventHandler.handleEvent(event, folder);
	    }
	}
	
	public void resetKey() throws WatchKeyResetException {
		if (!key.reset()) {
			throw new WatchKeyResetException("Key reset was unsuccessful");
		}
	}
	
}
