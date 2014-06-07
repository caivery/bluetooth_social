package edu.minggo.chat.util;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.os.Environment;
/**
 * ¼�����ò���
 * @author minggo
 * @created 2013-2-16����02:37:11
 */
public class ClippingSounds {
	
	 public static final String TALKSOUND_FILE = Environment.getExternalStorageDirectory().getAbsolutePath()+"/BlueChatImag/sounds/";
     
     public static String talkSoundName;
     
     public static String saveSounds(){
    	 File myCaptureFile;
		try{
 			
			myCaptureFile = new File(TALKSOUND_FILE);
 			if (!myCaptureFile.exists())
 				myCaptureFile.mkdirs();
 			
    	 }catch(Exception e){
    		 e.printStackTrace();
    		 return null;
    	 }
    	 return TALKSOUND_FILE+saveTalkSoundsFileNames();
     }
     /**
      * ������׼��
      * @return
      */
     public static String saveSounds2(){
    	 
    	 File myCaptureFile;
 		try{
  			
 			myCaptureFile = new File(TALKSOUND_FILE);
  			if (!myCaptureFile.exists())
  				myCaptureFile.mkdirs();
  			
     	 }catch(Exception e){
     		 e.printStackTrace();
     		 return null;
     	 }
     	 return TALKSOUND_FILE+saveTalkSoundsFileNames();

     }
    /**
     * �����ļ����ļ�������
     * @return
     */
	public static String saveTalkSoundsFileNames() {
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyyMMdd_HHmmss");
		talkSoundName = dateFormat.format(date)+ "_sound"+ ".mp3";
		return talkSoundName;
	}

}
