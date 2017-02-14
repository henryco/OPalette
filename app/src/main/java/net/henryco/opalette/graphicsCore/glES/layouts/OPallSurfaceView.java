package net.henryco.opalette.graphicsCore.glES.layouts;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OPallSurfaceView extends GLSurfaceView {


    public OPallSurfaceView(Context context) {
        super(context);
        reInit(context);
    }

    public OPallSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        reInit(context);
    }

    public void reInit(Context context) {
        setEGLContextClientVersion(2);
    }

    public void update() {
        requestRender();
    }
}
