package com.jrlepere.iBox.event_handlers.file_handlers;

import java.io.File;

public interface FileHandler {

	public void handleFileEvent(File file) throws FileHandleException;
	
}
