package devKaua.projeto.presentation;

import devKaua.projeto.application.InterfaceDeUsario;
import devKaua.projeto.application.InterfaceUsarioCLI;
import devKaua.projeto.application.MenuPrincipal;
import devKaua.projeto.application.PetService;
import devKaua.projeto.domain.PetRepository;
import devKaua.projeto.infrastructure.PetRepositoryTXT;

public class GeradorDaONG {

    public static void main(String[] args) {
        InterfaceDeUsario ui = new InterfaceUsarioCLI();
        PetRepository repository = new PetRepositoryTXT("petsCadastrados");
        PetService service = new PetService(ui, repository);
        MenuPrincipal menu = new MenuPrincipal(ui, service);

        repository.carregarDados();
        menu.run();
    }

}