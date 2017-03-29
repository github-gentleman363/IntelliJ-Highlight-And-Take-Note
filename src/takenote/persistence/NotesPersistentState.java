package takenote.persistence;

import java.util.ArrayList;
import java.util.List;

public class NotesPersistentState {
    private List<FilePathWithNotes> filePathWithNotesList = new ArrayList<>();

    public List<FilePathWithNotes> getFilePathWithNotesList() {
        return filePathWithNotesList;
    }

    public void setFilePathWithNotesList(List<FilePathWithNotes> filePathWithNotesList) {
        this.filePathWithNotesList = filePathWithNotesList;
    }
}
