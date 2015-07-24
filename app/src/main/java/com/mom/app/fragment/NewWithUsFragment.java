package com.mom.app.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Transaction;
import com.mom.app.utils.AppConstants;
import com.mom.app.widget.TextListViewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class NewWithUsFragment extends FragmentBase {
    private TextView _titleView;
    private TextView _tvNewWithUs;
    private TextView _tvNoTrans;
    private TextView _tvNewWithUsContact;


    public static NewWithUsFragment newInstance(PlatformIdentifier currentPlatform){
        NewWithUsFragment fragment        = new NewWithUsFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_with_us, null, false);
        String htmlString="<u>This text will be underlined</u>";
        String udata="Underlined Text";


        _currentPlatform        = IdentifierUtils.getPlatformIdentifier(getActivity());
        _titleView              = (TextView) view.findViewById(R.id.transactionHistoryHeader);
        _tvNoTrans              = (TextView) view.findViewById(R.id.noTransactionsMsg);
        _tvNewWithUs            = (TextView) view.findViewById(R.id.newWithUsTxtView);
        _tvNewWithUsContact     = (TextView) view.findViewById(R.id.newWithUsContactTxtView);
       // _titleView.setText(Html.fromHtml(htmlString));
        _titleView.setText(getResources().getString(R.string.lblNewWithUsTitle));

        _tvNoTrans.setText(getResources().getString(R.string.lblNewWithUsContentsRail) + "\n" +
                (getResources().getString(R.string.lblNewWithUsContentsAir)) + "\n" +
                        (getResources().getString(R.string.lblNewWithUsContentsBus)));
        _tvNewWithUs.setText(getResources().getString(R.string.lblNewWithUsContentsMessage));
        _tvNewWithUsContact.setText( getResources().getString(R.string.lblNewWithUsContact));
//        SpannableString content = new SpannableString(udata);
//        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
//        _tvNoTrans.setText(content);

        Log.d("NewWithUs", "onCreate: calling NewWithUs");

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
    }



}

