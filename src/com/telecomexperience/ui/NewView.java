package com.telecomexperience.ui;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.telecomexperience.R;
import com.telecomexperience.GameActivity;
import com.telecomexperience.SearchActivity;
import com.telecomexperience.SoftwareActivity;

public class NewView extends View {

	protected static int downGetReturn = -1;
	protected static int upGetReturn = -1;
	protected Context mContext;
	private int [] bitmaps = new int[]{R.drawable.menu1,R.drawable.menu2,R.drawable.menu3};
	private int [] arrows = new int[]{R.drawable.arrow_top,R.drawable.arrow_left,R.drawable.arrow_right};
	private int [] pressBitmaps = new int[]{R.drawable.menu1_over,R.drawable.menu2_over,R.drawable.menu3_over};
	private String [] titles = new String[]{"应用","游戏","搜索"};
	private String [] titles2 = new String[]{"Software","Game","Search"};
	// stone列表
	private BigStone[] mStones;

	static final int NONE = 0;

	// 数目
	private static int STONE_COUNT = 3;

	// 圆心坐标
	private float mPointX = 0, mPointY = 0;


	private int mode = 0;
	
	// 半径
	private int mRadius = 0;
	// 每两个点间隔的角度
	private int mDegreeDelta;

	private float maxX, maxY, minX, minY;
	private Bitmap lockBitmap;
	private Bitmap centerBitmapBg;
	private Bitmap bgBitmap;
	private Bitmap lockBitmapBg;
	private Bitmap arrowTopBitmap;
	private Bitmap arrowRightBitmap;
	private Bitmap arrowLeftBitmap;

	
	public NewView(Context context){
		super(context);
		mContext = context;
		initBitmap();
		init();
	}
	
	public NewView(Context context,AttributeSet attrs){
		super(context, attrs);
		mContext = context;
		initBitmap();
		init();
	}
	
	protected void initBitmap(){
		lockBitmap = BitmapFactory.decodeResource(getResources(),	R.drawable.bt_lock);
		centerBitmapBg =  BitmapFactory.decodeResource(getResources(),	R.drawable.bt_menu__bg);
		bgBitmap =  BitmapFactory.decodeResource(getResources(),	R.drawable.view_bg);
		lockBitmapBg = BitmapFactory.decodeResource(getResources(),	R.drawable.view_bg2);
		arrowTopBitmap = BitmapFactory.decodeResource(getResources(),	R.drawable.arrow_top);
		arrowRightBitmap = BitmapFactory.decodeResource(getResources(),	R.drawable.arrow_right);
		arrowLeftBitmap = BitmapFactory.decodeResource(getResources(),	R.drawable.arrow_left);
		mRadius =Math.min(bgBitmap.getWidth()/2,bgBitmap.getHeight()/2)-20;
	}
	
	public void init(){
		mPointX = 300;
		mPointY = 300;
		mRadius = 200;
		setupStones();
		computeCoordinates();
		path.addCircle(mPointX, mPointY, mRadius, Direction.CW);
	    path.computeBounds(rectF, true);
	    region.setPath(path, new Region((int) rectF.left,
                (int) rectF.top, (int) rectF.right,
                (int) rectF.bottom));
	}
	
	public void init(int x,int y){
		mPointX = x;
		mPointY = y;
		setupStones();
		computeCoordinates();
		path.addCircle(mPointX, mPointY, mRadius, Direction.CW);
	    path.computeBounds(rectF, true);
	    region.setPath(path, new Region((int) rectF.left,
                (int) rectF.top, (int) rectF.right,
                (int) rectF.bottom));
	}
	RectF rectF = new RectF();
	Path path = new Path();
    Region region = new Region();
   

	private float eventX;
	private float eventY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		if(isArea((int)x,(int)y)){
			eventX =x;
			eventY =y;
		}
		switch (e.getAction() ) {
		
		case MotionEvent.ACTION_DOWN:
			for (int i = 0; i < STONE_COUNT; i++) {
				if (x >= mStones[i].x - mStones[i].rank
						&& x <= mStones[i].x + mStones[i].rank
						&& y >= mStones[i].y-mStones[i].rank
						&& y <= mStones[i].y + mStones[i].rank) {
					downGetReturn =i;
					mode =2;
					invalidate();
					return true;
				}
			}
			
			if(x >= mPointX - mStones[0].rank
					&& x <=mPointX + mStones[0].rank
					&& y >= mPointY-mStones[0].rank
					&& y <= mPointY + mStones[0].rank){
				mode =1;
			}
			return true;

		case MotionEvent.ACTION_MOVE:
			for (int i = 0; i < STONE_COUNT; i++) {
				if (x >= mStones[i].x - mStones[i].rank
						&& x <= mStones[i].x + mStones[i].rank
						&& y >= mStones[i].y-mStones[i].rank
						&& y <= mStones[i].y + mStones[i].rank) {
					downGetReturn =i;
					invalidate();
					return true;
				}
			}
			downGetReturn =-1;
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			// System.out.println("ACTION_UP:" + e.getX() + " " + e.getY());

			// Log.d(TAG, "dispatchTouchEvent action:ACTION_UP");
			
			for (int i = 0; i < STONE_COUNT; i++) {				
				if (x >= mStones[i].x - mStones[i].rank
						&& x <= mStones[i].x + mStones[i].rank
						&& y >= mStones[i].y-mStones[i].rank
						&& y <= mStones[i].y + mStones[i].rank) {
					upGetReturn =i;
					if(downGetReturn==upGetReturn&&downGetReturn!=3){
						goActivity();
						((Activity)mContext).finish();
						break;
					}
				}
			}
			downGetReturn=-1;
			mode =0;
			invalidate();
			break;

		case MotionEvent.ACTION_POINTER_UP:
			// System.out.println("Pointer_up:	mode = NONE;");
			// Log.d(TAG, "dispatchTouchEvent action:ACTION_CANCEL");

			break;

		}
		return super.dispatchTouchEvent(e);
	}
	
	public void goActivity(){
		Intent intent = new Intent();
		Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE); 
        vib.vibrate(300);
		switch(upGetReturn){
		case 0:
			intent.setClass(mContext,SoftwareActivity.class);
			break;
		case 1:
			intent.setClass(mContext,GameActivity.class);
			break;
		case 2:
			intent.setClass(mContext,SearchActivity.class);
			break;
		}
	
		mContext.startActivity(intent);
	}

	@Override
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		paint.setAlpha(0xBB);
		
		
		if (mode == 0||mode==2){
			drawBitmap(canvas, lockBitmap, mPointX, mPointY); // 画中心锁
			drawBitmap(canvas, lockBitmapBg, mPointX, mPointY);	
		}else {
			if(downGetReturn==-1)drawBitmap(canvas, lockBitmap, eventX, eventY);
			else{
				drawBitmap(canvas, lockBitmapBg, mPointX, mPointY);	
			}
		}
