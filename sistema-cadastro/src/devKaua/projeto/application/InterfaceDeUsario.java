package devKaua.projeto.application;

import devKaua.projeto.domain.CriterioFiltro;
import devKaua.projeto.domain.Pet;
import devKaua.projeto.domain.Sexo;
import devKaua.projeto.domain.TipoAnimal;

import java.util.List;
import java.util.Map;

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

    int numeroPetListFiltrada();
    String confirmacaoDeletarPet(Pet pet);
    void mensagemDeletarPet();

    int solicitarOpcaoAlterar();

    void exibirMensagemAlteracaoConcluida();

    void exibirMensagemErrorConsulta();

    void exibirPet(Pet pet);

    void errorExibir(String mensagem);

    CriterioFiltro solicitarCriterioFiltro();

    String solicitarTextoBusca();

    Sexo solicitarSexoParaFiltro();

    void exibirListaPets(List<Pet> listaAtual);

    void erroSalvarArquivoPet();

    void erroSalvarObjPet();

    TipoAnimal solicitarTipoAnimalParaConsulta();

    void leituraFormulario();

    int solicitarAcaoGerenciamentoCriterios(Map<CriterioFiltro, String> criterios);

    CriterioFiltro solicitarCriterioParaRemover(Map<CriterioFiltro, String> criterios);

    TipoAnimal solicitarTipoAnimalParaFiltro();
}
