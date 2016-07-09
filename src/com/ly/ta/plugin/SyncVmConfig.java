package com.ly.ta.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author gxy23996
 * @version $Id: SyncVm.java, v0.1 2016/7/8 gxy23996 Exp $$
 */
public class SyncVmConfig extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
//        VirtualFile[] files = event.getData(DataKeys.VIRTUAL_FILE_ARRAY);
        //
        TaVisualPanel myEditor = new TaVisualPanel(false);
        myEditor.getPeer().setTitle("Tomcat信息设置");
        myEditor.show();
    }
}
