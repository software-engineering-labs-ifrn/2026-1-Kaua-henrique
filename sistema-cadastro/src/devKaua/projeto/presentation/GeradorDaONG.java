package devKaua.projeto.presentation;

import devKaua.projeto.application.InterfaceUsarioCLI;
import devKaua.projeto.application.MenuPrincipal;
import devKaua.projeto.application.PersistenciaDadosTXT;
import devKaua.projeto.application.PetServiceClass;

public class GeradorDaONG {
    public static void main(String[] args) {
        InterfaceUsarioCLI ui = new InterfaceUsarioCLI();
        PersistenciaDadosTXT persistencia = new PersistenciaDadosTXT("petsCadastrados");
        PetServiceClass service = new PetServiceClass(ui,persistencia);
        MenuPrincipal menu = new MenuPrincipal(ui, service);

        persistencia.carregarDados();
        menu.run();
    }
}