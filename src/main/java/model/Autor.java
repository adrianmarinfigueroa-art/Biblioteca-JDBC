package model;

/**
 * Representa un autor de la biblioteca.
 *
 * Cada autor tiene un identificador y un nombre.
 */
public class Autor {

    // Identificador del autor en PostgreSQL.
    // Puede ser null antes de insertar el autor.
    private Integer id;

    // Nombre del autor.
    private String nombre;

    /**
     * Constructor para crear un autor nuevo.
     *
     * El ID será null hasta que PostgreSQL
     * genere uno al insertar el autor.
     *
     * @param nombre Nombre del autor.
     */
    public Autor(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Constructor para representar un autor existente.
     *
     * @param id Identificador del autor.
     * @param nombre Nombre del autor.
     */
    public Autor(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Devuelve el identificador del autor.
     *
     * @return ID del autor.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Modifica el identificador del autor.
     *
     * @param id Nuevo identificador.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del autor.
     *
     * @return Nombre del autor.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre del autor.
     *
     * @param nombre Nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve una representación en texto del autor.
     *
     * @return Cadena con el ID y el nombre del autor.
     */
    @Override
    public String toString() {
        return "Autor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
