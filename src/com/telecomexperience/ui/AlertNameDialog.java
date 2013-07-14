package com.telecomexperience.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.telecomexperience.R;
import com.telecomexperience.net.NetThreadHelper;

public class AlertNameDialog extends Dialog implements OnClickListener {
	
	private Context mContext;
	private EditText mEditText;
	private Button btn1,btn2;

	public AlertNameDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.alertname);
		initCompoent();
		initEvent();
	}
	
	protected void initCompoent(){
		mEditText = (EditText) this.findViewById(R.id.edittext);
		btn1 = (Button)this.findViewById(R.id.bt1);
		btn2 = (Button)this.findViewById(R.id.bt2);
		String name = PreferenceManager.getDefaultSharedPreferences(mContext).getString("name", "电信体验系统");
		mEditText.setText(name);
	}
	
	protected void initEvent(){
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt1:
			cancel();
			break;
		case R.id.bt2:
			String name = mEditText.getText().toString();
			if(name==null||"".equals(name.trim())){
				Toast.makeText(mContext, "名称不能为空！",Toast.LENGTH_SHORT).show();
				return;
			}
			Editor edit = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
			edit.putString("name",name);
			edit.commit();
			NetThreadHelper.newInstance().setSelfName(name);
			NetThreadHelper.newInstance().refreshUsers();
			cancel();
			break;
		}
	}
	

}
