package com.example.idoodles.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class EraserDrawView extends LineDrawView {

	public EraserDrawView(Context context, int width, int height, int size, int color) {
		super(context, width, height, size, color);
		// TODO Auto-generated constructor stub
		
		setPaint(mPaintSize, mBitmapBackground);
		mPaintColor = mBitmapBackground;
	}
	
	public void setPaint(Paint paint) {
		mPaint = paint;
		mPaint.setColor(mBitmapBackground);
	}

	public void setPaint(int size, int color) {
		setPaintSize( size);
		mPaint.setColor(mBitmapBackground);
	}
	
	public void setPaintColor(int color){
		mPaint.setColor(mBitmapBackground);
	}
	
	public void setPaintSize(int size){
		mPaintSize = size;
		mPaint.setStrokeWidth(size);
	}

}
