package com.mom.app.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mom.app.R;
import com.mom.app.adapter.ImageTextViewAdapter;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;
import com.mom.app.widget.holder.ImageItem;

/**
 * Created by vaibhavsinha on 10/14/14.
 */
public class SettingsFragment  extends FragmentBase implements AsyncListener<String> {
    String _LOG             = AppConstants.LOG_PREFIX + "SETTINGS";

    GridView _gridView;
    ImageTextViewAdapter _gridViewAdapter;

    public static SettingsFragment newInstance(PlatformIdentifier currentPlatform){
        SettingsFragment fragment   = new SettingsFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, null, false);

        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(getActivity());

        _gridView = (GridView) view.findViewById(R.id.gridView);
        if (_currentPlatform == PlatformIdentifier.MOM) {
            _gridViewAdapter = new ImageTextViewAdapter<MoMScreen>(
                    getActivity(),
                    R.layout.grid_cell,
                    DataProvider.getSettingsScreens(getActivity())
            );

            _gridView.setAdapter(_gridViewAdapter);
            _gridView.setNumColumns(2);
            Log.i("Frag" , DataProvider.getSettingsScreens(getActivity()).toString());
        }else if (_currentPlatform == PlatformIdentifier.PBX) {
            _gridViewAdapter = new ImageTextViewAdapter<MoMScreen>(
                    getActivity(),
                    R.layout.grid_cell,
                    DataProvider.getPBXSettingsScreens(getActivity())
            );
            _gridView.setAdapter(_gridViewAdapter);

        }


        _gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Log.d(_LOG, "Clicked " + position);
                ImageItem<MoMScreen> item = (ImageItem<MoMScreen>) _gridViewAdapter.getItem(position);
                if (item == null) {
                    Log.e(_LOG, "No click target found, returning.");
                    return;
                }
                Log.d(_LOG, "Altering selection to " + !item.getSelected());

                item.setSelected(!item.getSelected());

                if (item.getSelected()) {
                    v.setBackgroundColor(getResources().getColor(R.color.row_selected));
                } else {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }

                Log.d(_LOG, "Going to selected activity");
                nextActivity(item);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
    }

    @Override
    protected void showBalance(float pfBalance) {

    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {

    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    private void nextActivity(ImageItem<MoMScreen> item) {
        MoMScreen screen                            = item.getItem();
        boolean isVerificationNeeded                = true;

       switch (item.getItem()){
//            case CHANGE_MPIN:
//                Log.d(_LOG, "Starting Change M-Pin");
//                intent      = new Intent(this, ChangePINActivity.class);
//                startActivity(intent);
//                Log.d(_LOG, "Started Change M-Pin");
//                break;
//            case CHANGE_TPIN:
//                Log.d(_LOG, "Starting Change T_Pin");
//                intent = new Intent(this, ChangePINActivity.class);
//
//                startActivity(intent);
//                Log.d(_LOG, "Started Change T-Pin");
//                break;
//            case CHANGE_PASSWORD:
//                Log.d(_LOG, "Starting Change Password");
//                intent      = new Intent(this, ChangePBXPasswordActivity.class);
//                startActivity(intent);
//                Log.d(_LOG, "Started Change Password");
//                break;
//        }
           case CHANGE_MPIN:
               Log.d(_LOG, "Starting M-Pin");
               isVerificationNeeded    = (_currentPlatform != PlatformIdentifier.PBX);
               break;
           case CHANGE_TPIN:
               Log.d(_LOG, "Starting T-Pin");
               isVerificationNeeded    = (_currentPlatform != PlatformIdentifier.PBX);
               break;

    }
        if(isVerificationNeeded){

        }

        Bundle bundle                   = new Bundle();
        bundle.putSerializable(AppConstants.BUNDLE_NEXT_SCREEN, screen);
        _callbackListener.processMessage(bundle);
    }
}
