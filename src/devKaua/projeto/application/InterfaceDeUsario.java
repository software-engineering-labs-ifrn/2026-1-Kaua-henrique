package devKaua.projeto.application;

import devKaua.projeto.domain.Pet;

import java.util.List;

public interface InterfaceDeUsario {
    int selecionarOpcao();
    void printMenuPrincipal();

    String solicitarNome();
    String solicitarRaca();
    int solicitarTipo();
    int solicitarSexo();
    String solicitarIdade();
    String solicitarPeso();
    String[] solicitarEndereco();

    int solicitarConfirmacaoSimNao();

    int numeroPetListFiltrada();
    String confirmacaoDeletarPet(Pet pet);
    void mensagemDeletarPet();

    int solicitarOpcaoAlterar();

    void exibirMensagemAlteracaoConcluida();

    void exibirMensagemErrorConsulta();

    void exibirPet(Pet pet);

    void errorExibir(String mensagem);

    int solicitarOpcaoFiltro();

    String solicitarTextoBusca();

    void exibirListaPets(List<Pet> listaAtual);

    void erroSalvarArquivoPet();

    void erroSalvarObjPet();

    int consultaCachorroOuGato();
}
