package com.mom.app.model.pbxpl;

import android.util.Log;

import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.xml.PullParser;
import com.mom.app.utils.MOMConstants;

import org.apache.http.NameValuePair;

import java.io.ByteArrayInputStream;

/**
 * Created by vaibhavsinha on 7/6/14.
 */
public class PBXPLDataExImpl extends DataExImpl implements AsyncListener {
    public PBXPLDataExImpl(AsyncListener pListener){
        this._listener  = pListener;
    }

    @Override
    public void onTaskComplete(String result, Methods callback) {
        switch (callback){
            case LOGIN:
                boolean bSuccess    = loginSuccessful(result);
                if(_listener != null){
                    _listener.onTaskComplete((new Boolean(bSuccess)).toString(), null);
                }
                break;
        }
    }

    @Override
    public double getBalance(){
        return 0.;
    }

    public void login(NameValuePair...params){
        String loginUrl				= MOMConstants.URL_PBX_PLATFORM;
        Log.i("PBXPL", "Calling Async login");

        AsyncDataEx dataEx		    = new AsyncDataEx(this, loginUrl, Methods.LOGIN);

        dataEx.execute(params);
    }

    public boolean loginSuccessful(String psResult){
        if("".equals(psResult)){
            return false;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.i("LoginResult", "Response: " + response);
            String[] strArrayResponse = response.split("~");

            int i = Integer.parseInt(strArrayResponse[0]);

            if(i == MOMConstants.NEW_PL_LOGIN_SUCCESS){
                Log.i("LoginResult", "Login successful");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
