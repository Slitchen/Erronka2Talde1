package modelo;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * Clase que representa un Centro educativo mapeado desde JSON.
 */
public class Centro implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("CCEN")
    private int idCentro;

    @SerializedName("NOM")
    private String nombreCas;

    @SerializedName("NOME")
    private String nombreEus;

    @SerializedName("DGENRC")
    private String tipoCentroCas;

    @SerializedName("DGENRE")
    private String tipoCentroEus;

    @SerializedName("GENR")
    private String codigoGenr;

    @SerializedName("MUNI")
    private int idMunicipio;

    @SerializedName("DMUNIC")
    private String municipioCas;

    @SerializedName("DMUNIE")
    private String municipioEus;

    @SerializedName("DTERRC")
    private String territorioCas;

    @SerializedName("DTERRE")
    private String territorioEus;

    @SerializedName("DEPE")
    private int dependencia;

    @SerializedName("DTITUC")
    private String titularidadCas;

    @SerializedName("DTITUE")
    private String titularidadEus;

    @SerializedName("DOMI")
    private String direccion;

    @SerializedName("CPOS")
    private int codigoPostal;

    @SerializedName("TEL1")
    private long telefono;

    @SerializedName("TFAX")
    private String fax; 

    @SerializedName("EMAIL")
    private String email;

    @SerializedName("PAGINA")
    private String paginaWeb;

    @SerializedName("COOR_X")
    private double coordenadaX;

    @SerializedName("COOR_Y")
    private double coordenadaY;

    @SerializedName("LATITUD")
    private double latitud;

    @SerializedName("LONGITUD")
    private double longitud;

    // Constructor vacío requerido por GSON
    public Centro() {
    }

    // --- GETTERS Y SETTERS ---

    public int getIdCentro() { return idCentro; }
    public void setIdCentro(int idCentro) { this.idCentro = idCentro; }

    public String getNombreCas() { return nombreCas; }
    public void setNombreCas(String nombreCas) { this.nombreCas = nombreCas; }

    public String getNombreEus() { return nombreEus; }
    public void setNombreEus(String nombreEus) { this.nombreEus = nombreEus; }

    public String getTipoCentroCas() { return tipoCentroCas; }
    public void setTipoCentroCas(String tipoCentroCas) { this.tipoCentroCas = tipoCentroCas; }

    public String getTipoCentroEus() { return tipoCentroEus; }
    public void setTipoCentroEus(String tipoCentroEus) { this.tipoCentroEus = tipoCentroEus; }

    public String getCodigoGenr() { return codigoGenr; }
    public void setCodigoGenr(String codigoGenr) { this.codigoGenr = codigoGenr; }

    public int getIdMunicipio() { return idMunicipio; }
    public void setIdMunicipio(int idMunicipio) { this.idMunicipio = idMunicipio; }

    public String getMunicipioCas() { return municipioCas; }
    public void setMunicipioCas(String municipioCas) { this.municipioCas = municipioCas; }

    public String getMunicipioEus() { return municipioEus; }
    public void setMunicipioEus(String municipioEus) { this.municipioEus = municipioEus; }

    public String getTerritorioCas() { return territorioCas; }
    public void setTerritorioCas(String territorioCas) { this.territorioCas = territorioCas; }

    public String getTerritorioEus() { return territorioEus; }
    public void setTerritorioEus(String territorioEus) { this.territorioEus = territorioEus; }

    public int getDependencia() { return dependencia; }
    public void setDependencia(int dependencia) { this.dependencia = dependencia; }

    public String getTitularidadCas() { return titularidadCas; }
    public void setTitularidadCas(String titularidadCas) { this.titularidadCas = titularidadCas; }

    public String getTitularidadEus() { return titularidadEus; }
    public void setTitularidadEus(String titularidadEus) { this.titularidadEus = titularidadEus; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public int getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(int codigoPostal) { this.codigoPostal = codigoPostal; }

    public long getTelefono() { return telefono; }
    public void setTelefono(long telefono) { this.telefono = telefono; }

    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPaginaWeb() { return paginaWeb; }
    public void setPaginaWeb(String paginaWeb) { this.paginaWeb = paginaWeb; }

    public double getCoordenadaX() { return coordenadaX; }
    public void setCoordenadaX(double coordenadaX) { this.coordenadaX = coordenadaX; }

    public double getCoordenadaY() { return coordenadaY; }
    public void setCoordenadaY(double coordenadaY) { this.coordenadaY = coordenadaY; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

	@Override
	public String toString() {
		return "Centro [id=" + idCentro + ", nombre=" + nombreCas + "]";
	}
}