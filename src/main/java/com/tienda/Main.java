package com.tienda;

import DAO.LibroDao;

public class Main {
    public static void main(String[] args) {

        System.out.println(
                Main.class.getClassLoader().getResource("db.properties")
        );
        var dao = new LibroDao();
        System.out.println("=== LIBROS ===");
        dao.findAll().forEach(System.out::println);
    }
}



