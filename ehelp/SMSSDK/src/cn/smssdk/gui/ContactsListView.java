/*
 * 瀹樼綉鍦扮珯:http://www.mob.com
 * 鎶�湳鏀寔QQ: 4006852216
 * 瀹樻柟寰俊:ShareSDK   锛堝鏋滃彂甯冩柊鐗堟湰鐨勮瘽锛屾垜浠皢浼氱涓�椂闂撮�杩囧井淇″皢鐗堟湰鏇存柊鍐呭鎺ㄩ�缁欐偍銆傚鏋滀娇鐢ㄨ繃绋嬩腑鏈変换浣曢棶棰橈紝涔熷彲浠ラ�杩囧井淇′笌鎴戜滑鍙栧緱鑱旂郴锛屾垜浠皢浼氬湪24灏忔椂鍐呯粰浜堝洖澶嶏級
 *
 * Copyright (c) 2014骞�mob.com. All rights reserved.
 */
package cn.smssdk.gui;

import static com.mob.tools.utils.R.getBitmapRes;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ContactsListView extends RelativeLayout {
	private ListView lvBody;
	private InnerAdapter innerAdapter;
	private GroupAdapter adapter;
	private TextView tvTitle;
	private int curFirstItem;
	private int titleHeight;
	private OnScrollListener osListener;

	public ContactsListView(Context context) {
		super(context);
		init(context);
	}

	public ContactsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ContactsListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		lvBody = new ListView(context);
		lvBody.setCacheColorHint(0);
		lvBody.setSelector(new ColorDrawable());
		int resId = getBitmapRes(context, "smssdk_cl_divider");
		if (resId > 0) {
			lvBody.setDivider(context.getResources().getDrawable(resId));
		}
		lvBody.setDividerHeight(1);
		lvBody.setVerticalScrollBarEnabled(false);
		lvBody.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (osListener != null) {
					osListener.onScrollStateChanged(view, scrollState);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				curFirstItem = firstVisibleItem;
				if (tvTitle != null) {
					ContactsListView.this.onScroll();
				}

				if (osListener != null) {
					osListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
				}
			}
		});
		lvBody.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(lvBody);
	}

	public void setAdapter(GroupAdapter adapter) {
		this.adapter = adapter;
		innerAdapter = new InnerAdapter(adapter);
		lvBody.setAdapter(innerAdapter);
		setTitle();
	}

	public GroupAdapter getAdapter() {
		return adapter;
	}

	private void notifyDataSetChanged() {
		innerAdapter.notifyDataSetChanged();
		setTitle();
	}

	// 璁剧疆鏍囬
	private void setTitle() {
		if (tvTitle != null) {
			removeView(tvTitle);
		}
		if(innerAdapter.getCount() == 0 ){
			return;
		}
		int group = innerAdapter.getItemGroup(curFirstItem);
		int position = innerAdapter.titleIndex.get(group);
		tvTitle = (TextView) innerAdapter.getView(position, null, this);
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_LEFT);
		lp.addRule(ALIGN_PARENT_TOP);
		addView(tvTitle, lp);

		tvTitle.measure(0, 0);
		titleHeight = tvTitle.getMeasuredHeight();
		onScroll();
	}

	public void setSelection(int group) {
		setSelection(group, -1);
	}

	public void setSelection(int group, int position) {
		int titleIndex = innerAdapter.titleIndex.get(group);
		int selection = titleIndex + position + 1;
		lvBody.setSelection(selection);
	}

	private void onScroll() {
		LayoutParams lp = (LayoutParams) tvTitle.getLayoutParams();
		if (innerAdapter.isLastItem(curFirstItem)) {
    		int group = innerAdapter.getItemGroup(curFirstItem);
			String title = adapter.getGroupTitle(group);
			tvTitle.setText(title);
			int top = lvBody.getChildAt(1).getTop();
			if (top < titleHeight) {
				lp.setMargins(0, top - titleHeight, 0, 0);
				tvTitle.setLayoutParams(lp);
				return;
			}
		}
		lp.topMargin = 0;
		tvTitle.setLayoutParams(lp);

		if (innerAdapter.isTitle(curFirstItem)) {
			int group = innerAdapter.getItemGroup(curFirstItem);
			String title = adapter.getGroupTitle(group);
			tvTitle.setText(title);
		}
	}

	// 璁剧疆婊氬姩鐩戝惉
	public void setOnScrollListener(OnScrollListener l) {
		osListener = l;
	}
	/** 鑷畾涔塴istview鐨勯�閰嶅櫒*/
	private static class InnerAdapter extends BaseAdapter {
		private GroupAdapter adapter;
		private ArrayList<Object> listData;
		private ArrayList<Integer> titleIndex;
		private ArrayList<Integer> lastItemIndex;

		public InnerAdapter(GroupAdapter adapter) {
			this.adapter = adapter;
			listData = new ArrayList<Object>();
			titleIndex = new ArrayList<Integer>();
			lastItemIndex = new ArrayList<Integer>();
			init();
		}

		private void init() {
			listData.clear();
			titleIndex.clear();
			lastItemIndex.clear();
			for (int g = 0, gc = adapter.getGroupCount(); g < gc; g++) {
				int c = adapter.getCount(g);
				if (c > 0) {
					titleIndex.add(listData.size());
					listData.add(adapter.getGroupTitle(g));
					for (int i = 0; i < c; i++) {
						listData.add(adapter.getItem(g, i));
					}
					lastItemIndex.add(listData.size() - 1);
				}
			}
		}

		public int getCount() {
			return listData.size();
		}

		public Object getItem(int position) {
			return listData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public int getItemGroup(int position) {
			int size = titleIndex.size();
			for (int i = 0; i < size; i++) {
				int titleIndex = this.titleIndex.get(i);
				if (position < titleIndex) {
					return i - 1;
				}
			}
			return size - 1;
		}

		public boolean isTitle(int position) {
			for (int i = 0, size = titleIndex.size(); i < size; i++) {
				if (titleIndex.get(i) == position) {
					return true;
				}
			}
			return false;
		}

		public int getViewTypeCount() {
			return 2;
		}

		public int getItemViewType(int position) {
			return isTitle(position) ? 0 : 1;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			int group = getItemGroup(position);
			if (isTitle(position)) {
				if (convertView != null && (convertView instanceof TextView)) {
					convertView = adapter.getTitleView(group, (TextView) convertView, parent);
				} else {
					convertView = adapter.getTitleView(group, null, parent);
				}

			} else {
				int item = position - titleIndex.get(group) - 1;
				convertView = adapter.getView(group, item, convertView, parent);
			}
			return convertView;
		}

		public void notifyDataSetChanged() {
			init();
			super.notifyDataSetChanged();
		}

		public boolean isLastItem(int position) {
			for (int i = 0, size = lastItemIndex.size(); i < size; i++) {
				if (lastItemIndex.get(i) == position) {
					return true;
				}
			}
			return false;
		}

	}
	/** 鑷畾涔夌粍鐨刟bstract绫伙紝鐢ㄤ簬濉厖listview鐨刬tem*/
	public static abstract class GroupAdapter {
		protected final ContactsListView view;

		public GroupAdapter(ContactsListView view) {
			this.view = view;
		}
		/** 鑾峰彇缁勭殑鏁版嵁涓暟*/
		public abstract int getGroupCount();

		/**
		 * 鑾峰彇鏌愮粍鐨刬tem涓暟
		 * @param group
		 * @return
		 */
		public abstract int getCount(int group);

		/**
		 * 鑾峰彇鏌愮粍鐨則itle鏍囩
		 * @param group
		 * @return
		 */
		public abstract String getGroupTitle(int group);

		/**
		 * 鑾峰彇鏌愮粍绗琾ositon鐨勪綅缃殑鏁版嵁瀵硅薄
		 * @param group
		 * @param position
		 * @return
		 */
		public abstract Object getItem(int group, int position);

		/**
		 * 鑾峰彇鏌愮粍鐨則itle鐨刅iew
		 * @param group
		 * @param convertView
		 * @param parent
		 * @return
		 */
		public abstract TextView getTitleView(int group, TextView convertView, ViewGroup parent);
		/**
		 * 鑾峰彇鏌愮粍绗琾ositon鐨勪綅缃殑item View
		 * @param group
		 * @param position
		 * @param convertView
		 * @param parent
		 * @return
		 */
		public abstract View getView(int group, int position, View convertView, ViewGroup parent);

		public void notifyDataSetChanged() {
			view.notifyDataSetChanged();
		}

	}

}
