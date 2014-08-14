package com.asfman.util;

import java.io.InputStream;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;

import android.content.Context;

public class FileUtils {
	//从resources中的raw 文件夹中获取文件并读取数据  
    public static String getFromRaw(int rawId, Context context){  
        String result = "";  
            try {  
                InputStream in = context.getResources().openRawResource(rawId);  
                //获取文件的字节数  
                int lenght = in.available();  
                //创建byte数组  
                byte[]  buffer = new byte[lenght];  
                //将文件中的数据读到byte数组中  
                in.read(buffer);  
                result = EncodingUtils.getString(buffer, HTTP.UTF_8);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return result;  
    }  
      
    //从assets 文件夹中获取文件并读取数据  
    public static String getFromAssets(String fileName, Context context){  
        String result = "";  
            try {  
                InputStream in = context.getResources().getAssets().open(fileName);  
                //获取文件的字节数  
                int lenght = in.available();  
                //创建byte数组  
                byte[]  buffer = new byte[lenght];  
                //将文件中的数据读到byte数组中  
                in.read(buffer);  
                result = EncodingUtils.getString(buffer, HTTP.UTF_8);  
            } catch (Exception e) {  
               return null;  
            }  
            return result;  
    }  
}
