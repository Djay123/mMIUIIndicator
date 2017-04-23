package com.gn.djay.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by DJAY on 2017/4/16.
 */

public class ViewPagerIndicator extends LinearLayout {

    //可见的Tab数量
    private int visiableNum = 4;
    //常态下的Title颜色
    private static final int COLOR_TEXT_NORMAL = 0x77000000;
    //被选中时的Title的颜色
    private static final int COLOR_TEXT_HIGHLIGHT = 0xFFFF0000;

    //指示器占一个Tab的比例,1/6不加F的话，任何数乘以它都得0
    public final static float RADIO_TRIANGEL = 1 / 6F;

    //偏移量
    private int mTranslationX;

    //初始化偏移量
    private int mInitTranslationX;

    //三角形的宽度
    private int mTriangleWidth;

    //三角形的高
    private int mTriangleHight;

    //线段
    private Path mPath;

    //画笔
    private Paint mPaint;

    private RectF mRect;

    //屏幕的宽度
    private int screenWidth;




    //接收Tab的标题
    String[] array;

    ViewPager mViewpager;
    public ViewPagerIndicator(Context context) {
        super(context);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#FFFFFFFF"));
        //设置拐角处有一些弧度
        mPaint.setPathEffect(new CornerPathEffect(3));
        mPaint.setStrokeWidth(15);

        screenWidth = getScreenWidth();
        mTriangleWidth = (int)(screenWidth / visiableNum * RADIO_TRIANGEL);



        //初始化三角形的最初偏移位置
        mInitTranslationX = (int)(screenWidth / visiableNum / 2 - mTriangleWidth / 2);

    }




    @Override
    protected void dispatchDraw(Canvas canvas) {


        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 3);


        canvas.drawPath(mPath,mPaint);
        //canvas.drawLine(0,0,200,0,mPaint);
        canvas.restore();

        //如果不加这一句，那么不会显示任何东西
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = getChildCount();
        if (cCount == 0)
            return;

        for (int i = 0; i < cCount; i++)
        {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view
                    .getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / visiableNum;
            view.setLayoutParams(lp);
        }

        OnClickListenerTab();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initTriangle();

    }

    public void initTriangle(){
        mPath = new Path();
        mTriangleHight = (int)(mTriangleWidth  / 2);
        mPath.moveTo(0,0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth / 2,-mTriangleHight);
        mPath.close();
    }


    /**
     * 根据传入的参数来确定Tab的数量
     * @param list
     */
    public void setTabTitles(String[] list){
        if (list != null && list.length > 0){
            array = list;
            this.removeAllViews();
            for(int i = 0; i < list.length; i++){
                addView(generateTextView(list[i]));
            }
            OnClickListenerTab();
        }
    }

    /**
     * 根据标题生成一个View
     * @param title
     * @return
     */
    public View generateTextView(String title){
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        params.width = getScreenWidth() / visiableNum;
        textView.setGravity(Gravity.CENTER);
        textView.setText(title);
        //将单位设置为sp
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        textView.setTextColor(COLOR_TEXT_NORMAL);
        textView.setLayoutParams(params);

        return textView;
    }


    /**
     * 获得屏幕的宽度
     * @return
     */
    public int getScreenWidth(){
        WindowManager wm = (WindowManager) getContext().
                getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }


    /**
     * 将该控件和ViewPager关联起来的函数
     */
    public void setViewpager(ViewPager viewpager,int position){
        mViewpager = viewpager;

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * 当ViewPager的界面滑动的时候调用该方法
             * @param position   当前页面
             * @param positionOffset    当前页面滑动的百分比
             * @param positionOffsetPixels    当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {


                scroll(position,positionOffset);

            }

            /**
             *当手指滑动够长，某个页面被选中的时候调用该方法
             * @param position：被选中的项
             */
            @Override
            public void onPageSelected(int position) {

                hightTabColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewpager.setCurrentItem(position);
        hightTabColor(position);
    }


    /**
     * 高亮显示选中的Tab文字
     */
    public void hightTabColor(int position){
        resetTabColor();

        View view = getChildAt(position);
        if(view instanceof TextView){
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }
    /**
     * 将所有的TextView的字体颜色设置成普通颜色
     */
    public void resetTabColor(){
        for(int i = 0; i < getChildCount(); i++){
            View view = getChildAt(i);
            if(view instanceof TextView){
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }


    /**
     * 对所有的Tab项进行监听，当点击了Tab项，切换到相应的Fragment
     */
    public void OnClickListenerTab(){
        int nCount = getChildCount();
        for(int i = 0; i < nCount; i++){
            final int j = i;
            View view = getChildAt(i);
            if(view instanceof TextView){
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewpager.setCurrentItem(j);
                    }
                });
            }
        }
    }

    public void scroll(int position, float offset){
        int tabWidth = screenWidth / visiableNum;


        /**
         * offset的值从0到最大在跳变到0，比如从position == 0的位置滑动到position == 1的位置，
         * offset的值从0变到接近于1，当继续滑动的时候position就变为1，offset就变成了0
         */
        mTranslationX = (int) (tabWidth * (offset + position));

        // 容器移动，在tab处于移动至最后一个时
        if (position >= (visiableNum - 2) && offset > 0
                && getChildCount() > visiableNum)
        {
            if (visiableNum != 1)
            {
                /**scrollTo(x,y),相对于(0,0)偏移到(x,y);
                 * 当滑动到倒数第二个的时候，控件才发生滚动，在x轴的滚动距离：
                 * (position - (visiableNum -2) + (int)(tabWidth * offset)) * tabWidth
                 * (int)(tabWidth * offset) 加上这个值滑动时看起来比较平滑
                 * 0 < (int)(tabWidth * offset) < tabWidth
                 */
                this.scrollTo((position - (visiableNum - 2)) * tabWidth
                        + (int) (tabWidth * offset), 0);
            } else
            {
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        invalidate();
    }


}
