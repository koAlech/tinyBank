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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.squareup.otto.Subscribe;
import com.tinybank.app.R;
import com.tinybank.app.backend.Server;
import com.tinybank.app.bean.Feed;
import com.tinybank.app.event.EventBus;
import com.tinybank.app.event.UserFeedsEvent;

public class ParentFeedActivity extends Activity {
	
	@InjectView(R.id.feed_listview) ListView listView;
	@InjectView(R.id.accountImageView) ImageView accountImageView;
	@InjectView(R.id.feed_header_balance) TextView feed_header_balance;
	@InjectView(R.id.feed_header_goal) TextView feed_header_goal;
	
	private ParentFeedAdapter parentFeedAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		EventBus.register(this);
		setContentView(R.layout.parent_feed);
		ButterKnife.inject(this);
		
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		setTitle(name);
		
		
		if ("Amitai".equals(name)) {
			accountImageView.setImageResource(R.drawable.user_amitai);//TODO
        } else if ("ET".equals(name)) {
        	accountImageView.setImageResource(R.drawable.user_et);//TODO
        } else if ("Yaniv".equals(name)) {
        	accountImageView.setImageResource(R.drawable.user_yaniv);//TODO
        } else if ("Roni".equals(name)) {
        	accountImageView.setImageResource(R.drawable.roni_profile_image);
        } else {
        	accountImageView.setImageResource(R.drawable.user_amitai);//TODO
        }
		
		double balance = intent.getDoubleExtra("balance", 0);
		NumberFormat numberFormat  = new DecimalFormat("#.00");
		feed_header_balance.setText("$" + numberFormat.format(balance));
		
		//TODO goals
		
		parentFeedAdapter = new ParentFeedAdapter(this);
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(parentFeedAdapter);
        swingBottomInAnimationAdapter.setAbsListView(listView);
		swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);
        listView.setAdapter(swingBottomInAnimationAdapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	  @Override
        	  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	    String username = parentFeedAdapter.getItem(position).getUsername();
        	    
//        	    Intent intent = new Intent(getApplicationContext(), ParentFeedActivity.class);
//    			intent.putExtra("name", username);
//    			startActivity(intent);
    			
        	    Toast.makeText(getApplicationContext(), "Click ListItem Number " + position + " - " + username, Toast.LENGTH_LONG).show();
        	    
        	  }
        	}); 
        
			Server.getUserFeed(name);
	}
	
	@Subscribe
	public void onUserFeedsEventFinished(UserFeedsEvent userFeedsEvent) {
		if (userFeedsEvent.isSuccess()) {
			//Toast.makeText(getApplicationContext(), userFeedsEvent.getFeeds().size(), Toast.LENGTH_LONG).show();
			int count = parentFeedAdapter.getCount();
			for (int i = 0; i < count; i++) {
				parentFeedAdapter.remove(parentFeedAdapter.getItem(i));
	        }
			
			ArrayList<Feed> feeds = userFeedsEvent.getFeeds();
			
			for (int i = 0; i < feeds.size(); i++) {
				Feed feed = feeds.get(i);
				parentFeedAdapter.add(feed);
			}
		} 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.feed, menu);
		return true;
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.feed_logout:
	      Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
	      break;
	    case android.R.id.home:
    		finish();
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
