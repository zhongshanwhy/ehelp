/*
 * 瀹樼綉鍦扮珯:http://www.mob.com
 * 鎶�湳鏀寔QQ: 4006852216
 * 瀹樻柟寰俊:ShareSDK   锛堝鏋滃彂甯冩柊鐗堟湰鐨勮瘽锛屾垜浠皢浼氱涓�椂闂撮�杩囧井淇″皢鐗堟湰鏇存柊鍐呭鎺ㄩ�缁欐偍銆傚鏋滀娇鐢ㄨ繃绋嬩腑鏈変换浣曢棶棰橈紝涔熷彲浠ラ�杩囧井淇′笌鎴戜滑鍙栧緱鑱旂郴锛屾垜浠皢浼氬湪24灏忔椂鍐呯粰浜堝洖澶嶏級
 *
 * Copyright (c) 2014骞�mob.com. All rights reserved.
 */
package cn.smssdk.gui;

import static com.mob.tools.utils.R.dipToPx;
import static com.mob.tools.utils.R.getStringRes;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import cn.smssdk.gui.ContactsListView.GroupAdapter;


public class ContactsAdapter extends GroupAdapter {
	private ArrayList<String> titles;
	private ArrayList<ArrayList<HashMap<String, Object>>> contacts;

	private ArrayList<HashMap<String, Object>> friendsInApp = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> contactsOutApp = new ArrayList<HashMap<String, Object>>();

	private ContactItemMaker itemMaker;
	private SearchEngine sEngine;

	public ContactsAdapter(ContactsListView view, ArrayList<HashMap<String, Object>> friendsInApp,
			ArrayList<HashMap<String, Object>> contactsOutApp) {
		super(view);
		this.friendsInApp = friendsInApp;
		this.contactsOutApp = contactsOutApp;
		initSearchEngine();
		search(null);
	}

	private void initSearchEngine() {
		sEngine = new SearchEngine();
		ArrayList<String> data = new ArrayList<String>();
		for (HashMap<String, Object> contact : friendsInApp) {
			String name = "";
			if (contact.containsKey("displayname")) {
				name = String.valueOf(contact.get("displayname"));
			}
			if (TextUtils.isEmpty(name)) {
				continue;
			}
			data.add(name);
		}
		for (HashMap<String, Object> contact : contactsOutApp) {
			String name = "";
			if (contact.containsKey("displayname")) {
				name = String.valueOf(contact.get("displayname"));
			}
			if (TextUtils.isEmpty(name)) {
				continue;
			}
			data.add(name);
		}
		sEngine.setIndex(data);
	}

	/**
	 * 鎼滅储
	 * @param token  鎼滅储鐨勫叧閿瓧
	 */
	public void search(String token) {
		ArrayList<String> res = sEngine.match(token);
		boolean isEmptyToken = false;
		if (res == null || res.size() <= 0) {
			res = new ArrayList<String>();
			isEmptyToken = true;
		}

		HashMap<String, String> resMap = new HashMap<String, String>();
		for (String r : res) {
			resMap.put(r, r);
		}

		titles = new ArrayList<String>();
		contacts = new ArrayList<ArrayList<HashMap<String, Object>>>();



		if (friendsInApp.size() > 0) {
			reSortFia(resMap, isEmptyToken, friendsInApp);
		}
		if (contactsOutApp.size() > 0) {
			reSortFoa(resMap, isEmptyToken, contactsOutApp);
		}

	}

	
	private void reSortFia(HashMap<String, String> resMap, boolean isEmptyToken,
			ArrayList<HashMap<String, Object>> data) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (HashMap<String, Object> contact : data) {
			String name = "";
			if (contact.containsKey("displayname")) {
				name = String.valueOf(contact.get("displayname"));
			}
			if (TextUtils.isEmpty(name)) {
				continue;
			}
			if (isEmptyToken || resMap.containsKey(name)) {
				list.add(contact);
			}
		}

		if (list.size() > 0) {
			int resId = getStringRes(view.getContext(), "smssdk_contacts_in_app");
			if (resId > 0) {
				titles.add(view.getContext().getResources().getString(resId));
			}
			contacts.add(list);
		}
	}

	/** 鏁版嵁澶勭悊 ,瀵逛笉鏄簲鐢ㄥ唴濂藉弸鍒楄〃鐨勬暟鎹繘琛屾帓鍒�*/
	private void reSortFoa(HashMap<String, String> resMap, boolean isEmptyToken,
			ArrayList<HashMap<String, Object>> data) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (HashMap<String, Object> contact : data) {
			String name = "";
			if (contact.containsKey("displayname")) {
				name = String.valueOf(contact.get("displayname"));
			}
			if (TextUtils.isEmpty(name)) {
				continue;
			}
			if (isEmptyToken || resMap.containsKey(name)) {
				list.add(contact);
			}
		}

		if (list.size() > 0) {
			int resId = getStringRes(view.getContext(), "smssdk_contacts_out_app");
			if (resId > 0) {
				titles.add(view.getContext().getResources().getString(resId));
			}
			contacts.add(list);
		}
	}

	public void setContactItemMaker(ContactItemMaker itemMaker) {
		this.itemMaker = itemMaker;
	}

