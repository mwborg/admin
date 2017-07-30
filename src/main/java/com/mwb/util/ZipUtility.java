package com.mwb.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;

public class ZipUtility {
	
	private static final int BUFFERSIZE = 204800; 
	/**
     * 压缩
     * 
     * @param zipFileName
     *            压缩产生的zip包文件名--带路径,如果为null或空则默认按文件名生产压缩文件名
     * @param relativePath
     *            相对路径，默认为空
     * @param directory
     *            文件或目录的绝对路径
     * @throws FileNotFoundException
     * @throws IOException
     */
	public static void zip(String zipFileName, String relativePath,
            String directory) throws FileNotFoundException, IOException {
		
        String fileName = zipFileName;
        if (fileName == null || fileName.trim().equals("")) {
        	File temp = new File(directory);
        	if (temp.isDirectory()) {
        		fileName = directory + ".zip";
        	} else {
        		if (directory.indexOf(".") > 0) {
        			fileName = directory.substring(0, directory.lastIndexOf(".")) + ".zip";
        		} else {
        			fileName = directory + ".zip";
        		}
        	}
        }
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName));
        zos.setEncoding("UTF-8");
        
        try {
        	zip(zos, relativePath, directory);
        } catch (IOException ex) {
        	throw ex;
        } finally {
        	if (null != zos) {
        		zos.close();
        	}
        }
    }
	
	/**
     * 压缩
     * 
     * @param zos
     *            压缩输出流
     * @param relativePath
     *            相对路径
     * @param absolutPath
     *            文件或文件夹绝对路径
     * @throws IOException
     */
    private static void zip(ZipOutputStream zos, String relativePath,
           String absolutPath) throws IOException {
        File file = new File(absolutPath);
        if (file.isDirectory()) {
        	File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];
                if (tempFile.isDirectory()) {
                	String newRelativePath = relativePath + tempFile.getName() + File.separator;
                    createZipNode(zos, newRelativePath);
                    zip(zos, newRelativePath, tempFile.getPath());
                } else {
                	zipFile(zos, tempFile, relativePath);
                }
            }
        } else {
        	zipFile(zos, file, relativePath);
        }
   }
	
    private static void zipFile(ZipOutputStream zos, File file, String relativePath) throws IOException {
    	ZipEntry entry = new ZipEntry(relativePath + file.getName());
        zos.putNextEntry(entry);
        InputStream is = null;
        try {
        	is = new FileInputStream(file);
        	int length = 0;
        	byte[] buffer = new byte[BUFFERSIZE];
        	while ((length = is.read(buffer, 0, BUFFERSIZE)) >= 0) {
        		zos.write(buffer, 0, length);
        	}
        } catch (IOException ex) {
        	throw ex;
        } finally {
        	if (is != null) {
        		is.close();
        	}
        }
    }
    
	private static void createZipNode(ZipOutputStream zos, String relativePath) throws IOException {
		ZipEntry zipEntry = new ZipEntry(relativePath);
		zos.putNextEntry(zipEntry);
		zos.closeEntry();
	}
	
}
