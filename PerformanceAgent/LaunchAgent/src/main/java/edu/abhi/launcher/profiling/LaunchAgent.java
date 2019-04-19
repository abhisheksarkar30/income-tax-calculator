package edu.abhi.launcher.profiling;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * Created by abhishek sarkar
 */
public class LaunchAgent {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, MalformedURLException, ClassNotFoundException {

		if (args.length == 0) {
			System.out.println("Target Application name not provided!");
			return;
		}

		File agentFile = new File("agent.jar");
		if (!agentFile.exists()) {
			System.out.println("Agent jar file not found!");
		}

		String javaHome = System.getProperty("java.home");
		String toolsJarURL = "file:" + javaHome + "/../lib/tools.jar";

		// Make addURL public
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		method.setAccessible(true);

		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		if (sysloader.getResourceAsStream("/com/sun/tools/attach/VirtualMachine.class") == null) {
			method.invoke(sysloader, (Object) new URL(toolsJarURL));
			Thread.currentThread().getContextClassLoader().loadClass("com.sun.tools.attach.VirtualMachine");
			Thread.currentThread().getContextClassLoader()
					.loadClass("com.sun.tools.attach.AttachNotSupportedException");
		}

		// iterate all jvms and get the first one that matches our application name
		String targetPid = null;
		for (VirtualMachineDescriptor process : VirtualMachine.list()) {

			String processName = process.displayName();
			System.out.println("jvm:{" + process.id() + "} : " + processName);

			if (processName.contains(args[0]) && !processName.contains("LaunchAgent")) {
				targetPid = process.id();
				try {
					System.out.println("Attaching to target JVM with PID: " + targetPid);
					VirtualMachine jvm = VirtualMachine.attach(targetPid);
					jvm.loadAgent(agentFile.getAbsolutePath());
					jvm.detach();
					System.out.println("Attached to target JVM and loaded Java agent successfully");
					break;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		if (targetPid == null) {
			System.out.println("Target Application not found");
			return;
		}
	}
}