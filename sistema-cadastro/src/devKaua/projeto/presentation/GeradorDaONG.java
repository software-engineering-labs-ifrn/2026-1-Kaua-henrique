package devKaua.projeto.presentation;

import devKaua.projeto.application.*;
import devKaua.projeto.domain.PetRepository;
import devKaua.projeto.infrastructure.PetRepositoryTXT;

public class GeradorDaONG {

    public static void main(String[] args) {
        InterfaceDeUsuario ui = new InterfaceUsuarioCLI();
        PetRepository repository = new PetRepositoryTXT("petsCadastrados");
        PetService service = new PetService(repository);
        PetFacade facade = new PetFacade(ui, service);
        MenuPrincipal menu = new MenuPrincipal(ui, facade);

        repository.carregarDados();
        menu.run();
    }

}