package takenote;

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

    public String getToolTip(int line, Editor editor) {
        if (this.hasNote(line)) {
            NoteManager noteManager = NoteManager.getInstance(this.project);
            Document document = this.editor.getDocument();
            VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
            final String filePath = virtualFile.getPath();

            Note note = noteManager.getNote(filePath, line);
            return note.getContent();
        }
        else return null;
    }

    public EditorFontType getStyle(int line, Editor editor) {
        return EditorFontType.BOLD;
    }

    public ColorKey getColor(int line, Editor editor) {
        return null;
    }

    public Color getBgColor(int line, Editor editor) {
        if (this.hasNote(line)) {
            NoteManager noteManager = NoteManager.getInstance(this.project);
            Document document = this.editor.getDocument();
            VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
            final String filePath = virtualFile.getPath();

            Note note = noteManager.getNote(filePath, line);
            return note.getColor();
        }
        else return null;
    }

    public java.util.List<AnAction> getPopupActions(int line, Editor editor) {
        return new java.util.ArrayList<AnAction>();
    }

    public void gutterClosed() {
    }

    public void doAction(int lineNum) {
        // FOR NOW:
        //  this can only be available if user has clicked to add a note
        //  so user clicked at a line:
        //      with a note:
        //          show the dialog with the note, and highlight the associated code? (have ViewAddedNoteAction entity to handle this)
        //      without a note: do nothing (for now)
        // System.out.println("Note Gutter Action triggered!");
        NoteManager noteManager = NoteManager.getInstance(this.project);

        // TODO refactor
        Document document = this.editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        final String filePath = virtualFile.getPath();

        Note note = noteManager.getNote(filePath, lineNum);
        if (note != null) {
            //  TODO handle if code in the editor in the range doesn't match what's in note
            int startOffset = note.getStartOffset();
            int endOffset = note.getEndOffset();
            final SelectionModel selectionModel = editor.getSelectionModel();
            selectionModel.setSelection(startOffset, endOffset);

            // set up dialog with note info + show
            TakeNoteDialogWrapper dialogWrapper = new TakeNoteDialogWrapper(project, false);
            dialogWrapper.setContent(note.getContent());
            TakeNoteDialog takeNoteDialog = dialogWrapper.getTakeNoteDialog();
            takeNoteDialog.setColor(note.getColor());

            dialogWrapper.show();

            if (dialogWrapper.isOK()) {
                String newContent = takeNoteDialog.getText();
                Color selectedColor = takeNoteDialog.getSelectedColor();
                String noteId = note.getId();
                noteManager.editNote(noteId, newContent, selectedColor);
                selectionModel.removeSelection();
            } else if (dialogWrapper.isDeleteNoteOnExit()) {
                boolean deleted = noteManager.deleteNote(filePath, lineNum);
                if (deleted) {
                    editor.getGutter().closeAllAnnotations();
                    NoteGutter noteGutter = new NoteGutter(project, editor);
                    editor.getGutter().registerTextAnnotation(noteGutter, noteGutter);
                }
            }
        }

    }


    // TODO
    // hasNote isn't that useful since we need the Note object in DoAction
    // try replacing it with 'getNote' instead?

    public Cursor getCursor(int lineNum) {
        return this.hasNote(lineNum) ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : null;
    }

    private boolean hasNote(int lineNum) {
        NoteManager noteManager = NoteManager.getInstance(this.project);
        Document document = this.editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        final String filePath = virtualFile.getPath();
        return noteManager.hasNoteInLine(filePath, lineNum);
    }

}
