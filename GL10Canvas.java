package github.OpenSourceAIX.OpenGL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.content.Context;
import android.view.ViewGroup;

import github.OpenSourceAIX.OpenGL10.util.BufferUtil;
import github.OpenSourceAIX.OpenGL10.util.Util;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.HVArrangement;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.YailList;

@DesignerComponent(version = Settings.EXTENSION_VERSION,
    description = Settings.EXTENSION_DESCRIPTION,
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = Settings.canvas.ICON)
@SimpleObject(external = true)
public final class GL10Canvas extends AndroidNonvisibleComponent
implements Component {

    private ComponentContainer container;
    private Context context;

    private GLSurfaceView glSurfaceView;

    /**
     * Events related
     */
    private boolean firstReady = true;

    /**
     * Properties related
     */
    private boolean asContentView;
    
    private boolean glRenderContinuously;

    private int bgColor = Settings.canvas.DEFAULT_BG_COLOR;
    private float[] bgColorF = new float[4];

    private int glShadeModel = Settings.canvas.DEFAULT_SHADE_MODEL;
    
    private boolean glDepthTestEnabled = Settings.canvas.DEFAULT_DEPTH_TEST_ENABLED;
    private int glDepthFunc = Settings.canvas.DEFAULT_DEPTH_FUNC;
    
    /**
     * Methods related
     */
    private final github.OpenSourceAIX.OpenGL10.Log log = 
        new github.OpenSourceAIX.OpenGL10.Log();
    private GL10 glRender = null;



    public GL10Canvas(ComponentContainer container) {
        super(container.$form());
        this.container = container;
        context = (Context) container.$context();
        BackgroundColor(Settings.canvas.DEFAULT_BG_COLOR);
    }
    

    @SimpleFunction(
        description = "Don't need to call this if property ScreenContentView is checked")
    public void RegisterArrangement(HVArrangement arrangement) {
        if (glSurfaceView != null) {
            return;
        }

        glSurfaceView = new GLSurfaceView(context);
        glSurfaceView.setRenderer(new Renderer());
        
        if (asContentView) {
            container.$form().setContentView(glSurfaceView);
        } else {
            ViewGroup vg = (ViewGroup)arrangement.getView();
            vg.removeAllViews();
            vg.addView(glSurfaceView);
        }
    }


    @SimpleProperty
    public int AndroidWidth() {
        return glSurfaceView.getWidth();
    }
    @SimpleProperty
    public int AndroidHeight() {
        return glSurfaceView.getHeight();
    }
    @SimpleProperty
    public float Width() {
        return (float)AndroidWidth() / container.$form().deviceDensity();
    }
    @SimpleProperty
    public float Height() {
        return (float)AndroidHeight() / container.$form().deviceDensity();
    }


    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, 
        defaultValue = Settings.canvas.DEFAULT_AS_CONTENT_VIEW ? "True" : "False")
    @SimpleProperty(userVisible = false)
    public void ScreenContentView(boolean asContentView) {
        if (glSurfaceView != null) {
            return;
        }
        this.asContentView = asContentView;
        if (asContentView) {
            // Parent arrangement is null does not affect when glSurfaceView is set as content view
            RegisterArrangement(null);
        }
    }
    
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, 
        defaultValue = Settings.canvas.DEFAULT_RENDER_CONTINUOUSLY ? "True" : "False")
    @SimpleProperty(userVisible = false)
    public void glRenderContinuously(boolean renderContinuously) {
        this.glRenderContinuously = renderContinuously;
        if (glSurfaceView!=null) {
            glSurfaceView.setRenderMode(
                renderContinuously ?
                GLSurfaceView.RENDERMODE_CONTINUOUSLY :
                GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }
    @SimpleProperty
    public boolean glRenderContinuously() {
        return glRenderContinuously;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR,
        defaultValue = ""+Settings.canvas.DEFAULT_BG_COLOR)
    @SimpleProperty
    public void BackgroundColor(int color) {
        bgColor = color;
        // write into buffer
        Util.colorI2F(color, bgColorF);
    }
    @SimpleProperty
    public int BackgroundColor() {
        return bgColor;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
        defaultValue = Settings.canvas.DEFAULT_SHADE_MODEL==GL10.GL_SMOOTH ? "True" : "False")
    @SimpleProperty(userVisible = false)
    public void glShadeModelSmooth(boolean smooth) {
        // zh - http://blog.csdn.net/chenqiai0/article/details/8316258
        this.glShadeModel = smooth ? GL10.GL_SMOOTH : GL10.GL_FLAT;
    }

    // avoid name this "glEnabledDepthTest" in order to keep it shown with "glDepthFunc"
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, 
        defaultValue = Settings.canvas.DEFAULT_DEPTH_TEST_ENABLED ? "True" : "False")
    @SimpleProperty(userVisible = false)
    public void glDepthTestEnabled(boolean enabled) {
        this.glDepthTestEnabled = enabled;
    }
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
        defaultValue = ""+Settings.canvas.DEFAULT_DEPTH_FUNC)
    @SimpleProperty(userVisible = false)
    public void glDepthFunc(int func){
        // EN    https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glDepthFunc.xhtml
        // ZH_CN http://blog.csdn.net/shuaihj/article/details/7230780
        this.glDepthFunc = func;
    }



    @SimpleFunction(
        description = "Request that the renderer render a frame. This is usually called "+
            "only when RenderContinuously is not checked (false)")
    public void RequestRender() {
        glSurfaceView.requestRender();
    }

    @SimpleEvent
    public void CanvasReady() {
        // This may be called for many times by onSurfaceCreated, but we catch only the first run.
        if (firstReady) {
            firstReady = false;
            EventDispatcher.dispatchEvent(this, "CanvasReady");
        }
    }

    @SimpleEvent
    public void Render() {
        EventDispatcher.dispatchEvent(this, "Render");
    }

    /**
     * sample function
     */
    @SimpleFunction
    public void gl() {
        if (glRender == null) {
            log.log(" on a null GL10 object");
            return;
        }
    }
    
    @SimpleFunction(description = "Enable a client-side capability.\n"+
            "Accepted Constant: GL_VERTEX_ARRAY, GL_NORMAL_ARRAY, GL_COLOR_ARRAY, GL_TEXTURE_COORD_ARRAY")
    public void glEnableClientState(int array) {
        if (glRender == null) {
            log.log("glEnableClientState on a null GL10 object");
            return;
        }
        glRender.glEnableClientState(array);
    }

    @SimpleFunction(description = "Disable a client-side capability.\n"+
            "Accepted client state: GL_VERTEX_ARRAY, GL_NORMAL_ARRAY, GL_COLOR_ARRAY, GL_TEXTURE_COORD_ARRAY")
    public void glDisableClientState(int array) {
        if (glRender == null) {
            log.log("glDisableClientState on a null GL10 object");
            return;
        }
        glRender.glDisableClientState(array);
    }

    private static final String GL_CAPABILITY_LIST =
            "Accepted capabilities: GL_ALPHA_TEST, GL_BLEND, GL_COLOR_LOGIC_OP, GL_COLOR_MATERIAL, GL_CULL_FACE, "+
            "GL_DEPTH_TEST, GL_DITHER, GL_FOG, GL_LIGHTING, GL_LINE_SMOOTH, GL_MULTISAMPLE, GL_NORMALIZE, GL_POINT_SMOOTH, "+
            "GL_POLYGON_OFFSET_FILL, GL_RESCALE_NORMAL, GL_SAMPLE_ALPHA_TO_COVERAGE, GL_SAMPLE_ALPHA_TO_ONE, "+
            "GL_SAMPLE_COVERAGE, GL_SCISSOR_TEST, GL_STENCIL_TEST, GL_TEXTURE_2D";
    
    @SimpleFunction(description = "Enable server-side GL capabilities.\n"+GL_CAPABILITY_LIST)
    public void glEnable(int capability) {
        if (glRender == null) {
            log.log("glEnable on a null GL10 object");
            return;
        }
        // en - glEnable/glDisable - https://www.khronos.org/registry/OpenGL-Refpages/gl2.1/xhtml/glEnable.xml
        // zh - glEnable/glDisable - http://blog.csdn.net/wangyuchun_799/article/details/7821592
        glRender.glEnable(capability);
    }

    @SimpleFunction(description = "Disable server-side GL capabilities.\n"+GL_CAPABILITY_LIST)
    public void glDisable(int capability) {
        if (glRender == null) {
            log.log("glDisable on a null GL10 object");
            return;
        }
        // en - glEnable/glDisable - https://www.khronos.org/registry/OpenGL-Refpages/gl2.1/xhtml/glEnable.xml
        // zh - glEnable/glDisable - http://blog.csdn.net/wangyuchun_799/article/details/7821592
        glRender.glDisable(capability);
    }

    @SimpleFunction(description = "Matrices operation: reset")
    public void glLoadIdentity() {
        if (glRender == null) {
            log.log("glLoadIdentity on a null GL10 object");
            return;
        }
        glRender.glLoadIdentity();
    }
    @SimpleFunction(description = "Matrices operation: push the current matrix stack")
    public void glPushMatrix() {
        if (glRender == null) {
            log.log("glPushMatrix on a null GL10 object");
            return;
        }
        glRender.glPushMatrix();
    }
    @SimpleFunction(description = "Matrices operation: pop the current matrix stack")
    public void glPopMatrix() {
        if (glRender == null) {
            log.log("glPopMatrix on a null GL10 object");
            return;
        }
        glRender.glPopMatrix();
    }
    @SimpleFunction(description = "Matrices operation: translate (moving)")
    public void glTranslate(float x, float y, float z) {
        if (glRender == null) {
            log.log("glTranslate on a null GL10 object");
            return;
        }
        glRender.glTranslatef(x, y, z);
    }
    @SimpleFunction(description = "Matrices operation: scale")
    public void glScale(float x, float y, float z) {
        if (glRender == null) {
            log.log("glScale on a null GL10 object");
            return;
        }
        glRender.glScalef(x, y, z);
    }
    @SimpleFunction(
        description = "Multiply the current matrix by a rotation matrix. "+
            "Rotate specific angle on axis that is not 0 (right handed coordinate system).\n"+
            "E.g. glRotate(angle=45, x=1, y=0 ,z=0) means that rotate anti-clockwise for 45 degree \n"+
            "THE ANGLE IS IN DRGREE, NOT IN RADIANS")
    public void glRotate(float angle, float x, float y, float z) {
        if (glRender == null) {
            log.log("glRotate on a null GL10 object");
            return;
        }
        // en - glRotate - https://www.khronos.org/registry/OpenGL-Refpages/gl2.1/xhtml/glRotate.xml
        // zh - glRotate - http://www.cnblogs.com/1024Planet/p/5647224.html
        glRender.glRotatef(angle, x, y, z);
    }

    @SimpleFunction
    public void glColor(int color) {
        if (glRender == null) {
            log.log("glColor on a null GL10 object");
            return;
        }
        float[] f = new float[4];
        Util.colorI2F(color, f);
        glRender.glColor4f(f[0], f[1], f[2], f[3]);
    }

    @SimpleFunction
    public void glVertexPointer(YailList pointList){
        if (glRender == null) {
            log.log("glVertexPointer on a null GL10 object");
            return;
        }
        try {
            FloatBuffer fb = BufferUtil.getBufferF(pointList);
            glRender.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
        } catch (IllegalArgumentException e) {
            String msg = "the format of the point list is not excepted: " + e.toString();
            log.log("ERROR: " + msg);
            container.$form().dispatchErrorOccurredEvent(
                this, 
                "glVertexPointer", 
                ErrorMessages.ERROR_EXTENSION_ERROR, 
                0, "GLCanvas", msg);
        }
    }
    @SimpleFunction(description = "This should run after glVertexPointer.\n"+
            "Parameters:\n"+
            "- mode: GL_POINTS, GL_LINES, GL_LINE_LOOP, GL_LINE_STRIP, GL_TRIANGLES, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN.\n"+
            "- start is the indedx of the first that want to draw in the point array (start from 0).\n"+
            "- count is the point that want to draw.")
    public void glDrawArrays(int mode, int start, int count) {
        if (glRender == null) {
            Log("glDrawArrays on a null GL10 object");
            return;
        }
        glRender.glDrawArrays(mode, start, count);
    }

    @SimpleFunction(description = "This should run after glVertexPointer.\n"+
            "Parameters:\n"+
            "- start is the indedx of the first that want to draw in the point array (start from 0).\n"+
            "- count is the point that want to draw.")
    public void glDrawArraysPoints(int start, int count) {
        if (glRender == null) {
            Log("glDrawArraysPoints on a null GL10 object");
            return;
        }
        this.glDrawArrays(GL10.GL_POINTS, start, count);
    }

    @SimpleFunction
    public void glPointSize(float size) {
        if (glRender == null) {
            Log("glPointSize on a null GL10 object");
            return;
        }
        glRender.glPointSize(size);
    }
    


    private class Renderer implements GLSurfaceView.Renderer {

        public Renderer() {
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            gl.glShadeModel(glShadeModel);
            // Depth buffer setup.
            gl.glClearDepthf(1.0f);
            if(glDepthTestEnabled){
                gl.glEnable(GL10.GL_DEPTH_TEST);
                gl.glDepthFunc(glDepthFunc);
            }else{
                gl.glDisable(GL10.GL_DEPTH_TEST);
            }
            // zh - glHint - http://blog.csdn.net/shuaihj/article/details/7230867
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

            CanvasReady();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);

            // zh - glMatrixMode - http://blog.csdn.net/jiangdf/article/details/8460012
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            // zh_CN - about camera - http://fansofjava.iteye.com/blog/1499991
            GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
        
            if (glRenderContinuously) {
                // or it render slower and slower
                log.reset();
            }
            log.log("");
            log.log("onDrawFrame {");
            log.pushOffset();
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glClearColor(bgColorF[0], bgColorF[1], bgColorF[2], bgColorF[3]);

            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, -4);

            glRender = gl;
            Render();
            glRender = null;

            log.popOffset();
            log.log("}");
        }
    }
}