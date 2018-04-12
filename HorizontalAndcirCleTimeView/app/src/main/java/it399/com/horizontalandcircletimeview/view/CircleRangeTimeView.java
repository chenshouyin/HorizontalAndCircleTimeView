package it399.com.horizontalandcircletimeview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import it399.com.horizontalandcircletimeview.utils.ChartUtils;
import it399.com.horizontalandcircletimeview.utils.DensityUtils;


/**
 * Created by shouyinchen on 2018/1/18.
 *
 * 在线助手:http://www.it399.com/
 *
 * Github:https://github.com/chenshouyin
 *
 *CSDN博客:https://blog.csdn.net/e_inch_photo
 *
 */
public class CircleRangeTimeView extends View {
    private Context mContext;
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 点的半径
     */
    private float pointRadius;

    /**
     * 空心点的宽度
     */
    private float pointWidth;
    /**
     * 空心点的宽度
     */

    /**
     *有记录的时间段
     */
    private int[] hours;

    private int maxLineHeight ;
    //private Drawable mDragDrawable, mDragPressDrawable;

    public CircleRangeTimeView(Context context) {
        this(context, null);
    }

    public CircleRangeTimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRangeTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        paint = new Paint();

        //获取自定义属性和默认值
        roundColor = Color.parseColor("#f5f5f5");
        roundProgressColor = Color.parseColor("#f5f5f5");
        roundWidth = DensityUtils.dip2px(context,9);
        textColor = Color.parseColor("#ff7b1a");
        textSize =  57;
        //最大刻度
        //max = 100;
        max = 24;
        pointRadius = 3;
        pointWidth = 2;

        // 加载拖动图标
//        mDragDrawable = getResources().getDrawable(R.drawable.ring_dot);// 圆点图片
//        int thumbHalfheight = mDragDrawable.getIntrinsicHeight();
//        int thumbHalfWidth = mDragDrawable.getIntrinsicWidth() ;
//        mDragDrawable.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);
//
//        mDragPressDrawable = getResources().getDrawable(R.drawable.ring_dot);// 圆点图片
//        thumbHalfheight = mDragPressDrawable.getIntrinsicHeight() ;
//        thumbHalfWidth = mDragPressDrawable.getIntrinsicWidth() ;
//        mDragPressDrawable.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);

        //外边距
        paddingOuterThumb = DensityUtils.dip2px(mContext,20);
    }


    @Override
    public void onDraw(Canvas canvas) {
        /**
         * 画最外层的大圆环
         */
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerY, radius, paint); //画出圆环

        /**
         * 画文字
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        String textTime = getTimeText(progress);
        float textWidth = paint.measureText(textTime);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        canvas.drawText(textTime, centerX - textWidth / 2, centerY + textSize / 2, paint);

        /**
         * 画圆弧 ，画圆环的进度
         */
        paint.setStrokeWidth(roundWidth + roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centerX - radius  , centerY - radius , centerX + radius , centerY +
                radius);  //用于定义的圆弧的形状和大小的界限
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 270, 360 * progress / max, false, paint);  //根据进度画圆弧


        /**
         * 圆环颜色
         */
        paint.setStrokeWidth(roundWidth + roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval2 = new RectF(centerX - radius  , centerY - radius , centerX + radius , centerY +
                radius);  //用于定义的圆弧的形状和大小的界限
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval2, 270, 360, false, paint);  //根据进度画圆弧



        // 画圆上的两个点
//        paint.setStrokeWidth(pointWidth);
//        PointF startPoint = ChartUtils.calcArcEndPointXY(centerX, centerY, radius, 0, 270);
//        canvas.drawCircle(startPoint.x, startPoint.y, pointRadius, paint);

//        PointF progressPoint = ChartUtils.calcArcEndPointXY(centerX, centerY, radius, 360 *
//                progress / max, 270);
        // 画Thumb
        canvas.save();

        //画用图片画
