package com.bing.lan.alipayview;

import android.graphics.Rect;

/**
 * Created by 520 on 2017/11/25.
 */

public class CameraManager {

    public Rect getFramingRect() {

        return new Rect(200, 200, 800, 800);
    }

    public Rect getFramingRectInPreview() {
        return new Rect(200, 200, 800, 800);
    }
}
