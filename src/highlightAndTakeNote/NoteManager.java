package highlightAndTakeNote;

import com.intellij.ide.startup.StartupManagerEx;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import highlightAndTakeNote.gutter.NoteGutterManager;
import highlightAndTakeNote.model.Note;
import highlightAndTakeNote.persistence.FilePathWithNotes;
import highlightAndTakeNote.persistence.NoteBean;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoteManager extends AbstractProjectComponent {

    private HashMap<String, List<Note>> filePathToNotes = new HashMap<>();
    private HashMap<String, Note> noteIdToNote = new HashMap<>();

    private final StartupManagerEx startupManager;

    protected NoteManager(Project project, final StartupManager startupManager) {
        super(project);
        this.startupManager = (StartupManagerEx)startupManager;
    }

    public static NoteManager getInstance(@NotNull Project project) {
        return project.getComponent(NoteManager.class);
    }

    public void loadFromState(List<FilePathWithNotes> filePathWithNotesList) {
        final Runnable runnable = new DumbAwareRunnable() {
            public void run() {
                for (FilePathWithNotes filePathWithNotes : filePathWithNotesList) {

                    // populate NoteManager
                    filePathToNotes.put(filePathWithNotes.getFilePath(), new ArrayList<>());
                    List<NoteBean> noteBeans = filePathWithNotes.getNoteBeans();
                    for (NoteBean noteBean : noteBeans) {
                        Note note = new Note(noteBean, myProject, filePathWithNotes.getFilePath());
                        noteIdToNote.put(note.getId(), note);
                        filePathToNotes.get(filePathWithNotes.getFilePath()).add(note);
                        NoteGutterManager.getInstance(myProject).updateNoteGutter(note, false);
                    }
                }
            }
        };

        if (startupManager.startupActivityPassed()) {
            runnable.run();
        } else {
            startupManager.registerPostStartupActivity(runnable);
        }
    }

    public Note addNewNote(Project project, int startOffset, int endOffset, int lineNumber, String content,
                           String filePath, String highlightedCode, Color color) {

        Note currentNote = new Note(project, startOffset, endOffset, lineNumber, content,
                filePath, highlightedCode, color);

        this.noteIdToNote.put(currentNote.getId(), currentNote);
        if (!this.filePathToNotes.containsKey(filePath)) {
            this.filePathToNotes.put(filePath, new ArrayList<Note>());
        }
        this.filePathToNotes.get(filePath).add(currentNote);
        return currentNote;
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

    public Note getNote(String id) {
        if (this.noteIdToNote.containsKey(id)) {
            return this.noteIdToNote.get(id);
        }
        return null;
    }

    public boolean editNote(String id, String content, Color color) {
        Note note = this.getNote(id);
        if (note == null) {
            return false;
        }

        note.setContent(content);
        note.setColor(color);
        return true;
    }

    public HashMap<String, List<Note>> getFilePathToNotes() {
        return filePathToNotes;
    }

    public boolean hasAnyNoteInFile(String filePath) {
        return this.filePathToNotes.containsKey(filePath)
                && this.filePathToNotes.get(filePath) != null
                && this.filePathToNotes.get(filePath).size() > 0;
    }

    public boolean deleteNote(String noteId) {
        Note note = this.getNote(noteId);
        if (note == null) {
            return false;
        }

        String filePath = note.getFilePath();
        Note noteToRemove = null;
        if (this.hasAnyNoteInFile(filePath)) {
            List<Note> listOfNotes = this.getFilePathToNotes().get(filePath);
            for (Note eachNote : listOfNotes) {
                if (eachNote.getId().equals(noteId)) {
                    noteToRemove = eachNote;
                    break;
                }
            }
            if (noteToRemove != null) {
                this.noteIdToNote.remove(noteId);
                this.getFilePathToNotes().get(filePath).remove(noteToRemove);
                return true;
            }
        }
        return false;
    }
}