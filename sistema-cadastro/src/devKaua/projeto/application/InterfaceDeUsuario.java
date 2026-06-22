package devKaua.projeto.application;

import java.util.List;
import java.util.Map;

public interface InterfaceDeUsuario {
    int selecionarOpcao();
    void printMenuPrincipal();

    String solicitarNome();
    String solicitarRaca();
    int solicitarTipo();
    int solicitarSexo();
    String solicitarIdade();
    String solicitarPeso();
    String[] solicitarEndereco();

    int numeroPetListFiltrada();
    String confirmacaoDeletarPet(String nomePet);
    void mensagemDeletarPet();

    int solicitarOpcaoAlterar();
    void exibirMensagemAlteracaoConcluida();
    void exibirMensagemErrorConsulta();
    void exibirPet(String petTexto);
    void errorExibir(String mensagem);

    int solicitarCriterioFiltro();
    String solicitarTextoBusca();
    int solicitarSexoParaFiltro();
    void exibirListaPets(String listaFormatada);
    void erroSalvarArquivoPet();
    void erroSalvarObjPet();

    int solicitarTipoAnimalParaConsulta();
    void leituraFormulario();

    int solicitarAcaoGerenciamentoCriterios(Map<String, String> criteriosExibicao);
    int solicitarCriterioParaRemover(List<String> descricoesCriterios);
    int solicitarTipoAnimalParaFiltro();
}