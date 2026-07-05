package devKaua.projeto.presentation;

import devKaua.projeto.application.*;
import devKaua.projeto.domain.PetRepository;
import devKaua.projeto.infrastructure.PetRepositoryTXT;

public class GeradorDaONG {

    public static void main(String[] args) {
        InterfaceDeUsuario ui = new InterfaceUsuarioCLI();
        PetRepository repository = new PetRepositoryTXT("petsCadastrados");
        repository.carregarDados();

        PetService service = new PetService(repository);
        PetFacade facade = new PetFacade(ui, service);

        ui.iniciarFluxoPrincipal(facade);
    }

}