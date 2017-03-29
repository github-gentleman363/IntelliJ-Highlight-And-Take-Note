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
    }

    private List<FilePathWithNotes> convertFilePathToNotes() {
        List<FilePathWithNotes> filePathWithNotesList = new ArrayList<>();
        Map<String, List<Note>> filePathToNotes = NoteManager.getInstance(myProject).getFilePathToNotes();
        Set<String> filePathSet = filePathToNotes.keySet();
        for (String filePath : filePathSet) {
            filePathWithNotesList.add(new FilePathWithNotes(filePath, filePathToNotes.get(filePath)));
        }
        return filePathWithNotesList;
    }
}
