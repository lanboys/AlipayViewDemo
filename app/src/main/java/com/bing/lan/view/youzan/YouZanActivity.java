package com.bing.lan.view.youzan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bing.lan.view.R;
import com.bing.lan.view.utils.LogUtil;

public class YouZanActivity extends AppCompatActivity {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);

    YouZanQrcodeView youZan;
    float length = 610;
    float length1 = 500;

    AutoScannerView mAutoScannerView;

    LinearLayout activity_scan_capture;
    ImageView iv_show_generate_view;
    TextView tv_pay_money_tips;
    TextView tv_pay_money;
    ImageView iv_show_scan_sqcode;
    LinearLayout ll_pay_logo;
    TextView tv_scan_tips;

    LinearLayout activity_scan_capture1;
    ImageView iv_show_generate_view1;
    TextView tv_pay_money_tips1;
    TextView tv_pay_money1;
    ImageView iv_show_scan_sqcode1;
    LinearLayout ll_pay_logo1;
    TextView tv_scan_tips1;

    int qrcodeHeight;
    int qrcodeHeight1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_zan);

        mAutoScannerView = (AutoScannerView) findViewById(R.id.autoView);
        mAutoScannerView.setCameraManager(new CameraManager());

        youZan = (YouZanQrcodeView) findViewById(R.id.youZan);

        activity_scan_capture = (LinearLayout) findViewById(R.id.activity_scan_capture);
        iv_show_generate_view = (ImageView) activity_scan_capture.findViewById(R.id.iv_show_generate_view);
        tv_pay_money_tips = (TextView) activity_scan_capture.findViewById(R.id.tv_pay_money_tips);
        tv_pay_money = (TextView) activity_scan_capture.findViewById(R.id.tv_pay_money);
        iv_show_scan_sqcode = (ImageView) activity_scan_capture.findViewById(R.id.iv_show_scan_sqcode);
        ll_pay_logo = (LinearLayout) activity_scan_capture.findViewById(R.id.ll_pay_logo);
        tv_scan_tips = (TextView) activity_scan_capture.findViewById(R.id.tv_scan_tips);

        activity_scan_capture1 = (LinearLayout) findViewById(R.id.activity_scan_capture1);
        iv_show_generate_view1 = (ImageView) activity_scan_capture1.findViewById(R.id.iv_show_generate_view1);
        tv_pay_money_tips1 = (TextView) activity_scan_capture1.findViewById(R.id.tv_pay_money_tips1);
        tv_pay_money1 = (TextView) activity_scan_capture1.findViewById(R.id.tv_pay_money1);
        iv_show_scan_sqcode1 = (ImageView) activity_scan_capture1.findViewById(R.id.iv_show_scan_sqcode1);
        ll_pay_logo1 = (LinearLayout) activity_scan_capture1.findViewById(R.id.ll_pay_logo1);
        tv_scan_tips1 = (TextView) activity_scan_capture1.findViewById(R.id.tv_scan_tips1);

        activity_scan_capture.setTranslationY(length);
        iv_show_generate_view.setAlpha(0);
        tv_pay_money_tips.setAlpha(0);
        tv_pay_money.setAlpha(0);
        ll_pay_logo.setAlpha(0);
        tv_scan_tips.setAlpha(0);

        iv_show_scan_sqcode.post(new Runnable() {
            @Override
            public void run() {
                qrcodeHeight = iv_show_scan_sqcode.getHeight();

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_show_scan_sqcode.getLayoutParams();
                layoutParams.height = (int) (qrcodeHeight * 0.3f);
                iv_show_scan_sqcode.setLayoutParams(layoutParams);
            }
        });

        iv_show_scan_sqcode1.post(new Runnable() {
            @Override
            public void run() {
                qrcodeHeight1 = iv_show_scan_sqcode1.getHeight();
            }
        });

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

                //if (centerYRatio > 1f) {
                //    centerYRatio = 1f;
                //} else if (centerYRatio < 0f) {
                //    centerYRatio = 0f;
                //}

                activity_scan_capture.setTranslationY(centerYRatio * length);

                float alpha = 0f;
                if (centerYRatio >= 0.5f) {
                    alpha = (centerYRatio - 0.5f) * 2;
                } else if (centerYRatio < 0.5f) {
                    alpha = 0;
                }
                log.i("onRectCenterScroll() alpha1: " + alpha);

                iv_show_generate_view1.setAlpha(alpha);
                tv_pay_money_tips1.setAlpha(alpha);
                tv_pay_money1.setAlpha(alpha);
                ll_pay_logo1.setAlpha(alpha);
                tv_scan_tips1.setAlpha(alpha);

                LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) iv_show_scan_sqcode1.getLayoutParams();
                layoutParams1.height = (int) (qrcodeHeight1 * centerYRatio);
                if (layoutParams1.height < 0) {
                    layoutParams1.height = 0;
                }
                iv_show_scan_sqcode1.setLayoutParams(layoutParams1);

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
                if (layoutParams1.height < 0) {
                    layoutParams1.height = 0;
                }
                iv_show_scan_sqcode.setLayoutParams(layoutParams);

                if (centerYRatio >= 0.5f) {
                    alpha = (centerYRatio - 0.5f) * 2;
                } else if (centerYRatio < 0.5f) {
                    alpha = 0;
                }
                log.i("onRectCenterScroll() alpha2: " + alpha);

                iv_show_generate_view.setAlpha(alpha);
                tv_pay_money_tips.setAlpha(alpha);
                tv_pay_money.setAlpha(alpha);
                ll_pay_logo.setAlpha(alpha);
                tv_scan_tips.setAlpha(alpha);

                activity_scan_capture1.setTranslationY(-centerYRatio * length1);
            }
        });
    }
}
