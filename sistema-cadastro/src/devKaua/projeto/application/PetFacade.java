package devKaua.projeto.application;

import java.util.List;
import java.util.Map;

public class PetFacade {
    private final InterfaceDeUsuario ui;
    private final PetService petService;
    private final AdotanteService adotanteService; // Novo Serviço injetado

    public PetFacade(InterfaceDeUsuario ui, PetService petService, AdotanteService adotanteService) {
        this.ui = ui;
        this.petService = petService;
        this.adotanteService = adotanteService;
    }

    public void executarAcaoPet(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarPet();
            case 2 -> listarPetsPorCriterio();
            case 3 -> alterarPet();
            case 4 -> removerPet();
            case 5 -> listarPetsCompleta();
        }
    }

    public void executarAcaoPessoa(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarAdotante();
            case 2 -> alterarAdotante();
        }
    }

    public void cadastrarAdotante() {
        String nome = ui.solicitarNomeAdotante();
        String cpf = ui.solicitarCpfAdotante();
        String rua = ui.solicitarRuaAdotante();
        String numero = ui.solicitarNumeroAdotante();
        String cidade = ui.solicitarCidadeAdotante();
        String telefone = ui.solicitarTelefoneAdotante();
        String email = ui.solicitarEmailAdotante();

        String resposta = adotanteService.registrarAdotante(nome, cpf, rua, numero, cidade, telefone, email);

        if ("SUCESSO".equals(resposta)) {
            ui.exibirSucesso("Adotante cadastrado com sucesso!");
        } else {
            ui.errorExibir(resposta);
        }
    }

    public void alterarAdotante() {
        // Usa o gerenciador de critérios específico para Adotantes
        if (!gerenciarCriteriosFluxoAdotantes()) return;

        String listagem = adotanteService.executarBuscaComCriteriosAtuais();
        if ("VAZIO".equals(listagem)) {
            ui.exibirMensagemErrorConsulta();
            return;
        }
        ui.exibirListaAdotantes(listagem); // Certifique-se de ter esse método na UI

        int numeroAdotante = ui.numeroAdotanteListFiltrada(); // Método na UI para pegar o índice escolhido
        int opcaoCampo = ui.solicitarOpcaoAlterarAdotante(); // Opções: 1-Nome, 2-Telefone, 3-Email

        String novoValor = switch (opcaoCampo) {
            case 1 -> ui.solicitarNomeAdotante();
            case 2 -> ui.solicitarTelefoneAdotante();
            case 3 -> ui.solicitarEmailAdotante();
            default -> "";
        };

        String resultado = adotanteService.alterarCampoAdotante(numeroAdotante, opcaoCampo, novoValor);

        if ("SUCESSO".equals(resultado)) {
            ui.exibirMensagemAlteracaoConcluida(); // "Alteração concluída com sucesso!"
        } else if (resultado.startsWith("ERRO:")) {
            ui.errorExibir(resultado.substring(5)); // Exibe o erro de validação vindo do Domínio
        }
    }

    private boolean gerenciarCriteriosFluxoAdotantes() {
        adotanteService.limparCriterios();
        while (true) {
            Map<String, String> dadosExibicao = adotanteService.obterCriteriosParaExibicao();
            int acao = ui.solicitarAcaoGerenciamentoCriterios(dadosExibicao);

            switch (acao) {
                case 1 -> {
                    int opcaoCrit = ui.solicitarCriterioFiltroAdotante(); // Ex: 1-Nome, 2-CPF
                    String valor = ui.solicitarTextoBusca();
                    adotanteService.adicionarCriterio(opcaoCrit, valor);
                }
                case 2 -> {
                    List<String> descricoes = adotanteService.obterDescricoesCriteriosAtivos();
                    int indice = ui.solicitarCriterioParaRemover(descricoes);
                    adotanteService.removerCriterioPorIndice(indice);
                }
                case 3 -> { return true; } // Filtrar e continuar
                case 4 -> { return false; } // Cancelar e voltar
            }
        }
    }

    public void cadastrarPet() {
        int tipo = ui.solicitarTipo();
        int sexo = ui.solicitarSexo();
        String[] endereco = ui.solicitarEndereco();
        String nome = ui.solicitarNome();
        String raca = ui.solicitarRaca();
        String idade = ui.solicitarIdade();
        String peso = ui.solicitarPeso();

        String resposta = petService.cadastrar(tipo, sexo, endereco, nome, raca, idade, peso);

        if ("SUCESSO".equals(resposta)) {
            // Sucesso opcionalmente tratado pelo fluxo principal
        } else {
            ui.erroSalvarObjPet();
        }
    }

    public void listarPetsCompleta() {
        String resultado = petService.listarTodos();
        ui.exibirListaPets(resultado);
    }

    public void alterarPet() {
        if (!gerenciarCriteriosFluxoPets()) return;

        String listagem = petService.executarBuscaComCriteriosAtuais();
        if ("VAZIO".equals(listagem)) {
            ui.exibirMensagemErrorConsulta();
            return;
        }
        ui.exibirListaPets(listagem);

        int numeroPet = ui.numeroPetListFiltrada();
        int opcaoCampo = ui.solicitarOpcaoAlterar();

        String novoValor = switch (opcaoCampo) {
            case 1 -> ui.solicitarNome();
            case 2 -> ui.solicitarIdade();
            case 3 -> ui.solicitarRaca();
            case 4 -> ui.solicitarPeso();
            default -> "";
        };

        String resultado = petService.alterarCampoPet(numeroPet, opcaoCampo, novoValor);

        if ("SUCESSO".equals(resultado)) {
            ui.exibirMensagemAlteracaoConcluida();
        } else if (resultado.startsWith("ERRO:")) {
            ui.errorExibir(resultado.substring(5));
        }
    }

    public void removerPet() {
        if (!gerenciarCriteriosFluxoPets()) return;

        String listagem = petService.executarBuscaComCriteriosAtuais();
        if ("VAZIO".equals(listagem)) {
            ui.exibirMensagemErrorConsulta();
            return;
        }
        ui.exibirListaPets(listagem);

        int numeroPet = ui.numeroPetListFiltrada();
        String nomePet = petService.obterNomePet(numeroPet);

        if ("INVALIDO".equals(nomePet)) {
            ui.errorExibir("Número do pet inválido.");
            return;
        }

        String confirmacao = ui.confirmacaoDeletarPet(nomePet);

        if (confirmacao.equalsIgnoreCase("SIM")) {
            petService.removerPet(numeroPet);
            ui.mensagemDeletarPet();
        }
    }

    public void listarPetsPorCriterio() {
        if (!gerenciarCriteriosFluxoPets()) return;

        String listagem = petService.executarBuscaComCriteriosAtuais();
        if ("VAZIO".equals(listagem)) {
            ui.exibirMensagemErrorConsulta();
            return;
        }
        ui.exibirListaPets(listagem);
    }

    private boolean gerenciarCriteriosFluxoPets() {
        petService.limparCriterios();
        while (true) {
            Map<String, String> dadosExibicao = petService.obterCriteriosParaExibicao();
            int acao = ui.solicitarAcaoGerenciamentoCriterios(dadosExibicao);

            switch (acao) {
                case 1 -> {
                    int opcaoCrit = ui.solicitarCriterioFiltro();
                    String valor = switch (opcaoCrit) {
                        case 5 -> String.valueOf(ui.solicitarSexoParaFiltro());
                        case 7 -> String.valueOf(ui.solicitarTipoAnimalParaFiltro());
                        default -> ui.solicitarTextoBusca();
                    };
                    petService.adicionarCriterio(opcaoCrit, valor);
                }
                case 2 -> {
                    List<String> descricoes = petService.obterDescricoesCriteriosAtivos();
                    int indice = ui.solicitarCriterioParaRemover(descricoes);
                    petService.removerCriterioPorIndice(indice);
                }
                case 3 -> { return true; }
                case 4 -> { return false; }
            }
        }
    }
}