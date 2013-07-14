package com.telecomexperience.ui.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aphidmobile.flip.FlipViewController;
import com.telecomexperience.DiceActivity;
import com.telecomexperience.R;
import com.telecomexperience.app.App;
import com.telecomexperience.utils.PackageUtils;
import com.telecomexperience.utils.XmlHelper;

public class AppDeatailAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	FlipViewController controller;
	private int repeatCount = 1;

	private List<App> appData;
	private Context mContext;

	public AppDeatailAdapter(Context context, FlipViewController controller) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		appData = XmlHelper.getInstance().getAllApps();
		this.controller = controller;
	}

	@Override
	public int getCount() {
		return appData.size() * repeatCount;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class HolderView {
		TextView nameText;
		ImageView imageView;
		ImageView btn;
		WebView webView;
		ImageView diceBgView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = convertView;
		HolderView hold = null;
		if (convertView == null) {
			hold = new HolderView();
			layout = inflater.inflate(R.layout.appdetail_item, null);
			hold.nameText = (TextView) layout
					.findViewById(R.id.appdeital_text_name);
			hold.imageView = (ImageView) layout
					.findViewById(R.id.appdetail_img_bg);
			hold.btn = (ImageView) layout.findViewById(R.id.appdetail_img_run);
			hold.webView = (WebView) layout
					.findViewById(R.id.appdetail_webview);
			hold.diceBgView =(ImageView) layout.findViewById(R.id.appdetail_image_dice);
			hold.btn.setOnClickListener(this);
			
			layout.setTag(hold);
		} else {
			hold = (HolderView) layout.getTag();
		}
		initWebViewComponent(hold.webView);
		
		App app = appData.get(position);
		hold.nameText.setText(app.name);
		XmlHelper.setImage(mContext, hold.imageView, app.residStr);
		hold.btn.setTag(app);
		
		if(App.DICE.equals(app.type)){
			hold.diceBgView .setVisibility(View.VISIBLE);
			hold.webView.setVisibility(View.INVISIBLE);
		}
		else{
			hold.diceBgView .setVisibility(View.INVISIBLE);
			hold.webView.setVisibility(View.VISIBLE);
			loadurl(hold.webView, app.webUrl);
		}
		
		return layout;
	}

	public void loadurl(final WebView view, final String url) {
		view.loadUrl(url);// 载入网页
	}

	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			getFile(mContext, url);
		}

	}

	public void initWebViewComponent(WebView webView) {
//		wv.getSettings().setJavaScriptEnabled(true);// 可用JS
//		wv.setScrollBarStyle(1);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		webView.setDownloadListener(new MyWebViewDownLoadListener());
		
		
		 webView.setWebViewClient(new WebViewClient() {
		        @Override
		        public void onPageStarted(WebView view, String url, Bitmap favicon) {
		        }

		        @Override
		        public void onPageFinished(WebView view, String url) {
		          controller.refreshPage(
		              view);//This works as the webView is the view for a page. Please use refreshPage(int pageIndex) if the webview is only a part of page view.

		        }
		      });

		      webView.setWebChromeClient(new WebChromeClient() {
		        private int lastRefreshProgress = 0;

		        @Override
		        public void onProgressChanged(WebView view, int newProgress) {
		          if (newProgress - lastRefreshProgress
		              > 20) { //limit the invocation frequency of refreshPage
		            controller.refreshPage(view);
		            lastRefreshProgress = newProgress;
		          }
		        }
		      });

	}

	/**
	 * 调用浏览器下载文件
	 * 
	 * @param mContext
	 * @param file
	 */
	public static void getFile(Context context, String url) {
		try {
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(i);
		} catch (Exception e) {

		}
	}

	@Override
	public void onClick(View v) {
		final App app = (App) v.getTag();
		if(App.DICE.equals(app.type)){
			Intent intent = new Intent();
			intent.setClass(mContext, DiceActivity.class);
			mContext.startActivity(intent);
			
		}
		else{
			if(!PackageUtils.jumpPackage(app.packagename, app.name)){
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("电信体验系统");
				builder.setMessage("暂未安装\""+app.name+"\",是否下载该应用");
				builder.setPositiveButton("取消",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.setNegativeButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getFile(mContext,app.getDownloadUrl());
						dialog.cancel();
					}
				});
				builder.create().show();
			}
		}
	}

}
