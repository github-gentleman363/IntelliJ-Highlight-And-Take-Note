package highlightAndTakeNote.takeNote;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import highlightAndTakeNote.gutter.NoteGutter;
import highlightAndTakeNote.NoteManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TakeNoteAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        /**
         *
         * 1) getNoteLocation | getNoteContext
         * 2) showTakeNoteDialog
         * 3) addNote
         * [4) open gutter for icon/annotation display - should be moved out eventually]
         *
         */
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        // TODO move this elsewhere + rename
        this.subscribeRegisterNotesOnFileOpened(project);

        final SelectionModel selectionModel = editor.getSelectionModel();

        // TODO for the next two blocks, extract them to a function
        //  such as getNoteLocation / getNoteContext

        // TODO extract to a function
        final int startOffset = selectionModel.getSelectionStart();
        final int endOffset = selectionModel.getSelectionEnd();
        if (selectionModel.getSelectionStartPosition() == null) return;
        final int lineNumber = selectionModel.getSelectionStartPosition().getLine();
        final String code = selectionModel.getSelectedText();

        // TODO extract to a function - getCurrentFilePath or something
        final VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        if (virtualFile == null) return;
        final String filePath = virtualFile.getPath();
        // TODO: use the code below if we want path to be relative to base directory
        // final String filePath = VfsUtil.getRelativeLocation(virtualFile, project.getBaseDir());


        // TODO set position & size
        TakeNoteDialogWrapper dialogWrapper = new TakeNoteDialogWrapper(project, true);
        dialogWrapper.show();

        if (dialogWrapper.isOK()) {
            NoteManager manager = NoteManager.getInstance(project);
            TakeNoteDialog takeNoteDialog = dialogWrapper.getTakeNoteDialog();
            String comment = takeNoteDialog.getText();

            Color color = takeNoteDialog.getSelectedColor();

            manager.addNewNote(startOffset, endOffset, lineNumber, comment, filePath, code, color);

            /* TODO: get rid of text annotation and use icon renderer
            OpenFileDescriptor openFileDescriptor = getOpenFileDescriptor(project, newNote.getFilePath(),
                    newNote.getStartOffset());
            if (openFileDescriptor == null) return;
            final Editor editor1 = FileEditorManagerEx.getInstance(project).
                    openTextEditor(openFileDescriptor, true);
            if (editor1 == null) return;
            MarkupModelEx markup = (MarkupModelEx) editor1.getMarkupModel();
            RangeHighlighter highlighter = markup.addPersistentLineHighlighter(newNote.getLineNumber(), HighlighterLayer.ERROR + 1,
                    null);
            if(highlighter == null) return;
            GutterIconRenderer gutterIconRenderer = new NoteGutterIconRenderer();
            highlighter.setGutterIconRenderer(gutterIconRenderer);
            */

            if (manager.hasAnyNoteInFile(filePath)) {
                editor.getGutter().closeAllAnnotations();
            }
            NoteGutter noteGutter = new NoteGutter(project, editor);
            editor.getGutter().registerTextAnnotation(noteGutter, noteGutter);
        }
    }

    /* TODO: methods needed for icon renderer
    private OpenFileDescriptor getOpenFileDescriptor(Project project, String filePath, int offset) {
        final VirtualFile virtualFile = getVirtualFile(project, filePath);
        if (virtualFile == null) return null;
        return new OpenFileDescriptor(project, virtualFile, offset);
    }

    private VirtualFile getVirtualFile(Project project, String filePath) {
        VirtualFile baseDir = project.getBaseDir();
        System.out.println("here");
        if(baseDir == null)  {return null;}
        System.out.println("there");
        return baseDir.findFileByRelativePath(filePath);
    }
    */

    private void subscribeRegisterNotesOnFileOpened(Project project) {
        MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerAdapter() {
            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                super.fileOpened(source, file);
                if (source.getSelectedTextEditor() == null) return;
                NoteGutter noteGutter = new NoteGutter(project, source.getSelectedTextEditor());
                source.getSelectedTextEditor().getGutter().registerTextAnnotation(noteGutter, noteGutter);
            }
        });
    }
}
