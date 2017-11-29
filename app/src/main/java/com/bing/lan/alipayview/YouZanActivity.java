package com.bing.lan.alipayview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class YouZanActivity extends AppCompatActivity {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);

    YouZanQrcodeView youZan;
    RelativeLayout rl_pay_scan_rqcode;
    LinearLayout activity_scan_capture;
    float length = 500;

    AutoScannerView mAutoScannerView;

    ImageView iv_show_generate_view;
    TextView tv_pay_money_tips;
    TextView tv_pay_money;
    ImageView iv_show_scan_sqcode;
    LinearLayout ll_pay_logo;
    TextView tv_scan_tips;

    int qrcodeHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_zan);

        youZan = (YouZanQrcodeView) findViewById(R.id.youZan);
        rl_pay_scan_rqcode = (RelativeLayout) findViewById(R.id.rl_pay_scan_rqcode);
        activity_scan_capture = (LinearLayout) findViewById(R.id.activity_scan_capture);
        iv_show_generate_view = (ImageView) activity_scan_capture.findViewById(R.id.iv_show_generate_view);
        tv_pay_money_tips = (TextView) activity_scan_capture.findViewById(R.id.tv_pay_money_tips);
        tv_pay_money = (TextView) activity_scan_capture.findViewById(R.id.tv_pay_money);
        iv_show_scan_sqcode = (ImageView) activity_scan_capture.findViewById(R.id.iv_show_scan_sqcode);
        ll_pay_logo = (LinearLayout) activity_scan_capture.findViewById(R.id.ll_pay_logo);
        tv_scan_tips = (TextView) activity_scan_capture.findViewById(R.id.tv_scan_tips);

        mAutoScannerView = (AutoScannerView) findViewById(R.id.autoView);

        mAutoScannerView.setCameraManager(new CameraManager());

        //rl_pay_scan_rqcode.setTranslationY(length);
        activity_scan_capture.setTranslationY(length);

        iv_show_scan_sqcode.post(new Runnable() {
            @Override
            public void run() {
                qrcodeHeight = iv_show_scan_sqcode.getHeight();
            }
        });

        //activity_scan_capture.setScaleX(0.2f);
        //activity_scan_capture.setScaleY(0.2f);

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

                rl_pay_scan_rqcode.setTranslationY(centerYRatio * length);

                activity_scan_capture.setTranslationY(centerYRatio * length);

                float ratio;
                centerYRatio = 1 - centerYRatio;

                if (centerYRatio > 1f) {
                    ratio = 1f;
                } else if (centerYRatio < 0.3f) {
                    ratio = 0.3f;
                } else {
                    ratio = centerYRatio;
                }

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_show_scan_sqcode.getLayoutParams();
                layoutParams.height = (int) (qrcodeHeight * ratio);
                iv_show_scan_sqcode.setLayoutParams(layoutParams);

                iv_show_generate_view.setAlpha(centerYRatio);
                tv_pay_money_tips.setAlpha(centerYRatio);
                tv_pay_money.setAlpha(centerYRatio);
                ll_pay_logo.setAlpha(centerYRatio);
                tv_scan_tips.setAlpha(centerYRatio);
            }
        });
    }
}
