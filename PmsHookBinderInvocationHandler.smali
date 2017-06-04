.class public Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;
.super Ljava/lang/Object;
.source "PmsHookBinderInvocationHandler.java"

# interfaces
.implements Ljava/lang/reflect/InvocationHandler;


# instance fields
.field private SIGN:Ljava/lang/String;

.field private appPkgName:Ljava/lang/String;

.field private base:Ljava/lang/Object;


# direct methods
.method public constructor <init>(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;I)V
    .locals 4
    .param p1, "base"    # Ljava/lang/Object;
    .param p2, "sign"    # Ljava/lang/String;
    .param p3, "appPkgName"    # Ljava/lang/String;
    .param p4, "hashCode"    # I

    .prologue
    .line 22
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 20
    const-string v1, ""

    iput-object v1, p0, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;->appPkgName:Ljava/lang/String;

    .line 24
    :try_start_0
    iput-object p1, p0, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;->base:Ljava/lang/Object;

    .line 25
    iput-object p2, p0, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;->SIGN:Ljava/lang/String;

    .line 26
    iput-object p3, p0, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;->appPkgName:Ljava/lang/String;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 30
    :goto_0
    return-void

    .line 27
    :catch_0
    move-exception v0

    .line 28
    .local v0, "e":Ljava/lang/Exception;
    const-string v1, "jw"

    new-instance v2, Ljava/lang/StringBuilder;

    const-string v3, "error:"

    invoke-direct {v2, v3}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-static {v0}, Landroid/util/Log;->getStackTraceString(Ljava/lang/Throwable;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v1, v2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0
.end method


# virtual methods
.method public invoke(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;
    .locals 7
    .param p1, "proxy"    # Ljava/lang/Object;
    .param p2, "method"    # Ljava/lang/reflect/Method;
    .param p3, "args"    # [Ljava/lang/Object;
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/lang/Throwable;
        }
    .end annotation

    .prologue
    const/4 v6, 0x0

    .line 34
    const-string v4, "jw"

    invoke-virtual {p2}, Ljava/lang/reflect/Method;->getName()Ljava/lang/String;

    move-result-object v5

    invoke-static {v4, v5}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 35
    const-string v4, "getPackageInfo"

    invoke-virtual {p2}, Ljava/lang/reflect/Method;->getName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_0

    .line 36
    aget-object v2, p3, v6

    check-cast v2, Ljava/lang/String;

    .line 37
    .local v2, "pkgName":Ljava/lang/String;
    const/4 v4, 0x1

    aget-object v0, p3, v4

    check-cast v0, Ljava/lang/Integer;

    .line 38
    .local v0, "flag":Ljava/lang/Integer;
    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v4

    const/16 v5, 0x40

    if-ne v4, v5, :cond_0

    iget-object v4, p0, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;->appPkgName:Ljava/lang/String;

    invoke-virtual {v4, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_0

    .line 39
    new-instance v3, Landroid/content/pm/Signature;

    iget-object v4, p0, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;->SIGN:Ljava/lang/String;

    invoke-direct {v3, v4}, Landroid/content/pm/Signature;-><init>(Ljava/lang/String;)V

    .line 40
    .local v3, "sign":Landroid/content/pm/Signature;
    iget-object v4, p0, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;->base:Ljava/lang/Object;

    invoke-virtual {p2, v4, p3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/content/pm/PackageInfo;

    .line 41
    .local v1, "info":Landroid/content/pm/PackageInfo;
    iget-object v4, v1, Landroid/content/pm/PackageInfo;->signatures:[Landroid/content/pm/Signature;

    aput-object v3, v4, v6

    .line 45
    .end local v0    # "flag":Ljava/lang/Integer;
    .end local v1    # "info":Landroid/content/pm/PackageInfo;
    .end local v2    # "pkgName":Ljava/lang/String;
    .end local v3    # "sign":Landroid/content/pm/Signature;
    :goto_0
    return-object v1

    :cond_0
    iget-object v4, p0, Lcn/wjdiankong/hookpms/PmsHookBinderInvocationHandler;->base:Ljava/lang/Object;

    invoke-virtual {p2, v4, p3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    goto :goto_0
.end method
