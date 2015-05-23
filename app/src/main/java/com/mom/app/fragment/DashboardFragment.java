package com.mom.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mom.app.R;

import com.mom.app.identifier.PlatformIdentifier;

import com.mom.app.model.IDataEx;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;
import com.mom.app.adapter.ImageTextViewAdapter;
import com.mom.app.widget.holder.ImageItem;

/**
 * Created by vaibhavsinha on 10/13/14.
 */
public class DashboardFragment extends FragmentBase {
    String _LOG         = AppConstants.LOG_PREFIX + "DASHBOARD";

    GridView gridView;
    IDataEx _dataEx     = null;
    ImageTextViewAdapter gridViewAdapter;

    public static DashboardFragment newInstance(PlatformIdentifier currentPlatform){
        DashboardFragment fragment      = new DashboardFragment();
        Bundle bundle                   = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view           = inflater.inflate(R.layout.activity_dashboard, null, false);
        Log.i(_LOG, "started onCreateView");

        gridView            = (GridView) view.findViewById(R.id.gridView);
        gridViewAdapter     = new ImageTextViewAdapter<MoMScreen>(
                getActivity(),
                R.layout.grid_cell,
                DataProvider.getScreens(getActivity(), _currentPlatform, true)
        );

        gridView.setAdapter(gridViewAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Log.d(_LOG, "Clicked " + position);
                ImageItem item  = (ImageItem) gridViewAdapter.getItem(position);
                if(item == null){
                    Log.e(_LOG, "No click target found, returning.");
                    return;
                }

                Log.d(_LOG, "Altering selection to " + !item.getSelected());

                item.setSelected(!item.getSelected());

                Log.d(_LOG, "Going to selected activity");
                showScreen(item);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(
                AppConstants.ACTIVE_PLATFORM
        );
    }



    private void showScreen(ImageItem<MoMScreen> item){
        MoMScreen screen                            = item.getItem();
        boolean isVerificationNeeded                = true;

        switch (item.getItem()){
            case MOBILE_RECHARGE:
                Log.d(_LOG, "Starting Mobile Recharge");
                isVerificationNeeded    = (_currentPlatform != PlatformIdentifier.PBX);
                break;
            case DTH_RECHARGE:
                Log.d(_LOG, "Starting DTH Recharge");
                isVerificationNeeded    = (_currentPlatform != PlatformIdentifier.PBX);
                break;
            case BILL_PAYMENT:
                Log.d(_LOG, "Starting Bill Payment");
                isVerificationNeeded    = (_currentPlatform != PlatformIdentifier.PBX);
                break;
            case BALANCE_TRANSFER:
                Log.d(_LOG, "Starting Balance Transfer");
                isVerificationNeeded    = (_currentPlatform != PlatformIdentifier.PBX);
                break;

            case LIC:
                Log.d(_LOG, "Starting Transaction History Activity");
                isVerificationNeeded    = (_currentPlatform != PlatformIdentifier.PBX);
                break;


            case IMPS:
                Log.d(_LOG, "Starting Transaction IMPS Activity");
                isVerificationNeeded    = (_currentPlatform != PlatformIdentifier.PBX);
                break;

            case HISTORY:
                Log.d(_LOG, "Starting Transaction History Activity");
                isVerificationNeeded    = false;
                break;

            case SETTINGS:
                Log.d(_LOG, "Starting Settings Activity");
                isVerificationNeeded    = false;
                break;
            case LOGOUT:
                Log.d(_LOG, "Logout called");
                isVerificationNeeded    = false;
                break;
        }

        /**
         * Get T-PIN from the user through a pop-up.
         */
        if(isVerificationNeeded){

        }

        Bundle bundle                   = new Bundle();
        bundle.putSerializable(AppConstants.BUNDLE_NEXT_SCREEN, screen);
        _callbackListener.processMessage(bundle);
    }
}
