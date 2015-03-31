package com.mom.apps.test.activity;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.TextView;

import com.mom.apps.R;
import com.mom.apps.activity.VerifyTPinActivity;
import com.mom.apps.model.DataExImpl;

/**
 * Created by vaibhavsinha on 7/8/14.
 */
public class VerifyTPinActivityTest extends ActivityInstrumentationTestCase2<VerifyTPinActivity> {
    Activity mActivity;

    public VerifyTPinActivityTest(){
        super(VerifyTPinActivity.class);
    }

    @Override
    public void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(false);

        mActivity = getActivity();

    }


    @UiThreadTest
    public void testOnTaskComplete(){
        TextView textView   = (TextView)mActivity.findViewById(R.id.msgDisplay);

        VerifyTPinActivity verifyTPinActivity   = (VerifyTPinActivity)mActivity;
        verifyTPinActivity.onTaskSuccess(null, DataExImpl.Methods.VERIFY_TPIN);

        String failedResult = textView.getText().toString();

        assertEquals(mActivity.getResources().getString(R.string.tpin_incorrect), failedResult);
    }
}
