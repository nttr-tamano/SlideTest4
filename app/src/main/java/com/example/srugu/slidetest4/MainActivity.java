package com.example.srugu.slidetest4;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity
        implements SurfaceHolder.Callback,Runnable {

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;

    // 円の描画テスト用
    int mCircleX = 300;
    int mCircleY = 300;
    final int mCircleRadius = 300;

    // アニメーション用
    Thread mThread;
    boolean isAttached = true; //false;

    // 画像の表示用
    Bitmap mBitmap;
    CustomView[] cv = new CustomView[4];

    final int ANIME_NONE = 0;
    final int ANIME_UP = 3;
    final int ANIME_LEFT = 4;
    final int ANIME_RIGHT = 2;
    final int ANIME_DOWN = 1;
    int mAnimeDirection = ANIME_NONE;

    // ANIME_FRAME * ANIME_WAIT_MSEC / 1000 [sec] がアニメーション時間
    final int ANIME_FRAME = 15;
    final int ANIME_WAIT_MSEC = 50; // msec

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mSurfaceView = new SampleSurfaceView(this);
//        LinearLayout baseLayout = (LinearLayout) findViewById(R.id.baseLayout);
//        LinearLayout layoutTop = (LinearLayout) findViewById(R.id.constraintLayout);
//
////オーバーレイビューの追加
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        //baseLayout.addView(mSurfaceView, params);
//        layoutTop.addView(mSurfaceView, params);

//        // アニメーション用スレッドは予め生成しておく
//        if (mThread == null) {
//            mThread = new Thread(this);
//        }

        ////////////////////////////////////////////////////////////
        // アニメーションのための、SurfaceView 関連

        // SurfaceViewを載せるLinearLayout
        final LinearLayout coverLayout = (LinearLayout) findViewById(R.id.coverLayout);
        coverLayout.setVisibility(LinearLayout.INVISIBLE);

        // SurfaceViewの初期化
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = mSurfaceView.getHolder();
        // コールバック設定
        mHolder.addCallback(this);
        // 透過
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        // 一番手前に表示
        mSurfaceView.setZOrderOnTop(true);

        // タップでテスト中。スライドパズルはフリック
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("Thread Status","mThread.getState()="+mThread.getState());

                // スレッド排他制御。起動していなければスタート
                if (mThread == null || mThread.isAlive() == false) {
                    // フリック方向のダミー。下右上左の繰り返し
                    mAnimeDirection += 1;
                    mAnimeDirection %= 4;
                    if (mAnimeDirection == 0) {
                        mAnimeDirection = 4;
                    }
                    //Log.d("Anime","mAnimeDirection="+mAnimeDirection+",isAttached="+isAttached);

                    int i;
                    int j;
                    switch (mAnimeDirection) {
                        case ANIME_DOWN:
                            i = 0;
                            j = 2;
                            break;
                        case ANIME_RIGHT:
                            i = 2;
                            j = 3;
                            break;
                        case ANIME_UP:
                            i = 3;
                            j = 1;
                            break;
                        case ANIME_LEFT:
                            i = 1;
                            j = 0;
                            break;
                        default:
                            i = 0;
                            j = 2;
                    }

                    // 移動先に画像情報をセット
                    cv[j].setRes(cv[i].getRes());

                    // スレッドの内容の実行許可
                    isAttached = true;
                    // 参考:スレッド生成をOnClick内から呼ぶときは、MainActivity必須か
                    mThread = new Thread(MainActivity.this);
                    // スレッド開始
                    // 1つのスレッドは1回しかstart()できない(java.lang.IllegalThreadStateException: Thread already started)
                    // http://blog.codebook-10000.com/entry/20140530/1401450268
                    mThread.start();
                }
            }
        });

