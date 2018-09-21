package com.maning.mndialoglibrary.utils;

import android.graphics.Color;

import com.maning.mndialoglibrary.config.MDialogConfig;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class MProgressDialogUtils {
     public static MDialogConfig getDefaultDialogConfig(){
         //自定义背景
         MDialogConfig mDialogConfig = new MDialogConfig.Builder()
                 //点击外部是否可以取消
                 .isCanceledOnTouchOutside(false)
                 //全屏背景窗体的颜色
                // .setBackgroundWindowColor(getMyColor(R.color.colorDialogWindowBg))
                 //View背景的颜色
                 .setBackgroundViewColor(Color.BLUE)
                 //View背景的圆角
                 //.setCornerRadius(20)
                 //View 边框的颜色
                 //.setStrokeColor(getMyColor(R.color.colorAccent))
                 //View 边框的宽度
                 //.setStrokeWidth(2)
                 //Progress 颜色
                 //.setProgressColor(getMyColor(R.color.colorDialogProgressBarColor))
                 //Progress 宽度
                 //.setProgressWidth(3)
                 //Progress 内圈颜色
                 //.setProgressRimColor(Color.YELLOW)
                 //Progress 内圈宽度
                 //.setProgressRimWidth(4)
                 //文字的颜色
                 //.setTextColor(getMyColor(R.color.colorDialogTextColor))
                 //ProgressBar 颜色
                 //.setProgressColor(Color.GREEN)
                 .build();
         return mDialogConfig;
     }
}
