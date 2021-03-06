package com.example.srugu.slidetest4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.widget.ImageView;

import java.util.Arrays;

/**
 * Created by tamano on 2018/01/26.
 * https://tech.recruit-mp.co.jp/mobile/remember_canvas1/
 */

public class CustomView extends android.support.v7.widget.AppCompatImageView {

    final int UNDEFINED_RESOURCE = 0;
    private Paint paint = new Paint();

    // View上に、最大4つの画像を貼り付け予定
    private final int BITMAP_COUNT = 4;
    private int[] mResources = new int[BITMAP_COUNT];
    // 整数配列の初期値はnullではないがそのまま扱えない
    // https://detail.chiebukuro.yahoo.co.jp/qa/question_detail/q111032016

    private Bitmap[] mBitmaps = new Bitmap[BITMAP_COUNT];

    // デフォルトコンストラクタ
    public CustomView(Context context) {
        this(context, false);

        // superなしで、CustumView(context, false); としたい
        // 他のコンストラクタを呼び出したい
        // http://www.atmarkit.co.jp/ait/articles/0912/17/news110_2.html
    }

    // 追加引数付きコンストラクタ
    public CustomView(Context context,boolean isImages) {
        super(context);

        // https://donsyoku.com/zakki/java-initialization-arrays-fill.html
        Arrays.fill(mResources,UNDEFINED_RESOURCE);

        // 画像を入れる(=true)か否か(=false)
        if (isImages) {
            mResources[1] = R.drawable.arrow_right;
            mResources[3] = R.drawable.arrow_left;

            // 画像読込（AccBall参照）
            mBitmaps[1] = BitmapFactory.decodeResource(getResources(),mResources[1]);

            // 画像読込（AccBall参照）
            mBitmaps[3] = BitmapFactory.decodeResource(getResources(),mResources[3]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // キャンバス（背景）を透過
        // https://qiita.com/androhi/items/a1ed36d3743d5b8cb771
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Bitmap[] bitmapWork = new Bitmap[4];

        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();

        int viewWidthHalf = viewWidth / 2;
        int viewHeightHalf = viewHeight / 2;

        canvas.drawColor(Color.CYAN);

        // ビュー内の4箇所（以上）に、配置すべき画像情報を決定して配置する（予定）

        int i=1;
        if (mBitmaps[i] != null) {
            // サイズ補正（AccBall参照）
            bitmapWork[i] = Bitmap.createScaledBitmap(mBitmaps[i],viewWidthHalf,viewHeightHalf,false);
            // View上に描画
            canvas.drawBitmap(bitmapWork[i],viewWidthHalf,0,paint);
        }

        i=3;
        if (mBitmaps[i] != null) {
            // サイズ補正（AccBall参照）
            bitmapWork[i] = Bitmap.createScaledBitmap(mBitmaps[i],viewWidthHalf,viewHeightHalf,false);
            // View上に描画
            canvas.drawBitmap(bitmapWork[i],0,viewHeightHalf,paint);
        }

    }

    // 画像のリソース情報の取得
    public int[] getRes() {
        return mResources;
    }

    // 画像のリソース情報の設定＋ビットマップ準備
    public void setRes(int[] mResources) {
        // 配列のコピー
        // https://eng-entrance.com/java-array-copy

        //System.arraycopy(this.mResources,BITMAP_COUNT,mResources,
        //        0,Math.min(BITMAP_COUNT,mResources.length));

        // https://developer.android.com/reference/java/util/Arrays.html#copyOf(int[], int)
        this.mResources = Arrays.copyOf(mResources,BITMAP_COUNT);

        for (int i = 0; i < BITMAP_COUNT; i++) {
            if (this.mResources[i] != UNDEFINED_RESOURCE) {
                // 画像読込（AccBall参照）
                this.mBitmaps[i] = BitmapFactory.decodeResource(getResources(),mResources[i]);
            }
        }
    }

}
