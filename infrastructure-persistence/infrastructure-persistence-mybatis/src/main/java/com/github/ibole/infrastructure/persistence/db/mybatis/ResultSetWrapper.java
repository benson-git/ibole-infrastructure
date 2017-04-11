package com.github.ibole.infrastructure.persistence.db.mybatis;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class ResultSetWrapper implements ResultSet
{

    private ResultSet wrapped;

    public ResultSetWrapper(ResultSet wrapped)
    {

        this.wrapped = wrapped;
    }

    public boolean absolute(int row) throws SQLException
    {

        return wrapped.absolute(row);
    }

    public void afterLast() throws SQLException
    {

        wrapped.afterLast();

    }

    public void beforeFirst() throws SQLException
    {

        wrapped.beforeFirst();
    }

    public void cancelRowUpdates() throws SQLException
    {

        wrapped.cancelRowUpdates();

    }

    public void clearWarnings() throws SQLException
    {

        wrapped.clearWarnings();

    }

    public void close() throws SQLException
    {

        wrapped.close();

    }

    public void deleteRow() throws SQLException
    {

        wrapped.deleteRow();

    }

    public int findColumn(String columnName) throws SQLException
    {

        return wrapped.findColumn(columnName);
    }

    public boolean first() throws SQLException
    {

        return wrapped.first();
    }

    public Array getArray(int i) throws SQLException
    {

        return wrapped.getArray(i);
    }

    public Array getArray(String colName) throws SQLException
    {

        return wrapped.getArray(colName);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException
    {

        return wrapped.getAsciiStream(columnIndex);
    }

    public InputStream getAsciiStream(String columnName) throws SQLException
    {

        return wrapped.getAsciiStream(columnName);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException
    {

        return wrapped.getBigDecimal(columnIndex);
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException
    {

        return wrapped.getBigDecimal(columnName);
    }

    @SuppressWarnings("deprecation")
	public BigDecimal getBigDecimal(int columnIndex, int scale)
                    throws SQLException
    {

        return wrapped.getBigDecimal(columnIndex, scale);
    }

    @SuppressWarnings("deprecation")
	public BigDecimal getBigDecimal(String columnName, int scale)
                    throws SQLException
    {

        return wrapped.getBigDecimal(columnName, scale);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException
    {

        return wrapped.getBinaryStream(columnIndex);
    }

    public InputStream getBinaryStream(String columnName) throws SQLException
    {

        return wrapped.getBinaryStream(columnName);
    }

    public Blob getBlob(int i) throws SQLException
    {

        return wrapped.getBlob(i);
    }

    public Blob getBlob(String colName) throws SQLException
    {

        return wrapped.getBlob(colName);
    }

    public boolean getBoolean(int columnIndex) throws SQLException
    {

        return wrapped.getBoolean(columnIndex);
    }

    public boolean getBoolean(String columnName) throws SQLException
    {

        return wrapped.getBoolean(columnName);
    }

    public byte getByte(int columnIndex) throws SQLException
    {

        return wrapped.getByte(columnIndex);
    }

    public byte getByte(String columnName) throws SQLException
    {

        return wrapped.getByte(columnName);
    }

    public byte[] getBytes(int columnIndex) throws SQLException
    {

        return wrapped.getBytes(columnIndex);
    }

    public byte[] getBytes(String columnName) throws SQLException
    {

        return wrapped.getBytes(columnName);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException
    {

        return wrapped.getCharacterStream(columnIndex);
    }

    public Reader getCharacterStream(String columnName) throws SQLException
    {

        return wrapped.getCharacterStream(columnName);
    }

    public Clob getClob(int i) throws SQLException
    {

        return wrapped.getClob(i);
    }

    public Clob getClob(String colName) throws SQLException
    {

        return wrapped.getClob(colName);
    }

    public int getConcurrency() throws SQLException
    {

        return wrapped.getConcurrency();
    }

    public String getCursorName() throws SQLException
    {

        return wrapped.getCursorName();
    }

    public Date getDate(int columnIndex) throws SQLException
    {

        return wrapped.getDate(columnIndex);
    }

    public Date getDate(String columnName) throws SQLException
    {

        return wrapped.getDate(columnName);
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException
    {

        return wrapped.getDate(columnIndex, cal);
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException
    {

        return wrapped.getDate(columnName, cal);
    }

    public double getDouble(int columnIndex) throws SQLException
    {

        return wrapped.getDouble(columnIndex);
    }

    public double getDouble(String columnName) throws SQLException
    {

        return wrapped.getDouble(columnName);
    }

    public int getFetchDirection() throws SQLException
    {

        return wrapped.getFetchDirection();
    }

    public int getFetchSize() throws SQLException
    {

        return wrapped.getFetchSize();
    }

    public float getFloat(int columnIndex) throws SQLException
    {

        return wrapped.getFloat(columnIndex);
    }

    public float getFloat(String columnName) throws SQLException
    {

        return wrapped.getFloat(columnName);
    }

    public int getInt(int columnIndex) throws SQLException
    {

        return wrapped.getInt(columnIndex);
    }

    public int getInt(String columnName) throws SQLException
    {

        return wrapped.getInt(columnName);
    }

    public long getLong(int columnIndex) throws SQLException
    {

        return wrapped.getLong(columnIndex);
    }

    public long getLong(String columnName) throws SQLException
    {

        return wrapped.getLong(columnName);
    }

    public ResultSetMetaData getMetaData() throws SQLException
    {

        return new ResultSetMetaDataWrapper(this.wrapped.getMetaData());
    }

    public Object getObject(int columnIndex) throws SQLException
    {

        return this.wrapped.getObject(columnIndex);
    }

    public Object getObject(String columnName) throws SQLException
    {

        return this.wrapped.getObject(columnName);
    }

    public Ref getRef(int i) throws SQLException
    {

        return this.wrapped.getRef(i);
    }

    public Ref getRef(String colName) throws SQLException
    {

        return this.wrapped.getRef(colName);
    }

    public int getRow() throws SQLException
    {

        return this.wrapped.getRow();
    }

    public short getShort(int columnIndex) throws SQLException
    {

        return this.wrapped.getShort(columnIndex);
    }

    public short getShort(String columnName) throws SQLException
    {

        return wrapped.getShort(columnName);
    }

    public Statement getStatement() throws SQLException
    {

        return wrapped.getStatement();
    }

    public String getString(int columnIndex) throws SQLException
    {

        return wrapped.getString(columnIndex);
    }

    public String getString(String columnName) throws SQLException
    {

        return wrapped.getString(columnName);
    }

    public Time getTime(int columnIndex) throws SQLException
    {

        return wrapped.getTime(columnIndex);
    }

    public Time getTime(String columnName) throws SQLException
    {

        return wrapped.getTime(columnName);
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException
    {

        return wrapped.getTime(columnIndex, cal);
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException
    {

        return wrapped.getTime(columnName, cal);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException
    {

        return wrapped.getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(String columnName) throws SQLException
    {

        return wrapped.getTimestamp(columnName);
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal)
                    throws SQLException
    {

        return wrapped.getTimestamp(columnIndex, cal);
    }

    public Timestamp getTimestamp(String columnName, Calendar cal)
                    throws SQLException
    {

        return wrapped.getTimestamp(columnName, cal);
    }

    public int getType() throws SQLException
    {

        return wrapped.getType();
    }

    public URL getURL(int columnIndex) throws SQLException
    {

        return wrapped.getURL(columnIndex);
    }

    public URL getURL(String columnName) throws SQLException
    {

        return wrapped.getURL(columnName);
    }

    @SuppressWarnings("deprecation")
	public InputStream getUnicodeStream(int columnIndex) throws SQLException
    {

        return wrapped.getUnicodeStream(columnIndex);
    }

    @SuppressWarnings("deprecation")
	public InputStream getUnicodeStream(String columnName) throws SQLException
    {

        return wrapped.getUnicodeStream(columnName);
    }

    public SQLWarning getWarnings() throws SQLException
    {

        return wrapped.getWarnings();
    }

    public void insertRow() throws SQLException
    {

        wrapped.insertRow();
    }

    public boolean isAfterLast() throws SQLException
    {

        return wrapped.isAfterLast();
    }

    public boolean isBeforeFirst() throws SQLException
    {

        return wrapped.isBeforeFirst();
    }

    public boolean isFirst() throws SQLException
    {

        return wrapped.isFirst();
    }

    public boolean isLast() throws SQLException
    {

        return wrapped.isLast();
    }

    public boolean last() throws SQLException
    {

        return wrapped.last();
    }

    public void moveToCurrentRow() throws SQLException
    {

        wrapped.moveToCurrentRow();
    }

    public void moveToInsertRow() throws SQLException
    {

        wrapped.moveToInsertRow();
    }

    public boolean next() throws SQLException
    {

        return wrapped.next();
    }

    public boolean previous() throws SQLException
    {

        return wrapped.previous();
    }

    public void refreshRow() throws SQLException
    {

        wrapped.refreshRow();
    }

    public boolean relative(int rows) throws SQLException
    {

        return this.wrapped.relative(rows);
    }

    public boolean rowDeleted() throws SQLException
    {

        return this.wrapped.rowDeleted();
    }

    public boolean rowInserted() throws SQLException
    {

        return this.wrapped.rowInserted();
    }

    public boolean rowUpdated() throws SQLException
    {

        return this.wrapped.rowUpdated();
    }

    public void setFetchDirection(int direction) throws SQLException
    {

        this.wrapped.setFetchDirection(direction);
    }

    public void setFetchSize(int rows) throws SQLException
    {

        this.wrapped.setFetchSize(rows);
    }

    public void updateArray(int columnIndex, Array x) throws SQLException
    {

        this.wrapped.updateArray(columnIndex, x);
    }

    public void updateArray(String columnName, Array x) throws SQLException
    {

        this.wrapped.updateArray(columnName, x);
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length)
                    throws SQLException
    {

        this.wrapped.updateAsciiStream(columnIndex, x, length);
    }

    public void updateAsciiStream(String columnName, InputStream x, int length)
                    throws SQLException
    {

        this.wrapped.updateAsciiStream(columnName, x, length);

    }

    public void updateBigDecimal(int columnIndex, BigDecimal x)
                    throws SQLException
    {

        this.wrapped.updateBigDecimal(columnIndex, x);
    }

    public void updateBigDecimal(String columnName, BigDecimal x)
                    throws SQLException
    {

        this.wrapped.updateBigDecimal(columnName, x);
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length)
                    throws SQLException
    {

        this.wrapped.updateBinaryStream(columnIndex, x, length);
    }

    public void updateBinaryStream(String columnName, InputStream x, int length)
                    throws SQLException
    {

        this.wrapped.updateBinaryStream(columnName, x, length);

    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException
    {

        this.wrapped.updateBlob(columnIndex, x);
    }

    public void updateBlob(String columnName, Blob x) throws SQLException
    {

        this.wrapped.updateBlob(columnName, x);
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException
    {

        this.wrapped.updateBoolean(columnIndex, x);
    }

    public void updateBoolean(String columnName, boolean x) throws SQLException
    {

        this.wrapped.updateBoolean(columnName, x);
    }

    public void updateByte(int columnIndex, byte x) throws SQLException
    {

        this.wrapped.updateByte(columnIndex, x);
    }

    public void updateByte(String columnName, byte x) throws SQLException
    {

        this.wrapped.updateByte(columnName, x);

    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException
    {

        this.wrapped.updateBytes(columnIndex, x);

    }

    public void updateBytes(String columnName, byte[] x) throws SQLException
    {

        this.wrapped.updateBytes(columnName, x);

    }

    public void updateCharacterStream(int columnIndex, Reader x, int length)
                    throws SQLException
    {

        this.wrapped.updateCharacterStream(columnIndex, x, length);

    }

    public void updateCharacterStream(String columnName, Reader reader,
                                      int length) throws SQLException
    {

        this.wrapped.updateCharacterStream(columnName, reader, length);

    }

    public void updateClob(int columnIndex, Clob x) throws SQLException
    {

        this.wrapped.updateClob(columnIndex, x);

    }

    public void updateClob(String columnName, Clob x) throws SQLException
    {

        this.wrapped.updateClob(columnName, x);
    }

    public void updateDate(int columnIndex, Date x) throws SQLException
    {

        this.wrapped.updateDate(columnIndex, x);
    }

    public void updateDate(String columnName, Date x) throws SQLException
    {

        this.wrapped.updateDate(columnName, x);

    }

    public void updateDouble(int columnIndex, double x) throws SQLException
    {

        this.wrapped.updateDouble(columnIndex, x);

    }

    public void updateDouble(String columnName, double x) throws SQLException
    {

        this.wrapped.updateDouble(columnName, x);

    }

    public void updateFloat(int columnIndex, float x) throws SQLException
    {

        this.wrapped.updateFloat(columnIndex, x);

    }

    public void updateFloat(String columnName, float x) throws SQLException
    {

        this.wrapped.updateFloat(columnName, x);
    }

    public void updateInt(int columnIndex, int x) throws SQLException
    {

        this.wrapped.updateInt(columnIndex, x);
    }

    public void updateInt(String columnName, int x) throws SQLException
    {

        this.wrapped.updateInt(columnName, x);
    }

    public void updateLong(int columnIndex, long x) throws SQLException
    {

        this.wrapped.updateLong(columnIndex, x);
    }

    public void updateLong(String columnName, long x) throws SQLException
    {

        this.wrapped.updateLong(columnName, x);

    }

    public void updateNull(int columnIndex) throws SQLException
    {

        this.wrapped.updateNull(columnIndex);

    }

    public void updateNull(String columnName) throws SQLException
    {

        this.wrapped.updateNull(columnName);
    }

    public void updateObject(int columnIndex, Object x) throws SQLException
    {

        this.wrapped.updateObject(columnIndex, x);
    }

    public void updateObject(String columnName, Object x) throws SQLException
    {

        this.wrapped.updateObject(columnName, x);
    }

    public void updateObject(int columnIndex, Object x, int scale)
                    throws SQLException
    {

        this.wrapped.updateObject(columnIndex, x, scale);
    }

    public void updateObject(String columnName, Object x, int scale)
                    throws SQLException
    {

        this.wrapped.updateObject(columnName, x, scale);
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException
    {

        this.wrapped.updateRef(columnIndex, x);
    }

    public void updateRef(String columnName, Ref x) throws SQLException
    {

        this.wrapped.updateRef(columnName, x);
    }

    public void updateRow() throws SQLException
    {

        this.wrapped.updateRow();

    }

    public void updateShort(int columnIndex, short x) throws SQLException
    {

        this.wrapped.updateShort(columnIndex, x);
    }

    public void updateShort(String columnName, short x) throws SQLException
    {

        this.wrapped.updateShort(columnName, x);
    }

    public void updateString(int columnIndex, String x) throws SQLException
    {

        this.wrapped.updateString(columnIndex, x);
    }

    public void updateString(String columnName, String x) throws SQLException
    {

        this.wrapped.updateString(columnName, x);
    }

    public void updateTime(int columnIndex, Time x) throws SQLException
    {

        this.wrapped.updateTime(columnIndex, x);

    }

    public void updateTime(String columnName, Time x) throws SQLException
    {

        this.wrapped.updateTime(columnName, x);
    }

    public void updateTimestamp(int columnIndex, Timestamp x)
                    throws SQLException
    {

        this.wrapped.updateTimestamp(columnIndex, x);
    }

    public void updateTimestamp(String columnName, Timestamp x)
                    throws SQLException
    {

        this.wrapped.updateTimestamp(columnName, x);
    }

    public boolean wasNull() throws SQLException
    {

        return this.wrapped.wasNull();
    }

    public int getHoldability() throws SQLException
    {

        return wrapped.getHoldability();
    }

    public Reader getNCharacterStream(int arg0) throws SQLException
    {

        return wrapped.getNCharacterStream(arg0);
    }

    public Reader getNCharacterStream(String arg0) throws SQLException
    {
        return wrapped.getNCharacterStream(arg0);
    }

    public NClob getNClob(int arg0) throws SQLException
    {
        return  wrapped.getNClob(arg0);
    }

    public NClob getNClob(String arg0) throws SQLException
    {
        return wrapped.getNClob(arg0);
    }

    public String getNString(int arg0) throws SQLException
    {
        return wrapped.getNString(arg0);
    }

    public String getNString(String arg0) throws SQLException
    {

        // TODO Auto-generated method stub
        return null;
    }

    public Object getObject(int arg0, Map<String, Class< ? >> arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub
        return null;
    }

    public Object getObject(String arg0, Map<String, Class< ? >> arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub
        return null;
    }

    public RowId getRowId(int arg0) throws SQLException
    {

        // TODO Auto-generated method stub
        return null;
    }

    public RowId getRowId(String arg0) throws SQLException
    {

        // TODO Auto-generated method stub
        return null;
    }

    public SQLXML getSQLXML(int arg0) throws SQLException
    {

        // TODO Auto-generated method stub
        return null;
    }

    public SQLXML getSQLXML(String arg0) throws SQLException
    {

        // TODO Auto-generated method stub
        return null;
    }

    public boolean isClosed() throws SQLException
    {

        // TODO Auto-generated method stub
        return false;
    }

    public void updateAsciiStream(int arg0, InputStream arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateAsciiStream(String arg0, InputStream arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateBinaryStream(int arg0, InputStream arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateBinaryStream(String arg0, InputStream arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateBlob(int arg0, InputStream arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateBlob(String arg0, InputStream arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateBlob(int arg0, InputStream arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateBlob(String arg0, InputStream arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateCharacterStream(int arg0, Reader arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateCharacterStream(String arg0, Reader arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateCharacterStream(int arg0, Reader arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateCharacterStream(String arg0, Reader arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateClob(int arg0, Reader arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateClob(String arg0, Reader arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateClob(int arg0, Reader arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateClob(String arg0, Reader arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNCharacterStream(int arg0, Reader arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNCharacterStream(String arg0, Reader arg1)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNClob(int arg0, NClob arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNClob(String arg0, NClob arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNClob(int arg0, Reader arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNClob(String arg0, Reader arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNClob(int arg0, Reader arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNClob(String arg0, Reader arg1, long arg2)
                    throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNString(int arg0, String arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateNString(String arg0, String arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateRowId(int arg0, RowId arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateRowId(String arg0, RowId arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException
    {

        // TODO Auto-generated method stub

    }

    public boolean isWrapperFor(Class< ? > arg0) throws SQLException
    {

        // TODO Auto-generated method stub
        return false;
    }

    public <T> T unwrap(Class<T> arg0) throws SQLException
    {

        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}

class ResultSetMetaDataWrapper implements ResultSetMetaData
{

    private ResultSetMetaData wrapped;

    public ResultSetMetaDataWrapper(ResultSetMetaData wrapped)
    {

        this.wrapped = wrapped;
    }

    public String getCatalogName(int column) throws SQLException
    {

        return this.wrapped.getCatalogName(column);
    }

    public String getColumnClassName(int column) throws SQLException
    {

        return this.wrapped.getColumnClassName(column);
    }

    public int getColumnCount() throws SQLException
    {

        return this.wrapped.getColumnCount();
    }

    public int getColumnDisplaySize(int column) throws SQLException
    {

        return this.wrapped.getColumnDisplaySize(column);
    }

    public String getColumnLabel(int column) throws SQLException
    {

        return this.wrapped.getColumnLabel(column);
    }

    public String getColumnName(int column) throws SQLException
    {

        return this.wrapped.getColumnName(column);
    }

    public int getColumnType(int column) throws SQLException
    {

        return this.wrapped.getColumnType(column);
    }

    public String getColumnTypeName(int column) throws SQLException
    {

        return this.wrapped.getColumnTypeName(column);
    }

    public int getPrecision(int column) throws SQLException
    {

        return this.wrapped.getPrecision(column);
    }

    public int getScale(int column) throws SQLException
    {

        int scale = this.wrapped.getScale(column);
        return scale < 0 ? 0 : scale;
    }

    public String getSchemaName(int column) throws SQLException
    {

        return this.wrapped.getSchemaName(column);
    }

    public String getTableName(int column) throws SQLException
    {

        return this.wrapped.getTableName(column);
    }

    public boolean isAutoIncrement(int column) throws SQLException
    {

        return this.wrapped.isAutoIncrement(column);
    }

    public boolean isCaseSensitive(int column) throws SQLException
    {

        return wrapped.isCaseSensitive(column);
    }

    public boolean isCurrency(int column) throws SQLException
    {

        return this.wrapped.isCurrency(column);
    }

    public boolean isDefinitelyWritable(int column) throws SQLException
    {

        return this.wrapped.isDefinitelyWritable(column);
    }

    public int isNullable(int column) throws SQLException
    {

        return this.wrapped.isNullable(column);
    }

    public boolean isReadOnly(int column) throws SQLException
    {

        return this.wrapped.isReadOnly(column);
    }

    public boolean isSearchable(int column) throws SQLException
    {

        return this.wrapped.isSearchable(column);
    }

    public boolean isSigned(int column) throws SQLException
    {

        return this.wrapped.isSigned(column);
    }

    public boolean isWritable(int column) throws SQLException
    {

        return wrapped.isWritable(column);
    }

    public boolean isWrapperFor(Class< ? > arg0) throws SQLException
    {

        return wrapped.isWrapperFor(arg0);
    }

    public <T> T unwrap(Class<T> arg0) throws SQLException
    {

        return wrapped.unwrap(arg0);
    }

}
