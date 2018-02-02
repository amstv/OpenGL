package github.OpenSourceAIX.OpenGL.GL10.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.google.appinventor.components.runtime.util.YailList;

public class BufferUtil {

    private BufferUtil() {
    }


    /**
     * Append a ByteBuffer to the old ByteBuffer
     * 
     * @param old     the old ByteBuffer
     * @param append  the ByteBuffer that will be append
     * 
     * @return a new ByteBuffer with position at 0
     */
    public static ByteBuffer appendB(ByteBuffer old, ByteBuffer append) {
        if (old==null || append==null) {
            throw new IllegalArgumentException("Null argument found");
        }
        ByteBuffer bb = allocateB(old.limit() + append.limit());
        bb.put(old.array());
        bb.put(append.array());
        bb.position(0);
        return bb;
    }

    /**
     * Append a FloatBuffer to the old FloatBuffer
     * 
     * @param old     the old FloatBuffer
     * @param append  the FloatBuffer that will be append
     * 
     * @return a new FloatBuffer with position at 0
     */
    public static FloatBuffer appendF(FloatBuffer old, FloatBuffer append) {
        if (old==null || append==null) {
            throw new IllegalArgumentException("Null argument found");
        }
        FloatBuffer bb = allocateF(old.limit() + append.limit());
        bb.put(old.array());
        bb.put(append.array());
        bb.position(0);
        return bb;
    }



    /**
     * @return a ByteBuffer in native order
     */
    public static ByteBuffer allocateB(int capacity) {
        // IMPORTANT: allocateDirect and nativeOrder
        ByteBuffer bb = ByteBuffer.allocateDirect(capacity);
        bb.order(ByteOrder.nativeOrder());
        return bb;
    }

    /**
     * @return a FloatBuffer in native order
     */
    public static FloatBuffer allocateF(int capacity) {
        return allocateB(capacity*4).asFloatBuffer();
    }

    /**
     * @return a FloatBuffer in native order
     */
    public static ShortBuffer allocateS(int capacity) {
        return allocateB(capacity*2).asShortBuffer();
    }



    /**
     * @param list format like this: [[0,0,0], [0,0,1], ...]
     */
    public static FloatBuffer getBufferF(YailList list) throws IllegalArgumentException {
        int size = list.size();
        FloatBuffer fb = allocateF(size*3);
        Object item;
        YailList sublist;
        for (int i=0; i<size; i++) {
            item = list.getObject(i);
            if (!(item instanceof YailList)) {
                throw new IllegalArgumentException("item "+i+" is not a list");
            }
            sublist=(YailList)item;
            if (sublist.size()<3) {
                throw new IllegalArgumentException("item "+i+" contains less than three sub-items");
            }
            int j=0;
            try {
                for (j=0; j<3; j++) {
                    fb.put(i*3+j, Float.parseFloat(sublist.getString(j)));
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("item "+i+" contains a non-number sub-items (the sub-item "+(j+1)+")"); // j start with 0
            }
        }
        return fb;
    }



    public static String toString(FloatBuffer fb) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i=0; i<fb.limit()-1; i++) {
            sb.append(fb.get(i));
            sb.append(", ");
        }
        sb.append(fb.get(fb.limit()-1));
        sb.append(")");
        return sb.toString();
    }

}