package cn.wjdiankong.jw;

import java.io.File;

public final class Const {
	
	public final static String METAINFO = "META-INF/";
	public final static String unZipDir = File.separator + "unzipapk" + File.separator;

	public final static String smaliTmpDir = File.separator + "smali_tmp";
	public final static String signLineTag = ".line 46";
	public final static String pkgNameLineTag = ".local v0, \"qqSign\":Ljava/lang/String;";
	public final static String pmsSmaliDir = "cn" + File.separator + "wjdiankong" + File.separator + "hookpms" + File.separator;
	public final static String smaliFileHandler = "PmsHookBinderInvocationHandler.smali";
	public final static String smaliFilePMS = "ServiceManagerWraper.smali";
	public final static String applicationAttachLineTag = ".method protected attachBaseContext(Landroid/content/Context;)V";
	public final static String applicationCreateLineTag = ".method public onCreate()V";
	public final static String activityCreateLineTag = ".method protected onCreate(Landroid/os/Bundle;)V";
	public final static String methodEndStr = ".end method";
	public final static String hookAttachCodeStr = "\tinvoke-static {p1}, Lcn/wjdiankong/hookpms/ServiceManagerWraper;->hookPMS(Landroid/content/Context;)V\n";
	public final static String hookCreateCodeStr = "\tinvoke-static/range {p0 .. p0}, Lcn/wjdiankong/hookpms/ServiceManagerWraper;->hookPMS(Landroid/content/Context;)V\n";
	
	public static String entryClassName = "";
	public static String appSign = "";
	public static String appPkgName = "";
	public static boolean isApplicationEntry = true;
	
}
