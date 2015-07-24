/*
 * 瀹樼綉鍦扮珯:http://www.mob.com
 * 鎶�湳鏀寔QQ: 4006852216
 * 瀹樻柟寰俊:ShareSDK   锛堝鏋滃彂甯冩柊鐗堟湰鐨勮瘽锛屾垜浠皢浼氱涓�椂闂撮�杩囧井淇″皢鐗堟湰鏇存柊鍐呭鎺ㄩ�缁欐偍銆傚鏋滀娇鐢ㄨ繃绋嬩腑鏈変换浣曢棶棰橈紝涔熷彲浠ラ�杩囧井淇′笌鎴戜滑鍙栧緱鑱旂郴锛屾垜浠皢浼氬湪24灏忔椂鍐呯粰浜堝洖澶嶏級
 *
 * Copyright (c) 2014骞�mob.com. All rights reserved.
 */
package cn.smssdk.gui;

import static com.mob.tools.utils.R.getIdRes;
import static com.mob.tools.utils.R.getLayoutRes;
import static com.mob.tools.utils.R.getStringRes;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.mob.tools.FakeActivity;


public class ContactDetailPage extends FakeActivity implements OnClickListener{

	private String phoneName = "";
	private ArrayList<String> phoneList = new ArrayList<String>();

	@Override
	public void onCreate() {
		int resId = getLayoutRes(activity, "smssdk_contact_detail_page");
		if (resId > 0) {
			activity.setContentView(resId);
			resId = getIdRes(activity, "ll_back");
			activity.findViewById(resId).setOnClickListener(this);
			resId = getIdRes(activity, "tv_title");
			TextView tvTitle = (TextView) activity.findViewById(resId);
			resId = getStringRes(activity, "smssdk_contacts_detail");
			tvTitle.setText(resId);

			resId = getIdRes(activity, "tv_contact_name");
			TextView tvContactName = (TextView) activity.findViewById(resId);
			tvContactName.setText(phoneName);

			resId = getIdRes(activity, "tv_contact_phones");
			TextView tvPhonesList = (TextView) activity.findViewById(resId);
			StringBuilder phones = new StringBuilder();
			for(String phone : phoneList){
				phones.append("\n");
				phones.append(phone);
			}
			if(phones.length() > 0){
				phones.deleteCharAt(0);
				tvPhonesList.setText(phones.toString());
			}

			resId = getIdRes(activity, "tv_invite_hint");
			TextView tvInviteHint = (TextView) activity.findViewById(resId);
			resId = getStringRes(activity, "smssdk_not_invite");
			String hint = getContext().getResources().getString(resId, phoneName);
			tvInviteHint.setText(Html.fromHtml(hint));

			resId = getIdRes(activity, "btn_invite");
			activity.findViewById(resId).setOnClickListener(this);
		}
	}

	@Override
	public void onResume(){
	  	super.onResume();
	}

	@Override
	public void onPause() {
	   	super.onPause();
	}

	/**
	 * 璁剧疆鑱旂郴浜哄璞�	 * @param contact
	 */
	@SuppressWarnings("unchecked")
	public void setContact(HashMap<String, Object> contact){
		if(contact.containsKey("displayname")){
			phoneName = String.valueOf(contact.get("displayname"));
		} else if (contact.containsKey("phones")) {
			ArrayList<HashMap<String, Object>> phones
					= (ArrayList<HashMap<String, Object>>) contact.get("phones");
			if (phones != null && phones.size() > 0) {
				phoneName = (String) phones.get(0).get("phone");
			}
		}
		ArrayList<HashMap<String, Object>> phones = (ArrayList<HashMap<String, Object>>) contact.get("phones");
		if (phones != null && phones.size() > 0) {
			for (HashMap<String, Object> phone : phones) {
				String pn = (String) phone.get("phone");
				phoneList.add(pn);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		int id_ll_back = getIdRes(activity, "ll_back");
		int id_btn_invite = getIdRes(activity, "btn_invite");
		if (id == id_ll_back) {
			finish();
		} else if (id == id_btn_invite) {
			if(phoneList.size()>1){
				showDialog();
				return;
			}else{
				String phone = phoneList.size() > 0 ? phoneList.get(0) : "";
				sendMsg(phone);
			}
		}
	}

	/**
	 * 鍙戦�娑堟伅
	 * @param String phone
	 */
	private void sendMsg(String phone){
		Uri smsToUri = Uri.parse("smsto:"+phone);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		int resId = getStringRes(activity, "smssdk_invite_content");
		if (resId > 0) {
			intent.putExtra("sms_body", activity.getString(resId));
		}
		startActivity(intent);
	}

	
	private void showDialog() {
		String[] phones = new String[phoneList.size()];
		phones = phoneList.toArray(phones);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		int resId = getStringRes(activity, "smssdk_invite_content");
		if (resId > 0) {
			builder.setTitle(resId);
		}
		builder.setCancelable(true);
		resId = getStringRes(activity, "smssdk_cancel");
		if (resId > 0) {
			builder.setNegativeButton(resId, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}

			});
		}
		builder.setItems(phones, new DialogInterface.OnClickListener() {
		@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				sendMsg(phoneList.get(which));
			}
		});
		builder.create().show();
	}


}
