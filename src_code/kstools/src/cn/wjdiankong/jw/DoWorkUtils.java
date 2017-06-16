package cn.wjdiankong.jw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.wjdiankong.kstools.AnalysisApk;
import cn.wjdiankong.kstools.ApkSign;

public class DoWorkUtils {
	
	public static ArrayList<String> allDexList = new ArrayList<String>();
	public static ArrayList<String> errorDexList = new ArrayList<String>();
	
	/**
	 * 获取应用签名信息
	 * @param srcApkFile
	 * @return
	 */
	public static boolean getAppSign(File srcApkFile){
		try{
			long time = System.currentTimeMillis();
			System.out.println("第一步==> 获取apk文件签名信息");
			String sign = ApkSign.getApkSignInfo(srcApkFile.getAbsolutePath());
			Const.appSign = sign;
			System.out.println("signed:"+sign);
			System.out.println("获取apk签名信息成功===耗时:"+((System.currentTimeMillis()-time)/1000)+"s\n\n");
			return true;
		}catch(Exception e){
			System.out.println("获取apk签名信息失败，退出！:"+e.toString());
			return false;
		}
	}
	
	/**
	 * 获取应用入口类
	 */
	public static boolean getAppEnter(File srcApkFile){
		try{
			long time = System.currentTimeMillis();
			System.out.println("第二步==> 获取apk文件入口信息");
			String enter = AnalysisApk.getAppEnterApplication(srcApkFile.getAbsolutePath());
			Const.entryClassName = enter.replace(".", "/");
			System.out.println("应用入口类:"+enter);
			System.out.println("获取apk入口类信息成功===耗时:"+((System.currentTimeMillis()-time)/1000)+"s\n\n");
			return true;
		}catch(Exception e){
			System.out.println("获取apk入口类信息失败，退出！:"+e.toString());
			FileUtils.printException(e);
			return false;
		}
	}
	
	/**
	 * 解压apk
	 */
	public static boolean zipApkWork(File srcApkFile, String unZipDir){
		try {
			long time = System.currentTimeMillis();
			System.out.println("第三步==> 解压apk文件:"+srcApkFile.getAbsolutePath());
			FileUtils.decompressDexFile(srcApkFile.getAbsolutePath(), unZipDir);
			System.out.println("解压apk文件结束===耗时:"+((System.currentTimeMillis()-time)/1000)+"s\n\n");
			return true;
		} catch (Throwable e) {
			System.out.println("解压apk文件失败，退出！:"+e.toString());
			return false;
		}
	}
	
	/**
	 * 删除签名文件
	 */
	public static boolean deleteMetaInf(String unZipDir, String aaptCmdDir, String srcApkPath){
		try{
			long time = System.currentTimeMillis();
			File metaFile = new File(unZipDir + Const.METAINFO);
			System.out.println("第四步==> 删除签名文件");
			if(metaFile.exists()){
				File[] metaFileList = metaFile.listFiles();
				File aaptFile = new File(aaptCmdDir);
				String cmd = aaptFile.getAbsolutePath() + " remove " + new File(srcApkPath).getAbsolutePath();
				for(File f : metaFileList){
					cmd = cmd + " " + Const.METAINFO + f.getName();
				}
				System.out.println("删除签名文件命令:"+cmd);
				execCmd(cmd, true);
			}
			System.out.println("删除签名文件结束===耗时:"+((System.currentTimeMillis()-time)/1000)+"s\n\n");
			return true;
		}catch(Throwable e){
			System.out.println("删除签名文件失败，退出！:"+e.toString());
			return false;
		}
	}
	
	/**
	 * 将dex转化成smali
	 */
	public static boolean dexToSmali(String dexFile, String smaliDir){
		File smaliDirF = new File(smaliDir);
		if(smaliDirF.exists()){
			smaliDirF.delete();
		}
		smaliDirF.mkdirs();
		System.out.println("第五步==> 将dex转化成smali");
		String javaCmd = "java -jar libs"+File.separator+"baksmali.jar -o "+smaliDir + " " + dexFile;
		long startTime = System.currentTimeMillis();
		try {
			Process pro = Runtime.getRuntime().exec(javaCmd);
			int status = pro.waitFor();
			if(status == 0){
				System.out.println("dex转化smali成功===耗时:"+((System.currentTimeMillis()-startTime)/1000)+"s\n\n");
				return true;
			}
			System.out.println("dex转化smali失败,status:"+status);
			return false;
		} catch (Exception e) {
			System.out.println("dex转化smali失败:"+e.toString());
			return false;
		}
	}
	
