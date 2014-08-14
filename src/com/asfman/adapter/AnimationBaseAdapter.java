package com.asfman.adapter;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;

import com.asfman.animation.SpeedScrollListener;

public abstract class AnimationBaseAdapter extends BaseAdapter {

	protected static final long ANIM_DEFAULT_SPEED = 1000L;

	protected Interpolator interpolator;

	protected SparseBooleanArray positionsMapper;
	protected List<? extends Object> items;
	protected int height, width, previousPostition;
	protected SpeedScrollListener scrollListener;
	protected double speed;
	protected long animDuration;
	protected View v;

	@SuppressWarnings("deprecation")
	protected AnimationBaseAdapter(SpeedScrollListener scrollListener, List<? extends Object> items, Context context) {
		this.scrollListener = scrollListener;
		this.items = items;
		int size = 0;
		if (items != null) {
			size = items.size();
		}

		previousPostition = -1;
		positionsMapper = new SparseBooleanArray(size);
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		width = windowManager.getDefaultDisplay().getWidth();
		height = windowManager.getDefaultDisplay().getHeight();

		defineInterpolator();
	}

	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public Object getItem(int position) {
		return items != null && position < items.size() ? items.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getAnimatedView(position, convertView, parent);
	}

	protected abstract View getAnimatedView(int position, View convertView, ViewGroup parent);

	protected abstract void defineInterpolator();
}
