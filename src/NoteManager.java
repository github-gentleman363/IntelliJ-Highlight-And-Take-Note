import com.intellij.openapi.editor.VisualPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoteManager {

    private static final NoteManager INSTANCE = new NoteManager();

    private NoteManager() {}
    private HashMap<String, List<Note>> filePathToNotes = new HashMap<String, List<Note>>();

    public static NoteManager getInstance() {
        return INSTANCE;
    }

    public Note addNewNote(VisualPosition startPosition, VisualPosition endPosition, String highlightedCode, String content, String filePath) {
        // TODO PERSIST!
        // re-eval return type
        Note currentNote = new Note(startPosition, endPosition, highlightedCode, content, filePath);
        if (!this.filePathToNotes.containsKey(filePath)) {
            this.filePathToNotes.put(filePath, new ArrayList<Note>());
        }
        this.filePathToNotes.get(filePath).add(currentNote);
        return currentNote;
    }

    public boolean hasNoteInLine(String filePath, int lineNum) {
        if (this.filePathToNotes.containsKey(filePath)) {
            List<Note> noteList = this.filePathToNotes.get(filePath);
            for (Note eachNote : noteList) {
                if (eachNote.getLineNumber() == lineNum) {
                    return true;
                }
            }
        }
        return false;
    }
}