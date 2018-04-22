package codemetrics;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;


public class JARInspect {
	/*  Notes:
		1- Break it first, break all the time
		2- Read the files first
		3- Print it to know whats inside
		4- Recursive if its .jar
		5- Read the content (Open stream)
		6- Analzye -> think more about it do not it immediately
	 */

	public static void main(String[] args) throws IOException {
		
		// check for arguments
		if (args.length == 0) {
			System.out.println("Please enter jar-files as arguments when you start this program.");
			System.exit(1);
		}
		
		// get filenames from arguments list
		ArrayList<String> files = new ArrayList<>();
		for(int i = 0; i < args.length; i++) {
			files.add(args[i]);
		}
		
		// go through filenames and find classes
		for (int i = 0; i < files.size(); i++) {
			checkJar(files.get(i));
		}	
	}
	
	public static void checkJar(String fileName) throws IOException {
		
		// check if file exists
		File file = new File(fileName.toString());
		
		if (!file.exists()) {
			System.out.println("File not found: " + file.getAbsolutePath());
			return;
		}
		
		// check jar file for classes
		JarFile jf = new JarFile(file);
		System.out.println("\n--------------------------------------------------");
		System.out.println("Java archive: " + jf.getName()); 
		
		ArrayList<String> classNames = new ArrayList<>();
		Enumeration<JarEntry> jfEntries = jf.entries();
		
		while (jfEntries.hasMoreElements()) {
			
			JarEntry j = jfEntries.nextElement();
			
			// if there is a class, scan it
			if (j.getName().endsWith(".class")) {
				String newClassName = j.getName().replace(".class","");
				newClassName = newClassName.replace("/", ".");
				classNames.add(newClassName);
				
				
			} else if (j.getName().endsWith(".jar")) {
				System.out.println("THERES ANOTHER JAR: " + j.getName());
				checkJar(j.getName()); // open the archive recursively // probably not needed
			}		
		}
		jf.close();	
		
		URL[] urls = { new URL ("jar:file:" + file.getAbsolutePath() + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		
		
		// if the 
		if (classNames.isEmpty()) {
			System.out.println("--------------------------------------------------");			
			System.out.println("   no classes found");
			return;
		}
		
		// store metrics of every class in a list
		ArrayList<ClassMetrics> classMetricsSet = new ArrayList<>();
		ClassMetrics cm = null;
		for (String name : classNames) {
			cm = getClassFromJar(cl, name);
			if (cm != null)
				classMetricsSet.add(cm);
		}
		cl.close();
		
		//go through the container of ClassMetrics and accumulate all values
		printJarStatistics(classMetricsSet);
	}
	
	public static void printJarStatistics(ArrayList<ClassMetrics> classMetricsSet) {
		
		int minTotal = Integer.MAX_VALUE;
		int maxTotal = Integer.MIN_VALUE;
		int totalCnt = 0;
		int privCnt = 0;
		int pubCnt = 0;
		int protCnt = 0;
		
		int minInterface = Integer.MAX_VALUE;
		int maxInterface = Integer.MIN_VALUE;
		int interfaceCnt = 0;
		
		double avgParameters = 0;
		double avgDepth = 0;
		
		//create new metrics for the jar in total (min, max, avg)	
		for (int i = 0; i < classMetricsSet.size(); i++) {
			if (classMetricsSet.get(i) == null)
				continue;
			
			int p;
			// get method metrics
			privCnt += classMetricsSet.get(i).getMethodsPrivate();
			pubCnt += classMetricsSet.get(i).getMethodsPublic();
			protCnt += classMetricsSet.get(i).getMethodsProtected();
			
			p = classMetricsSet.get(i).getMethodsPrivate().intValue() +
				classMetricsSet.get(i).getMethodsPublic().intValue() +
				classMetricsSet.get(i).getMethodsProtected().intValue();
			
			minTotal = Math.min(minTotal, p);
			maxTotal = Math.max(maxTotal, p);
			
			// get interfaces
			interfaceCnt += classMetricsSet.get(i).getNrOfInterfaces();
			p = classMetricsSet.get(i).getNrOfInterfaces().intValue();
			minInterface = Math.min(minInterface, p);
			maxInterface = Math.max(maxInterface, p);
			
			// get parameters
			avgParameters = classMetricsSet.get(i).getParamsPerMethod();
			
			// get depth
			avgDepth = classMetricsSet.get(i).getDepth();
		}
		
		totalCnt = privCnt + pubCnt + protCnt;
//		double avgPriv = (double) privCnt / classMetricsSet.size();
//		double avgPub = (double) pubCnt / classMetricsSet.size();
//		double avgProt = (double) protCnt / classMetricsSet.size();
		
		double avgTotal = (double) totalCnt / classMetricsSet.size();
		double avgInterface = (double) interfaceCnt / classMetricsSet.size();
		
		avgParameters = avgParameters / classMetricsSet.size();
		avgDepth = avgDepth / classMetricsSet.size();
		System.out.println("--------------------------------------------------");
		System.out.println("   # of classes: " + classMetricsSet.size());
		System.out.print("   # of methods: " + totalCnt);
		System.out.println(" (avg: " + avgTotal + " min: " + minTotal +" max: " + maxTotal +")");
		System.out.print("   # of interfaces: " + interfaceCnt);
		System.out.println(" (avg: " + avgInterface + " min: " + minInterface +" max: " + maxInterface +")");
		System.out.println("   avg # of parameters: " + avgParameters);
		System.out.println("   avg depth of classes: " + avgDepth);
		System.out.println("--------------------------------------------------");
		System.out.println();
		
		// print Metrics for every class in the JAR
		for (int i = 0; i < classMetricsSet.size(); i++) {
			System.out.print("" + classMetricsSet.get(i).toString());
		}
		
	}
	

	public static ClassMetrics getClassFromJar(URLClassLoader cl, String name) {
		
		Class<?> c = null;
		ClassMetrics cm = new ClassMetrics();
		
		try {
			c = cl.loadClass(name);
//			System.out.println("   Class: " + name);
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			System.out.println("Sorry, could not load class " + name);
			return null;
		} catch (NoClassDefFoundError e) {
//			e.printStackTrace();
			System.out.println("Sorry, could not load class " + name);
			return null;
		}
		
		Double avgPar = 0.;
		int pub = 0;
		int priv = 0;
		int prot = 0;
		
		// list all classes in JAR
//		System.out.println("   " + c.getName());
		
		// check every method per class		
		try {
			for (Method m : c.getDeclaredMethods()) {
				if (m.toString().startsWith("private")) {
					priv++;
				} else if (m.toString().startsWith("public")) {
					pub++;
				} else if (m.toString().startsWith("protected")) {
					prot++;
				}
				
				avgPar += m.getParameterCount();
			}	
		} catch (NoClassDefFoundError e) {
//			e.printStackTrace();
			return null;
		} catch (SecurityException e) {
//			e.printStackTrace();
			return null;
		} 

		
		// store metrics in object
		cm.setClassName(c.getName());
		
		// set # of methods
		cm.setMethodsPrivate((double) priv);
		cm.setMethodsPublic((double) pub);
		cm.setMethodsProtected((double) prot);
		cm.setMethodsTotal((double) priv+pub+prot);
		
		// set avg parameters per method in a class
		avgPar = avgPar / (priv+pub+prot);
		cm.setParamsPerMethod(avgPar);

		// set depth
		int depth = 0;
		Class<?> h = c;
		while (h.getSuperclass() != null) {
			depth++;
			h = h.getSuperclass();
		}
		cm.setDepth((double) depth);
		h = null;

		// set interfaces
		Class<?>[] interf = c.getInterfaces();
		cm.setNrOfInterfaces((double) interf.length);
		
		return cm;
	}

}


