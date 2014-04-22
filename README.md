ModernDataBase (ORM simple para la administracion de bases de datos MYSQL)
================================

ModernDataBase es un ORM que permite el acceso y la administracion de bases de datos mysql de forma simple y sencilla.

==============================
#TUTORIAL

Para apovechar todas las opciones que nos ofrece ***ModernDataBase*** es necesario realizar determinadas configuraciones antes de ponerla a arrancar.

#CONFIGURACION
Paquete "***Models***"
Para comenzar a usar ModernDataBase **es necesario tener dentro del proyecto java un paquete llamado "*models***"  (sin las comillas), **es obligatorio la implementacion de este paquete asi como su nombre**, dentro de este vamos a crear todos los modelos que necesite nuestra aplicacion asi como los datos de connecion a la base de datos.

##Creando un Modelo
**Todo modelo dentro de ModernDataBase necesita extender de la clase *ModernDataBase*** y tambien **es obligatorio que los modelos terminen en *Model*** para cuestion de estandar por ejemplo "UsuarioModel", "RegistroModel", etc. 

Tambien **es obligatorio que cada tabla tenga un ID autoincrement**. 
La estructura general de los modelos es la siguiente:

```java
package models; // es obligatorio tener el modelo dentro del paquete models

import com.cresposoft.moderndatabase.ModernDataBase; /*Importamos la clase ModernDataBase*/

public class UserModel extends ModernDataBase { /* extendemos de ModernDataBase*/

    public static final String TABLE = "users"; /*Nombre de la tabla que representara este modelo en la base de datos*/
    public static final String PK = "email";/*el atributo PK es la clave secundaria en la base de datos (la primaria siempre sera ID que a su vez es autoincrement), 
    en el caso de un usuario su identificador despues del ID en este caso sera el campo email dentro de la base de datos,
    si su tabla no tiene una clave secundaria omita este atributo, no lo deje en blanco*/

    public UserModel() {
        super(User.class, TABLE); /*Llamamos al padre que en este caso es ModernDataBase, 
        y le pasamos como parametro el tipo de objeto que queremos que nos devuelva cada vez que realicemos una consulta. 
        En este ejemplo como este modelo representara a un usuario queremos que nos devuelva cada vez que consultemos, 
        un objeto de la case User (que definiremos adelante). El segundo parametro es el atributo que contiene el nombre 
        de la tabla en la base de datos*/
    }
}
```

Esta es toda la configuracion de los modelos, ahora definiremos la clase User que son los objetos que queremos que el modelo nos retorne.

##Creando objetos

 - Los atributos deben llamarse exactamente igual a su nombre en la tabla de la base de datos, y deben estar en orden de aparicion en la base de datos.
 - El campo ID es obligatorio en las tablas de la base de datos.
 - Declarar los tipos de los atributos de acuerdo a su tipo en la base de datos.
 - Los atributos de los objetos deben ser "private" por estandar.
```java
package models;

import java.util.ArrayList;

public class User {
/*Los atributos deben llamarse exactamente igual a su nombre en la tabla de la base de datos, y deben estar en orden de aparicion en la base de datos*/
    private int id; // Como dijimos el campo ID es obligatorio dentro de la base de datos y debe ser el primer campo en la tabla
    private String name; // No olvidemos declarar los tipos de los atributos de acuerdo a su tipo en la base de datos
    private String email; // Los atributos de los objetos deben ser "private" por estandar
    private String password;
    public User(){

    }
    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public ArrayList<Task> tasks(){
        TaskModel taskModel = new TaskModel();
        System.out.println(id);
        return taskModel.getRows("users_id = "+id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
```

##Configurando base de datos:
El siguiente paso despues de haber configurado los modelos y los objetos es declarar los valores de coneccion con la base de datos, en una clase dentro de models llamada "Configuration".

La estructura de esta clase es la siguiente:

 - Los atributos obligatoriamente deben estar en **MAYUSCULAS**.

```java
package models;

public class Configuration {
    public static String HOST = "localhost";
    public static String DATABASE = "nombredelabasededatos";
    public static String PORT = "3306";
    public static String USERNAME = "root";
    public static String PASSWORD = "";
}
```
Una vez hayas llegado a este paso y hayas realizado los pasos correctamente ModernDataBase estara lista para funcionar, garantizandote un acceso simple a los datos en la base de datos:

#FUNCIONES 
A continuacion tienes una lista con las funciones disponibles, asi como ejemplos:
##getRows():
Nos permite obtener los registros de los modelos, por ejemplo para el modelo ***UserModel*** declarado previamente seria:
```java
UserModel modelousuario = UserModel();
ArrayList<User> usuarios = modelousuario.getRows(""); 
// esto nos devolvera un arraylist con los registros en la tabla de usuarios
```
Si queremos realizar filtraciones ***where*** debemos especificarlas asi:
```java
ArrayList<User> usuarios = modelousuario.getRows("id>7"); 
// esto nos devolvera un array con todos los usuarios cuyos ids sean mayores a 7
```
##find(int id):
Este metodo nos permite encontrar un usuario por su id si le pasamos como parametro un entero, por ejemplo:
```java
UserModel modelousuario = UserModel();
User usuario = modelousuario.find(1); 
// Nos devolvera el objeto usuario cuyo registro en la base de datos tenga de id 1
```
##find(Object object):
Este metodo nos permite encontrar un usuario pasando como parametro un objeto,por ejemplo:
```java
UserModel modelousuario = UserModel();
User usuario1 = new user("Juan", "juan@gmail.com", "contrasena");
User usuario2 = modelousuario.find(usuario1); 
// esto nos devolvera el objeto que representa usuario1 en la base de datos
```
##findbyKey(String key):
Este metodo nos permite buscar en la base de datos por el PK que declaramos anteriormente dentro del modelo, si no declaramos ningun PK no lo podremos usar, por ejemplo en el modelo de usuario declaramos el PK como email, asi que podremos realizar la siguiente operacion:
```java
UserModel modelousuario = UserModel();
User usuario = modelousuario.findbyKey("juan@gmail.com");
//esto nos devolvera un objeto usuario con la informacion de Juan 
```
##insert(Object object):
Este metodo nos permite ingresar un registro a la base de datos, por ejemplo:
```java
UserModel modelousuario = UserModel();
User usuario = new Usuario("Miguel", "miguel.crespo6@gmail.com", "contrasena de miguel");
modelousuario.insert(usuario);
//con esta simple linea ingresamos un nuevo registro en la base de datos esta funcion si lo queremos tambien nos retornara un objeto de tipo User que referenciara al objeto en la base de datos:
User usuario2 = modelousuario.insert(usuario); 
```
##update(Object object):
Este metodo nos permite actualizar un objecto en la base de datos:
```java
UserModel modelousuario = UserModel();
User usuario = modelousuario.findbyKey("miguel.crespo6@gmail.com"); // obtenemos el objeto de la base de datos
usuario.setPassword("nueva contraseña");
modelousuario.update(usuario);
//Con estas lineas habremos actualizado la contraseña del usuario Miguel en la base de datos.
```
##delete(int id):
Este metodo elimina un registro de acuerdo al id
```java
UserModel modelousuario = UserModel();
modelousuario.delete(2);
//con estas lineas hemos eliminado al usuario cuyo id en la base de datos es 2
```
##delete(Object object):
```java
UserModel modelousuario = UserModel();
User usuario = modelousuario.findbyKey("miguel.crespo6@gmail.com");
modelousuario.delete(usuario);
//con estas lineas hemos eliminado al usuario cuyo email es miguel.crespo6@gmail.com.
```







