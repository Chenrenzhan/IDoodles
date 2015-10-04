package com.example.idoodles;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.idoodles.dialog.ColorPickerDialog;
import com.example.idoodles.dialog.PaintChooseDialog;
import com.example.idoodles.dialog.ColorPickerDialog.OnColorChangedListener;
import com.example.idoodles.dialog.PaintChooseDialog.ChoosePaintListener;
import com.example.idoodles.util.PaintFactory;
import com.example.idoodles.view.DrawView;

public class MainActivity extends Activity implements OnClickListener {

	private PaintFactory mPaintFactory;
	private List mPaintInfoList = null;
	
	private ImageButton newBtn;
	private ImageButton saveBtn;
	private ImageButton paintBtn;
	private ImageButton colorBtn;
	private ImageButton undoBtn;
	private ImageButton redoBtn;

	private LinearLayout mCanvasLayout; // 包含画布的linearlayout
	
	private DrawView mDrawView; 
	private LayoutParams mLayoutParams;
	
	private PaintChooseDialog mPaintChooseDialog;

	private int mPaintColor = Color.BLACK; //画笔默认颜色
	private int mPaintId = 1; //画笔ID，默认为曲线
	private int mPaintSize = 30; //画笔大小，默认为30
	
	private boolean hasMeasured = false;
	public static  int mLayoutWidth; //linearLayout宽
	public static  int mLayoutHeight; //linearLayout高
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏

		setContentView(R.layout.activity_main);
		
		mPaintFactory =PaintFactory.getInstance();
		
		mPaintInfoList = mPaintFactory.getPaintInfoList();

		initWdget();

		addWdgetToOnClickListener();
		
		 obtainViewSize();
	}

	private void initWdget() {
		newBtn = (ImageButton) findViewById(R.id.new_btn);
		saveBtn = (ImageButton) findViewById(R.id.save_btn);
		paintBtn = (ImageButton) findViewById(R.id.paint_btn);
		colorBtn = (ImageButton) findViewById(R.id.color_btn);
		undoBtn = (ImageButton) findViewById(R.id.undo_btn);
		redoBtn = (ImageButton) findViewById(R.id.redo_btn);

		mCanvasLayout = (LinearLayout) findViewById(R.id.canvas_layout);
		
		mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	private void addWdgetToOnClickListener() {
		newBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		paintBtn.setOnClickListener(this);
		colorBtn.setOnClickListener(this);
		undoBtn.setOnClickListener(this);
		redoBtn.setOnClickListener(this);
	}

	private void initCanvasView(int width, int height) {

		mDrawView = mPaintFactory.createPaint(MainActivity.this,
				mPaintId, mLayoutWidth, mLayoutHeight, mPaintSize, mPaintColor);
		mDrawView.setPaint(mPaintSize, mPaintColor);
		
		mDrawView.setMinimumWidth(width);
		mDrawView.setMinimumHeight(height);
		
		mCanvasLayout.addView(mDrawView, mLayoutParams);
	}
	
	
	// 获取LinearLayout的宽和高
	private void obtainViewSize() {
		ViewTreeObserver vto = mCanvasLayout.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if (hasMeasured == false) {
					
					mLayoutWidth = mCanvasLayout.getMeasuredWidth();
					mLayoutHeight = mCanvasLayout.getMeasuredHeight();
					
					hasMeasured = true;
					initCanvasView(mLayoutWidth, mLayoutHeight);
				}
				return true;
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();

		switch (viewId) {
		case R.id.new_btn:
			Log.e("mydebug", "---------  new");
			
			mDrawView.flushCanvas();

			break;
		case R.id.save_btn:
			Log.e("mydebug", "---------  save");
			
			mDrawView.savePic();

			break;
		case R.id.paint_btn:
			Log.e("mydebug", "---------  paint");

			mPaintChooseDialog = new PaintChooseDialog(this, mPaintInfoList, mPaintId, mPaintSize);
			mPaintChooseDialog.show();
			mPaintChooseDialog.setmChoosePaintListener(new ChoosePaintListener() {
				
				@Override
				public void ok(int paintId, int size) {
					// TODO Auto-generated method stub
					if(paintId != mPaintId){
						mPaintId = paintId;
						Bitmap bitmap = mDrawView.getmBitmap();
						mCanvasLayout.removeAllViews();
						mDrawView = null;
						mDrawView = mPaintFactory.createPaint(MainActivity.this,
								mPaintId, mLayoutWidth, mLayoutHeight, mPaintSize, mPaintColor);

						mDrawView.undo();
						mDrawView.redo();
						mCanvasLayout.addView(mDrawView, mLayoutParams);
						mDrawView.invalidate();
					}
					mPaintSize = size;
					mDrawView.setPaintSize(mPaintSize);
				}
				
				@Override
				public void cancel() {
					// TODO Auto-generated method stub
					
				}
			});

			break;
		case R.id.color_btn:
			Log.e("mydebug", "---------  color");
			onColorPikerDialog();
			break;
		case R.id.undo_btn:
			Log.e("mydebug", "---------  undo");
			
			mDrawView.undo();

			break;
		case R.id.redo_btn:
			Log.e("mydebug", "---------  redo");
			
			mDrawView.redo();

			break;
		}

	}

	private void onColorPikerDialog() {
		String colorPickerTitle = getResources().getString(
				R.string.color_pick_title);
		new ColorPickerDialog(this, mPaintColor, colorPickerTitle,
				new OnColorChangedListener() {

					@Override
					public void colorChanged(int color) {
						// TODO Auto-generated method stub
						mPaintColor = color;
						mDrawView.setPaintColor(mPaintColor);
					}
				}).show();
	}

}
