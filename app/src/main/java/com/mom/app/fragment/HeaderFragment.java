package com.mom.app.fragment;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.utils.MOMConstants;

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
            Float balance   = EphemeralStorage.getInstance(getActivity()).getFloat(MOMConstants.USER_BALANCE, MOMConstants.ERROR_BALANCE);
            String sBal         = null;
            if(balance == MOMConstants.ERROR_BALANCE){
                sBal            = getString(R.string.error_getting_balance);
                return view;
            }else {
                DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                sBal = df.format(balance);
            }

            balanceTv.setText("Balance: " + getResources().getString(R.string.Rupee) + sBal);
        }

        return view;
    }
}
