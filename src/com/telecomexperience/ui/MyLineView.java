package com.telecomexperience.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyLineView extends View{
	private static final String TAG = "MyRelativeLayout";
	private Paint mPaint = new Paint();
	public int height1;
	public int height2;
	public int leftWidth;
	public int centerWidth;
	public int windowWidth;
	private final int diffX = 30;
	
	public int currentPage =2;
	private  Path mPath ;
	private  Path mPath1 ;
	private Paint mPaint1 = new Paint();
	private int pageSize =4;
	private final int diffx = 30;
	public static final int diffy1 = 20;
	public int distPage = 0;	
	public boolean isSlide = false;
	
	public int startPosition =0;
	public int endPosition =0;
	public int offsetX =0;
	
	public int offsetTotal =0;
	public void scrollToPage(int page){
		this.distPage = page;
		int cellWidth = (centerWidth-2*diffx)/pageSize;
		startPosition = cellWidth*currentPage;
		endPosition = cellWidth*distPage;
		offsetTotal = startPosition-endPosition;
		isSlide = true;
		offsetX=0;
		invalidate();
	}
	
	public MyLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MyLineView(Context context) {
		super(context);
		init();
	}

	public void init(){
		mPaint.setAntiAlias(true);
		mPaint.setDither(false);
		mPaint.setColor(Color.rgb(0xff, 0xff, 0xff));
		mPaint.setStrokeWidth(2.0f);
		mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        
        mPath = new Path();
        mPath1 = new Path();
        
        mPaint1.setAntiAlias(true);
        mPaint1.setDither(false);
		mPaint1.setColor(Color.rgb(0x54f, 0xd1, 0x0f));
		mPaint1.setStrokeWidth(3.0f);
		mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeJoin(Paint.Join.ROUND);
        mPaint1.setStrokeCap(Paint.Cap.ROUND);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPath.reset();
		Log.d(TAG,String.format("leftHeight=%s,centerHeight=%s,leftWidth=%s,centerWidth=%s,windowWidth=%s", height1,height2,leftWidth,centerWidth,windowWidth));
		mPath.moveTo(0, height1+diffy1);
		mPath.lineTo(leftWidth, height1+diffy1);
		mPath.lineTo(leftWidth+diffx, height2);
		mPath.lineTo(leftWidth+centerWidth-diffx, height2);
		mPath.lineTo(leftWidth+centerWidth,height1+diffy1);
		mPath.lineTo(windowWidth, height1+diffy1);
		canvas.drawPath(mPath, mPaint);
		
		int cellWidth = (centerWidth-2*diffx)/pageSize;
		if(!isSlide){
			if(currentPage<1)currentPage =1;
			
			mPath1.reset();
			int startWidth = leftWidth+diffx+(currentPage-1)*cellWidth;
			int endWidth = leftWidth+diffx+currentPage*cellWidth;
			
			mPath1.moveTo(startWidth, height2-1);
			mPath1.lineTo(endWidth, height2-1);
			canvas.drawPath(mPath1, mPaint1);
		}
		else{
			
			mPath1.reset();
			int startWidth = leftWidth+diffx+(currentPage-1)*cellWidth-offsetX;
			int endWidth = leftWidth+diffx+currentPage*cellWidth-offsetX;
			
			mPath1.moveTo(startWidth, height2-1);
			mPath1.lineTo(endWidth, height2-1);
			canvas.drawPath(mPath1, mPaint1);
			if(offsetTotal>0&&offsetX<offsetTotal){
					offsetX+=40;
					postInvalidateDelayed(1l);
			}else if(offsetTotal<0&&offsetX>offsetTotal){
					offsetX-=40;
					postInvalidateDelayed(1l);
			}
			else{
				currentPage = distPage;
				isSlide = false;
				invalidate();
			}
		}
	}
}
