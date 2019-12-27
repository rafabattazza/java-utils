package info.agilite.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtils {
	public static <T> List<T> list(T ... ts){
		List<T> result = new ArrayList<>();
		result.addAll(Arrays.asList(ts));
		
		return result;
	}
	
	public static <T> T last(List<T> list) {
		if(list == null || list.size() == 0)return null;
		
		return list.get(list.size()-1);
	}
}
