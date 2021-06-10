package android.xrj.opengl;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.webkit.WebView;

public class CapturedWebView extends WebView {
    private SurfaceView surfaceView;

    public CapturedWebView(Context context) {
        this(context, null);
    }

    public CapturedWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CapturedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        surfaceView = findViewById(R.id.surfaceView);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (surfaceView != null) {
            Canvas surfaceCanvas = surfaceView.getHolder().lockCanvas();
            if (surfaceCanvas != null) {
                //translate canvas to reflect view scrolling
                float xScale = surfaceCanvas.getWidth() / (float) canvas.getWidth();
                surfaceCanvas.scale(xScale, xScale);
                surfaceCanvas.translate(-getScrollX(), -getScrollY());
                super.draw(surfaceCanvas);
                surfaceView.getHolder().unlockCanvasAndPost(surfaceCanvas);
            }
        }
    }


    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

}
