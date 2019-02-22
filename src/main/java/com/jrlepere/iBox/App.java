package com.jrlepere.iBox;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.jrlepere.iBox.event_handlers.PrintToConsoleEventHandler;
import com.jrlepere.iBox.exceptions.WatchKeyGenerationException;
import com.jrlepere.iBox.exceptions.WatchKeyResetException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "iBox", mixinStandardHelpOptions = true)
public class App implements Runnable {
	
	@Parameters(paramLabel = "FOLDER", description = "Folder to watch.")
    private static File watchFolder;
	
	public static void main(String[] args) throws WatchKeyGenerationException, WatchKeyResetException {
		
		// parse command line arguments with picocli
		CommandLine.run(new App(), args);
		
		final Kind<?>[] events = {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
		
		WatchKey key = generateWatchKey(watchFolder.toPath(), events);
		FolderListener folderListener = new FolderListener(key, new PrintToConsoleEventHandler());
		
		while (true) {
			folderListener.checkForAndHandleEvents();
			folderListener.resetKey();
		}
		
    }

	private static WatchKey generateWatchKey(Path dir, Kind<?>[] events) throws WatchKeyGenerationException {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			return dir.register(watcher, events);
		} catch (IOException e) {
			throw new WatchKeyGenerationException("Could not generate the watch key w/ exception: " + e.getMessage());
		}
	}
	
	public void run() {}
}
