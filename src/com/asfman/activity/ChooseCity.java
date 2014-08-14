package com.asfman.activity;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.asfman.R;
import com.asfman.util.FileUtils;
import com.asfman.widget.SideBar;
import com.asfman.widget.SideBar.OnTouchingLetterChangedListener;

public class ChooseCity extends Activity {

	private CityAdapter cityAdapter;
	private SideBar sideBar;
	private TextView dialog;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switch_city);
		listView = (ListView) findViewById(R.id.list);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		initHeaderView();
		ArrayList<City> arrayList = new ArrayList<City>();
		try {
			String cityJSONString = FileUtils.getFromAssets("city_json.txt",
					this);
			JSONArray jsonArray = new JSONArray(cityJSONString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject itemObject = jsonArray.getJSONObject(i);
				@SuppressWarnings("unchecked")
				Iterator<String> keys = itemObject.keys();
				if (keys.hasNext()) {
					String key = keys.next();
					JSONArray cityArray = itemObject.getJSONArray(key);
					for (int j = 0; j < cityArray.length(); j++) {
						String value = cityArray.getJSONObject(j).getString(
								"name");
						City city = new City();
						city.setName(value);
						city.setSectionName(key);
						arrayList.add(city);
					}

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		cityAdapter = new CityAdapter(this, arrayList);
		listView.setAdapter(cityAdapter);
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 已经停止
					dialog.setVisibility(View.INVISIBLE);
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 开始滚动
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 正在滚动
					dialog.setVisibility(View.VISIBLE);
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
					City city = cityAdapter.getItem(firstVisibleItem);
					dialog.setText(city.getSectionName());
			}
		});

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = cityAdapter.getPositionForSection(s.charAt(0));
				Log.i("info", "position: " + position);
				if (position != -1) {
					listView.setSelection(position <= 1 ? 0 : position + listView.getHeaderViewsCount());
				}

			}
		});
	}

	private void initHeaderView() {
		View headerView = LayoutInflater.from(this).inflate(R.layout.item_switch_city_header, null);
		listView.addHeaderView(headerView);
		TextView nameTextView = (TextView) headerView
				.findViewById(com.asfman.R.id.title);
		TextView sectionTextView = (TextView) headerView
				.findViewById(com.asfman.R.id.catalog);	
		sectionTextView.setText("GPS定位城市");
		nameTextView.setText("正在定位");
	}

}

class City {
	private String name;
	private String sectionName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
}

class CityAdapter extends BaseAdapter implements SectionIndexer {

	private Context mContext;
	private ArrayList<City> cityList;
	private static enum VIEW_TYPE {
		HEADER,
		ITEM
	}

	public CityAdapter(Context mContext, ArrayList<City> cityList) {
		this.mContext = mContext;
		this.cityList = cityList;
	}

	@Override
	public int getCount() {
		return cityList.size();
	}

	@Override
	public City getItem(int position) {
		return cityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public interface ViewHolder<T> {
		public void init(View v);
		public void render(T t);
	}
	
	final static class ViewHolderHeader implements ViewHolder<City> {
		TextView tvLetter;
		TextView tvTitle;
		@Override
		public void init(View v) {
			tvTitle = (TextView) v
					.findViewById(com.asfman.R.id.title);
			tvLetter = (TextView) v
					.findViewById(com.asfman.R.id.catalog);
		}
		@Override
		public void render(City city) {
			String sectionName = city.getSectionName();
			if(sectionName.toLowerCase().equals("a")) {
				tvLetter.setText("全部城市");
			} else {
				tvLetter.setText(sectionName);
			}
			tvTitle.setText(city.getName());
		}
	}
	
	final static class ViewHolderItem implements ViewHolder<City> {
		TextView tvTitle;
		@Override
		public void init(View v) {
			tvTitle = (TextView) v
					.findViewById(com.asfman.R.id.title);
		}
		@Override
		public void render(City city) {
			tvTitle.setText(city.getName());
		}
	}

	@Override
	public int getItemViewType(int position) {
		City city = getItem(position);
		String sectionName = city.getSectionName();
		int sectionFirstIndex = getPositionForSection(getSectionForPosition(position));
		if (position == sectionFirstIndex
				&& !"bcdefghijklmnopqrstuvwxyz".contains(sectionName.toLowerCase())) {
			return VIEW_TYPE.HEADER.ordinal();
		}
		return VIEW_TYPE.ITEM.ordinal();
		
	}
	
	private VIEW_TYPE getItemType(int position) {
		City city = getItem(position);
		String sectionName = city.getSectionName();
		int sectionFirstIndex = getPositionForSection(getSectionForPosition(position));
		if (position == sectionFirstIndex
				&& !"bcdefghijklmnopqrstuvwxyz".contains(sectionName.toLowerCase())) {
			return VIEW_TYPE.HEADER;
		}
		return VIEW_TYPE.ITEM;
		
	}
	
	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE.values().length;
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder<City> viewHolder = null;
		City city = getItem(position);
		VIEW_TYPE itemViewType = getItemType(position);
		Log.i("info", "position: " + position + ", itemViewType: " + itemViewType);
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			switch (itemViewType) {
			case HEADER:
				convertView = inflater.inflate(
						R.layout.item_switch_city_header, null);
				viewHolder = new ViewHolderHeader();
				break;

			default:
				convertView = inflater.inflate(
						R.layout.item_switch_city, null);
				viewHolder = new ViewHolderItem();
				break;
			}
			viewHolder.init(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder<City>) convertView.getTag();
		}
		viewHolder.render(city);
		return convertView;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int sectionChar) {
		for (int i = 0; i < getCount(); i++) {
			City city = getItem(i);
			int sectionNameChar = (int) (city.getSectionName().charAt(0));
			if (sectionNameChar == sectionChar) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		City city = getItem(position);
		return city.getSectionName().charAt(0);
	}

}
