package com.example.srugu.slidetest4;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by tamano on 2018/01/22.
 * http://blog.oukasoft.com/%E3%83%97%E3%83%AD%E3%82%B0%E3%83%A9%E3%83%A0/%E3%80%90android%E3%80%91surfaceview%E3%82%92%E4%BD%BF%E3%81%A3%E3%81%A6%E3%82%B2%E3%83%BC%E3%83%A0%E3%81%A3%E3%81%BD%E3%81%84%E3%82%A2%E3%83%97%E3%83%AA%E3%82%92%E4%BD%9C%E3%81%A3%E3%81%A6%E3%81%BF/
 */

public class SampleHolderCallBack implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder holder = null;
    private Thread thread = null;
    private boolean isAttached = true;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO 自動生成されたメソッド・スタブ
        this.holder = holder;

        // https://qiita.com/circularuins/items/a61c5e7149f355a54a8b
        Canvas canvas = holder.lockCanvas();

        //描画例（円を描く）
        Paint p = new Paint();
        p.setARGB(255, 255, 0, 0);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(300, 300, 300, p);

        holder.unlockCanvasAndPost(canvas);

        //thread = new Thread(this);
        //thread.start(); //スレッドを開始
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO 自動生成されたメソッド・スタブ
        // 使わない(´・ω・`)
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO 自動生成されたメソッド・スタブ
        isAttached = false;
        thread = null; //スレッドを終了
    }

    @Override
    public void run() {
        // TODO 自動生成されたメソッド・スタブ
        // メインループ（無限ループ）
        while( isAttached ){
            //Log.w("テスト", "ループなう");
        }
    }
}
