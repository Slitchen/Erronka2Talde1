package modelo.Combos;

public class ComboIkastetxeaItem {
    private int id;
    private String nombre;

    public ComboIkastetxeaItem(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre; // Esto es lo que se mostrará en el JComboBox
    }
}
