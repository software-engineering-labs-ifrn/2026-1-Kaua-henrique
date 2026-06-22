package devKaua.projeto.application;

public class MenuPrincipal {
    private final InterfaceDeUsuario ui;
    private final PetFacade facade;

    public MenuPrincipal(InterfaceDeUsuario ui, PetFacade facade) {
        this.ui = ui;
        this.facade = facade;
    }

    public void run() {
        boolean sairTrue = true;
        while (sairTrue) {
            ui.printMenuPrincipal();
            int opcao = ui.selecionarOpcao();
            switch (opcao) {
                case 1:
                    ui.leituraFormulario();
                    try {
                        facade.cadastrarPet();
                    } catch (RuntimeException e) {
                        ui.errorExibir(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        facade.listarPetsPorCriterio();
                    } catch (RuntimeException e) {
                        ui.errorExibir(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        facade.alterarPet();
                    } catch (RuntimeException e) {
                        ui.errorExibir(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        facade.removerPet();
                    } catch (RuntimeException e) {
                        ui.errorExibir(e.getMessage());
                    }
                    break;
                case 5:
                    facade.listarPetsCompleta();
                    break;
                case 6:
                    sairTrue = false;
                    break;
            }
        }
    }
}
