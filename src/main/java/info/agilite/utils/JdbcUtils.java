package info.agilite.utils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtils {

	public static List<TableMap> createTableMapByResultSet(ResultSet rs) throws SQLException{
		List<TableMap> listMap = new ArrayList<>();
		ResultSetMetaData metaData = rs.getMetaData();
		
		while(rs.next()) {
			TableMap row = new TableMap();
			for(int i = 1; i <= metaData.getColumnCount(); i++){
				row.put(metaData.getColumnName(i), convertJDBCObject(rs.getObject(i)));
			}
			listMap.add(row);
		}
		
		return listMap;
	}
	
	public static List<TableMap> createTableMapByData(List<Object[]> dados, String ... colunas){
		List<TableMap> listMap = new ArrayList<>();
		
		for(Object[] row : dados) {
			TableMap tm = new TableMap();
			
			for(int i = 0; i < colunas.length; i++) {
				tm.put(colunas[i], row[i]);
			}
			
			listMap.add(tm);
		}
		
		return listMap;
	}
	
	public static <T, E> Map<T, E> createMapByResultSet(ResultSet rs, String colKey, String colVal) throws SQLException{
		Map<T, E> result = new HashMap<T, E>();
		
		while(rs.next()) {
			result.put((T)rs.getObject(colKey), (E)rs.getObject(colVal));
		}
		
		return result;
	}
	
	private static Object convertJDBCObject(Object obj) {
		if(obj == null)return null;
		if(obj instanceof Double)return new BigDecimal(obj.toString());
		if(obj instanceof java.sql.Date)return ((java.sql.Date)obj).toLocalDate();
		if(obj instanceof java.sql.Time)return ((java.sql.Time)obj).toLocalTime();
		if(obj instanceof java.sql.Timestamp)return ((java.sql.Timestamp)obj).toLocalDateTime();
		
		return obj;
	}
}
