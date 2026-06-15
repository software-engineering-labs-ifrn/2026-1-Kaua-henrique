package devKaua.projeto.application;

import devKaua.projeto.domain.CriterioFiltro;
import devKaua.projeto.domain.Pet;
import devKaua.projeto.domain.Sexo;
import devKaua.projeto.domain.TipoAnimal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InterfaceUsarioCLI implements InterfaceDeUsario {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public int selecionarOpcao() {
        int opcao;
        do {
            System.out.println("Escolha sua opção: (Digite apenas de 1 a 6)");
            opcao = scanner.nextInt();
            scanner.nextLine();
        } while (opcao < 1 || opcao > 6);
        return opcao;
    }

    @Override
    public void printMenuPrincipal() {
        System.out.println("1: Cadastrar um novo pet");
        System.out.println("2: Listar pets por algum critério");
        System.out.println("3: Alterar os dados do pet cadastrado");
        System.out.println("4: Deletar um pet cadastrado");
        System.out.println("5: Lista todos os pets cadastrados");
        System.out.println("6: Sair");
        System.out.println();
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
    public String confirmacaoDeletarPet(Pet pet) {
        System.out.println("Digite apenas 'SIM ou NÃO'");
        System.out.println("Tem certeza que deseja deletar '" + pet.getNome() + "' do sistema (SIM ou NÃO)? ");
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
    public CriterioFiltro solicitarCriterioFiltro() {
        CriterioFiltro criterio = null;
        while (criterio == null) {
            System.out.println("---------------------------");
            System.out.println("Escolha o critério de filtro:");
            for (CriterioFiltro c : CriterioFiltro.values()) {
                System.out.println("  " + c.valor() + " = " + c.descricao());
            }
            System.out.println("---------------------------");
            try {
                int opcao = this.scanner.nextInt();
                this.scanner.nextLine();
                criterio = CriterioFiltro.fromValor(opcao);
            } catch (IllegalArgumentException e) {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
        return criterio;
    }

    @Override
    public String solicitarTextoBusca() {
        System.out.print("Informe o dado para busca: ");
        return this.scanner.nextLine();
    }

    @Override
    public Sexo solicitarSexoParaFiltro() {
        Sexo sexo = null;
        while (sexo == null) {
            System.out.println("Escolha o sexo para filtrar:");
            for (Sexo s : Sexo.values()) {
                System.out.println("  " + s.valor() + " = " + s.tipo());
            }
            try {
                int opcao = this.scanner.nextInt();
                this.scanner.nextLine();
                sexo = Sexo.fromValor(opcao);
            } catch (IllegalArgumentException e) {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
        return sexo;
    }

    @Override
    public int solicitarAcaoGerenciamentoCriterios(Map<CriterioFiltro, String> criterios) {
        System.out.println("===========================");
        if (criterios.isEmpty()) {
            System.out.println("Nenhum critério aplicado ainda.");
        } else {
            System.out.println("Critérios aplicados:");
            int i = 1;
            for (Map.Entry<CriterioFiltro, String> entry : criterios.entrySet()) {
                System.out.println("  " + i + ". " + entry.getKey().descricao() + " = " + entry.getValue());
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
    public CriterioFiltro solicitarCriterioParaRemover(Map<CriterioFiltro, String> criterios) {
        List<CriterioFiltro> chaves = new ArrayList<>(criterios.keySet());
        System.out.println("Qual critério deseja remover?");
        for (int i = 0; i < chaves.size(); i++) {
            CriterioFiltro c = chaves.get(i);
            System.out.println("  " + (i + 1) + ". " + c.descricao() + " = " + criterios.get(c));
        }

        int indice;
        do {
            indice = this.scanner.nextInt();
            this.scanner.nextLine();
        } while (indice < 1 || indice > chaves.size());

        return chaves.get(indice - 1);
    }

    @Override
    public void exibirListaPets(List<Pet> listaAtual) {
        StringBuilder builder = new StringBuilder();
        int contador = 0;
        for (Pet pet : listaAtual) {
            builder.append(++contador).append(" - ").append(pet.toString()).append("\n");
        }
        System.out.println("Pets Cadastrados com base na sua consulta: \n" + builder);
    }

    @Override
    public void exibirPet(Pet pet) {
        System.out.println(pet.toString());
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
    public TipoAnimal solicitarTipoAnimalParaConsulta() {
        TipoAnimal tipo = null;
        while (tipo == null) {
            System.out.println("Escolha o tipo de animal:");
            System.out.println("  1 = Cachorro");
            System.out.println("  2 = Gato");
            try {
                int resposta = this.scanner.nextInt();
                this.scanner.nextLine();
                tipo = TipoAnimal.fromValor(resposta);
            } catch (IllegalArgumentException e) {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
        return tipo;
    }

    @Override
    public TipoAnimal solicitarTipoAnimalParaFiltro() {
        TipoAnimal tipo = null;
        while (tipo == null) {
            System.out.println("Escolha o tipo de animal para filtrar:");
            for (TipoAnimal t : TipoAnimal.values()) {
                System.out.println("  " + t.valor() + " = " + t.animal());
            }
            try {
                int resposta = this.scanner.nextInt();
                this.scanner.nextLine();
                tipo = TipoAnimal.fromValor(resposta);
            } catch (IllegalArgumentException e) {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
        return tipo;
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
}
