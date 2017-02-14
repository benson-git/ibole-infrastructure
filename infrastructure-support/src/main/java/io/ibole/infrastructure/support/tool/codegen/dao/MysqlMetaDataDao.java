package io.ibole.infrastructure.support.tool.codegen.dao;

import io.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;
import io.ibole.infrastructure.support.tool.codegen.model.TableModel;
import io.ibole.infrastructure.support.tool.codegen.pojo.ColPojo;
import io.ibole.infrastructure.support.tool.codegen.util.StringUtils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlMetaDataDao extends BaseDao {

	Logger log = Logger.getLogger(getClass());
	public MysqlMetaDataDao(){
		
	}
	/**
	 * 获取表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<String> getTables() throws SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("show tables");
		ResultSet rs = ps.executeQuery();
		List<String> tables = new ArrayList<String>();
		while (rs.next()) {
			String str = rs.getString(1);
			// if(!tables.contains(str))
			if(!str.toLowerCase().equals("systemmaxid"))
				tables.add(str);
		}
		close(conn,null,null);
		return tables;
	}

	/**
	 * 获取主键
	 * @param tabname
	 * @return
	 * @throws SQLException
	 */
	public List<String> getPks(String tabname) throws SQLException {
		List<String> pks = new ArrayList<String>();
		Connection conn = getConnection();
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getPrimaryKeys(null, null, tabname);
		while (rs.next()) {
			String pk = rs.getString(4);
			pks.add(pk);
		}
		close(conn, null, rs);
		return pks;
	}

	/**
	 * 根据表名获取列
	 * 
	 * @param tableName
	 * @return List<ColPojo>
	 * @throws SQLException
	 */
	public List<ColPojo> getColumnsForTablesName(String tableName) throws SQLException {
		String sql = "select * from " + tableName;
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		List<ColPojo> result = new ArrayList<ColPojo>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String colname = rsmd.getColumnName(i);
			String typeName = rsmd.getColumnTypeName(i);
			int itype = rsmd.getColumnType(i);
			int size = rsmd.getColumnDisplaySize(i);
			String a = rsmd.getColumnTypeName(i);
			ColPojo col = new ColPojo(
					String.valueOf(itype), 
					typeName,
					size,
					colname,
					(String) FreeMarkerEnvConfig.getInstance().getJdbcTypeMap().get(String.valueOf(itype)),
					StringUtils.toCamelString(colname));
			String value = FreeMarkerEnvConfig.getInstance().getColumnsInfo().get(tableName.toLowerCase() + "-" + colname.toLowerCase());
			String code  = colname;
			String lable = colname;
			if(value == null){
				log.error("【" + tableName + "】表的【" +colname.toLowerCase()+ "】字段，在appColumns表中没有注册");
			}else{
				code  = value.split("-")[0];
				lable = value.split("-")[1];
			}
			
			
			col.setCode(code);
			col.setLable(lable);
			result.add(col);
		}
		close(conn, ps, rs);
		Collections.sort(result);
		return result;
	}
	
