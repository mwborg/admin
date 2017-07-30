package com.mwb.util;

import com.mwb.dao.modle.Log;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public class FileUtility {
	private static final Log LOG = Log.getLog(FileUtility.class);
	/**
	 * 文件删除
	 * @param 
	 */
	public static void deleteTmpFile(File file){
		if (file.isFile()){
			file.delete();
		}else{
			File [] files = file.listFiles();
			for (File childFile : files) {
				deleteTmpFile(childFile);
			}
			
			file.delete();
		}
	}
	
	public static <T> File createCSVFile(List<String> header, List<T> data, String filePath) {
        File file = new File(filePath);
		BufferedWriter fileOutputStream = null;
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            file.createNewFile();
            // GB2312使正确读取分隔符","
            fileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "GB2312"), 1024);
            
            // 写入文件头部
            for (int i = 0;i < header.size();i ++) {
        		fileOutputStream.write(header.get(i));
            	if (i != header.size() - 1) {
            		fileOutputStream.write(",");
            	}
            }
            fileOutputStream.newLine();
            
            // 写入文件内容
            for (T t : data) {
            	Class<? extends Object> clazz = t.getClass();
            	Field[] fields = clazz.getDeclaredFields();
            	int length = fields.length;
            	if (length == 0) {
            		continue;
            	}
            	
            	for (int i = 0;i < length;i ++) {
            		 PropertyDescriptor pd = new PropertyDescriptor(fields[i].getName(), clazz);
            		 //	获得get方法
                     Method getMethod = pd.getReadMethod();
                     // 执行get方法返回一个Object
                     Object value = getMethod.invoke(t);
                     fileOutputStream.write(String.valueOf(value));
                     if (i != length - 1) {
                    	 fileOutputStream.write(",");
                     }
            	}
                fileOutputStream.newLine();  
            }
            fileOutputStream.flush();  
        } catch (Exception e) {  
        	LOG.error("Catch an exception", e);
        	return null;
        } finally {  
           try {
        	   if (file != null) { 
                  fileOutputStream.close(); 
        	   }
            } catch (IOException e) {  
            	LOG.error("Catch an exception", e);
           }  
       }
		return file;  
    }
}
