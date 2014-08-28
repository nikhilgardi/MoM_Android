package com.mmpl.app;


import com.mmpl.app.R;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabLayoutActivity extends TabActivity {
	TabHost tabHost;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);
		tabHost = getTabHost();
		this.addTab("Mobile",(Context)this,HomeActivity.class); 
	    this.addTab("DTH ",(Context)this,HomeActivity1.class); 
		this.addTab("Bill",(Context)this,HomeActivity2.class); 
		this.addTab("History",(Context)this,HistoryActivity.class);
		this.addTab("Settings",(Context)this,InfoActivity.class);
		getTabWidget().setStripEnabled(false);
		
	}
	public void addTab(String title,Context context, Class<?> IntentClass) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View tabIndicator=inflater.inflate(R.drawable.tabs_bg,null,false);
		TextView tv= (TextView)tabIndicator.findViewById(R.id.tabsText);
		tv.setText(title);
		Intent intent = new Intent(context, IntentClass);
		TabSpec spec = tabHost.newTabSpec(title);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
		
	}
	@Override
	public void onUserLeaveHint() {
		this.finish();

	}
}