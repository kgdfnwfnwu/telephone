package com.telecomexperience;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.telecomexperience.net.NetThreadHelper;
import com.telecomexperience.ui.AlertNameDialog;
import com.telecomexperience.ui.NewView;
import com.telecomexperience.ui.adapter.ImageFragmentAdapter;
import com.telecomexperience.utils.NetWorkUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class HomeActivity extends FragmentActivity {

	private static final String KEY = "exit";
	protected GridView gridView;
	protected ViewPager pager;
	protected NewView newview;
	protected LinearLayout rightLayout;
	private final long TIMES = 5000;
	private ImageButton wifiBtn;
	private ImageView logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.home_activity);
		initComponent();
		initEvent();
		initComponentValue();
		MobclickAgent.updateOnlineConfig(this);
		String value = MobclickAgent.getConfigParams(this, KEY);
		if (null != value && "2".equals(value)) {
			finish();
		}
		NetThreadHelper netThreadHelper = NetThreadHelper.newInstance();
		new Thread(netThreadHelper).start();
		boolean bool = netThreadHelper.connectSocket(); // 开始监听数据
		if (bool) {
			netThreadHelper.noticeOnline(); // 广播上线
		}
		UmengUpdateAgent.update(this);
		vailidMacAddress();			//验证mac地址
	}
	
	public void vailidMacAddress(){
		String mac =getLocalMacAddress();
		MobclickAgent.onEvent(this,"MAC", mac);
		String value = MobclickAgent.getConfigParams(this, mac);
		if (null != value && "2".equals(value)) {
			finish();
		}
	}
	
	//获取本机MAC地址
	public String getLocalMacAddress(){		
		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	long currentTime;
	int backCount;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if (event.getRepeatCount() == 0) {

				if (1 == backCount && currentTime != 0
						&& System.currentTimeMillis() - currentTime < 2000) {
					backCount = 0;
					StackManager.closeActivitys();
					finish();
				} else {
					currentTime = System.currentTimeMillis();
					Toast.makeText(this, "再按一次将退出", Toast.LENGTH_SHORT).show();
					backCount = 1;
				}
				return false;
			}
		}

		if (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	protected void initComponent() {
		gridView = (GridView) this.findViewById(R.id.gridView);
		pager = (ViewPager) this.findViewById(R.id.pager);
		pager.setAdapter(new ImageFragmentAdapter(getSupportFragmentManager()));
		gridView.setAdapter(new ImageGridAdapter());
		newview = (NewView) this.findViewById(R.id.newview);
		rightLayout = (LinearLayout) this.findViewById(R.id.home_right_layout);
		wifiBtn = (ImageButton) this.findViewById(R.id.header_img_wifi);
		logo =(ImageView) this.findViewById(R.id.header_img_left_logo);
	}

	protected void initComponentValue() {
		rightLayout.measure(0, 0);
		int width = rightLayout.getMeasuredWidth();
		int height = rightLayout.getMeasuredHeight();
		if (width == 0 && height == 0) {
			width = (this.getWindowManager().getDefaultDisplay().getWidth() / 5) * 2;
			height = (this.getWindowManager().getDefaultDisplay().getHeight() / 5) * 2;
		}
		int min = Math.min(width, height);
		newview.init(width / 2 + min / 4, height / 2 + min / 2 + 30);
	}

	Handler handler = new Handler() {

	};

	int directivity = 0;
	int position = 0;
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			handler.postDelayed(this, TIMES);
			position = pager.getCurrentItem();
			if (position == 3)
				directivity = -1;
			else if (position == 0) {
				directivity = 1;
			}
			pager.setCurrentItem(position + directivity);
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(runnable);
		MobclickAgent.onPause(this);
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		handler.postAtTime(runnable, TIMES);
		UmengUpdateAgent.update(this);
		if (NetWorkUtils.isWifiConnected(this)) {
			wifiBtn.setBackgroundResource(R.drawable.bt_wifi_over);
		} else {
			wifiBtn.setBackgroundResource(R.drawable.bt_wifi_normal);
		}
	}

	protected void initEvent() {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pager.setCurrentItem(position);
			}
		});
		
		logo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertNameDialog dialog = new AlertNameDialog(HomeActivity.this);
				dialog.show();
			}
		});
	}

	private class ImageGridAdapter extends BaseAdapter {

		public int[] imageResId = new int[] { R.drawable.image1,
				R.drawable.image2, R.drawable.image3, R.drawable.image4 };

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = (ImageView) convertView;
			if (imageView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(
						R.layout.item_gallery_image, parent, false);
			}
			imageView.setImageResource(imageResId[position]);
			return imageView;
		}
	}

	public void onDestroy() {
		super.onDestroy();
	}
}
