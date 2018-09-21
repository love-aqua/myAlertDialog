package com.maning.mndialoglibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maning.mndialoglibrary.config.MDialogConfig;
import com.maning.mndialoglibrary.utils.MSizeUtils;
import com.maning.mndialoglibrary.view.MProgressWheel;

/**
 * Created by maning on 2017/8/9.
 * 进度Dialog
 */

public class MProgressDialog {

    private static Dialog mDialog;
    private static MDialogConfig mDialogConfig;

    //布局
    private static RelativeLayout dialog_window_background;
    private static RelativeLayout dialog_view_bg;
    private static MProgressWheel progress_wheel;
    private static TextView tv_show;
    private static ImageView iv_cancel;

    private static CancelClickListener mCancelClickListener;
    private static Handler handler;
    private static Runnable runnable;
    private static long time = 0;//经过多少秒后，让取消框显示，默认是0(倒计时模式)
    private static long currenTime = 0;
    private static String str_msg;

    private static void initDialog(Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mProgressDialogView = inflater.inflate(R.layout.mn_progress_dialog_layout, null);// 得到加载view
        mDialog = new Dialog(mContext, R.style.MNCustomDialog);// 创建自定义样式dialog
        mDialog.setCancelable(false);// 不可以用“返回键”取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(mProgressDialogView);// 设置布局

        //设置整个Dialog的宽高
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int screenH = dm.heightPixels;

        WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
        layoutParams.width = screenW;
        layoutParams.height = screenH;
        mDialog.getWindow().setAttributes(layoutParams);


        //布局相关
        dialog_window_background = (RelativeLayout) mProgressDialogView.findViewById(R.id.dialog_window_background);
        dialog_view_bg = (RelativeLayout) mProgressDialogView.findViewById(R.id.dialog_view_bg);
        progress_wheel = (MProgressWheel) mProgressDialogView.findViewById(R.id.progress_wheel);
        tv_show = (TextView) mProgressDialogView.findViewById(R.id.tv_show);
        iv_cancel = mProgressDialogView.findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissProgress();
                if (mCancelClickListener != null)
                mCancelClickListener.onCancelClickListener(v);
            }
        });

        //默认相关
        progress_wheel.spin();

        //设置配置
        if (mDialogConfig == null) {
            mDialogConfig = new MDialogConfig.Builder().build();
        }
        configView(mContext);
        //点击事件
        dialog_window_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消Dialog
                if (mDialogConfig != null && mDialogConfig.canceledOnTouchOutside) {
                    dismissProgress();
                }
            }
        });


        //当time不等于0才运行线程
        if (time != 0){
            iv_cancel.setVisibility(View.GONE);
            currenTime = 0;
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (time > currenTime){
                        handler.postDelayed(runnable, 1000);
                        tv_show.setText(str_msg+"("+(time-currenTime)+")");
                        currenTime++;
                    }else{
                        tv_show.setText(str_msg);
                        iv_cancel.setVisibility(View.VISIBLE);
                        handler.removeCallbacks(runnable);
                    }
                }
            };
            handler.postDelayed(runnable, 0);
        }


    }

    private static void configView(Context mContext) {
        mDialog.setCanceledOnTouchOutside(mDialogConfig.canceledOnTouchOutside);
        dialog_window_background.setBackgroundColor(mDialogConfig.backgroundWindowColor);

        GradientDrawable myGrad = new GradientDrawable();
        myGrad.setColor(mDialogConfig.backgroundViewColor);
        myGrad.setStroke(MSizeUtils.dp2px(mContext, mDialogConfig.strokeWidth), mDialogConfig.strokeColor);
        myGrad.setCornerRadius(MSizeUtils.dp2px(mContext, mDialogConfig.cornerRadius));
        dialog_view_bg.setBackground(myGrad);

        progress_wheel.setBarColor(mDialogConfig.progressColor);
        progress_wheel.setBarWidth(MSizeUtils.dp2px(mContext, mDialogConfig.progressWidth));
        progress_wheel.setRimColor(mDialogConfig.progressRimColor);
        progress_wheel.setRimWidth(mDialogConfig.progressRimWidth);

        tv_show.setTextColor(mDialogConfig.textColor);
    }

    public static void showProgress(Context context) {
        showProgress(context, "加载中");
    }

    public static void showProgress(Context context, String msg) {
        showProgress(context, msg, null);
    }

    public static void showProgress(Context context, MDialogConfig mDialogConfig) {
        showProgress(context, "加载中", mDialogConfig);
    }

    //普通带消息的模式
    public static void showProgress(Context context, String msg, MDialogConfig mDialogConfig) {
        MProgressDialog.mDialogConfig = mDialogConfig;
        dismissProgress();
        initDialog(context);
        str_msg = msg;
        if (mDialog != null && tv_show != null) {
            if (TextUtils.isEmpty(msg)) {
                tv_show.setVisibility(View.GONE);
            } else {
                tv_show.setVisibility(View.VISIBLE);
                tv_show.setText(msg);
            }
            mDialog.show();
        }
    }

    //点击取消按钮后的回调模式
    public static void showProgress(Context context, String msg, MDialogConfig mDialogConfig,CancelClickListener listener) {
        mCancelClickListener = listener;
        showProgress(context,msg,mDialogConfig);
    }
    //使用倒计时的回调模式
    public static void showProgress(Context context, String msg, MDialogConfig mDialogConfig,long t) {
        time = t;
        showProgress(context,msg,mDialogConfig);
    }



    public static void dismissProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            //清除
            mDialog = null;
            mDialogConfig = null;
            dialog_window_background = null;
            dialog_view_bg = null;
            progress_wheel = null;
            tv_show = null;
        }
    }

    public static boolean isShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
    }

    public interface CancelClickListener{
        void onCancelClickListener(View view);
    }

    public static void setOnCancelClickListener(CancelClickListener listener){
        mCancelClickListener = listener;
    }

    public static void setCancelBtnIsShow(boolean isShow){
        if (isShow){
            iv_cancel.setVisibility(View.VISIBLE);
        }else{
            iv_cancel.setVisibility(View.GONE);
        }
    }

}
