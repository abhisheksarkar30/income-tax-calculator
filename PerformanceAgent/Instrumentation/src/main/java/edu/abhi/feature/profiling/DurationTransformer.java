package edu.abhi.feature.profiling;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class DurationTransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		byte[] byteCode = classfileBuffer;

		if (!InstrumentationAgent.filterClasses(className.replaceAll("/", "\\."))) { // replace . with /
			return byteCode;
		}

		System.out.println("[Agent] Transforming target class: " + className);
		try {
			ClassPool classPool = ClassPool.getDefault();
			CtClass ctClass = classPool.get(className.replaceAll("/", "\\."));
			CtMethod[] methods = ctClass.getDeclaredMethods();
			for (CtMethod method : methods) {
				method.addLocalVariable("startTime", CtClass.longType);
				method.insertBefore("startTime = System.currentTimeMillis();");

				StringBuilder endBlock = new StringBuilder();

				method.addLocalVariable("endTime", CtClass.longType);
				method.addLocalVariable("opTime", CtClass.longType);
				endBlock.append("endTime = System.currentTimeMillis();");
				endBlock.append("opTime = endTime-startTime;");

				endBlock.append(
						"System.out.println(\"" + method.getLongName() +":\" + opTime + \" milliseconds!\");");

				method.insertAfter(endBlock.toString());
			}

			byteCode = ctClass.toBytecode();
			ctClass.detach();
		} catch (NotFoundException | CannotCompileException | IOException e) {
			System.out.println("Exception" + e);
		}
		return byteCode;
	}
}