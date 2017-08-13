package highlightAndTakeNote.util;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class Util extends AbstractProjectComponent implements DumbAware {

    protected Util(Project project) {
        super(project);
    }

    public static Util getInstance(@NotNull Project project) {
        return project.getComponent(Util.class);
    }

    public Document getDocument(String filePath) {
        VirtualFile file = getVirtualFile(filePath);
        if (file == null) {
            return null;
        }
        return FileDocumentManager.getInstance().getDocument(file);
    }

    public VirtualFile getVirtualFile(String relFilePath) {
        VirtualFile baseDir = myProject.getBaseDir();
        if (baseDir == null) {
            return null;
        }
        return baseDir.findFileByRelativePath(relFilePath);
    }

    public OpenFileDescriptor getOpenFileDescriptor(String filePath, int offset) {
        final VirtualFile virtualFile = getVirtualFile(filePath);
        if (virtualFile == null) {
            return null;
        }
        return new OpenFileDescriptor(myProject, virtualFile, offset);
    }
}
