package com.mom.app.model;

public interface AsyncListener <T>{
	public void onTaskSuccess(T result, DataExImpl.Methods callback);
	public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback);
}
