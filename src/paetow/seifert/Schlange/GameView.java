package paetow.seifert.Schlange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	
	private SurfaceHolder surfaceHolder;
	private Bitmap bmp;
	private GameLoopThread theGameLoopThread;
	private int y = 0;
	private int x = 0;
	private int ySpeed;
	private int xSpeed;
	private final int ps = 8;
	private int test1, test2,test3,test4,test5,test6;
	
	float initialX = 0;
	float initialY = 0;
	float lastX = 0;
	float lastY = 0;
	float deltaX =0;
	float deltaY=0;
	final int minDistance = 150;
	//paint.setTextSize(80);


	@SuppressLint("WrongCall")
	public GameView(Context context) {
		super(context);
		theGameLoopThread = new GameLoopThread(this);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				theGameLoopThread.setRunning(false);
				while (retry) {
					try {
						theGameLoopThread.join();
						retry = false;
					} catch (InterruptedException e) {

					}
				}

			}

			public void surfaceCreated(SurfaceHolder holder) {
				theGameLoopThread.setRunning(true);
				theGameLoopThread.start();
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub

			}
		});
		bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.DKGRAY);
		if (x > this.getWidth() - bmp.getWidth() - xSpeed || x + xSpeed < 0) {
			xSpeed = -xSpeed;
		}
		x = x + xSpeed;
		if (y > this.getHeight() - bmp.getHeight() - ySpeed || y + ySpeed < 0) {
			ySpeed = -ySpeed;
		}
		y = y + ySpeed;

		canvas.drawBitmap(bmp, x, y, null);
		Paint farbe = new Paint();
		farbe.setColor(Color.WHITE);
		farbe.setTextSize(80);
		canvas.drawText("testX"+(test1)+"testY"+(test2), 0, 100, farbe);
		canvas.drawText("testX"+(test3)+"testY"+(test4), 0, 300, farbe);
		canvas.drawText("testX"+(test3-test1)+"testY"+(test4-test2), 0, 500, farbe);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// This prevents touchscreen events from flooding the main thread
		synchronized (event) {
			try {
				// Waits 16m
				event.wait(16);
				// when user touches the screen
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					// get initial positions
					initialX = event.getRawX();
					initialY = event.getRawY();
					test1 = (int)initialX;
					test2 = (int)initialY;
				}

				// when screen is released
				case MotionEvent.ACTION_UP: {
					lastX = event.getRawX() ;
					lastY = event.getRawY() ;
					test3=(int)event.getRawX();
					test4=(int)event.getRawY();
					}
				}
				deltaX=lastX-initialX;
				deltaY=lastY-initialY;

				test5=(int)deltaX;
				test6=(int)deltaY;
				if (Math.abs(deltaX) > Math.abs(minDistance)
							|| Math.abs(deltaY) > Math.abs(minDistance)) {
						if (Math.abs(deltaX) > Math.abs(deltaY)) {

							if (deltaX > 0) {
								// move right
								xSpeed = ps;
								ySpeed = 0;
							} else {
								// move left
								xSpeed = -ps;
								ySpeed = 0;
							}
						}

						else {
							if (deltaY > 0) {
								// move down
								xSpeed = 0;
								ySpeed = ps;
							} else {
								// move up
								xSpeed = 0;
								ySpeed = -ps;
							}
						}
				}
				
				
				return true;
			}

			catch (InterruptedException e) {
				return true;
			}
		}
	}
}