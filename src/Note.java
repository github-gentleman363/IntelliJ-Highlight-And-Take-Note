import com.intellij.openapi.editor.VisualPosition;

public class Note {

    private static int nextId = 1;

    private int id;
    private int lineNumber;
    private VisualPosition startPosition;
    private VisualPosition endPosition;
    private String highlightedCode;
    private String content;
    private String filePath;

    public int getId() {
        return id;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Note(VisualPosition startPosition, VisualPosition endPosition, String highlightedCode, String content, String filePath) {
        this.id = Note.nextId++;
        this.lineNumber = startPosition.getLine();
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.highlightedCode = highlightedCode;
        this.content = content;
        this.filePath = filePath;
    }


}
