package com.example.idoodles.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;

public class LineDrawView extends DrawView {

	private Path mPath;
	private float mX, mY;

	public LineDrawView(Context context, int width, int height, int size, int color) {
		super(context, width, height, size, color);
		// TODO Auto-generated constructor stub

		mPath = new Path();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawPath(mPath, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onTouchDown(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			onTouchMove(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			onTouchUp(x, y);
			invalidate();
			break;
		default:
		}
		return true;
	}

	private void onTouchDown(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void onTouchMove(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx > 0 || dy > 0) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		} else if (dx == 0 || dy == 0) {
			mPath.quadTo(mX, mY, (x + 1 + mX) / 2, (y + 1 + mY) / 2);
			mX = x + 1;
			mY = y + 1;
		}
		mCanvas.drawPath(mPath, mPaint);
	}

	private void onTouchUp(float x, float y) {
		mPath.reset();
		
		mUndoRedo.addBitmap(mBitmap);
	}
	
	public void setPaint(int size, int color) {
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(size);
		mPaint.setColor(color);
	}
	
	public void setPaintColor(int color){
		mPaintColor = color;
		mPaint.setColor(color);
	}
	
	public void setPaintSize(int size){
		mPaintSize = size;
		mPaint.setStrokeWidth(size);
	}
	
	public void freeBitmaps() {
		mBitmap.recycle();
		mUndoRedo.freeBitmaps();
	}
}
