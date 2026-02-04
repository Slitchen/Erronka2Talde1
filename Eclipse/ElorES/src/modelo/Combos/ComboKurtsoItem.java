package modelo.Combos;

public class ComboKurtsoItem {
    private int id;
    private String curso;

    public ComboKurtsoItem(int id, String curso) {
        this.id = id;
        this.curso = curso;
    }

    public int getId() {
        return id;
    }

    public String getCurso() {
        return curso;
    }

    @Override
    public String toString() {
        return curso; 
    }
}
