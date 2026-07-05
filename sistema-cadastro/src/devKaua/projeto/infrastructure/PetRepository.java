package devKaua.projeto.infrastructure;

import devKaua.projeto.domain.Pet;

import java.util.List;

public interface PetRepository {

    void carregarDados();

    void salvar(Pet pet);

    boolean atualizar(Pet pet, String linhaNova);

    void deletar(Pet pet);

    List<Pet> listarTodos();
}