	/**
	 * 替换原始签名和包名
	 */
	public static boolean setSignAndPkgName(){
		System.out.println("第六步==> 代码中替换原始签名和包名信息");
		File pmsSmaliDirF = new File(JWMain.rootPath + Const.smaliTmpDir + File.separator + Const.pmsSmaliDir);
		if(!pmsSmaliDirF.exists()){
			pmsSmaliDirF.mkdirs();
		}
		FileReader reader = null;
        BufferedReader br = null;
        FileWriter writer = null;
		try{
			long startTime = System.currentTimeMillis();
			FileUtils.fileCopy(JWMain.rootPath+File.separator+Const.smaliFileHandler, pmsSmaliDirF.getAbsolutePath() + File.separator + Const.smaliFileHandler);
			writer = new FileWriter(pmsSmaliDirF.getAbsolutePath() + File.separator + Const.smaliFilePMS);
			reader = new FileReader(JWMain.rootPath+File.separator+Const.smaliFilePMS);
            br = new BufferedReader(reader);
            String str = null;
            while((str = br.readLine()) != null) {
            	if(str.contains(Const.signLineTag)){
            		writer.write(str+"\n");
            		String signStr = "\tconst-string v0, \"" + Const.appSign + "\"";
            		writer.write(signStr+"\n");
            		br.readLine();
            	}if(str.contains(Const.pkgNameLineTag)){
            		String pkgNameStr = "\tconst-string v1, \"" + Const.appPkgName + "\"";
            		writer.write(pkgNameStr+"\n");
            		br.readLine();
            	}else{
            		writer.write(str+"\n");
            	}
            }
            System.out.println("设置签名和包名成功===耗时:"+((System.currentTimeMillis()-startTime)/1000)+"s\n\n");
			return true;
		}catch(Exception e){
			System.out.println("设置签名和包名失败:"+e.toString());
		}finally{
			if(reader != null){
				try{
					reader.close();
				}catch(Exception e){
				}
			}
			if(br != null){
				try{
					br.close();
				}catch(Exception e){
				}
			}
			if(writer != null){
				try{
					writer.close();
				}catch(Exception e){
				}
			}
		}
		return false;
	}
	
	/**
	 * 插入hook代码
	 */
	public static boolean insertHookCode(){
		System.out.println("第七步==> 添加hook代码");
		long startTime = System.currentTimeMillis();
		String enterFile = JWMain.rootPath + Const.smaliTmpDir + File.separator + Const.entryClassName.replace(".", File.separator) + ".smali";
		String enterFileTmp = JWMain.rootPath + Const.smaliTmpDir + File.separator + Const.entryClassName.replace(".", File.separator) + "_tmp.smali";
		FileReader reader = null;
        BufferedReader br = null;
        FileWriter writer = null;
        boolean isWorkSucc = false;
        try{
        	reader = new FileReader(enterFile);
        	br = new BufferedReader(reader);
        	writer = new FileWriter(enterFileTmp);
            String str = null;
            boolean isSucc = false;
            int isEntryMethod = -1;
            while((str = br.readLine()) != null) {
            	if(isSucc){
            		writer.write(str+"\n");
            		continue;
            	}
            	if(Const.isApplicationEntry){
            		if(str.contains(Const.applicationAttachLineTag)){
            			isEntryMethod = 0;
            		}else if(str.contains(Const.applicationCreateLineTag)){
            			isEntryMethod = 1;
            		}
            	}else{
            		if(str.contains(Const.activityCreateLineTag)){
            			isEntryMethod = 2;
            		}
            	}
            	if(str.contains(Const.methodEndStr)){
            		isEntryMethod = -1;
            	}
            	
            	writer.write(str+"\n");
            	
            	if(isEntryMethod == 0){
            		writer.write(Const.hookAttachCodeStr);
            		isSucc = true;
            	}else if(isEntryMethod == 1){
            		writer.write(Const.hookCreateCodeStr);
            		isSucc = true;
            	}else if(isEntryMethod == 2){
            		writer.write(Const.hookCreateCodeStr);
            		isSucc = true;
            	}
            }
            System.out.println("插入hook代码成功===耗时"+((System.currentTimeMillis()-startTime)/1000)+"s\n\n");
            isWorkSucc = true;
        }catch(Exception e){
        	System.out.println("插入hook代码失败:"+e.toString());
        }finally{
        	if(reader != null){
				try{
					reader.close();
				}catch(Exception e){
				}
			}
			if(br != null){
				try{
					br.close();
				}catch(Exception e){
				}
			}
			if(writer != null){
				try{
					writer.close();
				}catch(Exception e){
				}
			}
        }
        
        File entryFile = new File(enterFile);
        entryFile.delete();
        File entryFileTmp = new File(enterFileTmp);
        entryFileTmp.renameTo(new File(enterFile));
        
		return isWorkSucc;
	}
	
