package takenote.persistence;

import takenote.Note;

import java.awt.*;

public class NoteBean {

    private int id;
    private int lineNumber;
    private int startOffset;
    private int endOffset;
    private String highlightedCode;
    private String content;
    private String color;

    public NoteBean() {
    }

    public NoteBean(Note note) {
        this.id = note.getId();
        this.lineNumber = note.getLineNumber();
        this.startOffset = note.getStartOffset();
        this.endOffset = note.getEndOffset();
        this.highlightedCode = note.getHighlightedCode();
        this.content = note.getContent();
        this.mapColor(note.getColor());
    }

    private void mapColor(Color color) {
        if (color == Color.RED) {
            this.color = "RED";
        } else if (color == Color.BLUE) {
            this.color = "BLUE";
        } else if (color == Color.GREEN) {
            this.color = "GREEN";
        } else {
            this.color = "YELLOW";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public String getHighlightedCode() {
        return highlightedCode;
    }

    public void setHighlightedCode(String highlightedCode) {
        this.highlightedCode = highlightedCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
