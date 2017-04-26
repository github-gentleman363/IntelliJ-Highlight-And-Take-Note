package highlightAndTakeNote.gutter;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import highlightAndTakeNote.model.Note;

import java.util.HashMap;
import java.util.Map;

public class NoteGutterManager extends AbstractProjectComponent implements DumbAware {
    private final Map<Note, NoteGutter> noteToNoteGutter = new HashMap<>();

    public NoteGutterManager(Project project) {
        super(project);
    }

    public static NoteGutterManager getInstance(Project project) {
        return project.getComponent(NoteGutterManager.class);
    }

    public void updateNoteGutter(Note note, boolean isToDelete) {
        NoteGutter noteGutter = this.noteToNoteGutter.get(note);
        if (noteGutter == null) {
            noteGutter = new NoteGutter(note);
            this.noteToNoteGutter.put(note, noteGutter);
        } else {
            if (isToDelete) {
                this.noteToNoteGutter.remove(note);
                noteGutter.getHighlighter().dispose();
                return;
            }
        }
        noteGutter.update();
    }
}
