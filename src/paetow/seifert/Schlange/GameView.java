package paetow.seifert.Schlange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float initialX = 0;
		float initialY = 0;
		float deltaX = 0;
		float deltaY = 0;
		final int minDistance = 150;

		// This prevents touchscreen events from flooding the main thread
		synchronized (event) {
			try {
				// Waits 16m
				event.wait(16);

				// when user touches the screen
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					// reset deltaX and deltaY
					deltaX = deltaY = 0;

					// get initial positions
					initialX = event.getRawX();
					initialY = event.getRawY();
				}

				// when screen is released
				case MotionEvent.ACTION_UP: {
					deltaX = event.getRawX() - initialX;
					deltaY = event.getRawY() - initialY;

					if (Math.abs(deltaX) > Math.abs(minDistance)
							|| Math.abs(deltaY) > Math.abs(minDistance)) {
						if (Math.abs(deltaX) > Math.abs(deltaY)) {

							if (deltaX > 0) {
								// move right
								xSpeed = 3;
								ySpeed = 0;
							} else {
								// move left
								xSpeed = -3;
								ySpeed = 0;
							}
						}

						if (Math.abs(deltaX) < Math.abs(deltaY)) {
							if (deltaY > 0) {
								// move down
								xSpeed = 0;
								ySpeed = 3;
							} else {
								// move up
								xSpeed = 0;
								ySpeed = -3;
							}
						}
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