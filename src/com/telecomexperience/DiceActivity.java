package com.telecomexperience;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.Keyframe;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.telecomexperience.data.DiceRule;
import com.telecomexperience.net.BaseReceiveActivity;
import com.telecomexperience.net.NetThreadHelper;
import com.telecomexperience.net.SendMessageUtils;
import com.telecomexperience.ui.ShapeHolder;

public class DiceActivity extends BaseReceiveActivity {

	protected static final String TAG = "DiceActivity";
	protected TextView resultText;
	protected NetThreadHelper netThreadHelper;
	protected MyAnimationView animView;
	protected Button starter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.diceactivity);
		initComponent();
		initEvent();
		netThreadHelper= NetThreadHelper.newInstance();
	}

	public void onStart() {
		super.onStart();
	}

	public void onPause() {
		super.onPause();
		//发送下线广播
	}


	@Override
	public void handler_shake() {
		super.handler_shake();
		animView.startAnimation();
	}

	public void initComponent() {
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		resultText = (TextView) this
				.findViewById(R.id.diceactivity_text_result);
		animView = new MyAnimationView(this);
		container.addView(animView);
		starter = (Button) findViewById(R.id.startButton);
		starter.setVisibility(View.GONE);
	}

	public void initEvent() {
		starter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				animView.startAnimation();
			}
		});
	}
	
	public void onDestroy(){
		super.onDestroy();
	}

	public class MyAnimationView extends View implements
			Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
		protected int[] dices = new int[] { R.drawable.dice1, R.drawable.dice2,
				R.drawable.dice3, R.drawable.dice4, R.drawable.dice5,
				R.drawable.dice6 };
		protected Bitmap[] bitmaps = new Bitmap[6];
		
		protected boolean isRefresh = true;
		
		protected boolean isEnd = true;

		final int DEFAULT = 5;

		private static final float BALL_SIZE = 50f;

		private static final int DURATION = 1000;

		ObjectAnimator bouncer1;
		ObjectAnimator bouncer2;
		ObjectAnimator bouncer3;
		ObjectAnimator bouncer4;
		ObjectAnimator bouncer5;
		ObjectAnimator bouncer6;

		private int[] results = new int[6];

		public void initBitmap() {
			for (int i = 0; i < dices.length; i++) {
				bitmaps[i] = BitmapFactory.decodeResource(getResources(),
						dices[i]);
			}
		}

		public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
		AnimatorSet animation = null;
		Animator bounceAnim = null;
		ShapeHolder ball = null;

		public MyAnimationView(Context context) {
			super(context);
			initBalls();
			initBitmap();
		}

		public int getRandom() {
			return getRandom(DEFAULT);
		}

		public int getRandom(int max) {
			int i = (int) (Math.random() * max);
			return Math.random() > 0.5f ? (-1 * i) : i;
		}

		private void createAnimation() {
			// if (bounceAnim == null) {
			ShapeHolder ball;
			ball = balls.get(0);
			// 小球1
			bouncer1 = getBounceAnim(ball);
			bouncer1.addUpdateListener(this);
			// 小球2
			ball = balls.get(1);
			bouncer2 = getBounceAnim(ball);
			ball = balls.get(2);
			bouncer3 = getBounceAnim(ball);
			ball = balls.get(3);
			bouncer4 = getBounceAnim(ball);
			ball = balls.get(4);
			bouncer5 = getBounceAnim(ball);
			ball = balls.get(5);
			bouncer6 = getBounceAnim(ball);

			bounceAnim = new AnimatorSet();
			((AnimatorSet) bounceAnim).playTogether(bouncer1, bouncer2,
					bouncer3, bouncer4, bouncer5, bouncer6);
			bounceAnim.addListener(this);
		}

		public ObjectAnimator getBounceAnim(ShapeHolder ball) {
			ObjectAnimator bouncer = null;
			int random = (int) (Math.random() *4);
			switch (random) {
			case 0:
				bouncer = ObjectAnimator.ofFloat(ball, "x",ball.getX(), ball.getX()-getRandom(50),
						ball.getX()).setDuration(DURATION);
				bouncer.setInterpolator(new AccelerateInterpolator());

				break;
			case 1:
				PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y",ball.getY(),
						ball.getY()-getRandom(50), ball.getY());
				bouncer = ObjectAnimator.ofPropertyValuesHolder(ball, pvhY)
						.setDuration(DURATION );
				bouncer.setInterpolator(new AccelerateInterpolator());

				break;
			case 2:
				PropertyValuesHolder pvTX = PropertyValuesHolder.ofFloat("x",
						ball.getX(),ball.getX() + getRandom(60), ball.getX());
				PropertyValuesHolder pvTY = PropertyValuesHolder.ofFloat("y",
						ball.getY(),ball.getY() + getRandom(80), ball.getY());
				bouncer = ObjectAnimator.ofPropertyValuesHolder(ball, pvTX,
						pvTY).setDuration(DURATION/2);

				break;
			case 3:
				pvhY = PropertyValuesHolder.ofFloat("y", ball.getY(),ball.getY()+getRandom(20),ball.getY());
				float ballX = ball.getX();
				Keyframe kf0 = Keyframe.ofFloat(0f, ballX);
				Keyframe kf1 = Keyframe.ofFloat(.5f, ballX + getRandom(50));
				Keyframe kf2 = Keyframe.ofFloat(1f, ballX);
				PropertyValuesHolder pvhX = PropertyValuesHolder.ofKeyframe(
						"x", kf0, kf1, kf2);
				bouncer = ObjectAnimator.ofPropertyValuesHolder(ball, pvhY,
						pvhX).setDuration(DURATION );
				break;
			case 4:
				bouncer = ObjectAnimator.ofFloat(ball, "rotation",
						ball.getY(), ball.getY()+getRandom(50), ball.getY()).setDuration(
						DURATION);
				break;
			default:
				bouncer = ObjectAnimator.ofFloat(ball, "y", ball.getY() / 2,
						ball.getY()).setDuration(DURATION);
				bouncer.setInterpolator(new AccelerateInterpolator());
				break;
			}
			bouncer.setRepeatCount(1);
			bouncer.setRepeatMode(ValueAnimator.REVERSE);
			return bouncer;
		}
		
		public void initBalls(){
			
			balls.clear();
			int random = (int)(Math.random()*2);
			switch(random){
			case 0:
				addBall(160 + getRandom(), 40 + getRandom());
				addBall(100 + getRandom(), 70 + getRandom());
				addBall(240 + getRandom(), 80 + getRandom());
				addBall(90 + getRandom(), 150 + getRandom());
				addBall(150 + getRandom(), 140 + getRandom());
				addBall(210 + getRandom(), 160 + getRandom());
				break;
			case 1:
				addBall(100 + getRandom(), 70 + getRandom());
				addBall(160 + getRandom(), 80 + getRandom());
				addBall(200 + getRandom(), 70 + getRandom());
				addBall(100 + getRandom(), 140 + getRandom());
				addBall(155 + getRandom(), 145 + getRandom());
				addBall(210 + getRandom(), 140 + getRandom());
				break;
			case 2:
				addBall(100 + getRandom(), 70 + getRandom());
				addBall(160 + getRandom(), 80 + getRandom());
				addBall(200 + getRandom(), 70 + getRandom());
				addBall(100 + getRandom(), 140 + getRandom());
				addBall(155 + getRandom(), 145 + getRandom());
				addBall(210 + getRandom(), 140 + getRandom());
				break;
			}
			
		}

		public void startAnimation() {
			if(!isEnd) return;
			isEnd = false;
			
			initBalls();
			
			if (bouncer1 != null) {
				bouncer1.reverse();
			}

			if (bouncer2 != null) {

				bouncer2.reverse();
			}

			if (bouncer3 != null) {

				bouncer3.reverse();
			}

			if (bouncer4 != null) {

				bouncer4.reverse();
			}

			if (bouncer5 != null) {

				bouncer5.reverse();
			}

			if (bouncer6 != null) {

				bouncer6.reverse();
			}
			createAnimation();
			bounceAnim.start();
			isRefresh = true;
			
		}

		private ShapeHolder addBall(float x, float y) {
			ShapeHolder shapeHolder = new ShapeHolder();
			shapeHolder.setX(x);
			shapeHolder.setY(y);
			balls.add(shapeHolder);
			return shapeHolder;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			int position = 0;
			int index = 0;
			for (ShapeHolder ball : balls) {
				canvas.translate(ball.getX(), ball.getY());
				// ball.getShape().draw(canvas);
								
				if(isRefresh){
					position = (int) (Math.random() * 5);
					if (index < 6){
						results[index] = position;	
					}	
				}
				else{
					position =results[index]	;	
				}
							
				index++;
				canvas.drawBitmap(bitmaps[position], ball.getX(), ball.getY(),
						null);
				canvas.translate(-ball.getX(), -ball.getY());

				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			Log.i(TAG,"结果:"+results[0]+
						","+results[1]+
						","+results[2]+
						","+results[3]+
						","+results[4]+
						","+results[5]); 
		}

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			invalidate();
			Log.d(TAG,"刷新");
		}

		@Override
		public void onAnimationStart(Animator animation) {
			if (animation instanceof AnimatorSet) {

			} else {

			}
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			if (animation instanceof AnimatorSet) {
				
			} else {

			}
			isEnd = true;
			isRefresh = false;
			String result = DiceRule.getInstance().getResult(results);
			resultText.setText(result);
			SendMessageUtils.sendShakeMessage(result);
		}

		@Override
		public void onAnimationCancel(Animator animation) {

		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			
		}
	}

	
	
}
