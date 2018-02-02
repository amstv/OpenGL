package github.OpenSourceAIX.OpenGL10;

import javax.microedition.khronos.opengles.GL10;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(version = Settings.EXTENSION_VERSION,
    description = "Constants for OpenGL ES 1.0",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = Settings.EXTENSION_ICON)
@SimpleObject(external = true)
public final class GL10Constant extends AndroidNonvisibleComponent implements Component {

    public GL10Constant(ComponentContainer container) {
        super(container.$form());
    }

    /**
     * As a sample
     */
    @SimpleProperty
    public int GL_SMOOTH() {
        return GL10.GL_SMOOTH;
    }
}