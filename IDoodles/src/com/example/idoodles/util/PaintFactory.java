package com.example.idoodles.util;

import java.util.ArrayList;
import java.util.List;

import com.example.idoodles.R;
import com.example.idoodles.R.drawable;
import com.example.idoodles.view.CrayonDrawView;
import com.example.idoodles.view.DrawView;
import com.example.idoodles.view.EraserDrawView;
import com.example.idoodles.view.LineDrawView;

import android.content.Context;
import android.util.Log;

public class PaintFactory {
	private List paintInfoList = null;
//	private PaintFactory mPaintFactoryInstance;
	
	public enum PaintEnum{
		LINE("曲线", 0, R.drawable.line_paint),
		CRAYON("蜡笔", 1,  R.drawable.crayon_paint),
		ERASER("橡皮",  3,  R.drawable.eraser_paint);
		
		private String text;
		private int id;
		private int piantDrawableId;
		
		private PaintEnum(String text, int id, int drawableId){
			this.text = text;
			this.piantDrawableId = drawableId;
			this.id = id;
		}
		
		public String text(){
			return text;
		}
		public int drawbleId(){
			return piantDrawableId;
		}
		public int id(){
			return id;
		}
	}
	
	private PaintFactory(){
		
	}
	
	//用静态内部类实现多线程安全的单例模式
	private static class Instace{
		public static final PaintFactory instance = new PaintFactory();
	}
	
	public static PaintFactory getInstance(){
		return Instace.instance;
	}
	
	public  List<PaintInfo> createPaintInfoList(){
		 paintInfoList = new ArrayList<PaintInfo>();
		//通过反射机制遍历自定义枚举类
		Class cls = PaintEnum.class;
		for(Object obj : cls.getEnumConstants()){
			PaintInfo pi = new PaintInfo(((PaintEnum) obj).text(), 
					((PaintEnum) obj).drawbleId(), ((PaintEnum) obj).id());
			paintInfoList.add(pi);
		}
		return paintInfoList;
	}
	
	public List<PaintInfo> getPaintInfoList(){
		if(paintInfoList == null){
			return createPaintInfoList();
		}
		return paintInfoList;
	}

	public DrawView createPaint(Context context, 
			int paintId, int width, int height, int size, int color) {
		// TODO Auto-generated method stub
		DrawView drawView = null;
		
		switch(paintId){
		case 0:
			drawView = new LineDrawView(context, width, height, size, color);
			break;
			
		case 1:
			drawView = new CrayonDrawView(context, width, height, size, color);
			break;
			
		case 2:
			drawView = new EraserDrawView(context, width, height, size, color);
			break;
		}
		return drawView;
	}

}
