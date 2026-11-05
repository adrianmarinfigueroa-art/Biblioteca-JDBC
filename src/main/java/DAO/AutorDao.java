package DAO;

import com.dam.ad.db.Db;
import model.Autor;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de acceder a la tabla autor
 * de nuestra base de datos PostgreSQL.
 */

public class AutorDao {

    /**
     * Recupera todos los autores almacenados
     * en la base de datos.
     *
     * @return Lista de autores.
     * @throws SQLException Si ocurre un error de acceso a datos.
     */
    public List<Autor> findAll() throws SQLException {

        // Lista donde guardaremos los autores recuperados.
        List<Autor> autores = new ArrayList<>();

        // Consulta SQL que utilizaremos para recuperar los autores.
        String sql = "SELECT id, nombre FROM autor ORDER BY id";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                //Obtenemos valores de las columnas.
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                //Convertimos la fila en un objeto java.
                Autor autor = new Autor(id, nombre);

                //Guardamos el objeto en la lista.
                autores.add(autor);
            }

        }

        //Devolvemos todos los autores recuperados.
        return autores;
    }

    /**
     * Busca un autor a través de su id.
     *
     * @return deveulve null si no encuentra el autor.
     * @throws SQLException si ocurre un error en el acceso a datos.
     */
    public Autor findById(int id) throws SQLException {

        //Consulta SQL para buscar un autor concreto.
        String sql = "SELECT id, nombre FROM autor WHERE id = ?";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            //Asignamos el ID recibido al primer parámetro ?.
            ps.setInt(1, id);

            //Ejecutamos la consulta y obtenemos los resultados.
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int idAutor = rs.getInt("id");

                    String nombre = rs.getString("nombre");

                    return new Autor(idAutor, nombre);
                }

            }
        }

        return null;
    }

    /**
     * Inserta un autor nuevo en PostgreSQL.
     *
     * @param autor Objeto Autor que queremos insertar.
     * @throws SQLException Si ocurre un error de acceso a datos.
     */
    public void insert(Autor autor) throws SQLException {

        // Consulta SQL para insertar un autor.
        String sql = "INSERT INTO autor (nombre) VALUES (?)";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, autor.getNombre());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {

                    int idGenerado = rs.getInt(1);

                    autor.setId(idGenerado);
                }
            }
        }

    }

}
