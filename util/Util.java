package github.OpenSourceAIX.OpenGL10.util;

public final class Util {

    private Util() {
    }

    public static int colorF2I(float[] f){
        int A=(int)f[3]&0xFF,
            R=(int)f[0]&0xFF,
            G=(int)f[1]&0xFF,
            B=(int)f[2]&0xFF;
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 16 | (B & 0xff);
    }
    public static void colorI2F(int color, float[] f){
        int A=(color>>24)&0xff, 
            R=(color>>16)&0xff,
            G=(color>> 8)&0xff,
            B=(color    )&0xff;
        f[0]=(float)R/0xFF;
        f[1]=(float)G/0xFF;
        f[2]=(float)B/0xFF;
        f[3]=(float)A/0xFF;
    }

    public static String getFullTrackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

}