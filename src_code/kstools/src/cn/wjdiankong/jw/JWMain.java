package cn.wjdiankong.jw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * 
 * 第一步：解压apk获取到所有的dex文件
 * 第二步：使用dex2jar工具类，添加代码
 * 第三步：使用dx工具生成对应的dex文件
 * 第四步：压缩到修改之后的apk文件
 * 第五步：使用jarsigner进行apk签名
 * 
 * @author jiangwei1-g
 *
 */
public class JWMain {

	public static String aaptCmdDir;
	public static String srcApkPath;
	public static String unSignedApkPath = "unsigned.apk";
	public static String rootPath;
	
	public static void main(String[] args) {
		
		if(args.length < 1){
			System.out.println("args error");
			return;
		}
		
		String optionStr = args[0];
		if("++sign".equals(optionStr)){
			generateApkSign(args);
		}else if("++hook".equals(optionStr)){
			hookWork(args);
			return;
		}else{
			System.out.println("option args error!");
			return;
		}
		
	}
	
	private static void generateApkSign(String[] args){
		if(args.length < 2){
			System.out.println("args error");
			return;
		}
		File srcSignApkFile = new File(args[1]+".apk");
		if(!srcSignApkFile.exists()){
			System.out.println("apk file " + srcSignApkFile.getAbsolutePath()+" is not exist!");
			return;
		}
		boolean isSucc = DoWorkUtils.getAppSign(srcSignApkFile);
		if(isSucc){
			System.out.println("获取签名信息成功:"+Const.appSign);
		}else{
			System.out.println("获取签名信息失败,可以尝试手动获取签名信息,具体方法参见说明文档");
			return;
		}
		
		File signFile = new File("apksign.txt");
		if(signFile.exists()){
			signFile.delete();
		}
		
		FileWriter writer = null;
		try{
			writer = new FileWriter(signFile);
			writer.write(Const.appSign);
			System.out.println("回写签名信息成功,保存在apksign.txt文档中(切勿删除)");
		}catch(Exception e){
			System.out.println("");
		}finally{
			if(writer != null){
				try{
					writer.close();
				}catch(Exception e){
				}
			}
		}
	}
	
	private static void hookWork(String[] args){
		
		if(args.length < 4){
			System.out.println("args error");
			return;
		}
		
		rootPath = args[1];
		srcApkPath = args[2];

		File srcApkFile = new File(srcApkPath);
		if(!srcApkFile.exists()){
			System.out.println("src apk file is not exist");
			return;
		}
		
		aaptCmdDir = args[3];
		
		File aaptCmdDirFile = new File(aaptCmdDir);
		if(!aaptCmdDirFile.exists()){
			System.out.println("aapt工具不存在,你可能需要手动配置bat文件中的aapt路径(路径最好不要配置到C盘下)!");
			return;
		}
		
		String javaHome = System.getenv("JAVA_HOME");
		if(javaHome == null || "".equals(javaHome)){
			System.out.println("未配置JAVA_HOME环境变量,找不到jarsigner.exe工具!");
			return;
		}
		
		//查看有没有最新的签名信息配置
		File signFile = new File("apksign.txt");
		if(signFile.exists()){
			System.out.println("配置了签名值,开始读取进行转化...");
			FileInputStream fis = null;
			try{
				fis = new FileInputStream("apksign.txt");
				int size = fis.available();
		        byte[] buffer=new byte[size];
		        fis.read(buffer);
		        Const.appSign = new String(buffer);
				if(Const.appSign != null && Const.appSign.length() != 0){
					System.out.println("获取签名配置信息成功："+Const.appSign);
				}else{
					System.out.println("获取签名配置签名信息失败,默认采用apk自带的签名信息...\n");
				}
			}catch(Exception e){
			}
		}else{
			System.out.println("没有找到配置签名信息,默认采用apk自带的签名信息...\n");
		}

		File unZipFile = new File(Const.unZipDir);
		if(!unZipFile.exists()){
			unZipFile.mkdirs();
		}
		
		//拷贝原始apk文件一份命名为unsigned.apk
		if(!FileUtils.fileCopy(srcApkPath, unSignedApkPath)){
			unSignedApkPath = srcApkPath;
		}
		
		if(Const.appSign == null || Const.appSign.length() == 0){
			if(!DoWorkUtils.getAppSign(srcApkFile)){
				DoWorkUtils.deleteTmpFile(rootPath);
				return;
			}
		}
		
		if(!DoWorkUtils.getAppEnter(srcApkFile)){
			DoWorkUtils.deleteTmpFile(rootPath);
			return;
		}
		
		if(!DoWorkUtils.zipApkWork(srcApkFile, rootPath+Const.unZipDir)){
			DoWorkUtils.deleteTmpFile(rootPath);
			return;
		}
		
		if(!DoWorkUtils.deleteMetaInf(rootPath+Const.unZipDir, aaptCmdDir, unSignedApkPath)){
			DoWorkUtils.deleteTmpFile(rootPath);
			return;
		}
		
		if(!DoWorkUtils.dexToSmali(rootPath + Const.unZipDir+File.separator+"classes.dex", rootPath+Const.smaliTmpDir)){
			DoWorkUtils.deleteTmpFile(rootPath);
			return;
		}
		
		if(!DoWorkUtils.setSignAndPkgName()){
			DoWorkUtils.deleteTmpFile(rootPath);
			return;
		}
		
		if(!DoWorkUtils.insertHookCode()){
			DoWorkUtils.deleteTmpFile(rootPath);
			return;
		}
		
		if(!DoWorkUtils.smaliToDex(rootPath+File.separator+Const.smaliTmpDir, rootPath + File.separator+"classes.dex")){
			DoWorkUtils.deleteTmpFile(rootPath);
			return;
		}
		
		if(!DoWorkUtils.addDexToApk(aaptCmdDir, rootPath+Const.unZipDir, unSignedApkPath)){
			DoWorkUtils.deleteTmpFile(rootPath);
			return;
		}
		
		if(!DoWorkUtils.signApk(unSignedApkPath, rootPath)){
			DoWorkUtils.deleteTmpFile(rootPath);
			return;
		}
		
		DoWorkUtils.deleteTmpFile(rootPath);
		
	}

}
