package edu.abhi.test.preferences;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author abhis
 *
 */
public class TestPreferenceDefault {
	
	static {
		try {
			PreferenceLoader.configure();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws IOException, BackingStoreException {
		
		Preferences node = Preferences.userRoot().node(TestPreferenceDefault.class.getSimpleName());
		node.put("test", args[0]);
		node.flush(); // writes to windows registry(Windows OS) and file system (Linux & MAC OS) 
		
		Preferences childNode = node.node("child");
	    childNode.putDouble("ParentKey0", Math.E);
	    childNode.putFloat("ParentKey1", (float) Math.PI);
	    childNode.putLong("ParentKey2", Long.MAX_VALUE);

		
		System.out.println("Node at : " + node.absolutePath());
		
		node.sync();
		System.out.println("Fetched data : " + node.get("test", null));
		
		writeToXML(node); //writing to file explicitly even for Windows OS, not a default behavior.
		
	}

	private static void writeToXML(Preferences prefs) {
		String fileNamePrefix = "System";
	    if (prefs.isUserNode()) {
	        fileNamePrefix = "User";
	    }
	    try {
	        OutputStream osTree = new BufferedOutputStream(
	                new FileOutputStream(fileNamePrefix + "Tree.xml"));
	        prefs.exportSubtree(osTree);
	        osTree.close();

	        OutputStream osNode = new BufferedOutputStream(
	                new FileOutputStream(fileNamePrefix + "Node.xml"));
	        prefs.exportNode(osNode);
	        osNode.close();
	    } catch (IOException | BackingStoreException e) {
	        e.printStackTrace();
	    }
	}
	
}
