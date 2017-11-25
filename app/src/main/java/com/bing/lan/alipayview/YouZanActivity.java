package com.bing.lan.alipayview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class YouZanActivity extends AppCompatActivity {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);

    YouZanQrcodeView youZan;
    TextView text1;
    TextView text2;
    float length = 600;

    AutoScannerView mAutoScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_zan);

        youZan = (YouZanQrcodeView) findViewById(R.id.youZan);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);

        mAutoScannerView = (AutoScannerView) findViewById(R.id.autoView);

        mAutoScannerView.setCameraManager(new CameraManager());

        text1.setTranslationY(length);
        text2.setTranslationY(length);

        youZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YouZanActivity.this, "youZan被点击了", Toast.LENGTH_SHORT).show();
            }
        });

        youZan.setOnTopBottomClickListener(new YouZanQrcodeView.OnTopBottomClickListener() {
            @Override
            public void onTopClick(YouZanQrcodeView view) {
                //Toast.makeText(YouZanActivity.this, "youZan被点击了上面", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBottomClick(YouZanQrcodeView view) {
                //Toast.makeText(YouZanActivity.this, "youZan被点击了下面", Toast.LENGTH_SHORT).show();
            }
        });

        youZan.setOnRectCenterScrollListener(new YouZanQrcodeView.OnRectCenterScrollListener() {
            @Override
            public void onRectCenterScroll(YouZanQrcodeView view, float centerX, float centerY, float centerXRatio, float centerYRatio) {
                log.i("onRectCenterScroll() centerY: " + centerY);
                log.i("onRectCenterScroll() centerYRatio: " + centerYRatio);

                //text1.setX();
                text1.setTranslationY(centerYRatio * length);

                text2.setScaleX(1 - centerYRatio + 0.2f);
                text2.setScaleY(1 - centerYRatio + 0.2f);
                text2.setTranslationY(centerYRatio * length);
            }
        });
    }
}
