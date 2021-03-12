package info.agilite.utils;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Esse objeto é um Map<String, Object> onde as chaves do MAP não são case sensitive, ou seja se adicionar map.put("nome", "X") pode-se obter o valor através de map.get("NOME");
 *
 * @author Rafael
 *
 */

@SuppressWarnings("unchecked")
public class TableMap extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	public TableMap() {
	}
	
	public TableMap(Map<?, ?> m) {
		if(m != null && m.size() > 0) {
			m.keySet().forEach(key->{
				if(key == null) {
					TableMap.this.put(null, m.get(key));
				} else {
					String k = (key instanceof String ? (String)key : key.toString());
					TableMap.this.put(k, m.get(key));
				}
			});
		}
	}
	
	public TableMap(Object ... values) {
		this(Utils.map(values));
	}
	
	
	@Override
	public Object get(Object key) {
		if(key == null)return super.get(null);
		return super.get(key.toString().toLowerCase());
	}
	
	public <T> T get(String key) {
		if(key == null)return (T)super.get(null);
		return (T)super.get(key.toLowerCase());
	}

	@Override
	public boolean containsKey(Object key) {
		if(key == null)return super.containsKey(null);
		return super.containsKey(key.toString().toLowerCase());
	}
	
	public boolean containsKey(String key) {
		if(key == null)return super.containsKey(null);
		return super.containsKey(key.toLowerCase());
	}

	@Override
	public Object put(String key, Object value) {
		if(hasOnChange())onChange.accept(key, value);
		if(key == null)return super.put(key, value);
		return super.put(key.toLowerCase(), value);
	}
	
	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		for(String key : m.keySet()) {
			if(hasOnChange())onChange.accept(key, m.get(key));
			put(key, m.get(key));
		}
	}

	@Override
	public Object remove(Object key) {
		if(key == null)return super.remove(key);
		return super.remove(key.toString().toLowerCase());
	}
	public void remove(String [] keys) {
		if(keys == null)return; 
		for(String key : keys)remove(key.trim());
	}
	public void remove(Collection<String> keys) {
		if(keys == null)return; 
		for(String key : keys)remove(key.trim());
	}
	
	
	//*******************************************************
	//****************Change Function************************
	private BiConsumer<String, Object> onChange;
	public void onChange(BiConsumer<String, Object> onChange) {
		this.onChange = onChange;
	}
	public boolean hasOnChange() {
		return onChange != null;
	}
	//*******************************************************
	//*************Métodos Úteis para Json*******************
	@JsonIgnore
	public String getString(String key) {
		Object val = get(key);
		if(val == null)return null;
		
		return val.toString();
	}

	@JsonIgnore
	public Long getLong(String key) {
		Object val = get(key);
		if(val == null)return null;
		if(val instanceof Long)return (Long) val;
		if(val instanceof String)return Long.parseLong((String)val);
		
		return (Long)compute(key, (k, v) -> ((Number)val).longValue());
	}
	@JsonIgnore
	public Integer getInteger(String key) {
		Object val = get(key);
		if(val == null)return null;
		if(val instanceof Integer)return (Integer) val;
		if(val instanceof String)return Integer.parseInt((String)val);
		
		return (Integer)compute(key, (k, v) -> ((Number)val).intValue());
	}
	@JsonIgnore
	public BigDecimal getBigDecimal(String key) {
		Object val = get(key);
		if(val == null)return null;
		if(val instanceof BigDecimal)return (BigDecimal) val;
		
		return (BigDecimal)compute(key, (k, v) -> new BigDecimal(val.toString()));
	}
	@JsonIgnore
	public LocalDate getDate(String key) {
		Object val = get(key);
		if(val == null)return null;
		if(val instanceof LocalDate)return (LocalDate) val;
		
		return (LocalDate)compute(key, (k, v)-> DateUtils.parseDate((String) v, "yyyyMMdd"));//JSonMapperCreator.create().read("\""+(String)v+"\"", LocalDate.class));
	}
	@JsonIgnore
	public LocalTime getTime(String key) {
		Object val = get(key);
		if(val == null)return null;
		if(val instanceof LocalTime)return (LocalTime) val;
		
		return (LocalTime)compute(key, (k, v)-> DateUtils.parseTime((String) v, "HHmmss"));//JSonMapperCreator.create().read("\""+(String)v+"\"", LocalTime.class));
	}
	@JsonIgnore
	public LocalDateTime getDateTime(String key) {
		Object val = get(key);
		if(val == null)return null;
		if(val instanceof LocalDateTime)return (LocalDateTime) val;
		
		return (LocalDateTime)compute(key, (k, v)-> DateUtils.parseDateTime((String) v, "yyyyMMddHHmmss"));//JSonMapperCreator.create().read("\""+(String)v+"\"", LocalDateTime.class));
	}
	
	@SuppressWarnings("rawtypes")
	@JsonIgnore
	public TableMap getTableMap(String key) {
		Object val = get(key);
		if(val == null)return null;
		if(val instanceof TableMap)return (TableMap) val;
		
		return (TableMap)compute(key, (k, v)-> new TableMap((Map)v));
	}
	
	@SuppressWarnings("rawtypes")
	@JsonIgnore
	public List<TableMap> getListTableMap(String key) {
		Object val = get(key);
		if(val == null)return null;
		if(!(val instanceof List))throw new RuntimeException("Erro ao obter ListTableMap, o objeto não é um List para ser convertido");
		
		List rows = (List)val;
		if(rows.size() == 0)return rows;
		
		if(rows.get(0) instanceof TableMap)return rows;
		List<TableMap> listTableMap = (List<TableMap>)rows.stream().map(map -> new TableMap((Map)map)).collect(Collectors.toList());
		put(key, listTableMap);
		
		return listTableMap;
	}
}
