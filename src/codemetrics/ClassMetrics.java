package codemetrics;

import java.util.TreeMap;

public class ClassMetrics {

	private String className;
	private TreeMap<String, Double> classInfo;
	
	public ClassMetrics() {
		classInfo = new TreeMap<String, Double>();
		
		classInfo.put("NrOfMethodsTotal", 0.);
		classInfo.put("NrOfMethodsPublic", 0.);
		classInfo.put("NrOfMethodsPrivate", 0.);
		classInfo.put("NrOfMethodsProtected", 0.);
		classInfo.put("NrOfInterfaces", 0.);
		classInfo.put("depth", 0.);
		classInfo.put("NrOfParamsPerMethod", 0.);
	}
	
	// **** Set methods
	void setClassName(String s) {
		className = s;
	}
	
	void setMethodsTotal(Double n) {
		classInfo.put("NrOfMethodsTotal", n*1.0);
	}
	
	void setMethodsPublic(Double n) {
		classInfo.put("NrOfMethodsPublic", n*1.0);
	}
	
	void setMethodsPrivate(Double n) {
		classInfo.put("NrOfMethodsPrivate", n*1.0);
	}
	
	void setMethodsProtected(Double n) {
		classInfo.put("NrOfMethodsProtected", n*1.0);
	}
	
	void setNrOfInterfaces(Double n) {
		classInfo.put("NrOfInterfaces", n*1.0);
	}
	
	void setDepth(Double n) {
		classInfo.put("depth", n*1.0);
	}
	
	void setParamsPerMethod(Double n) {
		classInfo.put("NrOfParamsPerMethod", n*1.0);
	}
	
	
	// **** Get methods
	String getClassName() {
		return className;
	}
	
	Double getMethodsTotal () {
		return classInfo.get("NrOfMethodsTotal");
	}
	
	Double getMethodsPublic () {
		return classInfo.get("NrOfMethodsPublic");
	}
	
	Double getMethodsPrivate () {
		return classInfo.get("NrOfMethodsPrivate");
	}
	
	Double getMethodsProtected () {
		return classInfo.get("NrOfMethodsProtected");
	}
	
	Double getNrOfInterfaces () {
		return classInfo.get("NrOfInterfaces");
	}
	
	Double getDepth () {
		return classInfo.get("depth");
	}
	
	Double getParamsPerMethod () {
		return classInfo.get("NrOfParamsPerMethod");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("# of methods (total): " + classInfo.get("methods") + "\n");
		sb.append("# of public methods: " + classInfo.get("publicMethods") + "\n");
		sb.append("# of private methods: " + classInfo.get("privateMethods") + "\n");
		sb.append("# of protected methods: " + classInfo.get("protectedMethods") + "\n");
		sb.append("avg # of parameters per method: " + classInfo.get("paramsPerMethod") + "\n");
		sb.append("avg # of interfaces: " + classInfo.get("interfaces") + "\n");
		sb.append("depth: " + classInfo.get("depth") + "\n");
		return sb.toString();
	}
}
