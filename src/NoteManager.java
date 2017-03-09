import com.intellij.openapi.editor.VisualPosition;

import java.util.HashMap;

public class NoteManager {

    private static final NoteManager INSTANCE = new NoteManager();

    private NoteManager() {}
    private HashMap<String, Note> filePathToNotes = new HashMap<String, Note>();

    public static NoteManager getInstance() {
        return INSTANCE;
    }

    public Note addNewNote(VisualPosition startPosition, VisualPosition endPosition, String highlightedCode, String content, String filePath) {
        // TODO PERSIST!
        // re-eval return type
        Note currentNote = new Note(startPosition, endPosition, highlightedCode, content, filePath);
        this.filePathToNotes.put(filePath, currentNote);
        return currentNote;
    }
}