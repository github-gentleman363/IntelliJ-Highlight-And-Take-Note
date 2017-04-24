package highlightAndTakeNote.gutter;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.actions.ActiveAnnotationGutter;
import com.intellij.openapi.vfs.VirtualFile;
import highlightAndTakeNote.model.Note;
import highlightAndTakeNote.NoteManager;
import highlightAndTakeNote.takeNote.TakeNoteDialog;
import highlightAndTakeNote.takeNote.TakeNoteDialogWrapper;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class NoteGutter implements ActiveAnnotationGutter {
    private final Project project;
    private final Editor editor;

    public NoteGutter(Project project, Editor editor) {
        this.project = project;
        this.editor = editor;
    }

    public String getLineText(int line, Editor editor) {
        return " ";
    }

    public String getToolTip(int lineNumber, Editor editor) {
        Note note = this.getNote(lineNumber);
        return note == null ? null : note.getContent();
    }

    public EditorFontType getStyle(int line, Editor editor) {
        return null;
    }

    public ColorKey getColor(int line, Editor editor) {
        return null;
    }

    public Color getBgColor(int lineNumber, Editor editor) {
        Note note = this.getNote(lineNumber);
        return note == null ? null : note.getColor();
    }

    public java.util.List<AnAction> getPopupActions(int line, Editor editor) {
        return new java.util.ArrayList<AnAction>();
    }

    public void gutterClosed() {}

    public void doAction(int lineNum) {
        // FOR NOW:
        //  this can only be available if user has clicked to add a note
        //  so user clicked at a line:
        //      with a note:
        //          show the dialog with the note, and highlight the associated code? (have ViewAddedNoteAction entity to handle this)
        //      without a note: do nothing (for now)
        // System.out.println("Note Gutter Action triggered!");
        NoteManager noteManager = NoteManager.getInstance(this.project);

        Note note = this.getNote(lineNum);

        if (note != null) {
            // TODO handle if code in the editor in the range doesn't match what's in note

            final SelectionModel selectionModel = editor.getSelectionModel();
            highlightNotedLines(note, selectionModel);

            TakeNoteDialogWrapper dialogWrapper = setupTakeNoteDialog(note);
            dialogWrapper.show();

            if (dialogWrapper.isOK()) {
                TakeNoteDialog takeNoteDialog = dialogWrapper.getTakeNoteDialog();
                String newContent = takeNoteDialog.getText();
                Color selectedColor = takeNoteDialog.getSelectedColor();
                String noteId = note.getId();
                noteManager.editNote(noteId, newContent, selectedColor);
                selectionModel.removeSelection();
            } else if (dialogWrapper.isDeleteNoteOnExit()) {
                boolean deleted = noteManager.deleteNote(note.getFilePath(), lineNum);
                if (deleted) {
                    editor.getGutter().closeAllAnnotations();
                    NoteGutter noteGutter = new NoteGutter(project, editor);
                    editor.getGutter().registerTextAnnotation(noteGutter, noteGutter);
                }
            }
        }

    }

    @NotNull
    private TakeNoteDialogWrapper setupTakeNoteDialog(Note note) {
        TakeNoteDialogWrapper dialogWrapper = new TakeNoteDialogWrapper(project, false);
        dialogWrapper.setContent(note.getContent());
        TakeNoteDialog takeNoteDialog = dialogWrapper.getTakeNoteDialog();
        takeNoteDialog.setColor(note.getColor());
        return dialogWrapper;
    }

    private void highlightNotedLines(Note note, SelectionModel selectionModel) {
        int startOffset = note.getStartOffset();
        int endOffset = note.getEndOffset();
        selectionModel.setSelection(startOffset, endOffset);
    }

    public Cursor getCursor(int lineNum) {
        Note note = this.getNote(lineNum);
        return note == null ? null : Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) ;
    }

    private Note getNote(int lineNum) {
        NoteManager noteManager = NoteManager.getInstance(this.project);
        Document document = this.editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        final String filePath = virtualFile.getPath();
        return noteManager.getNote(filePath, lineNum);
    }

}
