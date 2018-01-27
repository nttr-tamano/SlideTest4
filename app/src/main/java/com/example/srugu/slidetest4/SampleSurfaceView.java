package com.example.srugu.slidetest4;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by tamano on 2018/01/22.
 * http://blog.oukasoft.com/%E3%83%97%E3%83%AD%E3%82%B0%E3%83%A9%E3%83%A0/%E3%80%90android%E3%80%91surfaceview%E3%82%92%E4%BD%BF%E3%81%A3%E3%81%A6%E3%82%B2%E3%83%BC%E3%83%A0%E3%81%A3%E3%81%BD%E3%81%84%E3%82%A2%E3%83%97%E3%83%AA%E3%82%92%E4%BD%9C%E3%81%A3%E3%81%A6%E3%81%BF/
 */

public class SampleSurfaceView extends SurfaceView {

    private SampleHolderCallBack cb;

    public SampleSurfaceView(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        cb = new SampleHolderCallBack();
        holder.addCallback(cb);

        // https://qiita.com/circularuins/items/a61c5e7149f355a54a8b
        //ビューの背景を透過させる
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        //最前面に描画する
        setZOrderOnTop(true);

    }
}
