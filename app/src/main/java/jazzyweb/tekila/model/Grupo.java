package jazzyweb.tekila.model;

import java.util.List;

public class Grupo {
    private Long id;
    private String nombre;
    private List<Usuario> usuarios;
    private List<Compra> compras;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public String toString(){
        return nombre;
    }
}
