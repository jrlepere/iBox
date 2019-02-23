package com.jrlepere.iBox.event_handlers;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.jrlepere.iBox.event_handlers.file_handlers.FileHandleException;
import com.jrlepere.iBox.event_handlers.file_handlers.FileHandler;

public class FileEventHandlerOrganizerTest {

	private FileHandler createFileHandler, deleteFileHandler;
	private FileEventHandlerOrganizer organizer;
	private File eventLocation;
	private WatchEvent createEvent, deleteEvent;
	
	@Before
	public void setup() {
		
		// mock file event handlers
		createFileHandler = mock(FileHandler.class);
		deleteFileHandler = mock(FileHandler.class);
		
		// mock event location
		eventLocation = mock(File.class);
		
		// mock events
		createEvent = mock(WatchEvent.class);
		when(createEvent.kind()).thenReturn(ENTRY_CREATE);
		deleteEvent = mock(WatchEvent.class);
		when(deleteEvent.kind()).thenReturn(ENTRY_DELETE);
		
		// mock organizer
		organizer = mock(FileEventHandlerOrganizer.class);
		doCallRealMethod().when(organizer).setFileEventHandlerMap(any(Map.class));
		doCallRealMethod().when(organizer).handleEvent(any(WatchEvent.class), any(File.class));
		when(organizer.getFullFile(any(WatchEvent.class), any(File.class))).thenReturn(any(File.class));
		
	}
	
	@Test
	public void testHandleEventAllCalled() throws FileHandleException {
		Map<Kind<?>, FileHandler> fileEventHandlerMap = new HashMap<Kind<?>, FileHandler>() {{
			put(ENTRY_CREATE, createFileHandler);
			put(ENTRY_DELETE, deleteFileHandler);
		}};
		organizer.setFileEventHandlerMap(fileEventHandlerMap);
		organizer.handleEvent(createEvent, eventLocation);
		organizer.handleEvent(deleteEvent, eventLocation);
		verify(createFileHandler, times(1)).handleFileEvent(any(File.class));
		verify(deleteFileHandler, times(1)).handleFileEvent(any(File.class));
	}
	
	@Test
	public void testHandleEventNoneCalled() throws FileHandleException {
		Map<Kind<?>, FileHandler> fileEventHandlerMap = new HashMap<Kind<?>, FileHandler>() {{
			put(ENTRY_CREATE, createFileHandler);
			put(ENTRY_DELETE, deleteFileHandler);
		}};
		organizer.setFileEventHandlerMap(fileEventHandlerMap);
		verify(createFileHandler, times(0)).handleFileEvent(any(File.class));
		verify(deleteFileHandler, times(0)).handleFileEvent(any(File.class));
	}
	
	@Test
	public void testHandleEventOneCalled() throws FileHandleException {
		Map<Kind<?>, FileHandler> fileEventHandlerMap = new HashMap<Kind<?>, FileHandler>() {{
			put(ENTRY_CREATE, createFileHandler);
			put(ENTRY_DELETE, deleteFileHandler);
		}};
		organizer.setFileEventHandlerMap(fileEventHandlerMap);
		organizer.handleEvent(createEvent, eventLocation);
		organizer.handleEvent(createEvent, eventLocation);
		verify(createFileHandler, times(2)).handleFileEvent(any(File.class));
	}
	
	@Test
	public void testHandleEventNotHandledEventCalled() throws FileHandleException {
		Map<Kind<?>, FileHandler> fileEventHandlerMap = new HashMap<Kind<?>, FileHandler>() {{
			put(ENTRY_DELETE, deleteFileHandler);
		}};
		organizer.setFileEventHandlerMap(fileEventHandlerMap);
		organizer.handleEvent(createEvent, eventLocation);
		organizer.handleEvent(createEvent, eventLocation);
		verify(createFileHandler, times(0)).handleFileEvent(any(File.class));
	}
	
}
