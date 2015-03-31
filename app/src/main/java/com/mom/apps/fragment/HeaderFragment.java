package com.mom.apps.fragment;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mom.apps.R;
import com.mom.apps.model.local.EphemeralStorage;
import com.mom.apps.utils.AppConstants;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class HeaderFragment extends Fragment {

    public static HeaderFragment newInstance() {
        HeaderFragment fragment = new HeaderFragment();

        return fragment;
    }
    public HeaderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        TextView balanceTv  = (TextView) view.findViewById(R.id.balance);
        if(balanceTv != null){
            Float balance   = EphemeralStorage.getInstance(getActivity()).getFloat(AppConstants.USER_BALANCE, AppConstants.ERROR_BALANCE);
            String sBal         = null;
            if(balance == AppConstants.ERROR_BALANCE){
                sBal            = getString(R.string.error_getting_balance);
                return view;
            }else {
                DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                sBal = df.format(balance);
            }

            balanceTv.setText(getResources().getString(R.string.lblBal) + getResources().getString(R.string.Rupee) + sBal);
        }

        return view;
    }
}
