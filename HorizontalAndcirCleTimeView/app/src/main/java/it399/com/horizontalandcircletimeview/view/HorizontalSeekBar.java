package it399.com.horizontalandcircletimeview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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

public class HorizontalSeekBar extends View {
    private Paint paint;
    private Context mContext;
    private int padingLeft;
    //中线坐标
    private Point centerPoint;
    //默认宽度高度
    private int deafualtWidth;
    private int deafualtHeight;
    //倒三角边长 triangle
    private  int triangleWidth;
    //倒三角当前定点坐标
    private Point curentTrianglePosition;
    //每格之间的像素值
    private int eachWidth;
    //0~59之间的分钟数,用于区分线条颜色
    private int[] times;
    //用于缓冲
    private  Bitmap cacheBitmap;
    private Canvas cacheCanvas;
    private OnIndexChange onIndexChange;
    public HorizontalSeekBar(Context context) {
        this(context, null);
    }

    public HorizontalSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        paint.setAntiAlias(true);

        mContext = context;


        deafualtWidth = DensityUtils.dip2px(mContext,300);
        deafualtHeight = DensityUtils.dip2px(mContext,100);
        triangleWidth = DensityUtils.dip2px(mContext,20);

        padingLeft = triangleWidth/2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centerPoint = new Point(measureWidth(widthMeasureSpec)/2, measureHeight(heightMeasureSpec)/2);
        curentTrianglePosition  = new Point(0, centerPoint.y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //双缓冲,先在内存中绘制,避免滑动闪烁
        // 创建一个与该View相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(centerPoint.x*2,centerPoint.y*2, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        //设置cacheCanvas将会绘制到内存中的cacheBitmap上
        cacheCanvas.setBitmap(cacheBitmap);

        //水平线
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(20);
//        cacheCanvas.drawLine(0, centerPoint.y, centerPoint.x*2, centerPoint.y, paint);

        //画倒三角
        drawTouchBar(cacheCanvas,new Point(curentTrianglePosition.x,curentTrianglePosition.y));
        //画刻度
        drawTimeLines(cacheCanvas);
        //画文字 0  60
        drawMyText(cacheCanvas);

        //将cacheBitmap绘制到该View组件上
        canvas.drawBitmap(cacheBitmap, 0, 0, paint);
    }

    private void drawMyText(Canvas cacheCanvas) {
        paint.setColor(Color.parseColor("#7d7d7d"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setTextSize(40);
        cacheCanvas.drawText("0",padingLeft,centerPoint.y - DensityUtils.dip2px(mContext,20),paint);

        float textWidth = paint.measureText("60");
        cacheCanvas.drawText("60",centerPoint.x*2 - padingLeft - textWidth,centerPoint.y - DensityUtils.dip2px(mContext,20),paint);

    }

    /**
     * 分成60份
     * 画刻度
     * @param canvas
     */
    private void drawTimeLines(Canvas canvas) {
        //总长度/60 分成60份
        int lineY1 = 0;
        int lineY2 = 0;
        eachWidth = (centerPoint.x*2-padingLeft*2) /60;
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeWidth(4);
        //水平线 x1，x2不同,垂直线,y1,y2不同
        for (int i=0;i<=60;i++){
            //改变线条颜色
            if (times !=null && times.length>0){
                if (i<60 && times[i] == (i+1)){
                    paint.setColor(Color.parseColor("#29b473"));
                }else{
                    paint.setColor(Color.parseColor("#565656"));
                }
            }
            //长短线条
            if (i % 5==0){
                lineY2 = DensityUtils.dip2px(mContext,10);
                paint.setStrokeWidth(4);
            }else{
                lineY2 = DensityUtils.dip2px(mContext,5);
                paint.setStrokeWidth(2);
            }
            canvas.drawLine(padingLeft + eachWidth *i,centerPoint.y - lineY1,padingLeft + eachWidth *i,centerPoint.y - lineY2,paint);
        }
    }

    /**
     *
     * @param canvas
     * @param topPoint 倒三角最上边的点坐标
     */
    private void drawTouchBar(Canvas canvas,Point topPoint) {
        paint.setColor(Color.parseColor("#7d6ea8"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(20);

        Path path = new Path();
        //偏移量 相对于原点
        int offsetX = triangleWidth/2;
        int offsetY = 0 + triangleWidth/2;

        path.moveTo(topPoint.x - triangleWidth/2 + offsetX, topPoint.y + triangleWidth + offsetY);
        //底边
        path.lineTo(topPoint.x + triangleWidth/2 + offsetX, topPoint.y + triangleWidth + offsetY);
        //右边
        path.lineTo(topPoint.x + offsetX, topPoint.y + offsetY);
        //左边
        path.lineTo(topPoint.x - triangleWidth/2 + offsetX, topPoint.y + triangleWidth + offsetY);
        path.close();
        canvas.drawPath(path, paint);
    }


    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = DensityUtils.dip2px(mContext,deafualtWidth);
        }

        return result;
    }


    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            result = DensityUtils.dip2px(mContext,deafualtHeight);
        }
        return result;
    }

