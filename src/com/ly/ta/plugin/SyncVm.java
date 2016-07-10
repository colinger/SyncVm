package com.ly.ta.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;

import static com.sun.tools.doclint.Entity.prop;

/**
 * @author gxy23996
 * @version $Id: SyncVm.java, v0.1 2016/7/8 gxy23996 Exp $$
 */
public class SyncVm extends AnAction {

    public SyncVm() {
        VirtualFileManager.getInstance().addVirtualFileListener(new VirtualFileAdapter() {
            @Override
            public void contentsChanged(@NotNull VirtualFileEvent event) {
                super.contentsChanged(event);
                if (event.getFileName().endsWith("vm")) {
                    try {
                        Properties prop = new Properties();
                        prop.load(new FileInputStream("config.properties"));
                        //get the property value and print it out
                        String dir = prop.getProperty("dir");
                        if (dir == null || dir.equals("")) {
                            return;
                        }
                        //当前文件
                        copyVm(event.getFile(), dir);
                    } catch (IOException ex) {
                        //ex.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        VirtualFile[] files = event.getData(DataKeys.VIRTUAL_FILE_ARRAY);
        //
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("config.properties"));
            //get the property value and print it out
            String dir = prop.getProperty("dir");
            if (dir == null || dir.equals("")) {
                Messages.showMessageDialog(project, "先去Tools(工具)|TaSyncVMConfig配置一下吧!", "Information", Messages.getInformationIcon());
                return;
            }
            //当前文件
            VirtualFile selectedFile = files[0];
            copyVm(selectedFile, dir, project);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void copyVm(VirtualFile selectedFile, String dir) {
        //
        copyVm(selectedFile, dir, null);
    }

    private void copyVm(VirtualFile selectedFile, String dir, Project project) {
        //
        String filePath = selectedFile.getCanonicalPath();
        File movedFile = new File(filePath);
        int index = filePath.indexOf("webdocs");
        String position = filePath.substring(index + "webdocs".length());
        StringBuffer targetPath = new StringBuffer();
        if (dir.contains("WEB-INF")) {
            index = dir.indexOf("WEB-INF");
            targetPath.append(dir.substring(0, index));
            targetPath.append(position);
        } else {
            targetPath.append(dir);
            targetPath.append(position);
        }
        try {
            File target = new File(targetPath.toString());
            target.createNewFile();
            copyFileUsingChannel(movedFile, target);
        } catch (Exception e) {
            if (project != null) {
                Messages.showMessageDialog(project, "同步出错:" + e.getMessage(), "Information", Messages.getInformationIcon());
            }
        }
    }

    void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            sourceChannel.close();
            destChannel.close();
        }
    }
}
