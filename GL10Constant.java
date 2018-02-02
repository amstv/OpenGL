package github.OpenSourceAIX.OpenGL10;

import java.lang.reflect.Field;

import javax.microedition.khronos.opengles.GL10;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
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

    @SimpleFunction(description = "Return -1 if field(constant) does not exist")
    public int GetByName(String name) {
        try{
            Field field = GL10.class.getField(name);
            return field.getInt(null);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return -1;
    }

    /**
     * As a sample
     */
    @SimpleProperty
    public int GL_SMOOTH() {
        return GL10.GL_SMOOTH;
    }
    
    /**
     * glEnableClientState & glDisableClientState
     */
    @SimpleProperty
    public int GL_VERTEX_ARRAY() {
        return GL10.GL_VERTEX_ARRAY;
    }
    @SimpleProperty
    public int GL_NORMAL_ARRAY() {
        return GL10.GL_NORMAL_ARRAY;
    }
    @SimpleProperty
    public int GL_COLOR_ARRAY() {
        return GL10.GL_COLOR_ARRAY;
    }
    @SimpleProperty
    public int GL_TEXTURE_COORD_ARRAY() {
        return GL10.GL_TEXTURE_COORD_ARRAY;
    }

    /**
     * glEnable & glDisable
     */
    @SimpleProperty
    public int GL_ALPHA_TEST() {
        return GL10.GL_ALPHA_TEST;
    }
    @SimpleProperty
    public int GL_BLEND() {
        return GL10.GL_BLEND;
    }
    @SimpleProperty
    public int GL_COLOR_LOGIC_OP() {
        return GL10.GL_COLOR_LOGIC_OP;
    }
    @SimpleProperty
    public int GL_COLOR_MATERIAL() {
        return GL10.GL_COLOR_MATERIAL;
    }
    @SimpleProperty
    public int GL_CULL_FACE() {
        return GL10.GL_CULL_FACE;
    }
    @SimpleProperty
    public int GL_DEPTH_TEST() {
        return GL10.GL_DEPTH_TEST;
    }
    @SimpleProperty
    public int GL_DITHER() {
        return GL10.GL_DITHER;
    }
    @SimpleProperty
    public int GL_FOG() {
        return GL10.GL_FOG;
    }
    @SimpleProperty
    public int GL_LIGHTING() {
        return GL10.GL_LIGHTING;
    }
    @SimpleProperty
    public int GL_LINE_SMOOTH() {
        return GL10.GL_LINE_SMOOTH;
    }
    @SimpleProperty
    public int GL_MULTISAMPLE() {
        return GL10.GL_MULTISAMPLE;
    }
    @SimpleProperty
    public int GL_NORMALIZE() {
        return GL10.GL_NORMALIZE;
    }
    @SimpleProperty
    public int GL_POINT_SMOOTH() {
        return GL10.GL_POINT_SMOOTH;
    }
    @SimpleProperty
    public int GL_POLYGON_OFFSET_FILL() {
        return GL10.GL_POLYGON_OFFSET_FILL;
    }
    @SimpleProperty
    public int GL_RESCALE_NORMAL() {
        return GL10.GL_RESCALE_NORMAL;
    }
    @SimpleProperty
    public int GL_SAMPLE_ALPHA_TO_COVERAGE() {
        return GL10.GL_SAMPLE_ALPHA_TO_COVERAGE;
    }
    @SimpleProperty
    public int GL_SAMPLE_ALPHA_TO_ONE() {
        return GL10.GL_SAMPLE_ALPHA_TO_ONE;
    }
    @SimpleProperty
    public int GL_SAMPLE_COVERAGE() {
        return GL10.GL_SAMPLE_COVERAGE;
    }
    @SimpleProperty
    public int GL_SCISSOR_TEST() {
        return GL10.GL_SCISSOR_TEST;
    }
    @SimpleProperty
    public int GL_STENCIL_TEST() {
        return GL10.GL_STENCIL_TEST;
    }
    @SimpleProperty
    public int GL_TEXTURE_2D() {
        return GL10.GL_TEXTURE_2D;
    }
}