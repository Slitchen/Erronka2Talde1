package edu.controller.dto;

import modelo.Reuniones;

public class ReunionDTO {
    public Integer idReunion;
    public Integer idAlumno;
    public Integer idProfesor;
    public String estado;
    public String fecha;
    public String titulo;
    public String asunto;
    public String aula;
    public String idCentro;

    public ReunionDTO() {}

    public ReunionDTO(Reuniones reunion) {
        this.idReunion = reunion.getIdReunion();
        this.idAlumno = reunion.getUsersByAlumnoId().getId();
        this.idProfesor = reunion.getUsersByProfesorId().getId();
        this.estado = reunion.getEstado();
        this.fecha = reunion.getFecha().toString(); // "yyyy-MM-dd HH:mm:ss"
        this.titulo = reunion.getTitulo();
        this.asunto = reunion.getAsunto();
        this.aula = reunion.getAula();
        this.idCentro = reunion.getIdCentro();
    }



	public Integer getIdReunion() {
		return idReunion;
	}

	public void setIdReunion(Integer idReunion) {
		this.idReunion = idReunion;
	}

	public Integer getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(Integer idAlumno) {
		this.idAlumno = idAlumno;
	}

	public Integer getIdProfesor() {
		return idProfesor;
	}

	public void setIdProfesor(Integer idProfesor) {
		this.idProfesor = idProfesor;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public String getIdCentro() {
		return idCentro;
	}

	public void setIdCentro(String idCentro) {
		this.idCentro = idCentro;
	}
	
	
    
}
