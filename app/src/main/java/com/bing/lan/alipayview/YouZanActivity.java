package com.bing.lan.alipayview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class YouZanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_zan);
    }

    public void onBtnView(View view) {
        Toast.makeText(this, "我被点击了", Toast.LENGTH_SHORT).show();
    }
}
