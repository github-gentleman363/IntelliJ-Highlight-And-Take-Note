package takenote;

import takenote.persistence.NoteBean;

import java.awt.*;

public class Note {

    private static int nextId = 1;

    private int id;
    private int lineNumber;
    private int startOffset;
    private int endOffset;
    private String highlightedCode;
    private String content;
    private String filePath;
    private Color color;

    public Note() {
    }

    public Note(NoteBean noteBean, String filePath) {
        this.id = noteBean.getId();
        this.lineNumber = noteBean.getLineNumber();
        this.startOffset = noteBean.getStartOffset();
        this.endOffset = noteBean.getEndOffset();
        this.highlightedCode = noteBean.getHighlightedCode();
        this.content = noteBean.getContent();
        this.filePath = filePath;
        this.mapColor(noteBean.getColor());
    }

    private void mapColor(String color) {
        switch (color) {
            case "RED":
                this.color = Color.RED;
                break;
            case "BLUE":
                this.color = Color.BLUE;
                break;
            case "GREEN":
                this.color = Color.GREEN;
                break;
            case "YELLOW":
                this.color = Color.YELLOW;
        }
    }

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Note(int startOffset, int endOffset, int lineNumber, String content, String filePath, String highlightedCode, Color color) {
        this.id = Note.nextId++;
        this.lineNumber = lineNumber;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.highlightedCode = highlightedCode;
        this.content = content;
        this.filePath = filePath;
        this.color = color;
    }
}
