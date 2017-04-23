package highlightAndTakeNote.persistence;

import java.util.ArrayList;
import java.util.List;

public class FilePathWithNotes {

    private String filePath;
    private List<NoteBean> noteBeans = new ArrayList<>();

    public FilePathWithNotes() {
    }

    public FilePathWithNotes(String filePath, List<NoteBean> notes) {
        this.filePath = filePath;
        this.noteBeans = notes;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<NoteBean> getNoteBeans() {
        return noteBeans;
    }

    public void setNoteBeans(List<NoteBean> noteBeans) {
        this.noteBeans = noteBeans;
    }

}
