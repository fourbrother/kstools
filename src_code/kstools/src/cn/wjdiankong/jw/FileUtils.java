package cn.wjdiankong.jw;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtils {
	
	//private final static String DEX_MAGIC = "dex\n035";
	private final static byte[] DEX_MAGIC = new byte[]{0x64, 0x65, 0x78, 0x0a, 0x30, 0x33, 0x35,0x00};
	
	public static boolean isValidDexFile(String dexFile){
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(new File(dexFile));
			byte[] magic = new byte[8];
			fis.read(magic, 0, 8);
			boolean isValid = true;
			for(int i=0;i<8;i++){
				if(DEX_MAGIC[i] == magic[i]){
					isValid &= true;
				}else{
					isValid &= false;
				}
			}
			return isValid;
		}catch(Exception e){
		}finally{
			if(fis != null){
				try{
					fis.close();
				}catch(Exception e){
					
				}
			}
		}
		return false;
	}
	
	/** 
	 * 文件拷贝的方法 
	 */  
	public static boolean fileCopy(String src, String des) {  
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {  
			fis = new FileInputStream(src);
			fos = new FileOutputStream(des);
			int len = 0;
			byte[] buffer = new byte[10*1024];
			while((len=fis.read(buffer)) > 0){  
				fos.write(buffer, 0, len);
			}  
		} catch (Exception e) {  
			System.out.println("拷贝文件失败:"+e.toString());
			return false;
		}finally{  
			try {  
				if(fis!=null)  fis.close();  
				if(fos!=null)  fos.close();  
			} catch (Exception e) {  
				System.out.println("拷贝文件失败:"+e.toString());
				return false;
			} 
		}
		return true;

	}    
	
	private static void copy(InputStream input, OutputStream output) throws IOException {
        int bytesRead;
        final byte[] BUFFER = new byte[4096 * 1024];
        while ((bytesRead = input.read(BUFFER))!= -1) {
            output.write(BUFFER, 0, bytesRead);
        }
    }
	
	public static void addFileToZipFile(String fileName, String entryName, String zipFileName, String newZipFileName){
		try{
			@SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream(fileName);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while((len=fis.read(buffer)) > 0){
				bos.write(buffer, 0, len);
			}
			// read war.zip and write to append.zip
	        ZipFile war = new ZipFile(zipFileName);
	        ZipOutputStream append = new ZipOutputStream(new FileOutputStream(newZipFileName));
	        // first, copy contents from existing war
	        Enumeration<? extends ZipEntry> entries = war.entries();
	        while (entries.hasMoreElements()) {
	            ZipEntry e = entries.nextElement();
	            append.putNextEntry(e);
	            if (!e.isDirectory()) {
	                copy(war.getInputStream(e), append);
	            }
	            append.closeEntry();
	        }
	        ZipEntry e = new ZipEntry(entryName);
	        append.putNextEntry(e);
	        append.write(bos.toByteArray());
	        append.closeEntry();
	        // close
	        war.close();
	        append.close();
		}catch(Exception e){
		}
	}

	public static void zip(String zipFileName, File inputFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();        
		out.close(); // 输出流关闭
	}    

	private static void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws Exception { // 方法重载
		if (f.isDirectory()){
			File[] fl = f.listFiles();            
			if (fl.length == 0){
				out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
			}
		} else {
			out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b; 
			while ((b = bi.read()) != -1) {
				bo.write(b); // 将字节流写入当前zip目录
			}
			bi.close();
			in.close(); // 输入流关闭
		}
	} 
	
	public static void decompressZipFile(String zipPath, String targetPath) throws IOException  {      
		File file = new File(zipPath);    
		if (!file.isFile()) {    
			throw new FileNotFoundException("file not exist!");    
		}    
		if (targetPath == null || "".equals(targetPath)) {    
			targetPath = file.getParent();    
		}      
		@SuppressWarnings("resource")
		ZipFile zipFile = new ZipFile(file);    
		Enumeration<? extends ZipEntry> files = zipFile.entries();    
		ZipEntry entry = null;    
		File outFile = null;    
		BufferedInputStream bin = null;    
		BufferedOutputStream bout = null;    
		while (files.hasMoreElements()) {    
			entry = files.nextElement();    
			outFile = new File(targetPath + File.separator + entry.getName());    
			// 如果条目为目录，则跳向下一个     
			if(entry.isDirectory()){  
				outFile.mkdirs();    
				continue;    
			}    
			// 创建目录    
			if (!outFile.getParentFile().exists()) {    
				outFile.getParentFile().mkdirs();    
			}    
			// 创建新文件    
			outFile.createNewFile();    
			// 如果不可写，则跳向下一个条目    
			if (!outFile.canWrite()) {    
				continue;    
			}    
			try {    
				bin = new BufferedInputStream(zipFile.getInputStream(entry));    
				bout = new BufferedOutputStream(new FileOutputStream(outFile));    
				byte[] buffer = new byte[1024];    
				int readCount = -1;    
				while ((readCount = bin.read(buffer)) != -1) {    
					bout.write(buffer, 0, readCount);    
				}    
			} finally {    
				try {    
					bin.close();    
					bout.flush();    
					bout.close();    
				} catch (Exception e) {}    
			}    
		}    
	}  

	public static void decompressDexFile(String zipPath, String targetPath) throws IOException  {      
		File file = new File(zipPath);    
		if (!file.isFile()) {    
			throw new FileNotFoundException("file not exist!");    
		}    
		if (targetPath == null || "".equals(targetPath)) {    
			targetPath = file.getParent();    
		}      
		@SuppressWarnings("resource")
		ZipFile zipFile = new ZipFile(file);    
		Enumeration<? extends ZipEntry> files = zipFile.entries();    
		ZipEntry entry = null;    
		File outFile = null;    
		BufferedInputStream bin = null;    
		BufferedOutputStream bout = null;    
		while (files.hasMoreElements()) {    
			entry = files.nextElement();    
			outFile = new File(targetPath + File.separator + entry.getName());    
			// 如果条目为目录，则跳向下一个     
			if(entry.isDirectory()){  
				outFile.mkdirs();
				continue;    
			}    
			//这里只会解压dex文件和签名文件
			if(!(entry.getName().endsWith("classes.dex") || entry.getName().startsWith("META-INF"))){
				continue;
			}
			// 创建目录    
			if (!outFile.getParentFile().exists()) {    
				outFile.getParentFile().mkdirs();    
			}    
			// 创建新文件    
			outFile.createNewFile();    
			// 如果不可写，则跳向下一个条目    
			if (!outFile.canWrite()) {    
				continue;    
			}    
			try {    
				bin = new BufferedInputStream(zipFile.getInputStream(entry));    
				bout = new BufferedOutputStream(new FileOutputStream(outFile));    
				byte[] buffer = new byte[1024];    
				int readCount = -1;    
				while ((readCount = bin.read(buffer)) != -1) {    
					bout.write(buffer, 0, readCount);    
				}    
			} finally {    
				try {    
					bin.close();    
					bout.flush();    
					bout.close();    
				} catch (Exception e) {}    
			}    
		}    
	}  


	/** 
	 * 删除单个文件 
	 *  
	 * @param fileName 
	 *            要删除的文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public static boolean deleteFile(String fileName) {  
		File file = new File(fileName);  
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除  
		if (file.exists() && file.isFile()) {  
			if (file.delete()) {  
				return true;  
			} else {  
				return false;  
			}  
		} else {  
			return false;  
		}  
	}  

	/** 
	 * 删除目录及目录下的文件 
	 *  
	 * @param dir 
	 *            要删除的目录的文件路径 
	 * @return 目录删除成功返回true，否则返回false 
	 */  
	public static boolean deleteDirectory(String dir) {  
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符  
		if (!dir.endsWith(File.separator))  
			dir = dir + File.separator;  
		File dirFile = new File(dir);  
		// 如果dir对应的文件不存在，或者不是一个目录，则退出  
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {  
			return false;  
		}  
		boolean flag = true;  
		// 删除文件夹中的所有文件包括子目录  
		File[] files = dirFile.listFiles();  
		for (int i = 0; i < files.length; i++) {  
			// 删除子文件  
			if (files[i].isFile()) {  
				flag = deleteFile(files[i].getAbsolutePath());  
				if (!flag)  
					break;  
			}  
			// 删除子目录  
			else if (files[i].isDirectory()) {  
				flag = deleteDirectory(files[i]  
						.getAbsolutePath());  
				if (!flag)  
					break;  
			}  
		}  
		if (!flag) {  
			return false;  
		}  
		// 删除当前目录  
		if (dirFile.delete()) {  
			return true;  
		} else {  
			return false;  
		}  
	}  
	
	public static void printException(Throwable e){
		if(e == null){
			return;
		}
		StackTraceElement[] eles = e.getStackTrace();
		for(StackTraceElement ele : eles){
			System.out.println(ele.toString());
		}
	}
	
}
