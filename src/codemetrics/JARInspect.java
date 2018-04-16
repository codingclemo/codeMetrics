package codemetrics;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
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

		// get filenames from arguments list
		ArrayList<String> files = new ArrayList<>();
		for(int i = 0; i < args.length; i++) {
			files.add(args[i]);
		}
		
		// go through filenames and analyse them
		for (int i = 0; i < files.size(); i++) {
			System.out.println(i);
			checkJar(files.get(i));
		}
	}
	
	public static void checkJar(String fileName) throws IOException {
		
		File file = new File(fileName.toString());
		if (!file.exists()) {
			System.out.println("File not found: " + fileName + "\n" + file.getAbsolutePath());
		}
		
		JarFile jf = new JarFile(file);
		System.out.println("Java archive: " + jf.getName()); // get the name of the jar file
		
		Collection<Class<?>> classes = new ArrayList<Class<?>>();
		ArrayList<String> entryNames = new ArrayList<>();
		Enumeration<JarEntry> jfEntries = jf.entries();
		
		while (jfEntries.hasMoreElements()) {
			
			JarEntry j = jfEntries.nextElement();
			
			// if there is a class, scan it
			if (j.getName().endsWith(".class")) {
				
				String newClassName = j.getName().replace(".class","");
				entryNames.add(newClassName);
				newClassName = newClassName.replace("/", ".");
				System.out.println(newClassName);
				
				// generate URL for accessing the each class
				String url = "file://" + System.getProperty("user.dir") + "/" + j.getName();
				
				URL classURL = null;
				try {
					classURL = new URL(url);
				} catch (MalformedURLException e) {
					System.out.println("URL could not be parsed '" + url + "'");
					e.printStackTrace();
				}
//				System.out.println("classURL: " + classURL.toString());
				URLClassLoader cl = new URLClassLoader(new URL[] { classURL });
				URL[] u = cl.getURLs();
		
				Class<?> c = null;
				try {
					System.out.println("Watcha want? " + newClassName);
					c = cl.loadClass(newClassName);
				} catch (ClassNotFoundException e) {
					System.out.println("Could not find " + newClassName);
					e.printStackTrace();
				}

				
//				Class<?> c = Class.forName(newClassName);
//				classes.add(newClassName);		
						
			} else if (j.getName().endsWith(".jar")) {
				System.out.println("THERES ANOTHER JAR: " + j.getName());
//				checkJar(j.getName()); // open the archive recursively
			}		
		}

		jf.close();
		

//		for (int i = 0; i < entryNames.size(); i++) {
//			System.out.println("    class: " + entryNames.get(i));
//		}
		
		
	}

}
