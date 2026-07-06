package devKaua.projeto.domain;
public enum CriterioFiltro {
    NOME(1, "Nome ou Sobrenome"),
    IDADE(2, "Idade"),
    RACA(3, "Raça"),
    PESO(4, "Peso"),
    SEXO(5, "Sexo"),
    CIDADE(6, "Cidade"),
    TIPO(7, "Tipo do animal");

    private final int valor;
    private final String descricao;

    CriterioFiltro(int valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public int valor() {
        return valor;
    }

    public String descricao() {
        return descricao;
    }

    public static CriterioFiltro fromValor(int valor) {
        for (CriterioFiltro c : values()) {
            if (c.valor == valor) {
                return c;
            }
        }
        throw new IllegalArgumentException("Critério de filtro inválido: " + valor);
    }
}