    private boolean downOnArc = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchOnTriangle(new Point(x,y))){
                    System.out.println("=====ACTION_DOWN 点到了===");
                    downOnArc = true;
                    //updateArc(x);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (downOnArc){
                    System.out.println("=====ACTION_MOVE 点到了===");
                    updateArc(x);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                downOnArc = false;
                invalidate();
                updateArc(x);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void updateArc(int x) {
        int offsetX = curentTrianglePosition.x - x;
        if (offsetX>0){
            //后退
            offsetX = Math.abs(offsetX);
            //滑动要是整格
            if (offsetX<eachWidth){
                offsetX = eachWidth;
            }else{
                offsetX = (offsetX/eachWidth +1)*eachWidth;
            }
            curentTrianglePosition.x = (int)(curentTrianglePosition.x - offsetX );

            if (curentTrianglePosition.x<=padingLeft){
                curentTrianglePosition.x  = 0 ;
            }
            int index = curentTrianglePosition.x/eachWidth;
            if (onIndexChange!=null && !downOnArc){
                onIndexChange.onIndexChanged(index);
            }
        }else if (offsetX<0){
            //前进
            offsetX = Math.abs(offsetX);
            //滑动要是整格
            if (offsetX<eachWidth){
                offsetX = eachWidth;
            }else{
                offsetX = (offsetX/eachWidth +1)*eachWidth;
            }
            curentTrianglePosition.x = (int)(curentTrianglePosition.x + offsetX);

            if (curentTrianglePosition.x > centerPoint.x*2  - padingLeft -triangleWidth/2){
                curentTrianglePosition.x  = centerPoint.x*2 - padingLeft - triangleWidth/2;
            }
        }
        int index = curentTrianglePosition.x/eachWidth;
        if (onIndexChange!=null && !downOnArc){
            onIndexChange.onIndexChanged(index);
        }
        System.out.println("======offsetX=="+offsetX);
        System.out.println("======curentTrianglePosition.x=="+curentTrianglePosition.x);
        invalidate();
    }


    private boolean isTouchOnTriangle(Point point){
        //扩大2倍返回
        if (((point.x > (curentTrianglePosition.x - triangleWidth/2)/2) && (point.x < (curentTrianglePosition.x + triangleWidth /2)*2))
                && ((point.y > curentTrianglePosition.y/2) && (point.y < (curentTrianglePosition.y + triangleWidth)*2))){
            return true;
        }
        return false;
    }

    /**
     *
     * @param times  0~59 的数值
     */
    public void updateLine(int[] times){
        this.times = times;
        invalidate();
    }

    public interface OnIndexChange{
        void onIndexChanged(int index);
    }

    public void setOnIndexChange(OnIndexChange onIndexChange){
        this.onIndexChange = onIndexChange;
    }
}
