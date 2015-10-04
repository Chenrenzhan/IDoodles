package com.example.idoodles.dialog;

import java.util.List;

import com.example.idoodles.R;
import com.example.idoodles.R.id;
import com.example.idoodles.R.layout;
import com.example.idoodles.R.string;
import com.example.idoodles.util.PaintInfo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PaintChooseDialog extends Dialog implements android.view.View.OnClickListener  {
	
	private Context mContext;
	
	private ListView mListView; 
	private ImageView mChosenView; //当前选择的画笔显示
	
	private SeekBar mSeekBarSize; //滑块选择画笔大小
	private TextView mSizeText; //显示滑块选择的画笔的大小
	
	private Button mBtnOk; //确定按钮
	private Button mBtnCancel; //取消按钮
	
	
	
	private List mPaintInfoList = null;
	
	private int mChosenSize ; //选择画笔大小
	private int mChosenPaintId; //所选择的画笔
	
	private ChoosePaintListener mChoosePaintListener; //选择之后回调监听

	


	public PaintChooseDialog(Context context,List paintInfos, int paint, int size) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mChosenPaintId = paint;
		this.mChosenSize = size;
		
		mPaintInfoList = paintInfos;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.paint_dialog);
		
		String title = mContext.getResources().getString(R.string.paint_choose_title);
		setTitle(R.string.paint_choose_title);
		
		dialogShowStyle();
		
		initView();
		
		listViewOnclickItem();
		
		seekBarOnChange();
		
	}
	
	public void dialogShowStyle() {
		Window dialogWindow = getWindow();
		Display display = dialogWindow.getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		lp.alpha = 0.5f;
		lp.width = display.getWidth();
		lp.height = display.getHeight() - 200;
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);
	}

	private void initView() {
		mListView = (ListView)findViewById(R.id.paint_list);
		mChosenView = (ImageView)findViewById(R.id.chosen_paint);
		mChosenView.setImageResource(
				((PaintInfo) mPaintInfoList.get(mChosenPaintId)).getPiantDrawableId());
		
		mSeekBarSize = (SeekBar)findViewById(R.id.paint_size);
		mSizeText = (TextView)findViewById(R.id.size_text);
		mSeekBarSize.setMax(100);
		mSeekBarSize.setProgress(mChosenSize);
		mSizeText.setText(String.valueOf(mChosenSize));
		
		mBtnOk = (Button)findViewById(R.id.ok_btn);
		mBtnCancel = (Button)findViewById(R.id.cancel_btn);
		mBtnOk.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		
		PaintListAdapter adapter = new PaintListAdapter(mContext, mPaintInfoList);
		mListView.setAdapter(adapter);
	}

	private void listViewOnclickItem(){
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mChosenView.setImageResource(
						((PaintInfo) mPaintInfoList.get(position)).getPiantDrawableId());
				mChosenPaintId = position;
			}
		});
	}

	private void seekBarOnChange(){
		mSeekBarSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
				mChosenSize = progress;
				
				mSizeText.setText(String.valueOf(mChosenSize));
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.ok_btn:
			mChoosePaintListener.ok(mChosenPaintId, mChosenSize);
			this.dismiss();
			break;
			
		case R.id.cancel_btn:
			mChoosePaintListener.cancel();
			this.dismiss();
			break;
		}
		
	}
	
	private class PaintListAdapter extends BaseAdapter{
		
		private Context context;
		private LayoutInflater mInflater;
		private List<PaintInfo> paintList;
		

		public PaintListAdapter(Context context, List<PaintInfo> paintInfos){
			 this.mInflater = LayoutInflater.from(context);
			
			this.context = context;
			this.paintList = paintInfos;
			
			
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return paintList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return paintList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return paintList.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder viewHolder;
			
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.paint_item, null);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			}
			else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			PaintInfo paintInfo = paintList.get(position);
			
			viewHolder.text.setText(paintInfo.getText());
			viewHolder.paintView.setImageResource(paintInfo.getPiantDrawableId());
			viewHolder.id = paintInfo.getId();
			
			
			return convertView;
		}
		
		final class ViewHolder{
			public TextView text;
			public ImageView paintView;
			public int id;
			
			public ViewHolder(View view){
				text = (TextView)view.findViewById(R.id.text);
				paintView = (ImageView)view.findViewById(R.id.paint_image);
			}
		}
	}
	
	public void setmChoosePaintListener(ChoosePaintListener mChoosePaintListener) {
		this.mChoosePaintListener = mChoosePaintListener;
	}
	
	public interface ChoosePaintListener{
		public void ok(int paintId, int size); //确定回调函数
		public void cancel(); //取消回调函数
	}

}
