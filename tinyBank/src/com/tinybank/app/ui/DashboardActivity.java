package com.tinybank.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.ButterKnife;

import com.tinybank.app.R;
import com.tinybank.app.event.EventBus;

public class DashboardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.register(this);
		setContentView(R.layout.dashboard);
		ButterKnife.inject(this);
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
