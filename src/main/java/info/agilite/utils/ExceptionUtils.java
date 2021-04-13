package info.agilite.utils;

public class ExceptionUtils {
	public static <T extends Throwable> T findCause(Throwable throwable, Class<T> causeClass) {
		Throwable t = throwable;
		int i = 0;
		while(true) {
			if(t.getClass().equals(causeClass)) {
				return (T)t;
			}
			t = t.getCause();
			
			i++;
			if(t == null || i >= 20)return null;
		}
	}
}
