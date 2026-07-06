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
            case 3 -> removerAdotante();
            case 4 -> listarTodosAdotantesPuros();
            case 5 -> listarTodosTutores();
            case 6 -> buscarTutoresPorCriterio();
            case 7 -> removerTutor();
        }
    }

    public void removerTutor() {
        System.out.println("\n=============================================");
        System.out.println("       PASSO 1: LOCALIZAR O TUTOR            ");
        System.out.println("=============================================");

        if (!gerenciarCriteriosFluxoAdotantes()) return;

        String listagem = adotanteService.executarBuscaTutoresComCriterios(petService);
        if ("VAZIO".equals(listagem)) {
            ui.exibirMensagemErrorConsulta();
            return;
        }
        ui.exibirListaTutores(listagem);

        int numeroTutor = ui.numeroAdotanteListFiltrada();

        String nomeTutor = adotanteService.obterNomeAdotantePorIndiceFiltrado(numeroTutor);
        if ("INVALIDO".equals(nomeTutor)) {
            ui.errorExibir("Número do tutor inválido.");
            return;
        }

        String confirmacao = ui.confirmacaoDeletarTutor(nomeTutor);

        if (confirmacao.equalsIgnoreCase("SIM")) {
            Long idTutorDeletado = adotanteService.removerTutorEObterId(numeroTutor);

            if (idTutorDeletado != null) {
                petService.desvincularPetsDoTutor(idTutorDeletado);
                ui.mensagemDeletarTutorSucesso();
            } else {
                ui.errorExibir("Erro ao processar a deleção do tutor.");
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private void listarTodosAdotantesPuros() {
        String listagem = adotanteService.listarTodosAdotantesPuros(petService);
        if ("VAZIO".equals(listagem)) {
            ui.errorExibir("Nenhum adotante sem pet cadastrado no sistema.");
        } else {
            ui.exibirListaAdotantes(listagem);
        }
    }

    private void listarTodosTutores() {
        String listagem = adotanteService.listarTodosTutores(petService);
        if ("VAZIO".equals(listagem)) {
            ui.errorExibir("Nenhum tutor (adotante com pet) registrado no sistema.");
        } else {
            ui.exibirListaTutores(listagem);
        }
    }

    private void buscarTutoresPorCriterio() {
        // Reutiliza a UI de critérios que você já tem pronta!
        if (!gerenciarCriteriosFluxoAdotantes()) return;

        String listagem = adotanteService.executarBuscaTutoresComCriterios(petService);
        if ("VAZIO".equals(listagem)) {
            ui.exibirMensagemErrorConsulta();
        } else {
            ui.exibirListaTutores(listagem);
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

    public void vincularPetAdotante() {
        // --- PASSO 1: LOCALIZAR O ADOTANTE ---
        System.out.println("\n=============================================");
        System.out.println("   PASSO 1: LOCALIZAR O ADOTANTE DESEJADO   ");
        System.out.println("=============================================");
        if (!gerenciarCriteriosFluxoAdotantes()) {
            System.out.println("Operação cancelada.");
            return;
        }

        String listagemAdotantes = adotanteService.executarBuscaComCriteriosAtuais();
        if ("VAZIO".equals(listagemAdotantes)) {
            ui.exibirMensagemErrorConsulta();
            return;
        }
        ui.exibirListaAdotantes(listagemAdotantes);
        Long idAdotante = ui.solicitarIdAdotante();


        System.out.println("\n=============================================");
        System.out.println("     PASSO 2: LOCALIZAR O PET DESEJADO       ");
        System.out.println("=============================================");
        if (!gerenciarCriteriosFluxoPets()) {
            System.out.println("Operação cancelada.");
            return;
        }

        String listagemPets = petService.executarBuscaComCriteriosAtuais();
        if ("VAZIO".equals(listagemPets)) {
            ui.exibirMensagemErrorConsulta();
            return;
        }
        ui.exibirListaPets(listagemPets);
        Long idPet = ui.solicitarIdPet();

        // --- PASSO 3: EXECUTAR VÍNCULO E VALIDAÇÕES ---
        String resultado = petService.vincularTutorAoPet(idAdotante, idPet, adotanteService);

        if ("SUCESSO".equals(resultado)) {
            ui.exibirSucesso("Adotante promovido a Tutor e Pet vinculado com sucesso!");
        } else {
            ui.errorExibir(resultado);
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

    public void removerAdotante() {
        // Reutiliza o gerenciador de critérios que criamos na US07
        if (!gerenciarCriteriosFluxoAdotantes()) return;

        String listagem = adotanteService.executarBuscaComCriteriosAtuais();
        if ("VAZIO".equals(listagem)) {
            ui.exibirMensagemErrorConsulta();
            return;
        }
        ui.exibirListaAdotantes(listagem);

        int numeroAdotante = ui.numeroAdotanteListFiltrada();
        String nomeAdotante = adotanteService.obterNomeAdotante(numeroAdotante);

        if ("INVALIDO".equals(nomeAdotante)) {
            ui.errorExibir("Número do adotante inválido.");
            return;
        }

        // Pede a confirmação "SIM" ou "NÃO" (reutilizando a lógica visual do pet)
        String confirmacao = ui.confirmacaoDeletarAdotante(nomeAdotante);

        if (confirmacao.equalsIgnoreCase("SIM")) {
            adotanteService.removerAdotante(numeroAdotante);
            ui.mensagemDeletarAdotante();
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