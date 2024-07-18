package com.mycompany.sneaksapp.logica;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String user;
    private String pass;
    private String role;

    // Lista estática de usuarios para el ejemplo.
    private static final List<Usuario> usuarios = new ArrayList<>();

    static {
        usuarios.add(new Usuario("Admin", "Admin1234", "admin"));
        usuarios.add(new Usuario("Seller", "Seller1234", "seller"));
        usuarios.add(new Usuario("Inventory", "Inv1234", "inventory"));
    }

    public Usuario() {
    }

    public Usuario(String user, String pass, String role) {
        this.user = user;
        this.pass = pass;
        this.role = role;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Método para verificar el login y devolver el usuario si las credenciales son correctas.
    public static Usuario verifyLogin(String username, String password) {
        return usuarios.stream()
                       .filter(u -> u.getUser().equals(username) && u.getPass().equals(password))
                       .findFirst()
                       .orElse(null);
    }
}