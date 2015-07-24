package com.mom.app.fragment;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Transaction;
import com.mom.app.utils.AppConstants;

import java.util.ArrayList;


public class ContactUsFragment extends FragmentBase  {
    private TextView _titleViewHeader;
    private TextView _tvViewWestZoneHeader;
    private TextView _tvViewWestZone;
    private TextView _tvViewNorthZoneHeader;
    private TextView _tvViewNorthZone;
    private TextView _tvViewEastZoneHeader;
    private TextView _tvViewEastZone;
    private TextView _tvViewSouthZoneHeader;
    private TextView _tvViewSouthZone;

    public static ContactUsFragment newInstance(PlatformIdentifier currentPlatform){
        ContactUsFragment fragment        = new ContactUsFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact_us, null, false);

        _currentPlatform        = IdentifierUtils.getPlatformIdentifier(getActivity());
        _titleViewHeader        = (TextView) view.findViewById(R.id.transactionHistoryHeader);
        _tvViewWestZoneHeader   = (TextView) view.findViewById(R.id.txtViewWestZoneHeader);
        _tvViewWestZone         = (TextView) view.findViewById(R.id.txtViewWestZone);
        _tvViewNorthZoneHeader  = (TextView) view.findViewById(R.id.txtViewNorthZoneHeader);
        _tvViewNorthZone        = (TextView) view.findViewById(R.id.txtViewNorthZone);
        _tvViewEastZoneHeader   = (TextView) view.findViewById(R.id.txtViewEastZoneHeader);
        _tvViewEastZone         = (TextView) view.findViewById(R.id.txtViewEastZone);
        _tvViewSouthZoneHeader  = (TextView) view.findViewById(R.id.txtViewSouthZoneHeader);
        _tvViewSouthZone        = (TextView) view.findViewById(R.id.txtViewSouthZone);
       // _titleView.setText(Html.fromHtml(htmlString));
      //  _titleViewHeader.setText(R.string.CustomerCare);

        _tvViewWestZoneHeader.setText( getResources().getString(R.string.WestZone));
        _tvViewWestZone.setText(getResources().getString(R.string.Timing)+" 24X7" + "\n" +
                getResources().getString(R.string.Phone)+ " +91- 8880881234,"+ "\n" +  "8082812345"  + "\n" +
                getResources().getString(R.string.E_mail) +" customercare@money-on-mobile.com");
        _tvViewNorthZoneHeader.setText( getResources().getString(R.string.NorthZone));
        _tvViewNorthZone.setText( getResources().getString(R.string.Timing)+" IST 09.00am - 09.00pm"  + "\n" +
                getResources().getString( R.string.Phone)+" +91-8880881234,"+ "\n" + "8082812345" + "\n" +
                getResources().getString(R.string.E_mail) +" north.customercare@money-on-mobile.net");
        _tvViewEastZoneHeader.setText( getResources().getString(R.string.EastZone));
        _tvViewEastZone.setText( getResources().getString(R.string.Timing)+" IST 09.00am - 09.00pm "  + "\n" +
                getResources().getString(R.string.Phone)+" +91-8880881234,"+ "\n" + "8082812345" + "\n" +
                getResources().getString(R.string.E_mail)+" east.customercare@money-on-mobile.net");
        _tvViewSouthZoneHeader.setText( getResources().getString(R.string.SouthZone));
        _tvViewSouthZone.setText( getResources().getString(R.string.Timing)+" IST 09.00am - 08.00pm "  + "\n" +
                getResources().getString(R.string.Phone)+" +91- 8880881234," + "\n" + "8082812345" + "\n" +
                getResources().getString(R.string.E_mail)+ " south.customercare@money-on-mobile.net");
        String sCustomerCare =  getResources().getString(R.string.CustomerCare);
        SpannableString content = new SpannableString(sCustomerCare);
        content.setSpan(new UnderlineSpan(), 0, sCustomerCare.length(), 0);
        _titleViewHeader.setText(content);

        Log.d("ContactUs", "onCreate: calling ContactUs");

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
    }







}

