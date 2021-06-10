package android.xrj.opengl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CapturedWebViewActivity extends Activity {
    private CapturedWebView webView;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_webview);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("https://www.baidu.com");
        surfaceView = findViewById(R.id.surfaceView);
        webView.setSurfaceView(surfaceView);
        webView.postDelayed(runnable,500);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            genBitmap();
            webView.postDelayed(runnable, 500);
        }
    };

    public void genBitmap() {
        long time1 = System.currentTimeMillis();
        int width = surfaceView.getWidth();
        int height = surfaceView.getHeight();
        int size = width * height;
        ByteBuffer buf = ByteBuffer.allocateDirect(size * 4);
        buf.order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, width, height,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buf);


        int data[] = new int[size];
        buf.asIntBuffer().get(data);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(data, size - width, -width, 0, 0, width, height);

        float scaleWidth = ((float) height) / width;
        float scaleHeight = ((float) width) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        long time2 = System.currentTimeMillis();
        android.util.Log.i("wqs", "getJpegDataFromGpu565----->d time= " + (time2 - time1) + " ms;");

    }
}
