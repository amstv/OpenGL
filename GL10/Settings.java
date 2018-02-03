package github.OpenSourceAIX.OpenGL.GL10;

import javax.microedition.khronos.opengles.GL10;

import com.google.appinventor.components.runtime.Component;

public class Settings {

    private Settings() {
    }

    public static final int EXTENSION_VERSION = 1;
    public static final String EXTENSION_ICON = "aiwebres/icon/OpenGL10.png";
    public static final String EXTENSION_DESCRIPTION = "See http://github.com/OpenSourceAIX/OpenGL10/";

    public static final class log {
        public static final String ONE_OFFSET = "  ";
    }

    public static final class canvas {

        public static final String ICON = "aiwebres/icon/GL10Canvas.png";
        
        public static final boolean DEFAULT_AS_CONTENT_VIEW = false;
    
        public static final int DEFAULT_BG_COLOR = Component.COLOR_WHITE;
    
        public static final int DEFAULT_SHADE_MODEL = GL10.GL_SMOOTH;
        
        public static final boolean DEFAULT_DEPTH_TEST_ENABLED = true;
        public static final int DEFAULT_DEPTH_FUNC = GL10.GL_LEQUAL;
        
        public static final boolean DEFAULT_RENDER_CONTINUOUSLY = true;

        public static final boolean DEFAULT_AUTO_RENDER = true;

    }

}