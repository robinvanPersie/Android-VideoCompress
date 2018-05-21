package com.antimage.videocompress.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antimage.videocompress.R;

/**
 * Created by xuyuming on 2018/5/4.
 */

public class AppProgressDialog {

    private Dialog progressDialog;

    public void show(Context context, boolean cancelable) {
        if (progressDialog == null) {
            progressDialog = new Dialog(context, R.style.MIDialog);
            ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleInverse);
            progressBar.setId(android.R.id.progress);
            TextView textView = new TextView(context);
            textView.setTextSize(16);
            textView.setTextColor(Color.WHITE);
            textView.setText("请稍后");
            RelativeLayout layout = new RelativeLayout(context);
            layout.setBackgroundResource(R.drawable.progress_dialog_bg);

            RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams(-2, -2);
            progressParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layout.addView(progressBar, 0, progressParams);
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(-2, -2);
            textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            textParams.addRule(RelativeLayout.BELOW, android.R.id.progress);
            layout.addView(textView, 1, textParams);
            progressBar.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setContentView(layout);
            //点击返回是否取消加载圈
            //setCancelable(cancelable);
        }
        progressDialog.setCancelable(cancelable);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public boolean isShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

    public void dismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
