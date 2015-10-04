package com.example.idoodles.view;

import com.example.idoodles.R;
import com.example.idoodles.R.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class CrayonDrawView extends DrawView implements Runnable {

	private int mStrokerDrawableId = R.drawable.crayon;
	
	private int mBrushDistance; //画两次bitmap画笔的间隔
	
	private GestureDetector brushGestureDetector = null;
	private BrushGestureListener brushGestureListener = null;

	public CrayonDrawView(Context context, int width, int height, int size, int color) {
		super(context, width, height, size, color);
		// TODO Auto-generated constructor stub

		mBrushDistance = (int)(mPaintSize * Math.log(mPaintSize) / 7); //间隔默认等于笔画大小
		
		brushGestureListener = new BrushGestureListener(casualStroke(
				mStrokerDrawableId, mPaintColor, mPaintSize), mBrushDistance, null);
		
		brushGestureDetector = new GestureDetector(brushGestureListener);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(mBitmapBackground);
		canvas.drawBitmap(mBitmap, 0, 0, null);
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		brushGestureDetector.onTouchEvent(event); //触发GestureDetector手势动作
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			
			break;
			
		case MotionEvent.ACTION_UP:
			
			mUndoRedo.addBitmap(mBitmap);
			break;
		}
		return true;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			postInvalidate();
		}
	}

	public void cleanPaintBitmap() {
		mCanvas.drawColor(mBitmapBackground, Mode.DST_OUT);
	}

	public Canvas getPaintCanvas() {
		return mCanvas;
	}

	private Bitmap casualStroke(int drawableId, int color, int size) {
		Bitmap originalMode = ((BitmapDrawable) this.getResources()
				.getDrawable(drawableId)).getBitmap();
		Bitmap newMode = zoomBitmap(originalMode, size, size);

		Bitmap bitmap = newMode.copy(Bitmap.Config.ARGB_8888, true);

		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);

		Paint paintUnder = new Paint();
		paintUnder.setColor(color);
		canvas.drawPaint(paintUnder);

		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

		canvas.drawBitmap(newMode, 0, 0, paint);
		
		return bitmap;
	}

	public void setBrushBitmap(int size, int color) {
		mPaintSize = size;
		mBrushDistance = (int)(mPaintSize * Math.log(mPaintSize) / 7); 
		Bitmap brushBitmap;
		int brushDistance;
		Paint brushPaint;
		brushBitmap = casualStroke(mStrokerDrawableId, color, size);
		brushPaint = null;
		brushGestureListener.setBrush(brushBitmap, mBrushDistance, brushPaint);
	}

	class BrushGestureListener extends GestureDetector.SimpleOnGestureListener {

		private Bitmap brushBitmap = null;
		private int brushDistance;
		private int halfBrushBitmapWidth;
		private Paint brushPaint = null;


		public BrushGestureListener(Bitmap brushBitmap, int brushDistance,
				Paint brushPaint) {
			super();
			setBrush(brushBitmap, brushDistance, brushPaint);
		}

		@Override
		public boolean onDown(MotionEvent e) {

			mCanvas.drawBitmap(brushBitmap, e.getX()
					- halfBrushBitmapWidth, e.getY() - halfBrushBitmapWidth,
					brushPaint);
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			float beginX = e2.getX();
			float beginY = e2.getY();
			float distance = (float) Math.sqrt(distanceX * distanceX
					+ distanceY * distanceY);
			float x = distanceX / distance, x_ = 0;
			float y = distanceY / distance, y_ = 0;
			while (Math.abs(x_) <= Math.abs(distanceX)
					&& Math.abs(y_) <= Math.abs(distanceY)) {
				x_ += x * brushDistance;
				y_ += y * brushDistance;
				mCanvas.save();
				mCanvas.rotate((float) (Math.random() * 10000), beginX
						+ x_, beginY + y_);
				mCanvas.drawBitmap(brushBitmap, beginX + x_
						- halfBrushBitmapWidth, beginY + y_
						- halfBrushBitmapWidth, brushPaint);
				mCanvas.restore();
			}
			CrayonDrawView.this.invalidate();
			
			return true;
		}

		public void setBrush(Bitmap brushBitmap, int brushDistance,
				Paint brushPaint) {
			this.brushBitmap = brushBitmap;
			this.brushDistance = brushDistance;
			halfBrushBitmapWidth = brushBitmap.getWidth() / 2;
			this.brushPaint = brushPaint;
		}

		public void freeBitmaps() {
			mBitmap.recycle();
			mBitmap.recycle();
			mUndoRedo.freeBitmaps();
		}
	}

	public void setPaint(int size, int color){
		setBrushBitmap(size, color);
	}
	
	public void setPaintSize(int size){
		this.setPaint(size, mPaintColor);
	}
	public void setPaintColor(int color){
		this.setPaint( mPaintSize, color);
		
	}
}