//        // surfaceviewに画像セット（AccBall参照）
//        Bitmap bitmap_work = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_right);
//        // サイズ補正
//        mBitmap = Bitmap.createScaledBitmap(bitmap_work,200,200,false);

        ////////////////////////////////////////////////////////////
        // アニメーションの後ろのImageView群 関連
        // ダミーの2x2

        // 上側LinearLayout
        LinearLayout layoutTop = (LinearLayout) findViewById(R.id.layoutTop);
        // カスタムViewを生成し、layout内に追加
        // cv[0] = new CustomView(this);
        cv[0] = new CustomView(this, true);
        cv[0].setVisibility(View.INVISIBLE);
        // http://blog.lciel.jp/blog/2013/12/16/android-capture-view-image/
        cv[0].setDrawingCacheEnabled(true);             // キャッシュを取得する設定にする
        cv[0].destroyDrawingCache();                    // キャッシュをクリア
        // カスタムViewのサイズ
        LinearLayout.LayoutParams lp0 = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp0.weight = 1.0F;
        layoutTop.addView(cv[0], lp0);      // 左上
        // カスタムViewを生成し、layout内に追加
        cv[1] = new CustomView(this);
        cv[1].setVisibility(View.INVISIBLE);
        // http://blog.lciel.jp/blog/2013/12/16/android-capture-view-image/
        cv[1].setDrawingCacheEnabled(true);             // キャッシュを取得する設定にする
        cv[1].destroyDrawingCache();                    // キャッシュをクリア
        // カスタムViewのサイズ
        LinearLayout.LayoutParams lp1 = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp1.weight = 1.0F;
        layoutTop.addView(cv[1], lp1);      // 右上

        // 下側LinearLayout
        LinearLayout layoutBottom = (LinearLayout) findViewById(R.id.layoutBottom);
        // カスタムViewを生成し、layout内に追加
        cv[2] = new CustomView(this);
        cv[2].setVisibility(View.INVISIBLE);
        // http://blog.lciel.jp/blog/2013/12/16/android-capture-view-image/
        cv[2].setDrawingCacheEnabled(true);             // キャッシュを取得する設定にする
        cv[2].destroyDrawingCache();                    // キャッシュをクリア
        // カスタムViewのサイズ
        LinearLayout.LayoutParams lp2 = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp2.weight = 1.0F;
        layoutBottom.addView(cv[2], lp2);      // 左上
        // カスタムViewを生成し、layout内に追加
        cv[3] = new CustomView(this);
        cv[3].setVisibility(View.INVISIBLE);
        // http://blog.lciel.jp/blog/2013/12/16/android-capture-view-image/
        cv[3].setDrawingCacheEnabled(true);             // キャッシュを取得する設定にする
        cv[3].destroyDrawingCache();                    // キャッシュをクリア
        // カスタムViewのサイズ
        LinearLayout.LayoutParams lp3 = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp3.weight = 1.0F;
        layoutBottom.addView(cv[3], lp3);

        // 表示デモ用にcv[0]だけ表示
        cv[0].setVisibility(View.VISIBLE);

        ////////////////////////////////////////////////////////////

    }

    // 非表示から復帰の際に落ちることへの対策
    @Override
    protected void onPause() {
        super.onPause();
        if (mThread != null) {
            while(mThread.isAlive()) {
                try {
                    Thread.sleep(200);
                }
                catch (Exception e) {
                }
            }
        }
        isAttached = false;
        mThread = null; //スレッドを終了
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // https://qiita.com/circularuins/items/a61c5e7149f355a54a8b
        Canvas canvas = holder.lockCanvas();

        // キャンバス（背景）を透過
        // https://qiita.com/androhi/items/a1ed36d3743d5b8cb771
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

//        //描画例（円を描く）
//        Paint p = new Paint();
//        p.setStrokeWidth(10);
//        p.setARGB(255, 255, 0, 0);
//        p.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, p);
//
//        holder.unlockCanvasAndPost(canvas);

        // 描画画像を、ImageViewから取得
        // http://falco.sakura.ne.jp/tech/2013/09/android-imageview-%E3%82%88%E3%82%8A-bitmap-%E3%82%92%E5%8F%96%E5%BE%97%E3%81%99%E3%82%8B%E3%81%AB%E3%81%AF%EF%BC%9F/
        //ImageView imageView = (ImageView) findViewById(R.id.imageViewTop); 　 // 成功
        //ImageView imageView = (ImageView) findViewById(R.id.imageViewBottom); // 成功

        // 描画終了
        mHolder.unlockCanvasAndPost(canvas);

//        // スレッド開始（即アニメーション開始の場合）
//        mThread = new Thread(this);
//        mThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isAttached = false;
        mThread = null; //スレッドを終了
    }

    // 一定時間毎に移動するアニメーション
    @Override
    public void run() {

        // Log.d()が効かない
        while(isAttached) {
            int cvIdSrc = -1;
            int cvIdDest = -1;
            switch (mAnimeDirection) {
            case ANIME_DOWN:
                cvIdSrc = 0;
                cvIdDest = 2;
                break;
            case ANIME_RIGHT:
                cvIdSrc = 2;
                cvIdDest = 3;
                break;
            case ANIME_UP:
                cvIdSrc = 3;
                cvIdDest = 1;
                break;
            case ANIME_LEFT:
                cvIdSrc = 1;
                cvIdDest = 0;
                break;
            }
            if (cvIdSrc == -1) {
                return;
            }

            final ImageView srcImageView = cv[cvIdSrc];

            //        // ImageView 側の画像色変換（→消す処理に変わる）
            //        // http://y-anz-m.blogspot.jp/2011/04/androidcolorfilter.html
            //        imageView.setColorFilter(0xcc0000ff, PorterDuff.Mode.SRC_IN);   // 青色のみにする？

            //        Bitmap bitmap_work = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

//            srcImageView.post(new Runnable() {
//                @Override
//                public void run() {
//                    // http://blog.lciel.jp/blog/2013/12/16/android-capture-view-image/
//                    // View の描画キャッシュを使用
//                    srcImageView.setDrawingCacheEnabled(true);             // キャッシュを取得する設定にする
//                    srcImageView.destroyDrawingCache();                    // 既存のキャッシュをクリアする
                    Bitmap bitmap_work = srcImageView.getDrawingCache();   // キャッシュを作成して取得する
                    //mBitmap = bitmap_work;
                    // ImageViewのサイズに合わせる
                    mBitmap = Bitmap.createScaledBitmap(bitmap_work, srcImageView.getMeasuredWidth(),
                            srcImageView.getMeasuredHeight(), false);
//                }
//            });

            // 描画開始位置
            int srcX = srcImageView.getLeft();
            // Y座標は親View(横長LinearLayout)から取得
            // http://ichitcltk.hustle.ne.jp/gudon2/index.php?pageType=file&id=Android059_ViewTree
            int srcY = ((View) srcImageView.getParent()).getTop();
            // Log.d("location", "imageView X=" + srcX + ",Y=" + srcY);

            //        int[] location = new int[2];
            //        // http://y-anz-m.blogspot.jp/2012/10/androidview.html
            //        imageView.getLocationOnScreen(location);
            //        //imageView.getLocationInWindow(location);
            //        mCircleX = location[0];
            //        mCircleY = location[1];
            //        Log.d("location","imageView X="+mCircleX+",Y="+mCircleY);

            // 描画例（画像）
            //Paint p = new Paint();
            //canvas.drawBitmap(mBitmap,mCircleX,mCircleY,p);
            //http://yamato-iphone.blogspot.jp/2013/03/imageview.html

            //imageView.setImageBitmap(mBitmap);

//            // アニメーション開始前ウェイト処理
//            // http://boco.hp3200.com/game-devs/view/3.html
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//            }

            // 移動元の画像を消す
            // スレッドからUI更新を行うと、CalledFromWrongThreadException 発生
            //imageView.setVisibility(View.INVISIBLE);
            // http://cuuma.publog.jp/archives/30332977.html
            // http://www.office-matsunaga.biz/android/description.php?id=8
            // runOnUiThread() or View.post() で対処可能
            srcImageView.post(new Runnable() {
                @Override
                public void run() {
                    srcImageView.setVisibility(View.INVISIBLE);
                    //srcImageView.setImageBitmap(null);
                }
            });

            // 移動先ImageViewに画像を書き込む
            final ImageView destImageView = cv[cvIdDest];
            destImageView.post(new Runnable() {
                @Override
                public void run() {
                    destImageView.setImageBitmap(mBitmap);
                }
            });

            // 移動先座標。switch caseで制御か
            int destX = 0;
            int destY = 0;
            int deltaX = 0;
            int deltaY = 0;

            switch (mAnimeDirection) {
                case ANIME_DOWN:
                    destX = srcX;
                    destY = srcY + ((View) srcImageView.getParent()).getHeight();
                    deltaX = 0;
                    deltaY = ((View) srcImageView.getParent()).getHeight() / ANIME_FRAME;
                    break;
                case ANIME_RIGHT:
                    destX = srcX + srcImageView.getWidth();
                    destY = srcY;
                    deltaX = srcImageView.getWidth() / ANIME_FRAME;
                    deltaY = 0;
                    break;
                case ANIME_UP:
                    destX = srcX;
                    destY = srcY - ((View) srcImageView.getParent()).getHeight();
                    deltaX = 0;
                    deltaY = -((View) srcImageView.getParent()).getHeight() / ANIME_FRAME;
                    break;
                case ANIME_LEFT:
                    destX = srcX - srcImageView.getWidth();
                    destY = srcY;
                    deltaX = -srcImageView.getWidth() / ANIME_FRAME;
                    deltaY = 0;
                    break;
            }

            // アニメーションのループ
            // int型を比較しており、値の補正を行っているため、座標一致で終了が可能
            while (srcY != destY || srcX != destX) {
            // while (Math.abs(srcY - destY) > 0 && Math.abs(srcX - destX) > 0) {

                // 移動

                // 左右へ
                srcX += deltaX;
                // 上下へ
                srcY += deltaY;

                // 移動先を通り過ぎたら、移動先へ補正
                if (deltaY > 0 && srcY > destY) {           // 下
                    srcY = destY;
                } else if ( deltaY < 0 && srcY < destY ) {  // 上
                    srcY = destY;
                }
                if (deltaX > 0 && srcX > destX) {           // 右
                    srcX = destX;
                } else if (deltaX < 0 && srcX < destX) {    // 左
                    srcX = destX;
                }

                // 次の描画
                Canvas canvas = mHolder.lockCanvas();

                // キャンバス（背景）を透過
                // https://qiita.com/androhi/items/a1ed36d3743d5b8cb771
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                //            //描画例（円を描く）
                //            Paint p = new Paint();
                //            p.setStrokeWidth(10);
                //            p.setARGB(255, 255, 0, 0);
                //            p.setStyle(Paint.Style.STROKE);
                //            canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, p);

                // 描画例（画像）
                Paint p = new Paint();
                canvas.drawBitmap(mBitmap, srcX, srcY, p);

                // 描画終了
                mHolder.unlockCanvasAndPost(canvas);

                // http://boco.hp3200.com/game-devs/view/3.html
                //ウェイト処理
                try {
                    Thread.sleep(ANIME_WAIT_MSEC);
                } catch (InterruptedException e) {
                }

            }

            // 移動先ImageViewの表示
            destImageView.post(new Runnable() {
                @Override
                public void run() {
                    destImageView.setVisibility(View.VISIBLE);
                }
            });

            // http://boco.hp3200.com/game-devs/view/3.html
            // アニメーション終了後ウェイト処理
            try {
                Thread.sleep(ANIME_WAIT_MSEC);
            } catch (InterruptedException e) {
            }

            // SurfaceViewのクリア
            Canvas canvas = mHolder.lockCanvas();
            // キャンバス（背景）を透過
            // https://qiita.com/androhi/items/a1ed36d3743d5b8cb771
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

//            //描画例（円を描く）
//            Paint p = new Paint();
//            p.setStrokeWidth(10);
//            p.setARGB(255, 255, 0, 0);
//            p.setStyle(Paint.Style.STROKE);
//            canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, p);

            mHolder.unlockCanvasAndPost(canvas);

            // スレッド終了
            isAttached = false;
        }
        // run()が終わるとスレッドは消滅する
        // http://www.techscore.com/tech/Java/JavaSE/Thread/4-4/
    }
}
