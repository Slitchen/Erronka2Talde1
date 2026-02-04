package modelo.enumerados;

public enum TiposEnum {
    GOD(1),
    ADMINISTRADOR(2),
    PROFESOR(3),
    ALUMNO(4);

    private final int codigo;
    
    TiposEnum(int codigo) {
        this.codigo = codigo;
    }

   
    public int getCodigo() {
        return codigo;
    }

    public static TiposEnum fromCodigo(int codigo) {
        for (TiposEnum tipo : TiposEnum.values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
