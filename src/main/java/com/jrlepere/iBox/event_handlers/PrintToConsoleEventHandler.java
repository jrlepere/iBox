package com.jrlepere.iBox.event_handlers;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.nio.file.WatchEvent;

public class PrintToConsoleEventHandler implements EventHandler {

	public void handleEvent(WatchEvent<?> event) {
		WatchEvent.Kind<?> kind = event.kind();
		if (kind == OVERFLOW) return;
		System.out.println(kind);
	}

}
