package app;

import com.dam.ad.db.Db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class PrepararBaseDatos {

    public static void main(String[] args) {

        // Abrimos una conexión con PostgreSQL.
        // try-with-resources la cerrará automáticamente.

        try (Connection conn = Db.getConnection()) {

            System.out.println("Conexión establecida.");

            // 1. Escribimos la instrucción SQL.
            String sql = """
                    CREATE TABLE IF NOT EXISTS autor (
                        id SERIAL PRIMARY KEY,
                        nombre VARCHAR(150) NOT NULL
                    )
                    """;

            // 2. Creamos el Statement para ejecutar SQL.
            try (Statement st = conn.createStatement()) {

                //3. Ejecutamos la instrucción en PostgreSQL.
                st.executeUpdate(sql);

                //Instrucción SQL para añadir la columna autor_id a libro.
                // IF NOT EXISTS evita crearla de nuevo si ya existe.
                String sqlColumna = """
                        ALTER TABLE libro
                        ADD COLUMN IF NOT EXISTS autor_id INTEGER
                        """;
                // Ejecutamos la instrucción SQL en PostgreSQL.

                st.executeUpdate(sqlColumna);

                System.out.println("Columna autor_id preparada correctamente.");

                // Creamos la clave foránea solo si todavía no existe.
                // PostgreSQL permite utilizar un bloque DO para comprobarlo.
                String sqlClaveForanea = """
                        DO $$
                        BEGIN
                            IF NOT EXISTS (
                                SELECT 1
                                FROM pg_constraint
                                WHERE conname = 'fk_libro_autor'
                                  AND conrelid = 'libro'::regclass
                            ) THEN
                                ALTER TABLE libro
                                ADD CONSTRAINT fk_libro_autor
                                FOREIGN KEY (autor_id)
                                REFERENCES autor(id);
                            END IF;
                        END
                        $$;
                        """;

                // Ejecutamos la instrucción SQL.
                st.executeUpdate(sqlClaveForanea);

                System.out.println("Clave foránea fk_libro_autor preparada correctamente.");

                // Evitamos que se puedan registrar dos autores con el mismo nombre.
                String sqlUnique = """
                        CREATE UNIQUE INDEX IF NOT EXISTS idx_autor_nombre
                        ON autor(nombre)
                        """;

                // Ejecutamos la creación del índice único.
                st.executeUpdate(sqlUnique);

                System.out.println("Índice único de autores preparado correctamente.");

            }

            // Instrucción SQL para insertar autores sin duplicarlos.
            String sqlInsertarAutor = """
                    INSERT INTO autor (nombre)
                    VALUES (?)
                    ON CONFLICT (nombre) DO NOTHING
                    """;

            // Guardamos los nombres de los tres autores.
            String[] autores = {"J. K. Rowling", "Ken Follett", "J. R. R. Tolkien"};

            // Preparamos la instrucción SQL.
            try (PreparedStatement ps = conn.prepareStatement(sqlInsertarAutor)) {

                // Recorremos el array de autores.
                for (String nombre : autores) {

                    // Asignamos el nombre al primer parámetro (?).
                    ps.setString(1, nombre);

                    // Ejecutamos el INSERT y obtenemos las filas insertadas.
                    int filas = ps.executeUpdate();

                    // Informamos del resultado.
                    System.out.println(nombre + ": " + filas + " fila(s) insertada(s).");
                }
            }

            // Consulta SQL para relacionar un libro con su autor.
            // Buscamos el ID real del autor mediante su nombre.
            String sqlRelacionar = """
                    UPDATE libro
                    SET autor_id = (
                        SELECT id
                        FROM autor
                        WHERE nombre = ?
                    )
                    WHERE isbn = ?
                    """;

            // ISBN de los libros existentes.
            // IMPORTANTE: sustituye estos valores por los ISBN reales de tu tabla.
            String[] isbnLibros = {"9780132350884", "9780134685991", "9780201633610" };

            // Autores correspondientes a cada ISBN.
            String[] autoresLibros = {"J. K. Rowling", "Ken Follett", "J. R. R. Tolkien"};

            // Preparamos la instrucción UPDATE.
            try (PreparedStatement ps = conn.prepareStatement(sqlRelacionar)) {

                // Recorremos los libros que queremos relacionar.
                for (int i = 0; i < isbnLibros.length; i++) {

                    // Primer parámetro: nombre del autor.
                    ps.setString(1, autoresLibros[i]);

                    // Segundo parámetro: ISBN del libro.
                    ps.setString(2, isbnLibros[i]);

                    // Ejecutamos UPDATE y contamos las filas afectadas.
                    int filasActualizadas = ps.executeUpdate();

                    // Comprobamos si se ha encontrado el ISBN.
                    if (filasActualizadas > 0) {
                        System.out.println("ISBN " + isbnLibros[i] + ": " + filasActualizadas + " fila(s) actualizada(s).");
                    } else {
                        System.out.println("AVISO: No se encontró el ISBN " + isbnLibros[i]);
                    }
                }
            }

        } catch (SQLException e) {

            //Mostramos el error si falla la conexión.

            System.out.println("Error al conectar con PostgreSQL");
            e.printStackTrace();
        }
    }
}
