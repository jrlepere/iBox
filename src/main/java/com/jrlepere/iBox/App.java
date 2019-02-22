package com.jrlepere.iBox;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "iBox", mixinStandardHelpOptions = true)
public class App implements Runnable {
	
	private static final Kind<?>[] events = new Kind<?>[] {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
	
	@Parameters(paramLabel = "FOLDER", description = "Folder to watch.")
    private static File watchFolder;
	
	public static void main(String[] args) throws WatchKeyGenerationException {
		CommandLine.run(new App(), args); // parse command line arguments with picocli
		WatchKey key = generateWatchKey(watchFolder.toPath());
		eventLoop(key);
    }

	private static WatchKey generateWatchKey(Path dir) throws WatchKeyGenerationException {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			return dir.register(watcher, events);
		} catch (IOException e) {
			throw new WatchKeyGenerationException("Could not generate the watch key w/ exception: " + e.getMessage());
		}
	}
	
	private static void eventLoop(WatchKey key) {
		while (true) {
			handleEvents(key);
		    if (!resetKey(key)) break;
		}
	}
	
	private static void handleEvents(WatchKey key) {
		for (WatchEvent<?> event : key.pollEvents()) {
	        handleEvent(event);
	    }
	}
	
	private static void handleEvent(WatchEvent<?> event) {
		WatchEvent.Kind<?> kind = event.kind();
		if (kind == OVERFLOW) return; 
        // WatchEvent<Path> ev = (WatchEvent<Path>) event;
        // Path filename = ev.context();
		System.out.println(kind);
	}
	
	private static boolean resetKey(WatchKey key) {
		return key.reset();
	}
	
	public void run() {}
}
