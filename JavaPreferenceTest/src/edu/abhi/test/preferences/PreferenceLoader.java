package edu.abhi.test.preferences;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author abhis
 *
 */
public class PreferenceLoader {

	public static void configure() throws IOException {

    	if(!System.getProperty("os.name").startsWith("Windows")){
    		final FileReader PREF_LOCATION_CONFIG = new FileReader("preferences-location-config.properties");
    		final Properties prop = new Properties();
    		prop.load(PREF_LOCATION_CONFIG);

    		final String userPrefsDirName = prop.getProperty("userRoot");
    		if(!userPrefsDirName.equals("")){
    			System.setProperty("java.util.prefs.userRoot", userPrefsDirName);
    			createDirectoryAndSetRWX(userPrefsDirName, ".java/.userPrefs", System.getProperty("user.home"));
    		}

    		final String systemPrefsDirName = prop.getProperty("systemRoot");
    		if(!systemPrefsDirName.equals("")){
    			System.setProperty("java.util.prefs.systemRoot", systemPrefsDirName);
    			createDirectoryAndSetRWX(systemPrefsDirName, ".systemPrefs", "/etc/.java");
    		}
    		
    		System.out.println("Preferred path loaded.");
    	}
		
	}

	/**
	 *
	 * @param parent
	 * @param child
	 * @param defaultParent
	 */
	private static void createDirectoryAndSetRWX(final String parent, final String child,  final String defaultParent){
		final File rootDir = new File(parent, child);
		if(!rootDir.exists()){
			if (rootDir.mkdirs()) {
				System.out.println(String.format("Created preferences directory in %s", rootDir.toString()));
			}else{
				System.out.println(String.format("Could not create preferences directory in %s, preferences are unusable.", rootDir.toString()));
			}
		}

		//Try to grant R-W-X if not set
		if(rootDir.exists()){
				final File defaultRootDir = new File(defaultParent, child);
				System.out.println(String.format("default root=\"%s\" is changed to:\"%s\"   ", defaultRootDir.getAbsolutePath(), rootDir.getAbsolutePath()));

			if(!rootDir.canRead() && !rootDir.setReadable(true)){
				System.out.println(" Read access could not be granted");
			}
			if(!rootDir.canWrite() && !rootDir.setWritable(true)){
				System.out.println(" Write access could not be granted");
			}
			if(!rootDir.canExecute() && !rootDir.setExecutable(true)){
				System.out.println(" Execution access could not be granted");
			}
		}
   }
	
}
