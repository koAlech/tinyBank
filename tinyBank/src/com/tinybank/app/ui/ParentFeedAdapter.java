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
		super(context, R.layout.tiny_account_item);
		mContext = context;
        mMemoryCache = new BitmapCache();
	}
    
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.tiny_account_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) view.findViewById(R.id.dashboard_account_name);
            viewHolder.textViewBalance = (TextView) view.findViewById(R.id.dashboard_account_balance);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.dashboard_account_imageview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textViewName.setText(getItem(position).getUsername());
        NumberFormat numberFormat  = new DecimalFormat("#.00");
        String balance = numberFormat.format(getItem(position).getAmount());
        viewHolder.textViewBalance.setText("Account balance: $"+balance);
        setImageView(viewHolder, position);

        return view;
    }

    private void setImageView(final ViewHolder viewHolder, final int position) {
        int imageResId;
        String username = getItem(position).getUsername();
        if ("Amitai".equals(username)) {
        	imageResId = R.drawable.user_amitai;
        } else if ("ET".equals(username)) {
        	imageResId = R.drawable.user_et;
        } else if ("Yaniv".equals(username)) {
        	imageResId = R.drawable.user_yaniv;
        } else if ("Roni".equals(username)) {
        	imageResId = R.drawable.user_roni;
        } else {
        	imageResId = R.drawable.user_amitai;
        }
//        switch (getItem(position) % 5) {
//            case 0:
//                imageResId = R.drawable.img_nature1;
//                break;
//            case 1:
//                imageResId = R.drawable.img_nature2;
//                break;
//            case 2:
//                imageResId = R.drawable.img_nature3;
//                break;
//            default:
//                imageResId = R.drawable.img_nature3;
//        }
        
        
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
        TextView textViewBalance;
        ImageView imageView;
    }
}
