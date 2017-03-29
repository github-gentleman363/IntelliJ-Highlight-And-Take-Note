package takenote.persistence;

import takenote.Note;

import java.util.List;

public class FilePathWithNotes {

    private String filePath;
    private List<Note> notes;

    public FilePathWithNotes(String filePath, List<Note> notes) {
        this.filePath = filePath;
        this.notes = notes;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
