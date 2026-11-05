package com.dam.ad.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Properties;

/**
 * Clase encargada de gestionar la conexión con la base de datos PostgreSQL.
 *
 * Lee los datos de conexión desde el archivo db.propertie y proporciona un método para obtener las conexiones JDBC.
 */
public class Db {

    // Almacena las propiedades de conexión:
    // URL de la base de datos, usuario y contraseña.
    private static final Properties PROPS = new Properties();

    static {

        // Busca el archivo db.properties dentro de src/main/resources.
        // try-with-resources cierra automáticamente el InputStream.
        try (InputStream in =

                     Db.class.getClassLoader().getResourceAsStream("db.properties")) {

            if (in == null) {
                throw new RuntimeException("No se encuentra db.properties en src/main/resources");
            }

            //Carga las propiedades del archivo en el objeto PROPS.
            PROPS.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Error cargando db.properties", e);
        }
    }
    /**
     * Establece y devuelve una conexión con PostgreSQL.
     * Utiliza la URL, el usuario y la contraseña almacenados en el archivo db.poperties.
     *
     * @return una conexión JDBC con la base de datos.
     * @throws SQLException si ocurre un error al establecer la conexión.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password")
        );
    }
}

