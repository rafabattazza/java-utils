package info.agilite.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

public class Utils {

	public static boolean in(Object value, Object ... in) {
		if(value == null || in == null)throw new NullPointerException("Impossível executar comparação IN com objetos nulos");

		for(Object val : in) {
			if(val == null)continue;
			if(val.equals(value))return true;
		}

		return false;
	}

	public static boolean inIgnoreCase(String value, String ... in) {
		if(value == null || in == null)throw new NullPointerException("Impossível executar comparação IN com objetos nulos");

		for(String val : in) {
			if(val.equalsIgnoreCase(value))return true;
		}

		return false;
	}

	public static final String urlEncode(String str) {
	    try {
	        return URLEncoder.encode(str, "UTF-8").replace("+", "%20");
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.size() == 0;
	}
	
	public static boolean jsBoolean(Object value) {
		if(value == null)return false;
		if(value instanceof String)return !inIgnoreCase((String)value, "", "false", "f", "0");
		if(value instanceof Number)return ((Number)value).intValue() != 0;
		
		return Boolean.valueOf(value.toString());
	}

	public static String cript(String chave) {
		return cript(chave, 1);
	}

	public static String cript(String chave, int fases) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			for(int i = 0; i < fases; i++) {
				byte[] strSnh = chave.getBytes();
				md.update(strSnh);
				chave = new BigInteger(1, md.digest()).toString(16);
			}

			return chave;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao criptografar senha", e);
		}
	}
	
	public static String createMD5(String chave) {
		return createMD5(chave, 1);
	}
	
	public static String createMD5(String chave, int fases) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			for(int i = 0; i < fases; i++) {
				byte[] strSnh = chave.getBytes();
				md.update(strSnh);
				chave = new BigInteger(1, md.digest()).toString(16);
			}
			
			return chave;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao criptografar senha", e);
		}
	}

	/**
	 * Cria um MAP baseado nos valores passados, os valores devem ser informados em pares, o primeiro será a Key e o segundo será o value
	 * @param keyAndValues
	 * @return
	 */
	public static Map<String, String> mapString(Object ... keyAndValues){
		if(keyAndValues == null || keyAndValues.length == 0)return null;
		if(keyAndValues.length % 2 != 0)throw new RuntimeException("Deve ser informado um número par de parâmetros para gerar o MAP");

		Map<String, String> retorno = new TreeMap<String, String>();
		for(int i = 0; i < keyAndValues.length; i+=2) {
			Object key = keyAndValues[i];
			Object value = keyAndValues[i+1];
			retorno.put(StringUtils.formatByClass(key), StringUtils.formatByClass(value));
		}

		return retorno;
	}

	/**
	 * Cria um map onde as chaves serão obtidas a partir das Keys que é uma String separada por vírgula
	 * @param keys
	 * @param values
	 * @return
	 */
	public static Map<String, Object> mapByPattern(String keys, Object ... values){
		if(Strings.isNullOrEmpty(keys))return null;
		List<String> keysList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(keys);
		if(keysList.size() != values.length)throw new RuntimeException("Deve ser informado um número de parâmetros igual a quanitade de chaves para gerar o MAP");

		Map<String, Object> retorno = new TreeMap<>();
		for(int i = 0; i < keysList.size(); i++) {
			String key = keysList.get(i);
			Object value = values[i];
			retorno.put(key, value);
		}

		return retorno;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K, V> Map<K, V> map(Object ... keyAndValues){
		if(keyAndValues == null || keyAndValues.length == 0)return null;
		if(keyAndValues.length % 2 != 0)throw new RuntimeException("Deve ser informado um número par de parâmetros para gerar o MAP");

		Map retorno = new TreeMap<>();
		for(int i = 0; i < keyAndValues.length; i+=2) {
			Object key = keyAndValues[i];
			Object value = keyAndValues[i+1];
			retorno.put(key, value);
		}

		return retorno;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> convertToNestedMap(Map<String, Object>  map){
		Map<String, Object> result = new HashMap<String, Object>();
		
		map.entrySet().forEach(entry -> {
			if(!entry.getKey().contains(".")) {
				result.put(entry.getKey(), entry.getValue());
			}else {
				List<String> keyList = Splitter.on(".").splitToList(entry.getKey());
				Map leaf = (Map)result.compute(keyList.get(0), (k, val) -> val == null ? new HashMap<String, Object>() : val);
				for(int i = 1; i < keyList.size()-1; i++) {
					leaf = (Map)leaf.compute(keyList.get(i), (k, val) -> val == null ? new HashMap<String, Object>() : val);
				}
				leaf.put(keyList.get(keyList.size()-1), entry.getValue());
			}
		});
		
		return result;
	}

	public static Map<String, Object> convertJpaTupleToNestedMap(String fields, Object[] rowData){
		List<String> fieldList = Splitter.on(",").trimResults().splitToList(fields);
		
		Map<String, Object> row = new HashMap<>();
		for(int i = 0; i < fieldList.size(); i++) {
			row.put(fieldList.get(i), rowData[i]);
		}
		return convertToNestedMap(row);
	}

	
	public static List<Map<String, Object>> convertJpaListTupleToNestedMap(String fields, List<Object[]> data){
		List<Map<String, Object>> result = new ArrayList<>();
		List<String> fieldList = Splitter.on(",").trimResults().splitToList(fields);
		
		for(Object[] rowData : data) {
			Map<String, Object> row = new HashMap<>();
			for(int i = 0; i < fieldList.size(); i++) {
				row.put(fieldList.get(i), rowData[i]);
			}
			result.add(convertToNestedMap(row));
		}
		
		return result;
	}

	
	

	public static boolean isAllNull(Object ... values) {
		for (Object object : values) {
			if(object != null)return false;
		}
		return true;
	}

	public static boolean isAllNotNull(Object ... values) {
		for (Object object : values) {
			if(object == null)return false;
		}
		return true;
	}

	public static boolean orAllisNullOrNotNull(Object ... values) {
		if(isAllNull(values))return true;
		if(isAllNotNull(values))return true;
		return false;
	}

	@SafeVarargs
	public static <T> T firstNotNull(T ... in) {
		if(in == null)return null;

		for(T val : in) {
			if(val != null)return val;
		}

		return null;
	}

	public static String headerStackTrace(Throwable exc) {
		StringBuilder stack = new StringBuilder();
		headerStackTrace(exc, stack, 0);
		return stack.toString();
	}

	public static void headerStackTrace(Throwable erro, StringBuilder stack, int cont){
		cont++;
		stack.append(erro.toString()).append("\n");
		if(erro.getCause() != null){
			if(cont == 10){
				stack.append("\n").append("Verifique o log para maiores detalhes... \n");
			}else{
				stack.append("\tCause: ");
				headerStackTrace(erro.getCause(), stack, cont);
			}
		}
	}

	public static String headerStackTraceToHtml(Throwable exc) {
		StringBuilder stack = new StringBuilder("<div class='stack-trace-sam'>");
		stack.append("<UL>");

		headerStackTraceToHtml(exc, stack, 0);

		stack.append("</UL>");
		stack.append("</div>");
		return stack.toString();
	}
	public static void headerStackTraceToHtml(Throwable erro, StringBuilder stack, int cont){
		stack.append("<LI>").append(cont > 0 ? "Cause: " : "").append(erro.toString()).append("</LI>");
		cont++;
		if(erro.getCause() != null){
			if(cont == 10){
				stack.append("<LI>Verifique o log para maiores detalhes...</LI>");
			}else{
				headerStackTraceToHtml(erro.getCause(), stack, cont);
			}
		}
	}

	public static Runnable compoundRunnable(final Runnable... runnables) {
		return new Runnable() {
			public void run() {
				for(Runnable r: runnables) r.run();
			} 
		};
	}
	
	public static <T> Supplier<T> compoundSupplier(final Supplier<T> supplier, final Consumer<T> consumer) {
		return ()->{
			T t= supplier.get();
			consumer.accept(t);
			
			return t;
		};
	}

	public static <T> List<T> joinLists(List<T> listA, List<T> listB){
		List<T> retorno = new ArrayList<>();

		if(listA != null && listA.size() > 0)retorno.addAll(listA);
		if(listB != null && listB.size() > 0)retorno.addAll(listB);

		if(retorno.size() == 0)return null;
		return retorno;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int compare(Object v1, Object v2) {
		if(v1 == null && v2 == null)return 0;
		if(v1 == null)return -1;
		if(v2 == null)return 1;

		return ((Comparable)v1).compareTo((Comparable)v2);
	}
}
