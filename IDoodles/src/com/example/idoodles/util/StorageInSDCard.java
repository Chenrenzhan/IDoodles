package com.example.idoodles.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.example.idoodles.R;
import com.example.idoodles.R.string;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

public class StorageInSDCard {
	
	public static boolean IsExternalStorageAvailableAndWriteable() {
		boolean externalStorageAvailable = false;
		boolean externalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			externalStorageAvailable = externalStorageWriteable = true;
		}
		else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			externalStorageAvailable = true;
			externalStorageWriteable = false;
		}
		else {
			externalStorageAvailable = externalStorageWriteable = false;
		}
		return externalStorageAvailable && externalStorageWriteable;
	}
	
	public static void saveBitmapInExternalStorage(Bitmap bitmap,Context context) {
		try {
			if(IsExternalStorageAvailableAndWriteable()) {
				File extStorage = new File(Environment.getExternalStorageDirectory().getPath() +"/idoodlespics");
				if (!extStorage.exists()) {
					extStorage.mkdirs();
				}
				File file = new File(extStorage,System.currentTimeMillis()+".png");
				FileOutputStream fOut = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				fOut.flush();
				fOut.close();
				Toast.makeText(context,  R.string.save_bitmap, Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(context,  R.string.fail_save_bitmap, Toast.LENGTH_SHORT).show();
			}
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
	
}
