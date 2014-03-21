package com.cresposoft.moderndatabase;

import java.sql.*;

/**
 * QueryBuilder builder of queries for mysql
 *
 * @author Cresposoft Technology <miguel.crespo6@gmail.com>
 * @version 0.1
 * @since 2014-03-18
 */
public final class QueryBuilder {

    private static QueryBuilder querybuilder = null;
    private String url;
    private String user;
    private String pass;
    private Connection connection;
    private Statement query;

    private QueryBuilder() {
        this.url = "jdbc:mysql://localhost:3307/tryphp";
        this.user = "root";
        this.pass = "usbw";
        connection();
    }

    private boolean connection() {
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            query = connection.createStatement();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static QueryBuilder getQueryBuilder() {
        if (querybuilder == null) {
            querybuilder = new QueryBuilder();
        }
        return querybuilder;
    }

    public int query_insert(String table, String[] array) throws SQLException {
        return query.executeUpdate("INSERT INTO " + table + " VALUES (NULL ," + buildValues(array) + ")");
    }

    private String buildValues(String[] array) {
        String values = "";
        if (array[0].equals("0")) {
            array[0] = "null";
        }
        for (int i = 1; i < array.length; i++) {
            if (i + 1 < array.length) {
                values += "'" + array[i] + "',";
            } else {
                values += "'" + array[i] + "'";
            }
        }
        return values;
    }

    private String buildValues(String[] array, String[] columns) {
        String values = "SET ";
        //System.out.println(array.length);
        for (int i = 1; i < array.length; i++) {
            if (i + 1 < array.length) {
                values += columns[i] + " = '" + array[i] + "', ";
            } else {
                values += columns[i] + " = '" + array[i] + "'";
            }
        }
        return values;
    }

    public void query_delete(String table, int id) {
        int result;
        try {
            result = query.executeUpdate("DELETE FROM " + table + " WHERE id=" + id);
            //System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query_select(String table, String filter) {
        ResultSet resultSet = null;
        try {
            resultSet = query.executeQuery("SELECT * FROM " + table + " " + filter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int query_update(String table, String[] array, String[] columns) {

        //System.out.println("UPDATE " + table +" "+buildValues(array, columns)+" WHERE id="+array[0]);
        try {
            return query.executeUpdate("UPDATE " + table + " " + buildValues(array, columns) + " WHERE id=" + array[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String[] getColumnsnames(ResultSet result) {
        ResultSetMetaData metadata = null;
        String[] columnsname = new String[0];
        try {
            metadata = result.getMetaData();
            columnsname = new String[metadata.getColumnCount()];
            for (int i = 0; i < metadata.getColumnCount(); i++) {
                columnsname[i] = metadata.getColumnName(i + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columnsname;
    }
}
