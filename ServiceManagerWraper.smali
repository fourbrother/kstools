.class public Lcn/wjdiankong/hookpms/ServiceManagerWraper;
.super Ljava/lang/Object;
.source "ServiceManagerWraper.java"


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 14
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static hookPMS(Landroid/content/Context;)V
    .locals 3
    .param p0, "context"    # Landroid/content/Context;

    .prologue
    .line 46
    const-string v0, "30820253308201bca00302010202044bbb0361300d06092a864886f70d0101050500306d310e300c060355040613054368696e61310f300d06035504080c06e58c97e4baac310f300d06035504070c06e58c97e4baac310f300d060355040a0c06e885bee8aeaf311b3019060355040b0c12e697a0e7babfe4b89ae58aa1e7b3bbe7bb9f310b30090603550403130251513020170d3130303430363039343831375a180f32323834303132303039343831375a306d310e300c060355040613054368696e61310f300d06035504080c06e58c97e4baac310f300d06035504070c06e58c97e4baac310f300d060355040a0c06e885bee8aeaf311b3019060355040b0c12e697a0e7babfe4b89ae58aa1e7b3bbe7bb9f310b300906035504031302515130819f300d06092a864886f70d010101050003818d0030818902818100a15e9756216f694c5915e0b529095254367c4e64faeff07ae13488d946615a58ddc31a415f717d019edc6d30b9603d3e2a7b3de0ab7e0cf52dfee39373bc472fa997027d798d59f81d525a69ecf156e885fd1e2790924386b2230cc90e3b7adc95603ddcf4c40bdc72f22db0f216a99c371d3bf89cba6578c60699e8a0d536950203010001300d06092a864886f70d01010505000381810094a9b80e80691645dd42d6611775a855f71bcd4d77cb60a8e29404035a5e00b21bcc5d4a562482126bd91b6b0e50709377ceb9ef8c2efd12cc8b16afd9a159f350bb270b14204ff065d843832720702e28b41491fbc3a205f5f2f42526d67f17614d8a974de6487b2c866efede3b4e49a0f916baa3c1336fd2ee1b1629652049"

    .line 47
    .local v0, "qqSign":Ljava/lang/String;
    const-string v1, "com.tencent.mobileqq"

    const/4 v2, 0x0

    invoke-static {p0, v0, v1, v2}, Lcn/wjdiankong/hookpms/ServiceManagerWraper;->hookPMS(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;I)V

    .line 48
    return-void
.end method

.method public static hookPMS(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;I)V
    .locals 16
    .param p0, "context"    # Landroid/content/Context;
    .param p1, "signed"    # Ljava/lang/String;
    .param p2, "appPkgName"    # Ljava/lang/String;
    .param p3, "hashCode"    # I

    .prologue
    .line 19
    :try_start_0
    const-string v12, "android.app.ActivityThread"

    invoke-static {v12}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v2

    .line 21
    .local v2, "activityThreadClass":Ljava/lang/Class;, "Ljava/lang/Class<*>;"
    const-string v12, "currentActivityThread"

    const/4 v13, 0x0

    new-array v13, v13, [Ljava/lang/Class;

    invoke-virtual {v2, v12, v13}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v4

    .line 22
    .local v4, "currentActivityThreadMethod":Ljava/lang/reflect/Method;
    const/4 v12, 0x0

    const/4 v13, 0x0

    new-array v13, v13, [Ljava/lang/Object;

    invoke-virtual {v4, v12, v13}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    .line 24
    .local v3, "currentActivityThread":Ljava/lang/Object;
    const-string v12, "sPackageManager"

    invoke-virtual {v2, v12}, Ljava/lang/Class;->getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v11

    .line 25
    .local v11, "sPackageManagerField":Ljava/lang/reflect/Field;
    const/4 v12, 0x1

    invoke-virtual {v11, v12}, Ljava/lang/reflect/Field;->setAccessible(Z)V

    .line 26
    invoke-virtual {v11, v3}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v10

    .line 28
    .local v10, "sPackageManager":Ljava/lang/Object;
    const-string v12, "android.content.pm.IPackageManager"

    invoke-static {v12}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v6

    .line 30
    .local v6, "iPackageManagerInterface":Ljava/lang/Class;, "Ljava/lang/Class<*>;"
    invoke-virtual {v6}, Ljava/lang/Class;->getClassLoader()Ljava/lang/ClassLoader;

    move-result-object v12

    .line 31
    const/4 v13, 0x1

    new-array v13, v13, [Ljava/lang/Class;

    const/4 v14, 0x0

    aput-object v6, v13, v14

    .line 32
    new-instance v14, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;

    const/4 v15, 0x0

    move-object/from16 v0, p1

    move-object/from16 v1, p2

    invoke-direct {v14, v10, v0, v1, v15}, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;-><init>(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;I)V

    .line 29
    invoke-static {v12, v13, v14}, Ljava/lang/reflect/Proxy;->newProxyInstance(Ljava/lang/ClassLoader;[Ljava/lang/Class;Ljava/lang/reflect/InvocationHandler;)Ljava/lang/Object;

    move-result-object v9

    .line 34
    .local v9, "proxy":Ljava/lang/Object;
    invoke-virtual {v11, v3, v9}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    .line 36
    invoke-virtual/range {p0 .. p0}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v8

    .line 37
    .local v8, "pm":Landroid/content/pm/PackageManager;
    invoke-virtual {v8}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v12

    const-string v13, "mPM"

    invoke-virtual {v12, v13}, Ljava/lang/Class;->getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v7

    .line 38
    .local v7, "mPmField":Ljava/lang/reflect/Field;
    const/4 v12, 0x1

    invoke-virtual {v7, v12}, Ljava/lang/reflect/Field;->setAccessible(Z)V

    .line 39
    invoke-virtual {v7, v8, v9}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 43
    .end local v2    # "activityThreadClass":Ljava/lang/Class;, "Ljava/lang/Class<*>;"
    .end local v3    # "currentActivityThread":Ljava/lang/Object;
    .end local v4    # "currentActivityThreadMethod":Ljava/lang/reflect/Method;
    .end local v6    # "iPackageManagerInterface":Ljava/lang/Class;, "Ljava/lang/Class<*>;"
    .end local v7    # "mPmField":Ljava/lang/reflect/Field;
    .end local v8    # "pm":Landroid/content/pm/PackageManager;
    .end local v9    # "proxy":Ljava/lang/Object;
    .end local v10    # "sPackageManager":Ljava/lang/Object;
    .end local v11    # "sPackageManagerField":Ljava/lang/reflect/Field;
    :goto_0
    return-void

    .line 40
    :catch_0
    move-exception v5

    .line 41
    .local v5, "e":Ljava/lang/Exception;
    const-string v12, "jw"

    new-instance v13, Ljava/lang/StringBuilder;

    const-string v14, "hook pms error:"

    invoke-direct {v13, v14}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-static {v5}, Landroid/util/Log;->getStackTraceString(Ljava/lang/Throwable;)Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v13

    invoke-virtual {v13}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v13

    invoke-static {v12, v13}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0
.end method
