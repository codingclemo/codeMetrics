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
		
		// go through filenames and find classes
		for (int i = 0; i < files.size(); i++) {
			System.out.println(i);
			checkJar(files.get(i));
		}
		
		// go through classes
//		getClassesFromJar(String jarURL, ArrayList<String> classNames)
	}
	
	public static void checkJar(String fileName) throws IOException {
		
		// check if file exists
		File file = new File(fileName.toString());
		if (!file.exists()) {
			System.out.println("File not found: " + fileName + "\n" + file.getAbsolutePath());
		}
		
		// check jar file for classes
		JarFile jf = new JarFile(file);
		System.out.println("Java archive: " + jf.getName()); 
		
//		Collection<Class<?>> classes = new ArrayList<Class<?>>();
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
				
			} else if (j.getName().endsWith(".jar")) {
				System.out.println("THERES ANOTHER JAR: " + j.getName());
//				checkJar(j.getName()); // open the archive recursively // probably not needed
			}		
		}

		jf.close();	
		
	}
	
	public static ArrayList<Class<?>> getClassesFromJar(String jarURL, ArrayList<String> classNames) {
		
		// generate URL for accessing the jar file
		String url = "file://" + System.getProperty("user.dir") + "\\" + fileName;
		System.out.println(url);
		
		// check if url is valid
		URL classURL = null;
		try {
			classURL = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println("URL could not be parsed '" + url + "'");
			e.printStackTrace();
		}
		
		// load classes from url of the jar file				
		URLClassLoader cl = new URLClassLoader(new URL[] { classURL });
		URL[] u = cl.getURLs();
		System.out.println(cl.loadClass(newClassName).getName());
		
		return null;
	}

}
