package info.agilite.utils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	private static Object convertJDBCObject(Object obj) {
		if(obj == null)return null;
		if(obj instanceof Double)return new BigDecimal(obj.toString());
		if(obj instanceof java.sql.Date)return ((java.sql.Date)obj).toLocalDate();
		if(obj instanceof java.sql.Time)return ((java.sql.Time)obj).toLocalTime();
		if(obj instanceof java.sql.Timestamp)return ((java.sql.Timestamp)obj).toLocalDateTime();
		
		return obj;
	}
}
