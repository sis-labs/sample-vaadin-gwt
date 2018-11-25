package io.rty.incub.shared;

public class FunctionalUtilities {

	public static <T> T ensureInstance(final T obj, final Class<T> cl) {
		if(null == obj) {
			try {
				return cl.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException("Error when trying to instanciate a new object (" + cl.getName() + "): " + e.getMessage());
			}
		}
		return obj;
	}
}
