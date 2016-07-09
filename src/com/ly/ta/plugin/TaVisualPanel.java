/**
 * LY.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ly.ta.plugin;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author gxy23996
 * @version $Id: TaVisualPanel.java, v0.1 2016/07/09 11:03 PM gxy23996 Exp $
 */
public class TaVisualPanel extends DialogWrapper {
    private JTabbedPane viewFileTab;
    private JButton buttonBrowse;
    private JTextField directoryName;
    private JFileChooser fileChooser = new JFileChooser("");
    private VirtualFile selectedVFile;
    // Set maximum allowed number of lines in a text file to edit.

    private JPanel myVisualUI;
    private String initialFileContent = null;


    public TaVisualPanel(boolean canBeParent) {
        super(canBeParent);
        init();
        //先加载之前保存的文件夹路径
        Properties prop = new Properties();
        try {
            //load a properties file
            prop.load(new FileInputStream("config.properties"));
            //get the property value and print it out
            String dir = prop.getProperty("dir");
            if(dir != null && !dir.equals("")){
                directoryName.setText(dir);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // The Browse button listener.
        buttonBrowse.addActionListener(e -> {
            if (OpenFile()) {
                directoryName.setText(selectedVFile.getUrl());
                //
            }
        });

    }

    // Display the Open dialog and open the selected file.
    public boolean OpenFile() {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //... Open a file dialog.
        int retval = fileChooser.showOpenDialog(null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            // Get virtual file
            selectedVFile = LocalFileSystem.getInstance().findFileByIoFile(fileChooser.getSelectedFile());
            //TODO 将目前文件保存起来
            Properties prop1 = new Properties();
            try {
                //set the properties value
                prop1.setProperty("dir", selectedVFile.getUrl());
                //save properties to project root folder
                prop1.store(new FileOutputStream("config.properties"), null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return true;
        }
        return false;
    }


    public JComponent createCenterPanel() {

        return (JComponent) myVisualUI;

    }
    // The OK button handler.
    protected void doOKAction() {
        if (initialFileContent == null) {
            this.close(0);
            return;
        }

        this.close(0);
        this.dispose();


    }
}
