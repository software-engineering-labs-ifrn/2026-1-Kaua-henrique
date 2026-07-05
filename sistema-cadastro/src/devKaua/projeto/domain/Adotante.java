package devKaua.projeto.domain;

public class Adotante extends Pessoa {

    // Construtor para Reconstituição (Usado ao ler do TXT)
    public Adotante(Long id, String nome, String cpf, String telefone, String email, Endereco endereco) {
        super(id, nome, cpf, telefone, email, endereco);
    }

    // Construtor privado para criação nova
    private Adotante(String nome, String cpf, String telefone, String email, Endereco endereco) {
        super(nome, cpf, telefone, email, endereco);
    }

    // Factory Method oficial do sistema para novos Adotantes
    public static Adotante criar(String nome, String cpf, String telefone, String email, Endereco endereco) {
        return new Adotante(nome, cpf, telefone, email, endereco);
    }

    @Override
    public String toString() {
        return ". " + getID() + " - " + getNome() + " - CPF: " + getCpf()
                + " - Tel: " + getTelefone() + " - Email: " + getEmail()
                + " - Endereço: " + getEndereco().toString();
    }
}