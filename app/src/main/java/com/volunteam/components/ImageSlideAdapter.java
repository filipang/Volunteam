package com.volunteam.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.volunteam.R;

import java.util.ArrayList;
import java.util.List;

public class ImageSlideAdapter extends PagerAdapter {
    private Context mContext;
    private Voluntariat voluntariat;
    ArrayList<Drawable> drawables;

    public ImageSlideAdapter(Context context, final Voluntariat voluntariat) {
        mContext = context;
        this.voluntariat = voluntariat;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final ImageView img = (ImageView) inflater.inflate(R.layout.image_slider_element, collection, false);
        img.setImageDrawable(voluntariat.getDrawable());
        Log.d("happ", "THIS HAPPENED AT " + position);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        collection.addView(img);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return voluntariat.getImageList().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //ModelObject customPagerEnum = ModelObject.values()[position];
        return "generic title";
    }
}
