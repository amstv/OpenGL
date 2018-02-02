package github.OpenSourceAIX.OpenGL.GL10;

public final class Log {

    private StringBuilder sb;
    private String offset;

    public Log() {
        sb = new StringBuilder();
        offset = "";
    }

    private void prepareNextLine() {
        if (sb.length() != 0) {
            sb.append('\n').append(offset);
        }
    }

    public void log(String message) {
        prepareNextLine();
        sb.append(message);
    }

    public void reset() {
        clear();
    }
    public void clear() {
        if (sb.length() != 0 || sb == null) {
            sb = new StringBuilder();
        }
    }

    public void pushOffset() {
        offset += Settings.log.ONE_OFFSET;
    }
    public void popOffset() {
        if (offset.length() < Settings.log.ONE_OFFSET.length()) {
            offset = "";
        } else {
            offset = offset.substring(offset.length()-Settings.log.ONE_OFFSET.length());
        }
        offset += "  ";
    }

    public String get() {
        return sb.toString();
    }
    
}