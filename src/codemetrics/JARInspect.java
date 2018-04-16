package codemetrics;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

public class JARInspect {
	/*
		1- Break it first, break all the time
		2- Read the files first
		3- Print it to know whats inside
		4- Reucrsive if its .jar
		5- Read the content (Open stream)
		6- Analzye -> think more about it do not it immediately





	 */

	public static void main(String[] args) throws IOException {
			
		//String fileName = "heap.jar";
		//importJar(fileName);

		ArrayList<String> files;
		for(int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
			files.add(args[i]);
		}

		for (int i = 0; i < files.size(); i++) {
			//importJar(files.get(i));
		}
	}
	
	public static void importJar(String fileName) throws IOException {
		
		//JarFile jf = new JarFile(new File(fileName));

		File file = new File(fileName.toString());
		JarFile jf = new JarFile(file);
		System.out.println(jf.toString());

//		int NrOfMethodsTotal = 0;
		//ArrayList<String> classNames = new ArrayList<>();
		
		//String fileDirectory = System.getProperty("user.dir");
		//URL JARLocation = new URL("file://" + fileDirectory + "/" + fileName);
		//System.out.println(JARLocation);
		//URLClassLoader urlCL = new URLClassLoader(
		//							new URL[] { JARLocation }
		//							);
		
		// go through all classes and gather data for metrics 
		// iterate over all classes
				//getClasses(jf, classNames);
		// calculate cumulative metrics for jar
		
		//urlCL.close();
	}
	
	private static void getClasses(JarFile jf, Arraylist<String> classNames) throws IOException {
		Enumeration<JarEntry> classes = jf.entries();
		JarEntry elem;
		while (classes.hasMoreElements()) {
			elem = classes.nextElement();
			if (elem.getName().endsWith(".class")) {
				String className = elem.getName();
				className.replace(".class", "");
				className.replace("\\", ".");   // from windows to javapath
				classNames.add(className);
				System.out.println(className);
			}
		}
		jf.close();
	}

}
