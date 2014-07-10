package com.mom.app.model;

import com.mom.app.identifier.PlatformIdentifier;

public interface AsyncListener <T>{
	public void onTaskComplete(T result, DataExImpl.Methods callback);
}
