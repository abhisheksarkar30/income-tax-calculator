/**
 * 
 */
package edu.abhi.util.prefs.file.property;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author abhis
 *
 */
public class TestPreferencePropertyFile {

	public static void main(String[] args) throws BackingStoreException
	  {
	    System.setProperty("java.util.prefs.PreferencesFactory", PropertyFilePreferencesFactory.class.getName());
	 
	    Preferences prefs = Preferences.userNodeForPackage(PropertyFilePreferencesFactory.class);
	 
	    for (String s : prefs.keys()) {
	      System.out.println("p[" + s + "]=" + prefs.get(s, null));
	    }
	 
	    prefs.putBoolean("hi", true);
	    prefs.put("Number", String.valueOf(System.currentTimeMillis()));
	    
	  }
	
}