//	public 
	/**
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public TableModel queryTableInfoByTableName(String tableName) throws SQLException {
		TableModel tm = new TableModel();
		tm.setTableName(tableName);
		String className = tableName.substring(0,1).toUpperCase()+tableName.substring(1);
//		if(className.startsWith("t_")){
//			className = className.substring(2);
//		}
//		tm.setClassName(StringUtils.omitUnderLineToCamelStr(className));
		tm.setClassName(className);
		tm.setPojoName(className);
		tm.setCols(this.getColumnsForTablesName(tableName));
		tm.setBasePackage(FreeMarkerEnvConfig.getInstance().getFreemarkerConf().getProperty("basepackage"));
		List<String> pks = this.getPks(tableName);
		tm.buildPks(pks);
		return tm;
	}
	
	public Map<String,String> getAppcolumns() throws SQLException{
		Map<String,String> result = new HashMap<String,String>();
		String sql = "SELECT t.TableCode,t.Code,t.Name FROM AppColumns t";
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				String key = rs.getString(1).toLowerCase() + "-" + rs.getString(2).toLowerCase();
				String value = rs.getString(2) + "-" + rs.getString(3);
				result.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
		return result;
	}

	/**
	 * еi?
	 * @return
	 * @throws SQLException
	 
	public List getTablesName() throws SQLException {
		DatabaseMetaData dbmd = getConnection().getMetaData();
		List tables = new ArrayList();
		ResultSet rs = dbmd.getTables(null, null, "", null);
		while (rs.next()) {
			String str = rs.getString(1);
			if (!tables.contains(str))
				tables.add(str);
		}
		return tables;
	}*/
	// public static void main(String[] args) {
	// Field fields[] = java.sql.Types.class.getFields();
	// for(int i=0;i<fields.length;i++){
	// String name = fields[i].getName();
	// Integer value = -1;
	// try {
	// value = (Integer) fields[i].get(java.sql.Types.class);
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// public static void main(String[] args) throws SQLException {
	// MetaDataDao dao = new MetaDataDao();
	// List tables = dao.getColumnsForTablesName("t_user");
	// if(tables.size()!=0){
	// for(int i=0;i<tables.size();i++){
	// //System.out.println(tables.get(i));
	// }}else{
	// System.out.println("none");
	// }
	// }

	// public static void main(String[] args) {
	// Field fields [] = JDBCType.class.getFields();
	// for(int i=0;i<fields.length;i++){
	// System.out.println(fields[i].getName());
	// }
	// }

	// public static void main(String[] args) throws SQLException {
	// MetaDataDao dao = new MetaDataDao();
	// List tables = dao.getTables();
	// if (tables.size() != 0) {
	// for (int i = 0; i < tables.size(); i++) {
	// System.out.println(tables.get(i));
	// }
	// } else {
	// System.out.println("none");
	// }
	// }
	public static void maian(String[] args) throws SQLException {
		MysqlMetaDataDao dao = new MysqlMetaDataDao();
		List<String> strs = dao.getPks("t_user");
		if (strs.size() != 0)
			for (int i = 0; i < strs.size(); i++) {
				System.out.println(strs.get(i));
			}
	}
	
	public static void maissn(String[] args) throws SQLException {
		MysqlMetaDataDao dao = new MysqlMetaDataDao();
		List<ColPojo> cols = dao.getColumnsForTablesName("e_customerinfo");
		for(int i=0;i<cols.size();i++){
			System.out.println(cols.get(i).getColName() + " type is " + cols.get(i).getDbTypeName() + " size = " + cols.get(i).getColLength());
		}
	}
	public static void dmain(String[] args) {
		MysqlMetaDataDao dao = new MysqlMetaDataDao();
		try {
			List<ColPojo> cols = dao.getColumnsForTablesName("e_customerinfo");
			for(ColPojo col:cols){
				System.out.println(col);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void ssmain(String[] args) {
		MysqlMetaDataDao dao = new MysqlMetaDataDao();
		try {
			List<String> tabs = dao.getTables();
			for(String t:tabs){
				System.out.println(t);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void sssmain(String[] args) {
		MysqlMetaDataDao dao = new MysqlMetaDataDao();
		try {
			dao.getColumnsForTablesName("B_AttachmentInfo");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws SQLException {
		MysqlMetaDataDao dao  = new MysqlMetaDataDao();
		List<ColPojo> cols = dao.getColumnsForTablesName("U_UserInfo");
		List<String> strList = dao.getTables();
		long start = System.currentTimeMillis();
		System.out.println("show Tables ");
		System.out.println("service is start" + start);
		for(String str:strList){
		  System.out.println(str);
		}
		System.out.println("service is end" + (System.currentTimeMillis()-start));
		
//		System.out.println("show pks : U_UserInfo");
//		Long pstart = System.currentTimeMillis();
//		System.out.println("service is started ~~~~" + pstart);
//		List<String> pks = dao.getPks("U_UserInfo");
//		for(String pk:pks){
//		  System.out.println(pk);
//		}
//		System.out.println("service is end ~~~~");
//		System.out.println(System.currentTimeMillis()-pstart);
		
//		for(int i=0;i<cols.size();i++){
//			System.out.print(cols.get(i).getFieldName() + " \t");
//		}
//		System.out.println();
//		
//		TableModel model = dao.queryTableInfoByTableName("U_UserInfo");
//		System.out.println(model);
		
//		System.out.println("getAppcolumns is start ~~~~~");
//		Map<String,String> map = dao.getAppcolumns();
//		for(Entry<String, String>  entry : map.entrySet()){
//		  System.out.println(entry.getKey() +":" + entry.getValue() + "  ");
//		}
//		System.out.println("getAppcolumns is end ~~~~");
		Collections.sort(cols);
		for(int i=0;i<cols.size();i++){
			System.out.print(cols.get(i).getFieldName() + " \t");
		}
	}
}