//	/** 鏁版嵁澶勭悊 */
//	private void reSortFia(String token, ArrayList<HashMap<String, Object>> data) {
//		boolean isEmptyToken = TextUtils.isEmpty(token);
//		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//		for (HashMap<String, Object> contact : data) {
//			String name = "";
//			if (contact.containsKey("nickname")) {
//				name = String.valueOf(contact.get("nickname"));
//			} else if (contact.containsKey("displayname")) {
//				name = String.valueOf(contact.get("displayname"));
//			} else if (contact.containsKey("phone")) {
//				name = String.valueOf(contact.get("phone"));
//			}
//			if (TextUtils.isEmpty(name)) {
//				continue;
//			}
//			if (isEmptyToken || (!TextUtils.isEmpty(name) && name.contains(token))) {
//				list.add(contact);
//			}
//		}
//
//		if (list.size() > 0) {
//			int resId = getStringRes(view.getContext(), "smssdk_contacts_in_app");
//			if (resId > 0) {
//				titles.add(view.getContext().getResources().getString(resId));
//			}
//			contacts.add(list);
//		}
//	}
//
//	/** 鏁版嵁澶勭悊 */
//	private void reSortFoa(String token, ArrayList<HashMap<String, Object>> data) {
//		boolean isEmptyToken = TextUtils.isEmpty(token);
//		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//		for (HashMap<String, Object> contact : data) {
//			String name = "";
//			if (contact.containsKey("displayname")) {
//				name = String.valueOf(contact.get("displayname"));
//			} else if (contact.containsKey("phones")) {
//				@SuppressWarnings("unchecked")
//				ArrayList<HashMap<String, Object>> phones
//						= (ArrayList<HashMap<String, Object>>) contact.get("phones");
//				if (phones != null && phones.size() > 0) {
//					name = (String) phones.get(0).get("phone");
//				}
//			}
//			if (TextUtils.isEmpty(name)) {
//				continue;
//			}
//			if (isEmptyToken || (!TextUtils.isEmpty(name) && name.contains(token))) {
//				list.add(contact);
//			}
//		}
//
//		if (list.size() > 0) {
//			int resId = getStringRes(view.getContext(), "smssdk_contacts_out_app");
//			if (resId > 0) {
//				titles.add(view.getContext().getResources().getString(resId));
//			}
//			contacts.add(list);
//		}
//	}

	public int getGroupCount() {
		return titles == null ? 0 : titles.size();
	}

	public int getCount(int group) {
		if (contacts == null) {
			return 0;
		}

		ArrayList<HashMap<String, Object>> list = contacts.get(group);
		if (list == null) {
			return 0;
		}

		return list.size();
	}

	public String getGroupTitle(int group) {
		if (titles.size() > 0) {
			return titles.get(group).toString();
		} else {
			return null;
		}
	}

	public HashMap<String, Object> getItem(int group, int position) {
		if (contacts.size() > 0) {
			return contacts.get(group).get(position);
		} else {
			return null;
		}
	}

	public TextView getTitleView(int group, TextView convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new TextView(parent.getContext());
			convertView.setBackgroundColor(0xffeae8ee);
			convertView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			convertView.setTextColor(0xff999999);
			int dp_11 = dipToPx(parent.getContext(), 11);
			convertView.setPadding(dp_11, 0, 0, 0);
			convertView.setWidth(LayoutParams.MATCH_PARENT);
			int dp_26 = dipToPx(parent.getContext(), 26);
			convertView.setHeight(dp_26);
			convertView.setGravity(Gravity.CENTER_VERTICAL);
		}
		String title = getGroupTitle(group);
		if (!TextUtils.isEmpty(title)) {
			convertView.setText(title);
		}
		return convertView;
	}

	public View getView(final int group, final int position, View convertView, ViewGroup parent) {
		HashMap<String, Object> data = getItem(group, position);
		return itemMaker.getView(data, convertView, parent);
	}

}
