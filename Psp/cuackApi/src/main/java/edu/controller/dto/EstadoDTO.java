package edu.controller.dto;

public class EstadoDTO {
    public String estado;

    public EstadoDTO() {}
    public EstadoDTO(String estado) {
        this.estado = estado;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}