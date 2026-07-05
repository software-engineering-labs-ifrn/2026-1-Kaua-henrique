package devKaua.projeto.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class InterfaceUsuarioCLI implements InterfaceDeUsuario {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void iniciarFluxoPrincipal(PetFacade facade) {
        int opcao = 0;
        while (opcao != 3) {
            printMenuPrincipal();
            opcao = selecionarOpcao();

            switch (opcao) {
                case 1 -> gerenciarMenuPets(facade);
                case 2 -> gerenciarMenuPessoas(facade);
                case 3 -> System.out.println("Sistema encerrado com sucesso. Até logo!");
            }
        }
    }

    @Override
    public int selecionarOpcao() {
        int opcao;
        while (true) {
            try {
                System.out.print("Escolha sua opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer
                break;
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            }
        }
        return opcao;
    }

    @Override
    public void printMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1: Gerenciar Pets");
        System.out.println("2: Gerenciar Pessoas (Adotantes/Tutores)");
        System.out.println("3: Sair");
        System.out.println("======================");
    }

    @Override
    public void printSubMenuPets() {
        System.out.println("\n--- SUBMENU: GERENCIAR PETS ---");
        System.out.println("1: Cadastrar um novo pet");
        System.out.println("2: Listar pets por algum critério");
        System.out.println("3: Alterar os dados do pet cadastrado");
        System.out.println("4: Deletar um pet cadastrado");
        System.out.println("5: Listar todos os pets cadastrados");
        System.out.println("6: Voltar ao Menu Principal");
        System.out.println("--------------------------------");
    }

    @Override
    public void printSubMenuPessoas() {
        System.out.println("\n--- SUBMENU: GERENCIAR PESSOAS ---");
        System.out.println("1: Cadastrar um novo adotante");
        System.out.println("2: Alterar os dados do adotante cadastrado");
        System.out.println("3: Voltar ao Menu Principal");
        System.out.println("----------------------------------");
    }

    private void gerenciarMenuPets(PetFacade facade) {
        int opcaoSub = 0;
        while (opcaoSub != 6) {
            printSubMenuPets();
            opcaoSub = selecionarOpcao();
            if (opcaoSub >= 1 && opcaoSub <= 5) {
                facade.executarAcaoPet(opcaoSub);
            } else if (opcaoSub != 6) {
                System.out.println("Opção inválida para o menu de Pets.");
            }
        }
    }

    private void gerenciarMenuPessoas(PetFacade facade) {
        int opcaoSub = 0;
        while (opcaoSub != 3) {
            printSubMenuPessoas();
            opcaoSub = selecionarOpcao();
            if (opcaoSub == 1 || opcaoSub == 2) {
                facade.executarAcaoPessoa(opcaoSub);
            } else if (opcaoSub != 3) {
                System.out.println("Opção inválida para o menu de Pessoas.");
            }
        }
    }


    @Override
    public String solicitarNome() {
        System.out.print("Nome e Sobrenome: ");
        return this.scanner.nextLine();
    }

    @Override
    public String solicitarRaca() {
        System.out.print("Raça do Pet: ");
        return this.scanner.nextLine();
    }

    @Override
    public int solicitarTipo() {
        System.out.print("Tipo do Pet(1=Cachorro/2=Gato): ");
        int tipoPetInt = this.scanner.nextInt();
        this.scanner.nextLine();
        return tipoPetInt;
    }

    @Override
    public int solicitarSexo() {
        System.out.print("Sexo do Pet(1=Macho/2=Fêmea): ");
        int sexoPetStr = this.scanner.nextInt();
        scanner.nextLine();
        return sexoPetStr;
    }

    @Override
    public String solicitarIdade() {
        System.out.print("Idade do Pet: ");
        return this.scanner.nextLine();
    }

    @Override
    public String solicitarPeso() {
        System.out.print("Peso do Pet: ");
        return this.scanner.nextLine();
    }

    @Override
    public String[] solicitarEndereco() {
        System.out.println("Endereço pet encontrado: ");
        System.out.print("Rua: ");
        String rua = this.scanner.nextLine();

        System.out.print("Número da casa: ");
        String numero = this.scanner.nextLine();

        System.out.print("Cidade: ");
        String cidade = this.scanner.nextLine();

        return new String[]{rua, numero, cidade};
    }

    @Override
    public int numeroPetListFiltrada() {
        System.out.println("Informe o número do pet para essa ação: ");
        int numeroPet = this.scanner.nextInt();
        this.scanner.nextLine();
        return numeroPet;
    }

    @Override
    public String confirmacaoDeletarPet(String nomePet) {
        System.out.println("Digite apenas 'SIM ou NÃO'");
        System.out.println("Tem certeza que deseja deletar '" + nomePet + "' do sistema (SIM ou NÃO)? ");
        return this.scanner.nextLine();
    }

    @Override
    public void mensagemDeletarPet() {
        System.out.println("Pet removido com sucesso!");
    }

    @Override
    public int solicitarOpcaoAlterar() {
        System.out.println("---------------------------");
        System.out.println("Digite apenas de '1' a '4'.");
        System.out.println("(1 = nome ou sobrenome/ 2 = idade): ");
        System.out.println("(3 = Raça / 4 = Peso): ");
        int consultaDesejada = this.scanner.nextInt();
        this.scanner.nextLine();
        return consultaDesejada;
    }

    @Override
    public void exibirMensagemAlteracaoConcluida() {
        System.out.println("Dado alterado com sucesso. ");
    }

    @Override
    public void exibirMensagemErrorConsulta() {
        System.out.println("Nenhum pet encontrado com esse dado. ");
    }

    @Override
    public int solicitarCriterioFiltro() {
        System.out.println("---------------------------");
        System.out.println("Escolha o critério de filtro:");
        System.out.println("  1 = Nome ou Sobrenome\n  2 = Idade\n  3 = Raça\n  4 = Peso\n  5 = Sexo\n  6 = Cidade\n  7 = Tipo do animal");
        System.out.println("---------------------------");
        int opcao = this.scanner.nextInt();
        this.scanner.nextLine();
        return opcao;
    }

    @Override
    public String solicitarTextoBusca() {
        System.out.print("Informe o dado para busca: ");
        return this.scanner.nextLine();
    }

    @Override
    public int solicitarSexoParaFiltro() {
        System.out.println("Escolha o sexo para filtrar:");
        System.out.println("  1 = Macho\n  2 = Fêmea");
        int opcao = this.scanner.nextInt();
        this.scanner.nextLine();
        return opcao;
    }

    @Override
    public int solicitarAcaoGerenciamentoCriterios(Map<String, String> criterios) {
        System.out.println("===========================");
        if (criterios.isEmpty()) {
            System.out.println("Nenhum critério aplicado ainda.");
        } else {
            System.out.println("Critérios aplicados:");
            int i = 1;
            for (Map.Entry<String, String> entry : criterios.entrySet()) {
                System.out.println("  " + i + ". " + entry.getKey() + " = " + entry.getValue());
                i++;
            }
        }
        System.out.println("===========================");
        System.out.println("1 = Adicionar critério");
        if (!criterios.isEmpty()) {
            System.out.println("2 = Remover critério");
        }
        System.out.println("3 = Executar busca");
        System.out.println("4 = Sair (voltar ao menu)");
        System.out.println("===========================");

        int acao;
        do {
            acao = this.scanner.nextInt();
            this.scanner.nextLine();
        } while (acao < 1 || acao > 4 || (acao == 2 && criterios.isEmpty()));

        return acao;
    }

    @Override
    public int solicitarCriterioParaRemover(List<String> descricoesCriterios) {
        System.out.println("Qual critério deseja remover?");
        for (int i = 0; i < descricoesCriterios.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + descricoesCriterios.get(i));
        }
        int indice = this.scanner.nextInt();
        this.scanner.nextLine();
        return indice;
    }

    @Override
    public void exibirListaPets(String listaFormatada) {
        System.out.println("Pets Cadastrados com base na sua consulta: \n" + listaFormatada);
    }

    @Override
    public void exibirPet(String petTexto) {
        System.out.println(petTexto);
    }

    @Override
    public void errorExibir(String mensagem) {
        System.out.println("ERRO DE VALIDAÇÃO:");
        System.out.println(mensagem);
    }

    @Override
    public void erroSalvarArquivoPet() {
        System.out.println("Erro ao tentar salvar o arquivo.");
    }

    @Override
    public void erroSalvarObjPet() {
        System.out.println("Erro ao tentar cadastrar novo Pet.");
    }

    @Override
    public int solicitarTipoAnimalParaConsulta() {
        System.out.println("Escolha o tipo de animal:");
        System.out.println("  1 = Cachorro\n  2 = Gato");
        int resposta = this.scanner.nextInt();
        this.scanner.nextLine();
        return resposta;
    }

    @Override
    public int solicitarTipoAnimalParaFiltro() {
        System.out.println("Escolha o tipo de animal para filtrar:");
        System.out.println("  1 = Cachorro\n  2 = Gato");
        int resposta = this.scanner.nextInt();
        this.scanner.nextLine();
        return resposta;
    }

    @Override
    public void leituraFormulario() {
        File formulario = new File("sistema-cadastro/formulario/formulario.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(formulario))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            errorExibir(e.getMessage());
        }
    }

    @Override
    public String solicitarNomeAdotante() {
        System.out.print("Nome Completo do Adotante: ");
        return this.scanner.nextLine();
    }

    @Override
    public String solicitarCpfAdotante() {
        System.out.print("CPF (apenas números): ");
        return this.scanner.nextLine();
    }

    @Override
    public String solicitarRuaAdotante() {
        System.out.print("Endereço - Rua/Logradouro: ");
        return this.scanner.nextLine();
    }

    @Override
    public String solicitarNumeroAdotante() {
        System.out.print("Endereço - Número: ");
        return this.scanner.nextLine();
    }

    @Override
    public String solicitarCidadeAdotante() {
        System.out.print("Endereço - Cidade: ");
        return this.scanner.nextLine();
    }

    @Override
    public String solicitarTelefoneAdotante() {
        System.out.print("Telefone (com DDD, apenas números): ");
        return this.scanner.nextLine();
    }

    @Override
    public String solicitarEmailAdotante() {
        System.out.print("E-mail: ");
        return this.scanner.nextLine();
    }

    @Override
    public void exibirSucesso(String mensagem) {
        System.out.println("\n✅ SUCESSO: " + mensagem + "\n");
    }

    public void exibirListaAdotantes(String listagem) {
        System.out.println("\n=== ADOTANTES ENCONTRADOS ===");
        System.out.println(listagem);
        System.out.println("=============================");
    }

    public int numeroAdotanteListFiltrada() {
        System.out.print("\nDigite o número sequencial do adotante que deseja selecionar: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Por favor, digite um número válido: ");
            scanner.next();
        }
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        return numero;
    }

    public int solicitarOpcaoAlterarAdotante() {
        System.out.println("\n--- O QUE DESEJA ALTERAR? ---");
        System.out.println("1: Nome");
        System.out.println("2: Telefone");
        System.out.println("3: E-mail");
        System.out.println("-----------------------------");
        System.out.print("Escolha uma opção: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Opção inválida. Escolha entre 1 e 3: ");
            scanner.next();
        }
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        return opcao;
    }

    public int solicitarCriterioFiltroAdotante() {
        System.out.println("\n--- FILTRAR ADOTANTE POR: ---");
        System.out.println("1: Nome");
        System.out.println("2: CPF");
        System.out.println("-----------------------------");
        System.out.print("Escolha uma opção: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Opção inválida. Escolha 1 ou 2: ");
            scanner.next();
        }
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        return opcao;
    }
}