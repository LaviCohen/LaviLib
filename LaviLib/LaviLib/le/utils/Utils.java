package le.utils;

public class Utils {

	
	public static boolean contains(Object[] array, Object instance) {
		for (Object object : array) {
			if (object.equals(instance)) {
				return true;
			}
		}
		return false;
	}
}
