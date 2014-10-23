package com.tinybank.app.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tinybank.app.R;
import com.tinybank.app.bean.Feed;
import com.tinybank.app.ui.util.BitmapCache;

public class ParentFeedAdapter extends ArrayAdapter<Feed> {

	private final Context mContext;
    private final BitmapCache mMemoryCache;

    public ParentFeedAdapter(Context context) {
		super(context, R.layout.parent_feed_item);
		mContext = context;
        mMemoryCache = new BitmapCache();
	}
    
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.parent_feed_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) view.findViewById(R.id.parent_feed_name);
            viewHolder.textViewDesc = (TextView) view.findViewById(R.id.parent_feed_desc);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.parent_feed_imageview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        
        String type = getItem(position).getType();
        String description = getItem(position).getDescription();
        NumberFormat numberFormat  = new DecimalFormat("#.00");
        Double amount = getItem(position).getAmount();
        if ("deposit".equals(type)) {
        	viewHolder.textViewName.setText("Deposit");
        	if (amount != null) {
            	String balance = numberFormat.format(getItem(position).getAmount());
            	viewHolder.textViewDesc.setText("$"+balance+" - "+description);
            } else {
            	viewHolder.textViewDesc.setText("");
            }
        } else if ("withdrawal".equals(type)) {
        	viewHolder.textViewName.setText("Withdrawal");
        	if (amount != null) {
            	String balance = numberFormat.format(getItem(position).getAmount());
            	viewHolder.textViewDesc.setText("$"+balance+" - "+description);
            } else {
            	viewHolder.textViewDesc.setText("");
            }
        } else if ("badge".equals(type)) {
        	viewHolder.textViewName.setText("Badge Earned");
        	viewHolder.textViewDesc.setText(description);
        } else if ("goal_updated".equals(type)) {
        	viewHolder.textViewName.setText("Goal Updated");
        	if (amount != null) {
            	String balance = numberFormat.format(getItem(position).getAmount());
            	viewHolder.textViewDesc.setText("Added $"+balance+" - "+description);
            } else {
            	viewHolder.textViewDesc.setText("");
            }
        } else {
        	viewHolder.textViewName.setText("Deposit");
        }
        
        /*
        NumberFormat numberFormat  = new DecimalFormat("#.00");
        Double amount = getItem(position).getAmount();
        if (amount != null) {
        	String balance = numberFormat.format(getItem(position).getAmount());
        	viewHolder.textViewBalance.setText("Account balance: $"+balance);
        } else {
        	viewHolder.textViewBalance.setText("");
        }
        */
        
        setImageView(viewHolder, position);

        return view;
    }

    private void setImageView(final ViewHolder viewHolder, final int position) {
        int imageResId;
        String type = getItem(position).getType();
        if ("deposit".equals(type)) {
        	imageResId = R.drawable.coin_card_gold;
        } else if ("withdrawal".equals(type)) {
        	imageResId = R.drawable.coin_card_red;
        } else if ("badge".equals(type)) {
        	imageResId = R.drawable.badge_card;
        } else if ("goal_updated".equals(type)) {
        	imageResId = R.drawable.rocket_card;
        } else {
        	imageResId = R.drawable.coin_card_gold;
        }
        
        Bitmap bitmap = getBitmapFromMemCache(imageResId);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
            addBitmapToMemoryCache(imageResId, bitmap);
        }
        viewHolder.imageView.setImageBitmap(bitmap);
    }

    private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(final int key) {
        return mMemoryCache.get(key);
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textViewName;
        TextView textViewDesc;
        ImageView imageView;
    }
}
