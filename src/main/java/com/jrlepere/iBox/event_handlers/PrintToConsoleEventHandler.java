package com.jrlepere.iBox.event_handlers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

public class PrintToConsoleEventHandler implements EventHandler {

	public void handleEvent(WatchEvent<?> event, File eventLocation) {
		if (event.context() instanceof Path) {
			File f = new File(eventLocation, ((Path) event.context()).toFile().getName());
			System.out.println(event.kind().toString() + ": " + f);
		}
	}

}
