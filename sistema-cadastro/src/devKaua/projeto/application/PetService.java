package devKaua.projeto.application;

import devKaua.projeto.domain.Pet;

import java.util.List;

public interface PetService {
    void cadastrar();
    void alterar();
    void remover();
    void listarPetsCompleta();
    List<Pet> listarPetsPorCriterio();
}