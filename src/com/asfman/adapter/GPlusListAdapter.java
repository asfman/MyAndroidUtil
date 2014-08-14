package com.asfman.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.asfman.animation.SpeedScrollListener;

public abstract class GPlusListAdapter extends AnimationBaseAdapter {

	public GPlusListAdapter(SpeedScrollListener scrollListener, List<? extends Object> items, Context context) {
		super(scrollListener, items, context);
	}

	@Override
	protected void defineInterpolator() {
		interpolator = new DecelerateInterpolator();
	}

	@Override
	public View getAnimatedView(int position, View convertView, ViewGroup parent) {
		v = getRowView(position, convertView, parent);

		if (v != null && !positionsMapper.get(position) && position > previousPostition) {
			speed = scrollListener.getSpeed();

			animDuration = (((int) speed) == 0) ? ANIM_DEFAULT_SPEED : (long) (1 / speed * 15000);

			if (animDuration > ANIM_DEFAULT_SPEED)
				animDuration = ANIM_DEFAULT_SPEED;

			previousPostition = position;
//			PropertyValuesHolder pvTX = PropertyValuesHolder.ofFloat("translationX", 0.0F, 0.0F);
//			PropertyValuesHolder pvTY = PropertyValuesHolder.ofFloat("translationY", height, 0.0F);
//			PropertyValuesHolder pvSX = PropertyValuesHolder.ofFloat("scaleX", 0.7F, 1.0F);
//			PropertyValuesHolder pvSY = PropertyValuesHolder.ofFloat("scaleY", 0.8F, 1.0F);
//			PropertyValuesHolder pvRX = PropertyValuesHolder.ofFloat("rotationX", 15.0F, 0.0F);
//			ObjectAnimator whxyBouncer = ObjectAnimator.ofPropertyValuesHolder(v, pvTX, pvTY, pvSX, pvSY,pvRX).setDuration(animDuration);
//			whxyBouncer.setInterpolator(interpolator);
			
			  v.setTranslationX(0.0F);
			  v.setTranslationY(height);
			  v.setRotationX(50.0F);
			  v.setScaleX(0.7F);
			  v.setScaleY(0.8F);
			 v.animate().rotationX(0.0F).rotationY(0.0F).translationX(0).translationY(0).setDuration(animDuration).scaleX(
			 1.0F).scaleY(1.0F).setInterpolator(interpolator);
			
//			whxyBouncer.start();
			positionsMapper.put(position, true);
		}
		return v;
	}

	/**
	 * Get a View that displays the data at the specified position in the data
	 * set. You can either create a View manually or inflate it from an XML
	 * layout file. When the View is inflated, the parent View (GridView,
	 * ListView...) will apply default layout parameters unless you use
	 * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
	 * to specify a root view and to prevent attachment to the root.
	 * 
	 * @param position
	 *            The position of the item within the adapter's data set of the
	 *            item whose view we want.
	 * @param convertView
	 *            The old view to reuse, if possible. Note: You should check
	 *            that this view is non-null and of an appropriate type before
	 *            using. If it is not possible to convert this view to display
	 *            the correct data, this method can create a new view.
	 * @param parent
	 *            The parent that this view will eventually be attached to
	 * @return A View corresponding to the data at the specified position.
	 */
	protected abstract View getRowView(int position, View convertView, ViewGroup parent);

}
