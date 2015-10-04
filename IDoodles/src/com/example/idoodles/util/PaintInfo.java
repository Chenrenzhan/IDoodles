package com.example.idoodles.util;

public class PaintInfo {
	
	private String text;
	private int id;
	private int piantDrawableId;
	
	public PaintInfo(String text, int drawableId, int id){
		this.text = text;
		this.piantDrawableId = drawableId;
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public int getId() {
		return id;
	}

	public int getPiantDrawableId() {
		return piantDrawableId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPiantDrawableId(int piantDrawableId) {
		this.piantDrawableId = piantDrawableId;
	}

}
