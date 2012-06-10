package com.cboxgames.idonia.backend.listener;

import com.cboxgames.idonia.backend.commons.DefaultValueTable;

import net.contentobjects.jnotify.JNotifyListener;

/**
 * Listener for monitoring any static table changes. When it happens, modified static tables are reloaded.
 * Author: Michael Chang
 */

public class DefaultValueTableWatchListener implements JNotifyListener {

    public void fileRenamed(int wd, String path, String old_name, String newname) {
//    	print("Renamed file " + old_name + " -> " + newname + " in " + path);
    }
    public void fileDeleted(int wd, String path, String name) {
//    	print("Deleted file " + name + " in " + path);
    }
    
    public void fileModified(int wd, String path, String name) {
    	print("Modified file " + name + " in " + path);
    	if (DefaultValueTable.reloadDefaultValueTable(name) == false)
    		print("Unable to reload modified file " + name);
    }
    
    public void fileCreated(int wd, String path, String name) {
//    	print("Created file " + name + " in " + path);
    }
    
    void print(String msg) {
    	System.err.println(msg);
    }
}