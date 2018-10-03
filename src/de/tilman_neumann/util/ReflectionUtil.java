package de.tilman_neumann.util;

/**
 * Static auxiliary methods for java objects meta data.
 * 
 * @author Tilman Neumann
 */
public class ReflectionUtil {
	private ReflectionUtil() {
		// static class
	}
	
	/**
	 * Returns the package name for the given class.
	 * 
	 * @param clazz Class
	 * @return Package name
	 */
	public static String getPackageName(Class<?> clazz) {
		String className = clazz.getName();
		int lastPointIndex = className.lastIndexOf('.');
		if (lastPointIndex < 0) {
			return "";
		}
		return className.substring(0, lastPointIndex);
	}
}
