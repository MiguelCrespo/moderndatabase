package com.cresposoft.moderndatabase;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * ModernDataBase Easy manager of the MySQL
 *
 * @author Cresposoft Technology <miguel.crespo6@gmail.com>
 * https://github.com/cresposoft/moderndatabase
 * 
 * @version 0.4
 * @since 2014-04-27
 */
public abstract class ModernDataBase {

    Field[] fields;
    Object obj;
    public Class<? extends Object> _class;
    String table;
    String[] columns;
    QueryBuilder query;
    private static double VERSION = 0.4;

    /**
     * Constructor.
     *
     * @param _class name of the object that you want that this class return
     * .
     * @param table Name of the table in the database that this you want
     * represent.
     */
    public ModernDataBase(Class _class, String table) {
        //this.classname = classname;
        this._class = _class;
        this.table = table;
        query = QueryBuilder.getQueryBuilder();
        this.columns = getColumns();
    }

    public double getVersion() {
        return VERSION;
    }

    /**
     * Return a string array with column names in the table of the database.
     *
     * @return Array with column names in the table of the database.
     */
    public String[] getColumns() {

        ResultSet result = query.query_select(table, "LIMIT 1"); // we limit 1
        return query.getColumnsnames(result);
    }

    /**
     * Get rows in the database depending to filter
     *
     * @param filter Is a filter for get the you search
     * @return Arraylist of Objects with the rows get in the query.
     */
    public <T> ArrayList<T> getRows(String filter) {
        // Class<T> _class2 = (Class<T>) _class;
        ArrayList<T> dataObjects = new ArrayList<T>();

        if (filter.equals("")) {
            filter = "1";
        }
        try {
            String[] row;
            ResultSet result = query.query_select(table, "WHERE " + filter);
            while (result.next()) {
                row = new String[columns.length];
                for (int i = 0; i < columns.length; i++) {
                    row[i] = result.getString(columns[i]);
                }
                dataObjects.add((T) createObject(row));
            }

        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return dataObjects;
    }

    public <T> T find(int id) {
        T object = null;
        try {

            ResultSet result = query.query_select(table, " WHERE id=" + id);
            String[] row = new String[columns.length];

            if (!result.next()) {
                return null;
            }
            for (int i = 0; i < row.length; i++) {
                row[i] = result.getString(columns[i]);

            }
            object = (T) createObject(row);

        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return object;
    }
    public <T> T findbyKey(String key) {
        T object = null;
        try {
            //System.out.println(getValueField(_class.getName() + "Model", "PK", _class.newInstance()));
            //System.out.println(table);
            ResultSet result = query.query_select(table, " WHERE "+getValueField(_class.getName() + "Model", "PK", _class.newInstance())+"='" + key+"'");
            String[] row = new String[columns.length];

            if (!result.next()) {
                return null;
            }
            for (int i = 0; i < row.length; i++) {
                row[i] = result.getString(columns[i]);

            }
            object = (T) createObject(row);

        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return object;
    }

    public <T> T insert(Object object) {
        T objectresult = null;
        try {
            ResultSet result2;
            String[] array = fieldsObject(object);
            String[] row = new String[columns.length];
            int result = query.query_insert(table, array);
            if (result == 1) {
                if (hasField(_class.getName() + "Model", "PK")) {
                    String key = getValueField(_class.getName() + "Model", "PK", object);
                    System.out.println("where " + key + "=" + getValueField(object,key )+ " LIMIT 1");
                     result2 = query.query_select(table, "where " + key+ "='" +getValueField(object,key ) + "' LIMIT 1");
                    row = new String[columns.length];
                }else{
                    // System.out.println("where id=" + array[1] + " LIMIT 1");
                    result2 = query.query_lastid(table);
                    row = new String[columns.length];

                }
                    if (!result2.next()) {
                        return null;
                    }
                    for (int i = 0; i < row.length; i++) {
                        row[i] = result2.getString(columns[i]);
                    }
                    try {
                        objectresult = (T) createObject(row);
                        System.out.println(objectresult.getClass().getName());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }


            }

        } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }

        return objectresult;
    }
    
    public <T> T create(Object object) { 
        return insert(object);
    }

    public int update(Object object) {
        int result = 0;
        try {
            String[] array = fieldsObject(object);
            result = query.query_update(table, array, columns);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean hasField(String classname, String field) {
        try {
            Class _class2 = Class.forName(classname);
            Field aliasField = _class2.getDeclaredField(field);
            return true;
        } catch (NoSuchFieldException | ClassNotFoundException e) {
           // e.printStackTrace();
            return false;
        }
    }

    private String getValueField(String classname, String field, Object object) {
        try {
            Class _class2 = Class.forName(classname);
            Field aliasField = _class2.getDeclaredField(field);
            aliasField.setAccessible(true);
            object = _class2.newInstance();
            Object valor = aliasField.get(object);
            return valor.toString();
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(int id) {  // when the parameter is an int

        query.query_delete(table, id);
        return true;
    }

    public boolean delete(Object object) {
        if (object == null) {
            return false;
        }
        try {
            String[] array = fieldsObject(object);
            query.query_delete(table, Integer.parseInt(array[0]));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }

    private <T> T createObject(String[] fieldsrow) throws IllegalAccessException, InstantiationException, SQLException, NoSuchFieldException {
        obj = _class.newInstance();
        fields = new Field[columns.length];
        for (int i = 0; i < columns.length; i++) {

            fields[i] = _class.getDeclaredField(columns[i]);
            fields[i].setAccessible(true);
        }

        for (int i = 0; i < fieldsrow.length; i++) {
            String nameType = fields[i].getType().getName();
            if (nameType.equals("int")) {
                fields[i].set(obj, Integer.parseInt(fieldsrow[i]));
            } else if (nameType.equals("java.lang.String")) {
                fields[i].set(obj, fieldsrow[i]);
            }
        }
        return (T) obj;
    }

    private String[] fieldsObject(Object object) throws NoSuchFieldException, IllegalAccessException {
        String[] values = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            values[i] = getValueField(object, columns[i]);
        }
        return values;
    }

    private String getValueField(Object object, String field){
        try {
            Field aliasField = _class.getDeclaredField(field);
            aliasField.setAccessible(true);
            Object valor = aliasField.get(object);
            return valor.toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;


    }
}
