package com.mom.apps.model;

public interface AsyncListener <T>{
	public void onTaskSuccess(T result, DataExImpl.Methods callback);
	public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback);

}
