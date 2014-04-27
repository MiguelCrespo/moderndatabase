package com.cresposoft.moderndatabase;

import java.lang.reflect.Field;

/**
 * Moderndatabase Configiguration
 *
 * @author Cresposoft Technology <miguel.crespo6@gmail.com>
 * https://github.com/cresposoft/moderndatabase
 * 
 * @version 0.4
 * @since 2014-04-27
 */
public class Config {
    String[] columns = {
            "HOST", "DATABASE", "USERNAME", "PASSWORD", "PORT"
    };
    public static String DATABASE;
    public static String USERNAME ;
    public static String PASSWORD;
    public static String PORT ;
    public static String HOST;
    public static Class _CLASS;
    public Config(){
        try {
            _CLASS = Class.forName("models.Configuration");
            String [] fields = fieldsObject(_CLASS.newInstance());
            HOST = fields[0];
            DATABASE = fields[1];
            USERNAME = fields[2];
            PASSWORD = fields[3];
            PORT = fields[4];
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private String[] fieldsObject(Object object) throws NoSuchFieldException, IllegalAccessException {
        String[] values = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            Field aliasField = _CLASS.getDeclaredField(columns[i]);
            aliasField.setAccessible(true);
            Object valor = aliasField.get(object);
            values[i] = valor.toString();
        }
        return values;
    }
}
