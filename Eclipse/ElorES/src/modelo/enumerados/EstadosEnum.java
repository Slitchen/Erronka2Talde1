package modelo.enumerados;

public enum EstadosEnum {
    PENDIENTE("pendiente"),
    ACEPTADA("aceptada"),
    DENEGADA("denegada"),
    CONFLICTO("conflicto");

    private final String valor;

    EstadosEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static EstadosEnum fromValor(String valor) {
        for (EstadosEnum e : values()) {
            if (e.valor.equalsIgnoreCase(valor)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + valor);
    }
}
