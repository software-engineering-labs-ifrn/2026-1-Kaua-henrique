package devKaua.projeto.domain;

public enum TipoAnimal {
    CACHORRO(1,"Cachorro"),
    GATO(2,"Gato");

    private int valor;
    private String animal;

    TipoAnimal(int valor, String animal) {
        this.animal = animal;
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public String getAnimal() {
        return animal;
    }
}
