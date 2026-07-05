package devKaua.projeto.presentation;

import devKaua.projeto.application.*;
import devKaua.projeto.infrastructure.AdotanteRepository;
import devKaua.projeto.infrastructure.AdotanteRepositoryTXT;
import devKaua.projeto.infrastructure.PetRepository;
import devKaua.projeto.infrastructure.PetRepositoryTXT;

public class GeradorDaONG {

    public static void main(String[] args) {
        PetRepository petRepository = new PetRepositoryTXT("petsCadastrados");
        petRepository.carregarDados();

        AdotanteRepository adotanteRepository = new AdotanteRepositoryTXT("adotantesCadastradosTXT");
        adotanteRepository.carregarDados();

        PetService petService = new PetService(petRepository);
        AdotanteService adotanteService = new AdotanteService(adotanteRepository);
        InterfaceDeUsuario ui = new InterfaceUsuarioCLI();

        PetFacade facade = new PetFacade(ui, petService, adotanteService);

        ui.iniciarFluxoPrincipal(facade);
    }

}