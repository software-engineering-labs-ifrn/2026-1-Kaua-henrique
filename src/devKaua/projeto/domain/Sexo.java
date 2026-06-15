package devKaua.projeto.domain;

public enum Sexo {
    MACHO(1,"Macho"),
    FEMEA(2,"femea");

    private int valor;
    private String tipo;

    Sexo(int valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    public int getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }
}
