package takenote.persistence;

import com.intellij.openapi.components.PersistentStateComponent;
import takenote.Note;

import java.util.HashMap;
import java.util.List;

class NotesPersistentComp implements PersistentStateComponent<NotesPersistentComp.State> {
    static class State {
        public HashMap<String, List<Note>> getFilePathToNotes() {
            return filePathToNotes;
        }

        public void setFilePathToNotes(HashMap<String, List<Note>> filePathToNotes) {
            this.filePathToNotes = filePathToNotes;
        }

        private HashMap<String, List<Note>> filePathToNotes;
    }

    private State myState;

    public State getState() {
        return myState;
    }

    public void loadState(State state) {
        myState = state;
    }

}
