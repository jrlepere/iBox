package com.jrlepere.iBox;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jrlepere.iBox.event_handlers.EventHandler;
import com.jrlepere.iBox.exceptions.WatchKeyResetException;

public class FolderListenerTest {

	private File folder;
	private WatchKey watchKey;
	private EventHandler eventHandler;
	private FolderListener folderListener;
	
	@Before
	public void setup() {
		folder = mock(File.class);
		watchKey = mock(WatchKey.class);
		eventHandler = mock(EventHandler.class);
		folderListener = new FolderListener(watchKey, eventHandler, folder);
	}
	
	@Test
	public void testCheckForAndHandleEventsNone() {
		WatchEvent<?> e = mock(WatchEvent.class);
		when(watchKey.pollEvents()).thenReturn(new LinkedList<WatchEvent<?>>());
		folderListener.checkForAndHandleEvents();
		verify(eventHandler, times(0)).handleEvent(e, folder);
	}
	
	@Test
	public void testCheckForAndHandleEventsMutliple() {
		WatchEvent<?> e = mock(WatchEvent.class);
		when(watchKey.pollEvents()).thenReturn(Arrays.asList(e, e, e));
		folderListener.checkForAndHandleEvents();
		verify(eventHandler, times(3)).handleEvent(e, folder);
	}
	
	@Test
	public void testResetKeySuccess() {
		when(watchKey.reset()).thenReturn(true);
		try {
			folderListener.resetKey();
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test(expected=WatchKeyResetException.class)
	public void testResetKeyFail() throws WatchKeyResetException {
		when(watchKey.reset()).thenReturn(false);
		folderListener.resetKey();
	}
	
}
