package com.jrlepere.iBox;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

import com.jrlepere.iBox.event_handlers.EventHandler;
import com.jrlepere.iBox.exceptions.WatchKeyResetException;

public class FolderListener {

	private WatchKey key;
	private EventHandler eventHandler;
	
	public FolderListener(WatchKey key, EventHandler eventHandler) {
		this.key = key;
		this.eventHandler = eventHandler;
	}
	
	public void checkForAndHandleEvents() {
		for (WatchEvent<?> event : key.pollEvents()) {
	        eventHandler.handleEvent(event);
	    }
	}
	
	public boolean resetKey() throws WatchKeyResetException {
		if (!key.reset()) {
			throw new WatchKeyResetException("Key reset was unsuccessful");
		}
		return true;
	}
	
}
