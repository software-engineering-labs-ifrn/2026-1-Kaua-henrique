package devKaua.projeto.application;

import devKaua.projeto.domain.Adotante;
import devKaua.projeto.infrastructure.AdotanteRepository; // Ajustado conforme seu import atual
import devKaua.projeto.domain.Endereco;

public class AdotanteService {
    private final AdotanteRepository repository;

    public AdotanteService(AdotanteRepository repository) {
        this.repository = repository;
    }

    public String registrarAdotante(String nome, String cpf, String rua, String numero, String cidade, String telefone, String email) {
        try {
            Endereco endereco = new Endereco(rua, numero, cidade);
            Adotante novoAdotante = Adotante.criar(nome, cpf, telefone, email, endereco);

            repository.salvar(novoAdotante);

            return "SUCESSO";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}