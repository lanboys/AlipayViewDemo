package com.bing.lan.alipayview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class YouZanActivity extends AppCompatActivity {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);

    YouZanQrcodeView youZan;
    RelativeLayout rl_pay_scan_rqcode;
    LinearLayout activity_scan_capture;
    float length = 600;

    AutoScannerView mAutoScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_zan);

        youZan = (YouZanQrcodeView) findViewById(R.id.youZan);
        rl_pay_scan_rqcode = (RelativeLayout) findViewById(R.id.rl_pay_scan_rqcode);
        activity_scan_capture = (LinearLayout) findViewById(R.id.activity_scan_capture);

        mAutoScannerView = (AutoScannerView) findViewById(R.id.autoView);

        mAutoScannerView.setCameraManager(new CameraManager());

        rl_pay_scan_rqcode.setTranslationY(length);
        activity_scan_capture.setTranslationY(length);

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

                //rl_pay_scan_rqcode.setX();
                rl_pay_scan_rqcode.setTranslationY(centerYRatio * length);

                activity_scan_capture.setScaleX(1 - centerYRatio + 0.2f);
                activity_scan_capture.setScaleY(1 - centerYRatio + 0.2f);
                activity_scan_capture.setTranslationY(centerYRatio * length);
            }
        });
    }
}
