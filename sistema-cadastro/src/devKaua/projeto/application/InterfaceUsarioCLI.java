package devKaua.projeto.application;

import devKaua.projeto.domain.Pet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class InterfaceUsarioCLI implements InterfaceDeUsario {
    Scanner scanner = new Scanner(System.in);

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
    public int solicitarConfirmacaoSimNao() {
        System.out.println("Deseja adicionar mais um critério (1= Sim/2= Não): ");
        int adicionarConsulta = this.scanner.nextInt();
        this.scanner.nextLine();
        return adicionarConsulta;
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
    public int solicitarOpcaoFiltro() {
        System.out.println("---------------------------");
        System.out.println("Digite apenas de '1' a '6'.");
        System.out.println("(1 = nome ou sobrenome/ 2 = idade/ 3 = Raça )");
        System.out.println("(4 = Peso/ 5 = Sexo/ 6 = Cidade )");
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
    public void exibirListaPets(List<Pet> listaAtual) {
        StringBuilder builder = new StringBuilder();
        int contador = 0;
        System.out.println("Pets Cadastrados com base na sua consulta: ");
        for (Pet pet : listaAtual) {
            builder.append(++contador + " - " + pet.toString());
        }
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
    public int consultaCachorroOuGato() {
        System.out.println("Digite apenas '1' e '2'.");
        System.out.println("(1 = Consulta Cachorro/ 2 = Consulta por Gato: ");
        int respostaTipoAnimal = this.scanner.nextInt();
        this.scanner.nextLine();
        return respostaTipoAnimal;
    }

    public void leituraFormulario() {
        File formulario = new File("sistema-cadastro/formulario/formulario.txt");
        try (FileReader fileReader = new FileReader(formulario)) {
            BufferedReader br = new BufferedReader(fileReader);
            String linha;
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            errorExibir(e.getMessage());
        }
    }
}
