package com.volunteam.components;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageSlideAdapter extends PagerAdapter {
    private Context mContext;
    private Voluntariat voluntariat;

    public ImageSlideAdapter(Context context, Voluntariat voluntariat) {
        mContext = context;
        this.voluntariat = voluntariat;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ImageView img = new ImageView(mContext);
        img.setImageDrawable(Voluntariat.loadDrawableFromURL(voluntariat.getImageList().get(position)));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