//        canvas.translate(progressPoint.x, progressPoint.y);
//        if (downOnArc) {
//            mDragPressDrawable.draw(canvas);
//        } else {
//            mDragDrawable.draw(canvas);
//        }



        //画刻度 24小时
        drawLines(canvas);
        //画内圆外圆颜色
        drawCircleColor(canvas);
        //画可拖动的圆点
        drawTouchBar(canvas);
        //画圆外文字
        drawOutSideText(canvas);
        //画出某些时间段内的圆弧
        drawEachTimeLines(canvas,hours);
    }

    /**
     * 画圆外文字
     * @param canvas
     */
    private void drawOutSideText(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        paint.setTextSize(40);
        paint.setColor(Color.parseColor("#565656"));
        int pading  = paddingOuterThumb/4;
        //测量字体宽度
        float textWidth1 = paint.measureText("0");
        float textWidth2 = paint.measureText("12");

        int left = centerX - radius - (int)roundWidth -(int) textWidth1 - pading;
        int top = centerY - radius - (int)roundWidth - pading;
        int right = centerX + radius + (int)roundWidth +pading;
        int bottom =  centerY + radius + (int)roundWidth + (int)textWidth2;
        canvas.drawText("0",(left + right)/2,top+ textWidth1/3,paint);
        canvas.drawText("18" ,left ,(top + bottom)/2,paint);
        canvas.drawText("6",right - textWidth2/3,(top + bottom)/2 ,paint);
        canvas.drawText("12",(left + right)/2 - textWidth2/3,bottom - textWidth1/3,paint);

    }

    /**
     * 画可拖动的圆点
     * @param canvas
     */
    private void drawTouchBar(Canvas canvas) {
        PointF progressPoint = ChartUtils.calcArcEndPointXY(centerX, centerY, radius, 360 *
                progress / max, 270);
        //直接用画笔画
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(Color.parseColor("#ff7b1a"));
        canvas.drawCircle(progressPoint.x, progressPoint.y,roundWidth/2,paint);
        canvas.restore();
    }

    /**
     * 画内圆外圆颜色
     * @param canvas
     */
    private void drawCircleColor(Canvas canvas) {
        //画外圆颜色
        //paint.setColor(Color.parseColor("#f7f7f7"));
        paint.setColor(Color.parseColor("#f7f7f7"));
        paint.setStrokeWidth(2);
        canvas.drawCircle(centerX,centerY,radius + roundWidth,paint);
        //画内圆颜色
        paint.setColor(Color.parseColor("#f6f6f6"));
        paint.setStrokeWidth(2);
        canvas.drawCircle(centerX,centerY,radius - roundWidth,paint);
    }

    /**
     * 画出刻度
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        paint.setColor(Color.parseColor("#e5e5e5"));
        //直线粗细
        paint.setStrokeWidth(10);
        for (int i = 0;i<24;i++){
            float mProgress = (i+1)*1.0f/24*max;
            PointF mProgressPoint = ChartUtils.calcArcEndPointXY(centerX, centerY, radius, 360 *
                    mProgress / max, 270);
            //圆上到圆心
            //canvas.drawLine(mProgressPoint.x,mProgressPoint.y,centerX,centerY,paint);
            float scale1 = radius*1.0F / roundWidth;
            float scale2 = radius*1.0F / (radius - roundWidth);
            //计算内圆上的点
            float disX = (scale1*mProgressPoint.x + scale2*centerX)/(scale1+ scale2);
            float disY =  (scale1*mProgressPoint.y + scale2*centerY)/(scale1+ scale2);
            //计算外圆上的点
            float disX2 = mProgressPoint.x*2 - disX;
            float disY2 =  mProgressPoint.y*2 - disY;
            if (mProgress%6 == 0){
                //直线3/4高度
                float disX3 = (disX*3 + disX2)/4;
                float disY3 =  (disY*3 + disY2)/4;
                canvas.drawLine(disX2 ,disY2,disX3,disY3,paint);

                maxLineHeight =  (int)(disX*1 + disX2*3)/4;
            }else{
                //直线1/2高度
                float disX3 = (disX*1 + disX2)/2;
                float disY3 =  (disY*1 + disY2)/2;
                canvas.drawLine(disX2 ,disY2,disX3,disY3,paint);
            }

        }

    }

    /**
     * 画出某些时间段内的圆弧
     * @param hours
     */
    public void drawEachTimeLines(int[] hours){
        this.hours = hours;
        invalidate();
    }



    /**
     * 根据进度画出圆弧
     * @param canvas
     * @param mProgress 这里的进度 = 时间
     */
    private void drawMyArc(Canvas canvas,int mProgress){
        paint.setStrokeWidth(roundWidth/4); //设置1/4
        paint.setColor(Color.parseColor("#ff7b1a"));  //设置进度的颜色
        RectF oval = new RectF(centerX - radius  + roundWidth/4*3, centerY - radius + roundWidth/4*3, centerX + radius - roundWidth/4*3, centerY +
                radius - roundWidth/4*3);  //用于定义的圆弧的形状和大小的界限
        paint.setStyle(Paint.Style.STROKE);

        float start = 270 + (mProgress*360/max);
        //这个end是说在起始度数上加的度数
        float end = 15;
        canvas.drawArc(oval, start,end, false, paint);  //根据进度画圆弧

    }


    private boolean downOnArc = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchArc(x, y)) {
                    downOnArc = true;
                    updateArc(x, y);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (downOnArc) {
                    updateArc(x, y);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                downOnArc = false;
                invalidate();
                if (changeListener != null) {
                    changeListener.onProgressChangeEnd(max, progress);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private int centerX, centerY;
    private int radius;
    private int paddingOuterThumb;

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        centerX = width / 2;
        centerY = height / 2;
        int minCenter = Math.min(centerX, centerY);

        radius = (int) (minCenter - roundWidth / 2 - paddingOuterThumb); //圆环的半径
        minValidateTouchArcRadius = (int) (radius - paddingOuterThumb * 1.5f);
        maxValidateTouchArcRadius = (int) (radius + paddingOuterThumb * 1.5f);
        super.onSizeChanged(width, height, oldw, oldh);
    }

    // 根据点的位置，更新进度
    private void updateArc(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        // 计算角度，得出（-1->1）之间的数据，等同于（-180°->180°）
        double angle = Math.atan2(cy, cx) / Math.PI;
        // 将角度转换成（0->2）之间的值，然后加上90°的偏移量
        angle = ((2 + angle) % 2 + (90 / 180f)) % 2;
        // 用（0->2）之间的角度值乘以总进度，等于当前进度
        progress = (int) (angle * max / 2);
        if (changeListener != null) {
            changeListener.onProgressChange(max, progress);
        }
        invalidate();
    }

    private int minValidateTouchArcRadius; // 最小有效点击半径
    private int maxValidateTouchArcRadius; // 最大有效点击半径

    // 判断是否按在圆边上
    private boolean isTouchArc(int x, int y) {
        double d = getTouchRadius(x, y);
        if (d >= minValidateTouchArcRadius && d <= maxValidateTouchArcRadius) {
            return true;
        }
        return false;
    }

    // 计算某点到圆点的距离
    private double getTouchRadius(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        return Math.hypot(cx, cy);
    }

    public String getTimeText(int progress) {
        //总进度100 分为24小时
        long hour = Math.round(progress*1.0/max * 24);
        int result = (int)hour;
        if (result == 24){
            result = 0;
        }
        return result+":00" + "-"+(result+1)+":00";
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    private OnProgressChangeListener changeListener;

    public void setChangeListener(OnProgressChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface OnProgressChangeListener {
        void onProgressChange(int duration, int progress);

        void onProgressChangeEnd(int duration, int progress);
    }


    /**
     * 画出某些时间段内的圆弧
     * @param hours
     */
    private void drawEachTimeLines(Canvas canvas,int[] hours){
        if (hours == null || hours.length==0){
            return;
        }
        for (int i = 0;i<hours.length;i++){
            drawMyArc(canvas,hours[i]);
        }

    }


}
