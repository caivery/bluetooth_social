package edu.minggo.chat.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
/**
 * ��ϢͼƬ����
 * @author minggo
 * @created 2013-2-17����08:41:12
 */
public class ClippingPicture {
	
	public static final String TALK_FILES = Environment.getExternalStorageDirectory().getAbsolutePath()+"/BlueChatImag/talkpic";
	public static final String GALLERY_FILES = Environment.getExternalStorageDirectory().getAbsolutePath()+"/BlueChatImag/userPic/gallery";
	public static final String USER_PIC_FILES = Environment.getExternalStorageDirectory().getAbsolutePath()+"/BlueChatImag/userPic";
	public static final String TALK_FILES2 = TALK_FILES+"/";
	public static String signinPicName = "";
	public static String talkPicName;
	public static String galleryPicName;
	public static Bitmap bitmap;
	/**
	 * ����ͼƬ
	 * @param bitmap
	 */
	public static void saveTalkBitmap(Bitmap bitmap) {
		
		//bitmap = ImageUitl.toRoundCorner(bitmap, 30);
		
		System.out.println(TALK_FILES);
		File myCaptureFile = new File(TALK_FILES);
		if (!myCaptureFile.exists())
			myCaptureFile.mkdirs();
		
		File f = new File(myCaptureFile.getAbsolutePath() + "/" + setTalkFileNames());
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(f));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * �����û�ͷ��
	 * @param bitmap
	 */
	public static String saveUserPortrait(Bitmap bitmap){
		File myCaptureFile = new File(USER_PIC_FILES);
		if (!myCaptureFile.exists())
			myCaptureFile.mkdirs();
		File f = new File(myCaptureFile.getAbsolutePath() + "/" + setUserPortraitNames());
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(f));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myCaptureFile.getAbsolutePath() + "/" + setUserPortraitNames();
	}
	/**
	 * ���ͼƬ���棬�ǵ���ѹ��
	 * @param bitmap
	 */
	public static String saveGalleryPic(Bitmap bitmap){
		System.out.println(GALLERY_FILES);
		File myCaptureFile = new File(GALLERY_FILES);
		if (!myCaptureFile.exists())
			myCaptureFile.mkdirs();
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		File f = new File(myCaptureFile.getAbsolutePath() + "/" + dateFormat.format(date)+ "_gallery"+ ".jpg");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(f));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myCaptureFile.getAbsolutePath() + "/" + dateFormat.format(date)+ "_gallery"+ ".jpg";
	}
	/**
	 * ����дӱ���ȡͼƬ
	 * @param activity
	 * @param uri
	 * @return
	 */
	public static Map<String,Object> saveGalleryPic(Activity activity,Uri uri){
		Map<String, Object> maps = new HashMap<String, Object>();
		String[] projection = {MediaStore.Images.Media.DATA};
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		int i = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String img_path = cursor.getString(i);
		if (new File(img_path).length()>1024*512) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 5;
			Bitmap bitmap1 = BitmapFactory.decodeFile(img_path,options);
			bitmap  = bitmap1;
			saveGalleryPic(bitmap1);
		}else{
			Bitmap bitmap1 = BitmapFactory.decodeFile(img_path);
			saveGalleryPic(bitmap1);
		}
		
		maps.put("galleryPicName", img_path);
		maps.put("bitmap", bitmap);
		return maps;
	}
	/**
	 * ����ͼƬ
	 * @return
	 */
	public static File createTalkImage(String subName){
		
		File appFile = new File(TALK_FILES);
		if(!appFile.exists()){
			appFile.mkdir();
		}
		return new File(TALK_FILES + "/" + setTalkFileNames(subName));
	}
	/**
	 * �ļ�����������ʽ��
	 * @return
	 */
	public static String setTalkFileNames() {
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		talkPicName = dateFormat.format(date)+ "_talk"+ ".jpg";
		return talkPicName;
	}
	/**
	 * �û�ͷ��
	 * @return
	 */
	public static String setUserPortraitNames() {
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date)+ "_portrait"+ ".jpg";
	}
	/**
	 * �ļ�����������ʽ��
	 * @return
	 */
	public static String setTalkFileNames(String subName) {
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		talkPicName = dateFormat.format(date)+ "_talk"+ subName;
		return talkPicName;
	}
	/**
	 * �ӱ�����ͼƬ�ļ��ñ���
	 * @param activity
	 * @param uri
	 * @return
	 */
	public static boolean saveGetPicFromLocal(Activity activity,Uri uri){
		String[] projection = {MediaStore.Images.Media.DATA};
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		int i = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String img_path = cursor.getString(i);
		galleryPicName = img_path;
		if (new File(img_path).length()>1024*512) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 5;
			Bitmap bitmap1 = BitmapFactory.decodeFile(img_path,options);
			bitmap  = bitmap1;
			saveTalkBitmap(bitmap1);
		}else{
			Bitmap bitmap1 = BitmapFactory.decodeFile(img_path);
			saveTalkBitmap(bitmap1);
		}
		return true;
	}
	/**
	 * ��ʾʱ���¶����С
	 * @param bmp
	 * @return
	 */
	public static Bitmap Resize(Bitmap bmp) {
		float scaleWidth = 1;
		float scaleHeight = 1;
		double scale = 0;

		// ���ͼƬ�Ŀ��
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		if (width <= 600 && height <= 600) {
			return bmp;
		}else if(width <= 800 && height <= 800){
			scale = 0.8;
		}else if(width <= 960 && height <= 800){
			scale = 0.7;
		}else if(width <= 1200 && height <= 1600){
			scale = 0.6;
		}else if(width > 1200 || height > 1200){
			scale = 0.5;
		}
		
		// �������ű���
		scaleWidth = (float) (scaleWidth * scale);
		scaleHeight = (float) (scaleHeight * scale);
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// �õ��µ�ͼƬ
		Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,true);
		return newbm;
	}
	/**
	 * drawableToBitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
	 * ����Drawable����
	 * @param bmp
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bmp){
		return new BitmapDrawable(bmp);
	}
	/**
	 * ɾ��ͼƬ
	 * @param fileName
	 */
    public static void deleteSDFile(String fileName) { 
        File file = new File(TALK_FILES + "//" + fileName); 
        if (file == null || !file.exists() || file.isDirectory()) {
        	
        }else{
        	file.delete();
        }
    }
    
}
