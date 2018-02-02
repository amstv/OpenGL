package github.OpenSourceAIX.OpenGL10;

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
        
        public static final boolean DEFAULT_RENDER_CONTINUOUSLY = true;

    }

}