package com.antimage.videocompress.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.antimage.videocompress.R;

/**
 * Created by xuyuming on 16/9/5.
 */
public class MIActionSheet extends Dialog implements View.OnClickListener{

    private static final int TRANSLATE_ANIM_DURATION = 300;

    private Context mContext;
    private int dip1;
    private int dip10;
    private int dip48;

    public MIActionSheet(Context context) {
        super(context, R.style.ActionSheetTheme);
        mContext = context;
        Resources res = mContext.getResources();
        dip1 = res.getDimensionPixelSize(R.dimen.dip_1);
        dip10 = res.getDimensionPixelSize(R.dimen.dip_10);
        dip48 = res.getDimensionPixelSize(R.dimen.dip_48);
        init();
        initView();
        initWindow();
    }

    private void initWindow() {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        Drawable drawable = new ColorDrawable();
        drawable.setAlpha(0);
        window.setBackgroundDrawable(drawable);
    }

    private int cancelBtnBackground;
    private int topBtnBackground;
    private int middleBtnBackground;
    private int bottomBtnBackground;
    private int textColor;

    private void init() {
        hideInputMethod();
        Resources res = mContext.getResources();
        cancelBtnBackground = R.drawable.circle_rectangle_bg_selector;
        topBtnBackground = R.drawable.top_circle_bg_selector;
        middleBtnBackground = R.drawable.middle_rect_bg_selector;
        bottomBtnBackground = R.drawable.bottom_circle_bg_selector;
        textColor = res.getColor(R.color.app_text_color);
    }

