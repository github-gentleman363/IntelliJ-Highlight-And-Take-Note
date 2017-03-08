import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;

/**
 * Created by jiwoong.youn on 3/8/17.
 */
public class TakeNoteAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        NoteGutter noteGutter = new NoteGutter();
        editor.getGutter().registerTextAnnotation(noteGutter);
    }
}
