package github.OpenSourceAIX.OpenGL.GL10.interfaces;

import javax.microedition.khronos.opengles.GL10;

import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.runtime.Component;

@SimpleObject(external = true)
public interface Drawable extends Component {

    public void draw(GL10 gl);
    
}