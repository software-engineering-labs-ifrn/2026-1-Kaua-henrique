package devKaua.projeto.application;
public class MenuPrincipal {

    private final InterfaceDeUsario ui;
    private final PetService service;

    public MenuPrincipal(InterfaceDeUsario ui, PetService service) {
        this.ui = ui;
        this.service = service;
    }

    public void run() {
        boolean sairTrue = true;
        while (sairTrue) {
            ui().printMenuPrincipal();
            int opcao = ui().selecionarOpcao();
            switch (opcao) {
                case 1:
                    ui().leituraFormulario();
                    try {
                        service().cadastrar();
                    } catch (RuntimeException e) {
                        ui().errorExibir(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        service().listarPetsPorCriterio();
                    } catch (RuntimeException e) {
                        ui().errorExibir(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        service().alterar();
                    } catch (RuntimeException e) {
                        ui().errorExibir(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        service().remover();
                    } catch (RuntimeException e) {
                        ui().errorExibir(e.getMessage());
                    }
                    break;
                case 5:
                    service().listarPetsCompleta();
                    break;
                case 6:
                    sairTrue = false;
                    break;
            }
        }
    }

    private InterfaceDeUsario ui() {
        return ui;
    }

    private PetService service() {
        return service;
    }
}
