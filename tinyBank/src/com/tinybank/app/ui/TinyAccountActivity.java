package com.tinybank.app.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

public class TinyAccountActivity extends Activity {

	@InjectView(R.id.account_listview) ListView listView;
	@InjectView(R.id.dashboard_header_name) TextView dashboard_header_name;
	@InjectView(R.id.dashboard_header_account) TextView dashboard_header_account;
	@InjectView(R.id.dashboard_header_balance) TextView dashboard_header_balance;
	
	private TinyAccountAdapter tinyAccountAdapter;
	
	private String bank_account_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.register(this);
		setContentView(R.layout.tiny_account);
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
		
		tinyAccountAdapter = new TinyAccountAdapter(this);
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(tinyAccountAdapter);
        swingBottomInAnimationAdapter.setAbsListView(listView);
		swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);
        listView.setAdapter(swingBottomInAnimationAdapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	  @Override
        	  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	    String username = tinyAccountAdapter.getItem(position).getUsername();
        	    Double balance = tinyAccountAdapter.getItem(position).getBalance();
        	    Intent intent = new Intent(getApplicationContext(), ParentFeedActivity.class);
    			intent.putExtra("name", username);
    			intent.putExtra("balance", balance);
    			startActivity(intent);
    			
        	    //Toast.makeText(getApplicationContext(), "Click ListItem Number " + position + " - " + username, Toast.LENGTH_LONG).show();
        	    
        	  }
        	}); 
        
        //Server.getTinyAccounts(bank_account_id);
	}
	
	@Subscribe
	public void onTinyAccountsEventFinished(TinyAccountsEvent tinyAccountsEvent) {
		if (tinyAccountsEvent.isSuccess()) {
			
			int count = tinyAccountAdapter.getCount();
			for (int i = 0; i < count; i++) {
				tinyAccountAdapter.remove(tinyAccountAdapter.getItem(i));
	        }
			
			ArrayList<TinyAccount> tinyAccounts = tinyAccountsEvent.getTinyAccounts();
			
			for (int i = 0; i < tinyAccounts.size(); i++) {
				TinyAccount tinyAccount = tinyAccounts.get(i);
				tinyAccountAdapter.add(tinyAccount);
			}
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Server.getTinyAccounts(bank_account_id);
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.account, menu);
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
