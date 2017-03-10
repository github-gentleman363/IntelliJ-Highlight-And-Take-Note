public class Note {

    private static int nextId = 1;

    private int id;
    private int lineNumber;
    private int startOffset;
    private int endOffset;
    private String highlightedCode;
    private String content;
    private String filePath;

    public int getId() {
        return id;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getHighlightedCode() {
        return highlightedCode;
    }

    public Note(int startOffset, int endOffset, int lineNumber, String content, String filePath, String highlightedCode) {
        this.id = Note.nextId++;
        this.lineNumber = lineNumber;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.highlightedCode = highlightedCode;
        this.content = content;
        this.filePath = filePath;
    }


}
