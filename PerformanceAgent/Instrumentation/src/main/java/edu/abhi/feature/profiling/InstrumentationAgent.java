package edu.abhi.feature.profiling;

import java.io.FileReader;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Properties;

public class InstrumentationAgent {

	private static String partialClassName = null;

	static {
		Properties props = new Properties();
		try {
			props.load(new FileReader("config.properties"));
		} catch (IOException e) {
			System.out.println("Unable to load config.properties \n" + e);
		}
		partialClassName = props.getProperty("partialClassName");
	}

	public static void premain(String agentArgs, Instrumentation inst) throws Exception {
		System.out.println("[Agent] In premain method");

		if (partialClassName == null) {
			throw new Exception("No class name found.");
		}

		inst.addTransformer(new DurationTransformer());
	}

	public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
		System.out.println("[Agent] In agentmain method");

		if (partialClassName == null) {
			throw new Exception("No class name found.");
		}

		transformClass(inst);
	}

	private static void transformClass(Instrumentation instrumentation) {
		Class<?> targetCls = null;
		ClassLoader targetClassLoader = null;
		// see if we can get the class using forName
		try {
			targetCls = Class.forName(partialClassName);
			targetClassLoader = targetCls.getClassLoader();
			transform(targetCls, targetClassLoader, instrumentation);
			return;
		} catch (Exception ex) {
			System.out.println("Class [{}] not found with Class.forName");
		}
		// otherwise iterate all loaded classes and find what we want
		for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
			// System.out.println(clazz.getName() + " " + className);
			if (filterClasses(clazz.getName())) {
				targetCls = clazz;
				targetClassLoader = targetCls.getClassLoader();
				transform(targetCls, targetClassLoader, instrumentation);
				return;
			}
		}
		throw new RuntimeException("Failed to find class [" + partialClassName + "]");
	}

	private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
		DurationTransformer dt = new DurationTransformer();
		instrumentation.addTransformer(dt, true);
		try {
			instrumentation.retransformClasses(clazz);
		} catch (Exception ex) {
			throw new RuntimeException("Transform failed for class: [" + clazz.getName() + "]", ex);
		}
	}

	public static boolean filterClasses(String className) {

		return className.contains(partialClassName) && !className.contains("profiling");
	}

}