//		drawBitmap(canvas, centerBitmapBg, mPointX, mPointY);		
		drawBitmap(canvas, bgBitmap, mPointX, mPointY);	
		drawBitmap(canvas, lockBitmapBg, mPointX, mPointY);	

		for (int index = 0; index < STONE_COUNT; index++) {
			if (!mStones[index].isVisible)
				continue;
			if(index==downGetReturn){
				drawInCenter(canvas, mStones[index].pressBitmp, mStones[index].x,
						mStones[index].y,index,paint);
				drawBitmap(canvas, centerBitmapBg,  mStones[index].x,  mStones[index].y);
			}
			else{
				drawInCenter(canvas, mStones[index].bitmap, mStones[index].x,
						mStones[index].y,index,paint);
			}
		}
	}
	
	void drawBitmap(Canvas canvas, Bitmap b, float x, float y) {
		canvas.drawBitmap(b, x - b.getWidth() / 2, y - b.getHeight() / 2, null); // tubiao
	}

	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,int position,Paint paint) {
		Rect rect = new Rect();
		canvas.drawBitmap(bitmap, left - bitmap.getWidth()/2,	top - bitmap.getHeight() / 2, null);
//		switch(position){
//		case 0:
//			canvas.drawBitmap(arrowRightBitmap , left - bitmap.getWidth()/2-arrowRightBitmap.getWidth(),	top - bitmap.getHeight()/2 +arrowRightBitmap.getHeight(), null);
//			break;
//		case 1:
//			canvas.drawBitmap(arrowLeftBitmap, left + bitmap.getWidth()/2+arrowLeftBitmap.getWidth()/2,	top - bitmap.getHeight()/2+arrowLeftBitmap.getHeight(), null);
//			break;
//		case 2:
//			canvas.drawBitmap(arrowTopBitmap, left - bitmap.getWidth()/2+arrowTopBitmap.getWidth(),	top + bitmap.getHeight()/2 +arrowTopBitmap.getHeight(), null);
//			break;
//		}
		canvas.drawText(titles[position], left+bitmap.getWidth()/3,top+bitmap.getHeight()/2, paint);
		paint.getTextBounds(titles[position],0,1,rect);
		paint.setTextSize(16);
		canvas.drawText(titles2[position], left+bitmap.getWidth()/3,top+bitmap.getHeight()/2+rect.height(), paint);
	}



	private void computeCoordinates() {
		BigStone stone;
		for (int index = 0; index < STONE_COUNT; index++) {
			stone = mStones[index];
			stone.x = mPointX
					+ (float) (mRadius * Math.cos(stone.angle * Math.PI / 180));
			stone.y = mPointY
					+ (float) (mRadius * Math.sin(stone.angle * Math.PI / 180));
		}
	}
	
	private boolean isArea(int x,int y){
		
        if (region.contains(x, y)) {
        	return true;
        }
        return false;
	}
	
	private void setupStones() {
		mStones = new BigStone[STONE_COUNT];
		BigStone stone;
		int angle = 0;
		mDegreeDelta = 360 /4;

		for (int index = 0; index < STONE_COUNT; index++) {
			stone = new BigStone();
			stone.angle = angle;
			stone.bitmap = BitmapFactory.decodeResource(getResources(),bitmaps[index]);
			stone.pressBitmp = BitmapFactory.decodeResource(getResources(),pressBitmaps[index]);
			stone.text = titles[index];
			stone.rank = Math.max(centerBitmapBg.getWidth(),centerBitmapBg.getHeight())/2;
			angle += mDegreeDelta;
			if(index==0){
				angle += mDegreeDelta;
			}
			mStones[index] = stone;
		}

	}

	class BigStone {

		// 图片
		Bitmap bitmap;
		Bitmap pressBitmp;
		// 角度
		int angle;
		// x坐标
		float x;
		// y坐标
		float y;
		float rank;
		String text;
		// 是否可见
		boolean isVisible = true;
	}
}
