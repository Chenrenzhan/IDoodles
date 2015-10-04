package com.example.idoodles.util;

import com.example.idoodles.MainActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class UndoAndRedo {
	
	private Context mContext;
	
	private  int mCapacity = 15;
	private int mFront;
	private int mCurrent;
	private int mEnd;
	private int[] pixels = null;
	private Bitmap[] mBitmaps = null;
	
	private int mWidth;
	private int mHeight;
	
	private UndoAndRedo undoAndRedoInstance;
	
	private UndoAndRedo(){
		mWidth = MainActivity.mLayoutWidth;
		mHeight = MainActivity.mLayoutHeight;
		
		mBitmaps = initial(mCapacity);
	}
	
	private static class Instace{
		public static final UndoAndRedo instance = new UndoAndRedo();
	}
	
	public static UndoAndRedo getInstance(){
		return Instace.instance;
	}
	
	private Bitmap[] initial(int capacity ) {
		Bitmap[] bitmaps = new Bitmap[mCapacity];
		for(int i = 0; i < bitmaps.length; i++)
			bitmaps[i] = Bitmap.createBitmap(mWidth,
					mHeight, Bitmap.Config.ARGB_8888);
		pixels = null;
		pixels = new int[mWidth * mHeight];
		mFront = mCurrent = mEnd = 0;
		
		return bitmaps;
	}
	
	public void addBitmap(Bitmap bitmap) {
		//满的
		if((mFront - mCurrent + mCapacity) % mCapacity == 1) {
			mFront = (mFront + 1) % mCapacity;	
		}
		mCurrent = (mCurrent + 1) % mCapacity;
		mEnd = mCurrent;
		bitmap.getPixels(pixels, 0, mWidth, 0, 0,
				mWidth, mHeight);
		mBitmaps[mCurrent].setPixels(pixels, 0, mWidth, 0, 0,
				mWidth, mHeight);
		
	}
	
	public boolean currentIsFirst() {
		if(mCurrent == mFront) {
			return true;
		}
		
		return false;
	}
	
	public boolean currentIsLast() {
		if(mCurrent == mEnd) {
			return true;
		}
		return false;
	}
	
	public void undo(Bitmap paintBitmap) {
		mCurrent = (mCurrent - 1 + mCapacity) % mCapacity;
		mBitmaps[mCurrent].getPixels(pixels, 0, mWidth, 0, 0,
				mWidth, mHeight);
		paintBitmap.setPixels(pixels, 0, mWidth, 0, 0,
				mWidth, mHeight);
	}
	
	public void redo(Bitmap paintBitmap) {
		mCurrent = (mCurrent + 1) % mCapacity;
		mBitmaps[mCurrent].getPixels(pixels, 0, mWidth, 0, 0,
				mWidth, mHeight);
		paintBitmap.setPixels(pixels, 0, mWidth, 0, 0,
				mWidth, mHeight);
	}
	
	public void freeBitmaps() {
		pixels = null;
		for(int i = 0; i < mBitmaps.length; i++)
			mBitmaps[i].recycle();
	}
	
	public void setCapacity(int capacity){
		
		if(capacity == mCapacity){
			return ;
		}
		
		reSetCapacity(capacity);
	}


	/**
	 * @param capacity :设置保存的容量
	 */
	private void reSetCapacity(int capacity) {
		int dfe = (mCapacity + mFront - mEnd) % mCapacity;
		int dce = (mCapacity + mCurrent - mEnd) % mCapacity;
		
		int front = mFront  % capacity;
		int current = mCurrent  % capacity;
		int end = mEnd  % capacity;
		
		Bitmap[] bitmaps  = initial(capacity);
		
		int[] pixs = new int[mWidth * mHeight];
		
		if(capacity < dfe){
			for(int i = capacity - 1; i >= 0; --i){
				mBitmaps[end--].getPixels(pixs, 0, mWidth, 0, 0,
						mWidth, mHeight);
				bitmaps[i].setPixels(pixs, 0, mWidth, 0, 0,
						mWidth, mHeight);
			}
			mFront = 0;
			mCurrent = dce > capacity ? 0 : capacity - dce;
			mEnd = capacity - 1;
		}
		else{
			while(end >= front){
				mBitmaps[end].getPixels(pixs, 0, mWidth, 0, 0,
						mWidth, mHeight);
				bitmaps[end].setPixels(pixs, 0, mWidth, 0, 0,
						mWidth, mHeight);
				--end;
			}
		}
		
		pixs = null;
		
		freeBitmaps();
		
		mCapacity = capacity;
		
		mBitmaps = bitmaps;
	}
	
	public int getCapacity(){
		return mCapacity;
	}
}
