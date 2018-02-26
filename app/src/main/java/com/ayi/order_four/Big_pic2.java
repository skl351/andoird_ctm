package com.ayi.order_four;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ayi.R;
import com.ayi.zidingyi_view.ZoomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class Big_pic2 extends Activity
{
	private ViewPager mViewPager;

	private ImageView[] mImageViews ;

	private List<String> list;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vp);
		list= (List<String>) getIntent().getSerializableExtra("imgs");

		mImageViews=new ImageView[list.size()];
		
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mViewPager.setAdapter(new PagerAdapter()
		{

			@Override
			public Object instantiateItem(ViewGroup container, int position)
			{
				ZoomImageView imageView = new ZoomImageView(
						getApplicationContext());
				ImageLoader.getInstance().displayImage(list.get(position), imageView);
				container.addView(imageView);
				mImageViews[position] = imageView;
				return imageView;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object)
			{
				container.removeView(mImageViews[position]);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1)
			{
				return arg0 == arg1;
			}

			@Override
			public int getCount()
			{
				return list.size();
			}
		});
		mViewPager.setCurrentItem(getIntent().getIntExtra("flag",0));

	}
}
