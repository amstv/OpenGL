package github.OpenSourceAIX.OpenGL.GL10.interfaces;

import javax.microedition.khronos.opengles.GL10;

public interface Drawable {

    public void setVisible(boolean visible);
    public boolean getVisible();

    public void draw(GL10 gl);
    
}