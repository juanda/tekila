package jazzyweb.tekila.model;

import java.util.List;

public class Participacion {
    private Long id;
    private Double porcentaje;
    private Usuario usuario;
    private Compra compra;

    public Long getId() {
        return id;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }
}
