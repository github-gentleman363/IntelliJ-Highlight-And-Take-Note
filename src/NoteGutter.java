import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.actions.ActiveAnnotationGutter;

import java.awt.*;

public class NoteGutter implements ActiveAnnotationGutter {
    private final Project project;

    public NoteGutter(Project project) {
        this.project = project;
    }

    public String getLineText(int line, Editor editor) {
        return "JIWOONG";
    }


    public String getToolTip(int line, Editor editor) {
        return "blah";
    }

    public EditorFontType getStyle(int line, Editor editor) {
        return EditorFontType.BOLD;
    }

    public ColorKey getColor(int line, Editor editor) {
        return null;
    }

    public Color getBgColor(int line, Editor editor) {
        return null;
    }

    public java.util.List<AnAction> getPopupActions(int line, Editor editor) {
        return new java.util.ArrayList<AnAction>();
    }

    public void gutterClosed() {
    }

    public void doAction(int lineNum) {
        System.out.println("Note Gutter Action triggered!");
    }

    public Cursor getCursor(int lineNum) {
        return null;
    }

}