    private View rootView;
    private LinearLayout container;
    private View animView;
    private TextView cancelBtn;

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.actionsheet_layout, null);
        rootView.setBackgroundColor(Color.argb(136, 0, 0, 0));
        rootView.setId(R.id.action_sheet_bg_id);
        rootView.setOnClickListener(this);

        cancelBtn = (TextView) rootView.findViewById(R.id.action_sheet_cancel_btn);
        cancelBtn.setOnClickListener(this);

        container = (LinearLayout) rootView.findViewById(R.id.action_sheet_container);
        animView = rootView.findViewById(R.id.action_sheet_anim_layout);
    }

    public MIActionSheet setCancelButtonTitle(String text) {
        cancelBtn.setText(text);
        return this;
    }

    public MIActionSheet setCancelButtonTitle(int textId) {
        cancelBtn.setText(textId);
        return this;
    }

    public MIActionSheet setCancelTextColor(int colorId) {
        cancelBtn.setTextColor(mContext.getResources().getColor(colorId));
        return this;
    }

    /**
     * 所有item文字都变色
     * @param colorId
     */
    public MIActionSheet setAllTextColor(int colorId) {
        textColor = mContext.getResources().getColor(colorId);
        if (arrays == null || arrays.length == 0) return this;
        for (int i = 0; i < arrays.length; i++) {
            setTextColor(colorId, i);
        }
        setCancelTextColor(colorId);
        return this;
    }

    /**
     * 指定item更换字体颜色
     * @param colorId     颜色
     * @param position   位置
     */
    public MIActionSheet setTextColor(int colorId, int position) {
        if (arrays == null || position >= arrays.length) return this;
        TextView textView = (TextView) container.getChildAt(position);
        if (textView == null) return this;
        textView.setTextColor(mContext.getResources().getColor(colorId));
        return this;
    }

    /**
     * 指定item更换字体大小
     * @param dimen      字体大小
     * @param position   位置
     */
    public MIActionSheet setTextSize(int dimen, int position) {
        if (arrays == null || position >= arrays.length) return this;
        TextView textView = (TextView) container.getChildAt(position);
        if (textView == null) return this;
        textView.setTextSize(dimen);
        return this;
    }

    /**
     * 指定item更换文本
     * @param text
     * @param position
     */
    public MIActionSheet setText(String text, int position) {
        if (arrays == null || position >= arrays.length) return this;
        TextView textView = (TextView) container.getChildAt(position);
        if (textView == null) return this;
        textView.setText(text);
        return this;
    }

    /**
     * 指定item更换文本
     * @param resId
     * @param position
     */
    public MIActionSheet setText(int resId, int position) {
        if (arrays == null || position >= arrays.length) return this;
        TextView textView = (TextView) container.getChildAt(position);
        if (textView == null) return this;
        textView.setText(resId);
        return this;
    }

    /**
     * 指定item更换北京
     * @param drawableId
     * @param position
     */
    public MIActionSheet setTextBackground(int drawableId, int position) {
        if (arrays == null || position >= arrays.length) return this;
        TextView textView = (TextView) container.getChildAt(position);
        if (textView == null) return this;
        textView.setBackgroundResource(drawableId);
        return this;
    }

    /**
     * 设置view是否可用
     * @param enable
     * @param position
     */
    public MIActionSheet setViewEnable(boolean enable, int position) {
        if (arrays == null | position >= arrays.length) return this;
        TextView textView = (TextView) container.getChildAt(position);
        if (textView == null) return this;
        textView.setEnabled(enable);
        return this;
    }

    private int[] arrays;

    public MIActionSheet setItemArray(int[] array) {
        if (array == null) {
            return this;
        }
        arrays = array;
        int length = arrays.length;
        for (int i = 0; i < length; i++) {
            TextView convertView = (TextView) container.getChildAt(i);
            TextView itemView = getItemView(convertView, i);
            if (convertView == null) {
                container.addView(itemView);
            }
        }
        for (int i = 0, count = container.getChildCount() - length; i < count; i++) {
            View childView = container.getChildAt(length + i);
            if (childView != null) setViewVisibility(childView, View.GONE);
        }
        return this;
    }

    @Override
    public void show() {
        super.show();
        getWindow().setContentView(rootView);
        animView.startAnimation(showAnim());
    }

    public void dismissMenu() {
        if (isShowing()) {
            dismiss();
            container.clearAnimation();
        }
    }

    private TranslateAnimation showAnim;

    private Animation showAnim() {
        if (showAnim == null) {
            int self = TranslateAnimation.RELATIVE_TO_SELF;
            showAnim = new TranslateAnimation(self, 0, self, 0, self, 1, self, 0);
            showAnim.setDuration(TRANSLATE_ANIM_DURATION);
        }
        return showAnim;
    }

    private TextView getItemView(TextView convertView, int position) {
            if (convertView == null) {
                convertView = new TextView(mContext);
                convertView.setId(R.id.action_sheet_item_id);
                convertView.setGravity(Gravity.CENTER);
                convertView.setTextColor(textColor);
                convertView.setTextSize(16);
                convertView.setMinHeight(dip48);
                convertView.setOnClickListener(this);
                convertView.setPadding(dip10, 0, dip10, 0);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                layoutParams.topMargin = dip1;
                convertView.setLayoutParams(layoutParams);
            } else {
                setViewVisibility(convertView, View.VISIBLE);
            }
            int item = getItem(position);
            convertView.setTag(item);
            convertView.setText(item);
        setBackground(convertView, position);
//        setParams(convertView);
        return convertView;
    }


    private int getItem(int position) {
        if (arrays == null || position < 0 || position >= arrays.length)
            return 0;
        return arrays[position];
    }

    private void setParams(TextView convertView) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) convertView.getLayoutParams();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.topMargin = dip1;
        convertView.setLayoutParams(lp);
    }

    private void setBackground(TextView convertView, int position) {
        int length = arrays.length;
        if (length == 0) return;
        if (length == 1) {
            convertView.setBackgroundResource(cancelBtnBackground);
        } else {
            if (position == 0) {
                convertView.setBackgroundResource(topBtnBackground);
            } else if (position == length - 1) {
                convertView.setBackgroundResource(bottomBtnBackground);
            } else {
                convertView.setBackgroundResource(middleBtnBackground);
            }
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel) {
            rootView.setOnClickListener(this);
        } else {
            rootView.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_sheet_bg_id:
                dismissMenu();
                break;
            case R.id.action_sheet_cancel_btn:
                dismissMenu();
                break;
            case R.id.action_sheet_item_id:
                int resId = Integer.parseInt(v.getTag().toString());
                if (menuItemClickListener != null) {
                    menuItemClickListener.onItemClick(resId);
                }
                break;
        }
    }


    private void setViewVisibility(View v, int status) {
        if (v == null || v.getVisibility() == status) return;
        v.setVisibility(status);
    }

    private InputMethodManager imm;

    private void hideInputMethod() {
        if (imm == null) {
            imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        View focusView = ((Activity)mContext).getCurrentFocus();
        if (focusView != null)
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
    }

    public void setMenuItemClickListener(MenuItemClickListener $listener) {
        menuItemClickListener = $listener;
    }

    private MenuItemClickListener menuItemClickListener;
}
