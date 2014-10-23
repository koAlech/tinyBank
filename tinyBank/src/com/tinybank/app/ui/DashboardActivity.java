package com.tinybank.app.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.squareup.otto.Subscribe;
import com.tinybank.app.R;
import com.tinybank.app.backend.Server;
import com.tinybank.app.bean.TinyAccount;
import com.tinybank.app.event.EventBus;
import com.tinybank.app.event.TinyAccountsEvent;

public class DashboardActivity extends Activity {

	@InjectView(R.id.activity_googlecards_listview) ListView listView;
	@InjectView(R.id.dashboard_header_name) TextView dashboard_header_name;
	@InjectView(R.id.dashboard_header_account) TextView dashboard_header_account;
	@InjectView(R.id.dashboard_header_balance) TextView dashboard_header_balance;
	
	private GoogleCardsAdapter mGoogleCardsAdapter;
	
	private String bank_account_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.register(this);
		setContentView(R.layout.dashboard);
		ButterKnife.inject(this);
		
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		double balance = intent.getDoubleExtra("balance", 0);
		String bank_name = intent.getStringExtra("bank_name");
		bank_account_id = intent.getStringExtra("bank_account_id");
		
		dashboard_header_name.setText("Good morning " + name);
		dashboard_header_account.setText("Account #"+bank_account_id+" @ " + bank_name);
		NumberFormat numberFormat  = new DecimalFormat("#.00");
		dashboard_header_balance.setText("Balance $" + numberFormat.format(balance));
		
		mGoogleCardsAdapter = new GoogleCardsAdapter(this);
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mGoogleCardsAdapter);
        swingBottomInAnimationAdapter.setAbsListView(listView);
		swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);
        listView.setAdapter(swingBottomInAnimationAdapter);
        
        Server.getTinyAccounts(bank_account_id);
	}
	
	@Subscribe
	public void onTinyAccountsEventFinished(TinyAccountsEvent tinyAccountsEvent) {
		if (tinyAccountsEvent.isSuccess()) {
			
			int count = mGoogleCardsAdapter.getCount();
			for (int i = 0; i < count; i++) {
	            mGoogleCardsAdapter.remove(mGoogleCardsAdapter.getItem(i));
	        }
			
			ArrayList<TinyAccount> tinyAccounts = tinyAccountsEvent.getTinyAccounts();
			
			for (int i = 0; i < tinyAccounts.size(); i++) {
				TinyAccount tinyAccount = tinyAccounts.get(i);
				mGoogleCardsAdapter.add(tinyAccount);
			}
			
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_add_tinyAccount:
	      Toast.makeText(this, "Add tinyAccount", Toast.LENGTH_SHORT).show();
	      break;
	    case R.id.action_logout:
	      Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
	      break;
	    default:
	      break;
	    }
	    return true;
	  } 
	
	@Override
	protected void onDestroy() {
		EventBus.unregister(this);
		super.onDestroy();
	}
	
}
