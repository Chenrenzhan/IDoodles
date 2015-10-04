package com.example.idoodles.view;

import com.example.idoodles.R;
import com.example.idoodles.R.drawable;
import com.example.idoodles.util.StorageInSDCard;
import com.example.idoodles.util.UndoAndRedo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class DrawView extends View {
	
	protected Context mContext;
	
	protected static UndoAndRedo mUndoRedo = null;
	
	protected int mWidth;
	protected int mHeight;
	
	protected Canvas mCanvas = null;
	protected Paint mPaint = null;
	protected int mPaintColor ; //画笔颜色
	protected int mPaintSize ; //画笔大小
	protected Bitmap mBitmap; //画布Bitmap
	protected int mBitmapBackground = Color.WHITE; //画布背景颜色，默认为白色
	
	public DrawView(Context context, int width, int height, int size, int color) {
		super(context);
		
		this.mContext = context;
		
		this.mWidth = width;
		this.mHeight = height;
		
		this.mPaintColor = color;
		this.mPaintSize = size;
		initial();
		
	}
	public Bitmap BackgroundImage;
	private void initial(){
		
		mUndoRedo = UndoAndRedo.getInstance();
		
		//创建位图
		mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
		
		mCanvas = new Canvas(mBitmap);
		
		mCanvas.drawColor(mBitmapBackground); 
		invalidate();
		
		mPaint=new Paint(Paint.DITHER_FLAG);//创建一个画笔
		mPaint.setStyle(Style.STROKE);//设置非填充
		mPaint.setStrokeWidth(mPaintSize); //设置默认笔宽
		mPaint.setColor(mPaintColor);//设置画笔默认颜色
		mPaint.setAntiAlias(true);//锯齿不显示
	}
	
	public void undo() {
		if(!mUndoRedo.currentIsFirst()) {
			mUndoRedo.undo(mBitmap);
			invalidate();
		}
		else{
			Toast.makeText(mContext, 
					"最多保存" + mUndoRedo.getCapacity() + "步，不能再后退了！", Toast.LENGTH_SHORT).show();
		}
	}
	public void redo() {
		if(!mUndoRedo.currentIsLast()) {
			mUndoRedo.redo(mBitmap);
			invalidate();
		}
		else{
			Toast.makeText(mContext, 
					"已经是最新的了！", Toast.LENGTH_SHORT).show();
		}
	}
	
	//刷新画布
	public void flushCanvas(  ){
		mCanvas.drawColor(mBitmapBackground); //设置画布背景颜色
		mUndoRedo.addBitmap(mBitmap);
		invalidate();
	}
	
	public void savePic(){
		savePic(mBitmap);
	}
	
	public void savePic(Bitmap bitmap) {
		StorageInSDCard.saveBitmapInExternalStorage(bitmap, mContext);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		setMeasuredDimension(mWidth, mHeight);
	}
	

    public Bitmap zoomBitmap(Bitmap bgimage, double newWidth, 
                    double newHeight) { 
            // 获取这个图片的宽和高 
            float width = bgimage.getWidth(); 
            float height = bgimage.getHeight(); 
            // 创建操作图片用的matrix对象 
            Matrix matrix = new Matrix(); 
            // 计算宽高缩放率 
            float scaleWidth = ((float) newWidth) / width; 
            float scaleHeight = ((float) newHeight) / height; 
            // 缩放图片动作 
            matrix.postScale(scaleWidth, scaleHeight); 
            Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, 
                            (int) height, matrix, true); 
            return bitmap; 
    } 

	public void setPaint(Paint paint) {
		mPaint = paint;
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
	
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	
	public void setmBitmap(Bitmap bitmap){
		mBitmap = bitmap;
//		invalidate();
	}

    
}
