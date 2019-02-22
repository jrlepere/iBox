package com.jrlepere.iBox.event_handlers;

import java.io.File;
import java.nio.file.WatchEvent;

public interface EventHandler {

	public void handleEvent(WatchEvent<?> event, File eventLocation);
	
}
