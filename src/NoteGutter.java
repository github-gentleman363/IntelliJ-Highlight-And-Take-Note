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
import com.intellij.ui.JBColor;

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
        String showComment;
        Document document = this.editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        final String filePath = virtualFile.getPath();

        NoteManager noteManager = NoteManager.getInstance();
        Note note = noteManager.getNote(filePath, line);
        showComment = note.getContent();
        return showComment;
    }

    public EditorFontType getStyle(int line, Editor editor) {
        return EditorFontType.BOLD;
    }

    public ColorKey getColor(int line, Editor editor) {
        return null;
    }

    public Color getBgColor(int line, Editor editor) {
        if (this.hasNote(line))
            return JBColor.RED;
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
        NoteManager noteManager = NoteManager.getInstance();

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
            dialogWrapper.show();

            if (dialogWrapper.isOK()) {
                String newContent = dialogWrapper.getTakeNoteDialog().getText();
                int noteId = note.getId();
                noteManager.editNote(noteId, newContent);
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
        NoteManager noteManager = NoteManager.getInstance();
        Document document = this.editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        final String filePath = virtualFile.getPath();
        return noteManager.hasNoteInLine(filePath, lineNum);
    }

}
