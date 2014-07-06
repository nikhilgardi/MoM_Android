package com.mom.app.model;

import com.mom.app.identifier.PlatformIdentifier;

public interface AsyncListener {
	public void onTaskComplete(String result, DataExImpl.Methods callback);
}
