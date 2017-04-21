package takenote;

import com.intellij.ide.startup.StartupManagerEx;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import takenote.persistence.FilePathWithNotes;
import takenote.persistence.NoteBean;

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
                        filePathToNotes.get(filePathWithNotes.getFilePath()).add(
                                new Note(noteBean, filePathWithNotes.getFilePath()));
                    }

                    // register annotations
                    final VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(
                        filePathWithNotes.getFilePath());
                    if (virtualFile == null) {
                        return;
                    }

                    OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(myProject, virtualFile);
                    final Editor editor = FileEditorManagerEx.getInstance(myProject).
                            openTextEditor(openFileDescriptor, false);

                    NoteGutter noteGutter = new NoteGutter(myProject, editor);
                    editor.getGutter().registerTextAnnotation(noteGutter, noteGutter);

                }
            }
        };

        if (startupManager.startupActivityPassed()) {
            runnable.run();
        } else {
            startupManager.registerPostStartupActivity(runnable);
        }
    }

    public Note addNewNote(int startOffset, int endOffset, int lineNumber, String content, String filePath, String highlightedCode, Color color) {
        // TODO PERSIST! + re-eval return type
        Note currentNote = new Note(startOffset, endOffset, lineNumber, content, filePath, highlightedCode, color);

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

    public Note getNote(String id) {
        if (this.noteIdToNote.containsKey(id)) {
            return this.noteIdToNote.get(id);
        }
        return null;
    }

    public Note editNote(String id, String content, Color color) {
        Note note = this.getNote(id);
        note.setContent(content);
        note.setColor(color);
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

    public boolean deleteNote(String filePath, int lineNum) {
        Note noteToRemove = null;
        if (this.hasAnyNoteInFile(filePath)) {
            List<Note> listOfNotes = this.getFilePathToNotes().get(filePath);
            for (Note note : listOfNotes) {
                if (note.getLineNumber() == lineNum) {
                    noteToRemove = note;
                    break;
                }
            }
            if (noteToRemove != null) {
                this.getFilePathToNotes().get(filePath).remove(noteToRemove);
            }
        }
        return false;
    }
}