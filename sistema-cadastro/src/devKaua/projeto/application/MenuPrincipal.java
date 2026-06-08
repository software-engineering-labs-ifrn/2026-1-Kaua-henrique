package devKaua.projeto.application;

public class MenuPrincipal {
    private final InterfaceUsarioCLI ui;
    private final PetService service;

    public MenuPrincipal(InterfaceUsarioCLI ui, PetServiceClass service) {
        this.ui = ui;
        this.service = service;
    }

    public void run() {
        boolean sairTrue = true;
        while (sairTrue) {
            getUi().printMenuPrincipal();
            int opcao = getUi().selecionarOpcao();
            switch (opcao) {
                case 1:
                    getUi().leituraFormulario();
                    try {
                        getService().cadastrar();
                    } catch (RuntimeException e) {
                        getUi().errorExibir(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        getService().listarPetsPorCriterio();
                    } catch (RuntimeException e) {
                        getUi().errorExibir(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        getService().alterar();
                    } catch (RuntimeException e) {
                        getUi().errorExibir(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        getService().remover();
                    } catch (RuntimeException e) {
                        getUi().errorExibir(e.getMessage());
                    }
                    break;
                case 5:
                    getService().listarPetsCompleta();
                    break;
                case 6:
                    sairTrue = false;
                    break;
            }
        }
    }

    private InterfaceUsarioCLI getUi() {
        return ui;
    }

    private PetService getService() {
        return service;
    }
}
