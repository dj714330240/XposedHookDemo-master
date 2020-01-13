package com.example.la0s.xposedhook;



//包名路径

import android.content.Context;
import android.os.Environment;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class XposedHook implements IXposedHookLoadPackage {



    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        // 判断是不是要Hook的包，不是直接返回   com.zhj.xposedhook为测试Demo的包名
        if (!"com.xmfx.wujiu590".equals(loadPackageParam.packageName)) {
            //打印日志加载的包名
            XposedBridge.log("Loaded app: " + loadPackageParam.packageName);
            return;
        }

        /***
         * @Description: hook - 红包签名
         * @Param: [loadPackageParam]
         * @return: void
         * @Author: 邓太阳
         * @Date: 2020-01-11 13:23
         */
        XposedHelpers.findAndHookMethod("com.xmfx.qq.f.b", loadPackageParam.classLoader,
                "a", String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("beforeHookedMethod"+param.args[0]);
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("afterHookedMethod"+param.getResult());
                        super.afterHookedMethod(param);
                    }
                }
        );


        /***
         * @Description: hook 红包信息
         * @Param: [loadPackageParam]
         * @return: void
         * @Author: 邓太阳
         * @Date: 2020-01-11 13:22
         */
        XposedHelpers.findAndHookMethod("com.xmfx.qq.database.WebSendMessage", loadPackageParam.classLoader,
                "getContent",  new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("红包信息-----"+param.getResult());
                        writeFile(param.getResult().toString());
                        super.afterHookedMethod(param);
                    }
                }
        );


        /***
         * @Description: hook key
         * @Param: [loadPackageParam]
         * @return: void
         * @Author: 邓太阳
         * @Date: 2020-01-11 13:22
         */
        XposedHelpers.findAndHookMethod("com.redpacket.common.c.j", loadPackageParam.classLoader,
                "d", Context.class,String.class,  new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("签名--key--"+param.args[1]+"-------"+param.getResult());
                        super.afterHookedMethod(param);
                    }
                }
        );
        /***
         * @Description: hook key
         * @Param: [loadPackageParam]
         * @return: void
         * @Author: 邓太阳
         * @Date: 2020-01-11 13:22
         */
        XposedHelpers.findAndHookMethod("com.xmfx.qq.f.c.a", loadPackageParam.classLoader,
                "b", String.class,String.class,  new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("Base64--key--"+param.args[1]);
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                }
        );
        /***
         * @Description: hook key
         * @Param: [loadPackageParam]
         * @return: void
         * @Author: 邓太阳
         * @Date: 2020-01-11 13:22
         */
        XposedHelpers.findAndHookMethod("com.xmfx.qq.bean.SkUserBaseinfoModel", loadPackageParam.classLoader,
                "getBalance",  new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("之前多少钱-"+param.args[0]);
//                        param.args[0]="19995";
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("之前多少钱-"+param.getResult());
                        param.setResult("19995");
                        super.afterHookedMethod(param);
                    }
                }
        );



    }


    /***
     * @Description: 写入文件
     * @Param: [content]
     * @return: void
     * @Author: 邓太阳
     * @Date: 2019-12-21 13:42
     */
    public static void   writeFile (String content) {

        String money = content.split("money\":")[1].split(",\"num")[0];
        if (Double.valueOf(money)>20) {
            return;
        }
        String id = content.split("redpacketId\":")[1].split(",")[0];
        OutputStream outputStream = null;

        try{
            outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString()+"/$MuMu共享文件夹/a"),true);
            outputStream.write((id+"\n").getBytes());
            outputStream.close();
        }catch (Exception e){

        }

    }

}