package app;

import model.Libro;
import DAO.LibroDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import java.util.Optional;

import DAO.AutorDao;

public class CrudDemo {
    public static void main(String[] args) throws SQLException {

        // Creamos el DAO para gestionar los libros.
        LibroDao dao = new LibroDao();

        // Creamos el DAO para consultar los autores de PostgreSQL.
        AutorDao autorDao = new AutorDao();

        // Creamos un Scanner para leer las opciones del usuario.
        Scanner sc = new Scanner(System.in);

        // Variable que almacenará la opción seleccionada.
        int opcion;

        // Repetimos el menú hasta que el usuario seleccione 0.
        do {

            // Mostramos las opciones disponibles.
            System.out.println("\n=== BIBLIOTECA ===");
            System.out.println("1. Listar todos los libros");
            System.out.println("2. Añadir un libro");
            System.out.println("3. Cambiar disponibilidad");
            System.out.println("4. Eliminar un libro");
            System.out.println("5. Listar libros por autor");
            System.out.println("0. Salir");

            // Solicitamos una opción.
            System.out.print("Selecciona una opción: ");

            // Leemos el número introducido.
            opcion = sc.nextInt();

            // Ejecutamos una operación según la opción seleccionada.
            switch (opcion) {

                case 1:
                    // Recuperamos todos los libros de PostgreSQL.
                    System.out.println("\n=== LISTADO DE LIBROS ===");

                    // Mostramos cada libro utilizando su método toString().
                    dao.findAll().forEach(System.out::println);

                    // Salimos del switch.
                    break;

                case 2:
                    // Mostramos el encabezado de la operación.
                    System.out.println("\n=== AÑADIR LIBRO ===");

                    // Recuperamos y mostramos los autores disponibles.
                    System.out.println("Autores disponibles:");
                    autorDao.findAll().forEach(System.out::println);

                    // Limpiamos el salto de línea pendiente de nextInt().
                    sc.nextLine();

                    // Solicitamos el título del libro.
                    System.out.print("Introduce el título: ");
                    String titulo = sc.nextLine();

                    // Solicitamos el ISBN del libro.
                    System.out.print("Introduce el ISBN: ");
                    String isbn = sc.nextLine();

                    // Solicitamos el año de publicación.
                    System.out.print("Introduce el año: ");
                    int anio = sc.nextInt();

                    // Solicitamos la disponibilidad del libro.
                    System.out.print("¿Está disponible? (true/false): ");
                    boolean disponible = sc.nextBoolean();

                    // Solicitamos el identificador del autor.
                    System.out.print("Introduce el ID del autor: ");
                    int autorId = sc.nextInt();

                    // Buscamos el autor seleccionado en PostgreSQL.
                    if (autorDao.findById(autorId) == null) {

                        // Informamos si el identificador no existe.
                        System.out.println("Error: no existe un autor con ese ID.");

                        // Salimos del case 2 sin insertar el libro.
                        break;
                    }

                    // Creamos el objeto Libro con su autor.
                    Libro nuevoLibro = new Libro(
                            titulo,
                            isbn,
                            anio,
                            disponible,
                            autorId
                    );

                    // Guardamos el libro en PostgreSQL.
                    int idGenerado = dao.insert(nuevoLibro);

                    // Informamos del resultado.
                    System.out.println("Libro insertado correctamente con ID: " + idGenerado);

                    break;

                case 3:

                    // Mostramos el encabezado de la operación.
                    System.out.println("\n=== CAMBIAR DISPONIBILIDAD ===");

                    // Solicitamos el identificador del libro.
                    System.out.print("Introduce el ID del libro: ");
                    int idLibro = sc.nextInt();

                    // Buscamos el libro en PostgreSQL mediante su ID.
                    Optional<Libro> libroEncontrado = dao.findById(idLibro);

                    // Comprobamos si el libro existe en la base de datos.
                    if (libroEncontrado.isPresent()) {

                        // Recuperamos el objeto Libro que contiene el Optional.
                        Libro libro = libroEncontrado.get();

                        // Mostramos los datos actuales del libro.
                        System.out.println("Libro encontrado: " + libro);

                        // Mostramos su disponibilidad actual.
                        System.out.println("Disponibilidad actual: " + libro.isDisponible());

                        // Solicitamos la nueva disponibilidad del libro.
                        System.out.print("Introduce la nueva disponibilidad (true/false): ");
                        boolean nuevaDisponibilidad = sc.nextBoolean();

                        // Modificamos el atributo disponible del objeto Libro.
                        libro.setDisponible(nuevaDisponibilidad);

                        // Guardamos los cambios en PostgreSQL.
                        boolean actualizado = dao.update(libro);

                        // Comprobamos si se ha actualizado correctamente.
                        if (actualizado) {
                            System.out.println("Disponibilidad actualizada correctamente.");
                        } else {
                            System.out.println("No se ha podido actualizar el libro.");
                        }

                    } else {

                        // Informamos si no existe un libro con ese ID.
                        System.out.println("No existe ningún libro con el ID: " + idLibro);
                    }

                    break;

                case 4:

                    // Mostramos el encabezado de la operación.
                    System.out.println("\n=== ELIMINAR LIBRO ===");

                    // Solicitamos el identificador del libro.
                    System.out.print("Introduce el ID del libro que quieres eliminar: ");
                    int idEliminar = sc.nextInt();

                    // Buscamos el libro antes de eliminarlo.
                    Optional<Libro> libroEliminar = dao.findById(idEliminar);

                    // Comprobamos si el libro existe.
                    if (libroEliminar.isPresent()) {

                        // Mostramos los datos del libro encontrado.
                        System.out.println("Libro encontrado: " + libroEliminar.get());

                        // Solicitamos confirmación antes de eliminar el libro.
                        System.out.print("¿Seguro que quieres eliminar este libro? (s/n): ");
                        String confirmacion = sc.next();

                        // Comprobamos si el usuario ha confirmado la eliminación.
                        if (confirmacion.equalsIgnoreCase("s")) {

                            // Eliminamos el libro de PostgreSQL mediante LibroDao.
                            boolean eliminado = dao.deleteById(idEliminar);

                            // Comprobamos si se ha eliminado correctamente.
                            if (eliminado) {
                                System.out.println("Libro eliminado correctamente.");
                            } else {
                                System.out.println("No se ha podido eliminar el libro.");
                            }

                        } else {

                            // Cancelamos la operación si el usuario no confirma.
                            System.out.println("Eliminación cancelada.");
                        }

                    } else {

                        // Informamos de que el libro no existe.
                        System.out.println("No existe ningún libro con ese ID.");
                    }

                    break;


                case 5: {

                    // Mostramos el encabezado de la operación.
                    System.out.println("\n=== LISTAR LIBROS POR AUTOR ===");

                    // Mostramos todos los autores registrados en PostgreSQL.
                    System.out.println("Autores disponibles:");
                    autorDao.findAll().forEach(System.out::println);

                    // Solicitamos el ID del autor que queremos consultar.
                    System.out.print("Introduce el ID del autor: ");
                    int idAutor = sc.nextInt();

                    // Comprobamos si el autor existe en la base de datos.
                    if (autorDao.findById(idAutor) == null) {

                        System.out.println("Error: no existe ningún autor con ese ID.");

                    } else {

                        // Recuperamos los libros asociados al autor seleccionado.
                        List<Libro> librosAutor = dao.findByAutorId(idAutor);

                        // Comprobamos si el autor tiene libros registrados.
                        if (librosAutor.isEmpty()) {

                            System.out.println("Este autor no tiene libros registrados.");

                        } else {

                            // Mostramos todos los libros encontrados.
                            System.out.println("\nLibros del autor:");

                            for (Libro libro : librosAutor) {
                                System.out.println(libro);
                            }
                        }
                    }

                    break;
                }


                case 0:
                    // Informamos de que el programa va a finalizar.
                    System.out.println("Saliendo de la biblioteca...");
                    break;

                default:
                    // Se ejecuta si la opción todavía no está implementada.
                    System.out.println("Opción no válida o todavía no implementada.");
                    break;
            }

        } while (opcion != 0);

        // Cerramos el Scanner cuando termina el programa.
        sc.close();
    }

}
