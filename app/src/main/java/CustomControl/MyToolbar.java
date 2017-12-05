package CustomControl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.waterteam.musicproject.R;

/**
 * Created by Administrator on 2017/12/5.
 */

public class MyToolbar extends Toolbar {
    private TextView left_title_toolbar;
    /**
     * 中间Title
     */
    private TextView noText_textView_toolbar;
    /**
     * 右侧Title
     */
    private ImageButton mTxtRightTitle;

    public MyToolbar(Context context) {
        this(context,null);
    }

    public MyToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        left_title_toolbar = (TextView) findViewById(R.id.toolbar_left_title);
        noText_textView_toolbar = (TextView) findViewById(R.id.toolbar_noText_textView);
        mTxtRightTitle = (ImageButton) findViewById(R.id.toolbar_imageButton);
    }

    //设置中间title的内容
    public void setMainTitle(String text) {
        this.setTitle(" ");
        noText_textView_toolbar.setVisibility(View.VISIBLE);
        noText_textView_toolbar.setText(text);
    }

    //设置中间title的内容文字的颜色
    public void setMainTitleColor(int color) {
        noText_textView_toolbar.setTextColor(color);
    }

    //设置title左边文字
    public void setLeftTitleText(String text) {
        left_title_toolbar.setVisibility(View.VISIBLE);
        left_title_toolbar.setText(text);
    }

    //设置title左边文字颜色
    public void setLeftTitleColor(int color) {
        left_title_toolbar.setTextColor(color);
    }

    //设置title左边图标
    public void setLeftTitleDrawable(int res) {
        Drawable dwLeft = ContextCompat.getDrawable(getContext(), res);
        dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
        left_title_toolbar.setCompoundDrawables(dwLeft, null, null, null);
    }
    //设置title左边点击事件
    public void setLeftTitleClickListener(OnClickListener onClickListener){
        left_title_toolbar.setOnClickListener(onClickListener);
    }

    //设置title右边文字
    public void setRightTitleText(String text) {
        mTxtRightTitle.setVisibility(View.VISIBLE);
    }

    //设置title右边文字颜色
    public void setRightTitleColor(int color) {

    }

    //设置title右边图标
    public void setRightTitleDrawable(int res) {
        Drawable dwRight = ContextCompat.getDrawable(getContext(), res);
        dwRight.setBounds(0, 0, dwRight.getMinimumWidth(), dwRight.getMinimumHeight());
    }

    //设置title右边点击事件
    public void setRightTitleClickListener(OnClickListener onClickListener){
        mTxtRightTitle.setOnClickListener(onClickListener);
    }
}
