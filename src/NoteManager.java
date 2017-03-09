import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoteManager {

    private static final NoteManager INSTANCE = new NoteManager();

    private NoteManager() {}

    private HashMap<String, List<Note>> filePathToNotes = new HashMap<String, List<Note>>();
    private HashMap<Integer, Note> noteIdToNote = new HashMap<Integer, Note>();

    public static NoteManager getInstance() {
        return INSTANCE;
    }

    public Note addNewNote(int startOffset, int endOffset, int lineNumber, String content, String filePath, String highlightedCode) {
        // TODO PERSIST! + re-eval return type
        Note currentNote = new Note(startOffset, endOffset, lineNumber, content, filePath, highlightedCode);
        this.noteIdToNote.put(currentNote.getId(), currentNote);
        if (!this.filePathToNotes.containsKey(filePath)) {
            this.filePathToNotes.put(filePath, new ArrayList<Note>());
        }
        this.filePathToNotes.get(filePath).add(currentNote);
        return currentNote;
    }

    public boolean hasNoteInLine(String filePath, int lineNum) {
        return this.getNote(filePath, lineNum) != null;
    }

    public Note getNote(String filePath, int lineNum) {
        if (this.filePathToNotes.containsKey(filePath)) {
            List<Note> noteList = this.filePathToNotes.get(filePath);
            for (Note eachNote : noteList) {
                if (eachNote.getLineNumber() == lineNum) {
                    return eachNote;
                }
            }
        }
        return null;
    }

    public Note getNote(int id) {
        if (this.noteIdToNote.containsKey(id)) {
            return this.noteIdToNote.get(id);
        }
        return null;
    }

    public Note editNote(int id, String content) {
        Note note = this.getNote(id);
        note.setContent(content);
        // TODO persist!
        return note;
    }

    public HashMap<String, List<Note>> getFilePathToNotes() {
        return filePathToNotes;
    }

    public boolean hasAnyNoteInFile(String filePath) {
        return this.filePathToNotes.containsKey(filePath)
                && this.filePathToNotes.get(filePath) != null
                && this.filePathToNotes.get(filePath).size() > 0;
    }
}