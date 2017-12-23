package com.example.desmond.clinc.Controller.Actions;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Desmond on 12/22/2017.
 */

public class SlidingImage extends PagerAdapter {


    private Context context;
    private List<Integer> hotelImages;

    public SlidingImage(Context context, List<Integer> hotelImages) {
        this.context = context;
        this.hotelImages = hotelImages;
    }

    @Override
    public int getCount() {
        return hotelImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setMinimumHeight(300);
        image.setImageResource(hotelImages.get(position));

        (container).addView(image, 0);

        return image;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);

    }
}
