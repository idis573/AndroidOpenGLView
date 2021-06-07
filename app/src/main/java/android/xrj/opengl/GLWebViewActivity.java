package android.xrj.opengl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.xrj.opengl.support.BaseGLRenderer;
import android.xrj.opengl.support.FooRenderer;
import android.xrj.opengl.support.view.GLWebView;
import android.xrj.opengl.util.DisplayUtil;
import android.xrj.opengl.util.StatusUtil;

/**
 * Create by LingYan on 2018-08-27
 */

public class GLWebViewActivity extends Activity {
    private static final String TAG = "GLWebViewActivity";
    private GLSurfaceView mGLSurfaceView;
    private GLWebView mWebView;
    private AppBarLayout appBarLayout;
    private BaseGLRenderer baseGlRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl_webview);
        mGLSurfaceView = findViewById(R.id.gl_surface_view);
        mWebView = findViewById(R.id.web_view);
        appBarLayout = findViewById(R.id.appbar_layout);

        int width = DisplayUtil.getWidth(this);
        int height = DisplayUtil.getHeight(this) / 2;

        baseGlRenderer = new FooRenderer(this, width, height);

        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(baseGlRenderer);


        mWebView.setViewToGLRenderer(baseGlRenderer);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("https://www.baidu.com");
        mWebView.postDelayed(runnable,500);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mGLSurfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    baseGlRenderer.genBitmap();
                    mWebView.postDelayed(runnable, 500);
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
