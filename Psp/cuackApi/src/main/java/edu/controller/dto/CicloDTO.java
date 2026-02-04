package edu.controller.dto;

import com.google.gson.annotations.Expose;

public class CicloDTO {

	@Expose
	Integer idCilco;
	@Expose
	String nombreCiclo;
	@Expose
	byte curso;
	
	public CicloDTO() {

	}
	
	public CicloDTO(Integer idCilco, String nombreCiclo, byte curso) {
		super();
		this.idCilco = idCilco;
		this.nombreCiclo = nombreCiclo;
		this.curso = curso;
	}

	public Integer getIdCilco() {
		return idCilco;
	}

	public void setIdCilco(Integer idCilco) {
		this.idCilco = idCilco;
	}

	public String getNombreCiclo() {
		return nombreCiclo;
	}

	public void setNombreCiclo(String nombreCiclo) {
		this.nombreCiclo = nombreCiclo;
	}

	public byte getCurso() {
		return curso;
	}

	public void setCurso(byte curso) {
		this.curso = curso;
	}
	
	
	
}