	/**
	 * 将smali转化成dex
	 */
	public static boolean smaliToDex(String smaliDir, String dexFile){
		System.out.println("第八步==> 将smali转化成dex");
		File dexFileF = new File(dexFile);
		if(dexFileF.exists()){
			dexFileF.delete();
		}
		String javaCmd = "java -jar libs"+File.separator+"smali.jar "+smaliDir + " -o " + dexFile;
		long startTime = System.currentTimeMillis();
		try {
			Process pro = Runtime.getRuntime().exec(javaCmd);
			int status = pro.waitFor();
			if(status == 0){
				System.out.println("smali转化dex成功===耗时:"+((System.currentTimeMillis()-startTime)/1000)+"s\n\n");
				return true;
			}
			System.out.println("smali转化dex失败,status:"+status);
			return false;
		} catch (Exception e) {
			System.out.println("smali转化dex失败:"+e.toString());
			return false;
		}
	}
	
	
	/**
	 * 使用aapt命令添加dex文件到apk中
	 */
	public static boolean addDexToApk(String aaptCmdDir, String unZipDir, String srcApkPath){
		try{
			System.out.println("第九步==> 将dex文件添加到源apk中");
			long time = System.currentTimeMillis();
			File aaptFile = new File(aaptCmdDir);
			String cmd = aaptFile.getAbsolutePath() + " remove " + new File(srcApkPath).getAbsolutePath();
			File classDir = new File(unZipDir);
			File[] classListFile = classDir.listFiles();
			for(File file : classListFile){
				if(file.getName().endsWith("classes.dex")){
					cmd = cmd + " " + file.getName();
				}
			}
			System.out.println("cmd:"+cmd);
			if(!execCmd(cmd, true)){
				System.out.println("添加dex文件到apk中失败，退出！");
				return false;
			}

			String addCmd = aaptFile.getAbsolutePath() + " add " + new File(srcApkPath).getAbsolutePath();
			for(File file : classListFile){
				if(file.getName().endsWith(".dex")){
					addCmd = addCmd + " " + file.getName();
				}
			}
			System.out.println("cmd:"+addCmd);
			if(!execCmd(addCmd, true)){
				System.out.println("添加dex文件到apk中失败，退出！");
				return false;
			}
			System.out.println("添加dex文件到apk中结束===耗时:"+((System.currentTimeMillis()-time)/1000)+"s\n\n");
			return true;
		}catch(Throwable e){
			System.out.println("添加dex文件到apk中失败，退出！:"+e.toString());
			return false;
		}
	}
	
	/**
	 * 签名apk文件
	 */
	public static boolean signApk(String srcApkPath, String rootPath){
		try{
			System.out.println("第十步==> 开始签名apk文件:"+srcApkPath);
			long time = System.currentTimeMillis();
			String keystore = "cyy_game.keystore";
			File signFile = new File(rootPath+File.separator+keystore);
			if(!signFile.exists()){
				System.out.println("签名文件:"+signFile.getAbsolutePath()+" 不存在，需要自己手动签名");
				return false;
			}
			String storePass = "cyy1888";
			StringBuilder signCmd = new StringBuilder("jarsigner.exe");
			signCmd.append(" -verbose -keystore ");
			signCmd.append(keystore);
			signCmd.append(" -storepass ");
			signCmd.append(storePass);
			signCmd.append(" -signedjar ");
			signCmd.append("signed.apk ");
			signCmd.append(srcApkPath + " ");
			signCmd.append(keystore + " ");
			signCmd.append("-digestalg SHA1 -sigalg MD5withRSA");
			execCmd(signCmd.toString(), false);
			System.out.println("签名apk文件结束===耗时:"+((System.currentTimeMillis()-time)/1000)+"s\n\n");
			return true;
		}catch(Throwable e){
			System.out.println("重新签名apk文件失败，退出！:"+e.toString());
			return false;
		}
	}
	
	/**
	 * 清理删除工作
	 */
	public static void deleteTmpFile(String rootPath){
		//删除解压之后的目录
		FileUtils.deleteDirectory(rootPath+Const.unZipDir);
		//删除smali目录
		FileUtils.deleteDirectory(rootPath+Const.smaliTmpDir);
		//删除临时dex文件
		FileUtils.deleteFile(rootPath+File.separator+"classes.dex");
	}
	
	/**
	 * 执行命令
	 * @param cmd
	 * @param isOutputLog
	 * @return
	 */
	public static boolean execCmd(String cmd, boolean isOutputLog){
		BufferedReader br = null;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				if(isOutputLog)
					System.out.println(line);
			}
		} catch (Exception e) {
			System.out.println("cmd error:"+e.toString());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}
