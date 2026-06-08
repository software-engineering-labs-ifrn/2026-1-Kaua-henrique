package devKaua.projeto.application;

import devKaua.projeto.domain.Pet;

public interface PersistenceUnit {
    boolean carregarDados();
    boolean salvar(Pet pet, String enderecoPetStr);
    boolean atualizar(Pet pet, String linhaNova);
    boolean deletar(Pet pet);
}
