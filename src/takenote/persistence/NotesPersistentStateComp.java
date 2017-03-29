package takenote.persistence;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import takenote.Note;
import takenote.NoteManager;

import java.util.HashMap;
import java.util.List;

@State(
        name = "NotesPersistence",
        storages = {
            @Storage("take-notes.xml")
        }
)
class NotesPersistentComp implements PersistentStateComponent<NotesPersistentComp.State> {
    static class State {
        public HashMap<String, List<Note>> getFilePathToNotes() {
            return filePathToNotes;
        }

        public void setFilePathToNotes(HashMap<String, List<Note>> filePathToNotes) {
            this.filePathToNotes = filePathToNotes;
        }

        public HashMap<String, List<Note>> filePathToNotes;
    }

    private State state = new State();

    public State getState() {
        state.setFilePathToNotes(NoteManager.getInstance().getFilePathToNotes());
        return state;
    }

    public void loadState(State state) {
        this.state = state;
    }

}
