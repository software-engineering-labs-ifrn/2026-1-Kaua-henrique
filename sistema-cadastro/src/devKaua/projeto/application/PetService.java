package devKaua.projeto.application;

import devKaua.projeto.domain.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PetService {
    private final PetFiltro petFiltro;
    private final PetRepository repository;

    private final Map<CriterioFiltro, String> criteriosAtivos = new LinkedHashMap<>();

    public PetService(PetRepository repository) {
        this.repository = repository;
        this.petFiltro = new PetFiltro();
    }

    public String cadastrar(int tipoPetInt, int sexoPetInt, String[] endArr,
                            String nome, String raca, String idade, String peso) {
        try {
            TipoAnimal tipo = TipoAnimal.fromValor(tipoPetInt);
            Sexo sexo = Sexo.fromValor(sexoPetInt);
            Endereco endereco = new Endereco(endArr[0], endArr[1], endArr[2]);

            Pet novoPet = Pet.criar(nome, endereco, sexo, tipo, idade, peso, raca);
            repository.salvar(novoPet);
            return "SUCESSO";
        } catch (IllegalArgumentException e) {
            return "ERRO";
        }
    }

    public void limparCriterios() {
        this.criteriosAtivos.clear();
    }

    public Map<String, String> obterCriteriosParaExibicao() {
        Map<String, String> exibicao = new LinkedHashMap<>();
        for (Map.Entry<CriterioFiltro, String> entry : criteriosAtivos.entrySet()) {
            exibicao.put(entry.getKey().descricao(), entry.getValue());
        }
        return exibicao;
    }

    public void adicionarCriterio(int opcaoCrit, String valorRaw) {
        CriterioFiltro crit = CriterioFiltro.fromValor(opcaoCrit);
        String valorFinal = valorRaw;

        if (crit == CriterioFiltro.SEXO) {
            valorFinal = Sexo.fromValor(Integer.parseInt(valorRaw)).tipo();
        } else if (crit == CriterioFiltro.TIPO) {
            valorFinal = TipoAnimal.fromValor(Integer.parseInt(valorRaw)).animal();
        }

        criteriosAtivos.put(crit, valorFinal);
    }

    public List<String> obterDescricoesCriteriosAtivos() {
        List<String> descricoes = new ArrayList<>();
        for (Map.Entry<CriterioFiltro, String> entry : criteriosAtivos.entrySet()) {
            descricoes.add(entry.getKey().descricao() + " = " + entry.getValue());
        }
        return descricoes;
    }

    public void removerCriterioPorIndice(int indiceBaseUm) {
        List<CriterioFiltro> chaves = new ArrayList<>(criteriosAtivos.keySet());
        if (indiceBaseUm > 0 && indiceBaseUm <= chaves.size()) {
            criteriosAtivos.remove(chaves.get(indiceBaseUm - 1));
        }
    }

    public String executarBuscaComCriteriosAtuais() {
        List<Pet> base = repository.listarTodos();
        List<Pet> resultado = criteriosAtivos.isEmpty() ? base : petFiltro.filtrar(base, criteriosAtivos);

        if (resultado.isEmpty()) return "VAZIO";
        return formatarListaParaTexto(resultado);
    }

    public String listarTodos() {
        return formatarListaParaTexto(repository.listarTodos());
    }

    public String obterNomePet(int numeroPet) {
        List<Pet> filtrados = obterListaFiltradaInterna();
        if (numeroPet < 1 || numeroPet > filtrados.size()) return "INVALIDO";
        return filtrados.get(numeroPet - 1).getNome();
    }

    public String alterarCampoPet(int numeroPet, int opcaoCampo, String novoValor) {
        List<Pet> filtrados = obterListaFiltradaInterna();
        if (numeroPet < 1 || numeroPet > filtrados.size()) return "ERRO:Número do pet inválido.";

        Pet pet = filtrados.get(numeroPet - 1);
        try {
            String linhaNova = "";
            switch (opcaoCampo) {
                case 1 -> { pet.alterarNome(novoValor); linhaNova = "1 - " + novoValor; }
                case 2 -> { pet.alterarIdade(novoValor); linhaNova = "5 - " + novoValor + " anos"; }
                case 3 -> { pet.alterarRaca(novoValor); linhaNova = "7 - " + novoValor; }
                case 4 -> { pet.alterarPeso(novoValor); linhaNova = "6 - " + novoValor + "kg"; }
                default -> { return "ERRO:Opção inválida."; }
            }
            repository.atualizar(pet, linhaNova);
            return "SUCESSO";
        } catch (IllegalArgumentException e) {
            return "ERRO:" + e.getMessage();
        }
    }

    public String removerPet(int numeroPet) {
        List<Pet> filtrados = obterListaFiltradaInterna();
        if (numeroPet < 1 || numeroPet > filtrados.size()) return "ERRO";

        repository.deletar(filtrados.get(numeroPet - 1));
        return "SUCESSO";
    }

    private List<Pet> obterListaFiltradaInterna() {
        List<Pet> base = repository.listarTodos();
        return criteriosAtivos.isEmpty() ? base : petFiltro.filtrar(base, criteriosAtivos);
    }

    private String formatarListaParaTexto(List<Pet> lista) {
        StringBuilder builder = new StringBuilder();
        int contador = 0;
        for (Pet pet : lista) {
            builder.append(++contador).append(" - ").append(pet.toString()).append("\n");
        }
        return builder.toString();
    }
}