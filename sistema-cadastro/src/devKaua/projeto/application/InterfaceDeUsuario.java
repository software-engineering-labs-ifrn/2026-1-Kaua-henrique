package devKaua.projeto.application;

import java.util.List;
import java.util.Map;

public interface InterfaceDeUsuario {
    void iniciarFluxoPrincipal(PetFacade facade);

    int selecionarOpcao();
    void printMenuPrincipal();

    void printSubMenuPets();
    void printSubMenuPessoas();

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

    void exibirListaAdotantes(String listagem);
    int numeroAdotanteListFiltrada();
    int solicitarOpcaoAlterarAdotante();
    int solicitarCriterioFiltroAdotante();

    int solicitarAcaoGerenciamentoCriterios(Map<String, String> criteriosExibicao);
    int solicitarCriterioParaRemover(List<String> descricoesCriterios);
    int solicitarTipoAnimalParaFiltro();

    String solicitarNomeAdotante();
    String solicitarCpfAdotante();
    String solicitarRuaAdotante();
    String solicitarNumeroAdotante();
    String solicitarCidadeAdotante();
    String solicitarTelefoneAdotante();
    String solicitarEmailAdotante();
    void exibirSucesso(String mensagem);
    String confirmacaoDeletarAdotante(String nomeAdotante);
    void mensagemDeletarAdotante();
    Long solicitarIdAdotante();
    Long solicitarIdPet();
    void exibirListaTutores(String listagem);
}