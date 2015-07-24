/*
 * 瀹樼綉鍦扮珯:http://www.mob.com
 * 鎶�湳鏀寔QQ: 4006852216
 * 瀹樻柟寰俊:ShareSDK   锛堝鏋滃彂甯冩柊鐗堟湰鐨勮瘽锛屾垜浠皢浼氱涓�椂闂撮�杩囧井淇″皢鐗堟湰鏇存柊鍐呭鎺ㄩ�缁欐偍銆傚鏋滀娇鐢ㄨ繃绋嬩腑鏈変换浣曢棶棰橈紝涔熷彲浠ラ�杩囧井淇′笌鎴戜滑鍙栧緱鑱旂郴锛屾垜浠皢浼氬湪24灏忔椂鍐呯粰浜堝洖澶嶏級
 *
 * Copyright (c) 2014骞�mob.com. All rights reserved.
 */
package cn.smssdk.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;



import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.mob.tools.FakeActivity;
import cn.smssdk.utils.SMSLog;

import static com.mob.tools.utils.R.getIdRes;
import static com.mob.tools.utils.R.getStringRes;
import static com.mob.tools.utils.R.getLayoutRes;


public class ContactsPage extends FakeActivity implements OnClickListener, TextWatcher{

	private EditText etSearch;
	private ContactsListView listView;
	private ContactsAdapter adapter;
	private ContactItemMaker itemMaker;

	private Dialog pd;
	private EventHandler handler;
	private ArrayList<HashMap<String,Object>> friendsInApp;
	private ArrayList<HashMap<String,Object>> contactsInMobile;

	@Override
	public void onCreate() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(activity);
		if (pd != null) {
			pd.show();
		}

