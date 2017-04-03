package takenote.persistence;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import takenote.Note;
import takenote.NoteManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@State(
        name = "NotesPersistence",
        storages = {
            @Storage("take-notes.xml")
        }
)
public class NotesPersistentStateComp extends AbstractProjectComponent implements PersistentStateComponent<NotesPersistentState> {

    private NotesPersistentState state = new NotesPersistentState();

    protected NotesPersistentStateComp(Project project) {
        super(project);
    }

    @Override
    public NotesPersistentState getState() {
        this.state.setFilePathWithNotesList(convertFilePathToNotes());
        return this.state;
    }

    @Override
    public void loadState(NotesPersistentState state) {
        this.state = state;
        this.convertFilePathWithNotes();
    }

    private void convertFilePathWithNotes() {
        List<FilePathWithNotes> notes = state.getFilePathWithNotesList();
        Map<String, List<Note>> filePathToNotes = NoteManager.getInstance(myProject).getFilePathToNotes();
        for (FilePathWithNotes filePathWithNotes : notes) {
            filePathToNotes.put(filePathWithNotes.getFilePath(), new ArrayList<>());
            List<NoteBean> noteBeans = filePathWithNotes.getNoteBeans();
            for (NoteBean noteBean : noteBeans) {
                filePathToNotes.get(filePathWithNotes.getFilePath()).add(
                        new Note(noteBean, filePathWithNotes.getFilePath()));
            }
        }
    }

    private List<FilePathWithNotes> convertFilePathToNotes() {
        List<FilePathWithNotes> filePathWithNotesList = new ArrayList<>();
        Map<String, List<Note>> filePathToNotes = NoteManager.getInstance(myProject).getFilePathToNotes();
        Set<String> filePathSet = filePathToNotes.keySet();
        for (String filePath : filePathSet) {
            List<Note> notes = filePathToNotes.get(filePath);
            List<NoteBean> noteBeans = new ArrayList<>();
            for (Note note : notes) {
                noteBeans.add(new NoteBean(note));
            }
            filePathWithNotesList.add(new FilePathWithNotes(filePath, noteBeans));
        }
        return filePathWithNotesList;
    }
}
