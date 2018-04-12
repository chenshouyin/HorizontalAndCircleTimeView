package it399.com.horizontalandcircletimeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


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
public class MainActivity extends AppCompatActivity {
    private it399.com.horizontalandcircletimeview.view.HorizontalSeekBar mHorizontalSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(it399.com.horizontalandcircletimeview.R.layout.activity_main);

        //水平刻度初始化
        mHorizontalSeekBar = findViewById(it399.com.horizontalandcircletimeview.R.id.mHorizontalSeekBar);
        mHorizontalSeekBar.setOnIndexChange(new it399.com.horizontalandcircletimeview.view.HorizontalSeekBar.OnIndexChange() {
            @Override
            public void onIndexChanged(int index) {
                Toast.makeText(MainActivity.this,  "index = " +index,Toast.LENGTH_LONG).show();
            }
        });
        //有数据的刻度
        int[] times = new int[60];
        times[0] = 0;
        times[2] = 3;
        times[4] = 5;
        times[19] = 20;
        times[25] = 26;
        times[30] = 31;
        times[35] = 36;
        times[58] = 59;
        mHorizontalSeekBar.updateLine(times);

        //圆形钟表刻度初始化
        final it399.com.horizontalandcircletimeview.view.CircleRangeTimeView cv = (it399.com.horizontalandcircletimeview.view.CircleRangeTimeView) findViewById(it399.com.horizontalandcircletimeview.R.id.cv);
        cv.setChangeListener(new it399.com.horizontalandcircletimeview.view.CircleRangeTimeView.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int duration, int progress) {
                Toast.makeText(MainActivity.this,  "duration = " + duration + ",progress = " + progress+"==getTimeText"+cv.getTimeText(progress),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgressChangeEnd(int duration, int progress) {
                Toast.makeText(MainActivity.this,  "duration = " + duration + ",progress = " + progress+"==getTimeText"+cv.getTimeText(progress),Toast.LENGTH_LONG).show();
            }
        });
        //有数据的时间段
        int[] times2 = new int[]{0,6,12,18};
        cv.drawEachTimeLines(times2);
    }
}