			SearchEngine.prepare(activity, new Runnable() {
			public void run() {
				afterPrepare();
			}
		});
	}

	private void afterPrepare() {
		runOnUIThread(new Runnable() {
			public void run() {
				friendsInApp = new ArrayList<HashMap<String,Object>>();
				contactsInMobile = new ArrayList<HashMap<String,Object>>();

				int resId = getLayoutRes(activity, "smssdk_contact_list_page");
				if (resId > 0) {
					activity.setContentView(resId);
					initView();
					initData();
				}
			}
		});
	}

	@Override
	public void onResume(){
	  	super.onResume();
	}

	@Override
	public void onPause() {
	   	super.onPause();
	}

	public void show(Context context) {
		show(context, new DefaultContactViewItem());
	}

	public void show(Context context, ContactItemMaker maker) {
		itemMaker = maker;
		super.show(context, null);
	}

	private void initView(){
		int resId = getIdRes(activity, "clContact");
		if (resId > 0) {
			listView = (ContactsListView) activity.findViewById(resId);
		}
		resId = getIdRes(activity, "ll_back");
		if (resId > 0) {
			activity.findViewById(resId).setOnClickListener(this);
		}
		resId = getIdRes(activity, "ivSearch");
		if (resId > 0) {
			activity.findViewById(resId).setOnClickListener(this);
		}
		resId = getIdRes(activity, "iv_clear");
		if (resId > 0) {
			activity.findViewById(resId).setOnClickListener(this);
		}
		resId = getIdRes(activity, "tv_title");
		if (resId > 0) {
			TextView tv = (TextView) activity.findViewById(resId);
			resId = getStringRes(activity, "smssdk_search_contact");
			if (resId > 0) {
				tv.setText(resId);
			}
		}
		resId = getIdRes(activity, "et_put_identify");
		if (resId > 0) {
			etSearch = (EditText) activity.findViewById(resId);
			etSearch.addTextChangedListener(this);
		}
	}

	private void initData(){
		handler = new EventHandler() {
			@SuppressWarnings("unchecked")
			public void afterEvent(final int event, final int result, final Object data) {
				if (result == SMSSDK.RESULT_COMPLETE) {
					if (event == SMSSDK.EVENT_GET_CONTACTS) {
						ArrayList<HashMap<String,Object>> rawList = (ArrayList<HashMap<String,Object>>) data;
						if (rawList == null) {
							contactsInMobile = new ArrayList<HashMap<String,Object>>();
						} else {
							contactsInMobile = (ArrayList<HashMap<String,Object>>) rawList.clone();
						}
						refreshContactList();
					} else if (event == SMSSDK.EVENT_GET_FRIENDS_IN_APP) {
						friendsInApp = (ArrayList<HashMap<String,Object>>) data;
						SMSSDK.getContacts(false);
					}
				} else {
					runOnUIThread(new Runnable() {
						public void run() {
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
							// 缃戠粶閿欒
							int resId = getStringRes(activity, "smssdk_network_error");
							if (resId > 0) {
								Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		};
		// 娉ㄥ唽浜嬩欢鐩戝惉鍣�		SMSSDK.registerEventHandler(handler);

		if(friendsInApp != null && friendsInApp.size() > 0){
			// 鑾峰彇鏈湴鑱旂郴浜�			SMSSDK.getContacts(false);
		}else{
			// 鑾峰彇搴旂敤鍐呯殑濂藉弸鍒楄〃
			SMSSDK.getFriendsInApp();
		}
	}

	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		try {
			int resId = getIdRes(activity, "llSearch");
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN
					&& activity.findViewById(resId).getVisibility() == View.VISIBLE) {
				activity.findViewById(resId).setVisibility(View.GONE);
				resId = getIdRes(activity, "llTitle");
				activity.findViewById(resId).setVisibility(View.VISIBLE);
				etSearch.setText("");
				return true;
			}
		} catch (Exception e) {
			SMSLog.getInstance().w(e);
		}
		return super.onKeyEvent(keyCode, event);
	}

	public void onDestroy() {
		// 閿�瘉浜嬩欢鐩戝惉鍣�		SMSSDK.unregisterEventHandler(handler);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		adapter.search(s.toString());
		adapter.notifyDataSetChanged();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		int id_ll_back = getIdRes(activity, "ll_back");
		int id_ivSearch = getIdRes(activity, "ivSearch");
		int id_iv_clear = getIdRes(activity, "iv_clear");

		if (id == id_ll_back) {
			finish();
		} else if (id == id_ivSearch) {
			int id_llTitle = getIdRes(activity, "llTitle");
			activity.findViewById(id_llTitle).setVisibility(View.GONE);
			int id_llSearch = getIdRes(activity, "llSearch");
			activity.findViewById(id_llSearch).setVisibility(View.VISIBLE);
			etSearch.requestFocus();
			etSearch.getText().clear();
		} else if (id == id_iv_clear) {
			etSearch.getText().clear();
		}
	}

	// 鑾峰彇鑱旂郴浜哄垪琛�	@SuppressWarnings("unchecked")
	private void refreshContactList() {
		// 閫犱竴涓�鐢佃瘽鍙风爜-鑱旂郴浜衡�鏄犲皠琛紝鍔犻�鏌ヨ
		ArrayList<ContactEntry> phone2Contact = new ArrayList<ContactEntry>();
		for (HashMap<String, Object> contact : contactsInMobile) {
			ArrayList<HashMap<String, Object>> phones
					= (ArrayList<HashMap<String, Object>>) contact.get("phones");
			if (phones != null && phones.size() > 0) {
				for (HashMap<String, Object> phone : phones) {
					String pn = (String) phone.get("phone");
					ContactEntry ent = new ContactEntry(pn, contact);
					phone2Contact.add(ent);
				}
			}
		}

		ArrayList<HashMap<String,Object>> tmpFia = new ArrayList<HashMap<String,Object>>();
		int p2cSize = phone2Contact.size();
		for (HashMap<String, Object> friend : friendsInApp) {
			String phone = String.valueOf(friend.get("phone"));
			if (phone != null) {
				for (int i = 0; i < p2cSize; i++) {
					ContactEntry ent = phone2Contact.get(i);
					String cp = ent.getKey();
					if (phone.equals(cp)) {
						friend.put("contact", ent.getValue());
						friend.put("fia", true);
						tmpFia.add((HashMap<String, Object>) friend.clone());
					}
				}
			}
		}
		friendsInApp = tmpFia;

		
		HashSet<HashMap<String, Object>> tmpCon = new HashSet<HashMap<String,Object>>();
		for (ContactEntry ent : phone2Contact) {
			String cp = ent.getKey();
			HashMap<String, Object> con = ent.getValue();
			if (cp != null && con != null) {
				boolean shouldAdd = true;
				for (HashMap<String, Object> friend : friendsInApp) {
					String phone = String.valueOf(friend.get("phone"));
					if (cp.equals(phone)) {
						shouldAdd = false;
						break;
					}
				}
				if (shouldAdd) {
					tmpCon.add(con);
				}
			}
		}
		contactsInMobile.clear();
		contactsInMobile.addAll(tmpCon);

		// 鍒犻櫎闈炲簲鐢ㄥ唴濂藉弸鍒嗙粍鑱旂郴浜虹數璇濆垪琛ㄤ腑宸茬粡娉ㄥ唽浜嗙殑鐢佃瘽鍙风爜
		for (HashMap<String, Object> friend : friendsInApp) {
			HashMap<String, Object> contact = (HashMap<String, Object>) friend.remove("contact");
			if (contact != null) {
				String phone = String.valueOf(friend.get("phone"));
				if (phone != null) {
					ArrayList<HashMap<String, Object>> phones
							= (ArrayList<HashMap<String, Object>>) contact.get("phones");
					if (phones != null && phones.size() > 0) {
						ArrayList<HashMap<String, Object>> tmpPs = new ArrayList<HashMap<String,Object>>();
						for (HashMap<String, Object> p : phones) {
							String cp = (String) p.get("phone");
							if (!phone.equals(cp)) {
								tmpPs.add(p);
							}
						}
						contact.put("phones", tmpPs);
					}
				}
				friend.put("displayname", contact.get("displayname"));
			}
		}

		// 鏇存柊listview
		runOnUIThread(new Runnable() {
			public void run() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}

				adapter = new ContactsAdapter(listView, friendsInApp, contactsInMobile);
				adapter.setContactItemMaker(itemMaker);
				listView.setAdapter(adapter);
			}
		});


//		if (pd != null && pd.isShowing()) {
//			pd.dismiss();
//		}
//
//		try {
//
//			// 閫犱竴涓�鐢佃瘽鍙风爜-鑱旂郴浜衡�鏄犲皠琛紝鍔犻�鏌ヨ
//			HashMap<String, HashMap<String, Object>> phone2Contact = new HashMap<String, HashMap<String,Object>>();
//			for (HashMap<String, Object> contact : contactsInMobile) {
//				ArrayList<HashMap<String, Object>> phones = (ArrayList<HashMap<String, Object>>) contact.get("phones");
//				if (phones != null && phones.size() > 0) {
//					for (HashMap<String, Object> phone : phones) {
//						String pn = (String) phone.get("phone");
//						//鏈夊彿鐮侊紝鏈ㄦ湁鍚嶅瓧锛涘悕瀛� = 鍙风爜
//						if(!contact.containsKey("displayname")){
//							contact.put("displayname", pn);
//						}
//						phone2Contact.put(pn, contact);
//					}
//				}
//			}
//
//			// 绉婚櫎鏈湴鑱旂郴浜哄垪琛ㄤ腑锛屽寘鍚凡鍔犲叆APP鐨勮仈绯讳汉
//			ArrayList<HashMap<String, Object>> tmpList = new ArrayList<HashMap<String,Object>>();
//			for (int i = 0; i < friendsInApp.size(); i++) {
//				HashMap<String, Object> friend = friendsInApp.get(i);
//				String phoneNum = String.valueOf(friend.get("phone"));
//				HashMap<String, Object> contact = phone2Contact.remove(phoneNum);
//				if (contact != null) {
//					String namePhone = String.valueOf(contact.get("displayname"));
//					if(TextUtils.isEmpty(namePhone)){
//						namePhone = phoneNum;
//					}
//					// 宸插姞鍏ュ簲鐢ㄧ殑鑱旂郴浜猴紝鏄剧ずcontact name, 鍚﹀垯鏄剧ず phoneNumber
//					friend.put("displayname", namePhone);
//					tmpList.add(friend);
//				}
//			}
//			friendsInApp = tmpList;
//			//閲嶆柊瀵瑰彿鐮佽繘琛岃繃婊わ紝鎺掗櫎閲嶅鐨刢ontact(涓�汉澶氱爜)
//			HashSet<HashMap<String, Object>> contactsSet = new HashSet<HashMap<String,Object>>(phone2Contact.values());
//			contactsInMobile = new ArrayList<HashMap<String,Object>>(contactsSet);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		//TODO 鏇存柊listview
//		adapter = new ContactsAdapter(listView, friendsInApp, contactsInMobile);
//		adapter.setContactItemMaker(itemMaker);
//		//adapter.setOnItemClickListener(this);
//		listView.setAdapter(adapter);

	}

}
