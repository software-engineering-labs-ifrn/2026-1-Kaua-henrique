package devKaua.projeto.application;

import devKaua.projeto.domain.Adotante;
import devKaua.projeto.domain.Endereco;
import devKaua.projeto.infrastructure.AdotanteRepository;
import java.util.*;

public class AdotanteService {

    private final AdotanteRepository adotanteRepository;

    private final List<Adotante> listaFiltrada = new ArrayList<>();
    private final Map<Integer, String> criteriosAtivos = new LinkedHashMap<>();

    public AdotanteService(AdotanteRepository adotanteRepository) {
        this.adotanteRepository = adotanteRepository;
    }

    public String registrarAdotante(String nome, String cpf, String rua, String numero, String cidade, String telefone, String email) {
        try {
            Endereco endereco = new Endereco(rua, numero, cidade);
            Adotante novoAdotante = Adotante.criar(nome, cpf, telefone, email, endereco);

            adotanteRepository.salvar(novoAdotante);
            return "SUCESSO";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String alterarCampoAdotante(int numeroAdotante, int opcaoCampo, String novoValor) {
        Adotante adotanteSelecionado = obterAdotantePorIndiceFiltrado(numeroAdotante);

        if (adotanteSelecionado == null) {
            return "ERRO: Adotante não encontrado.";
        }

        try {
            String linhaNova = "";

            switch (opcaoCampo) {
                case 1 -> {
                    adotanteSelecionado.alterarNome(novoValor);
                    linhaNova = "1 - " + adotanteSelecionado.getNome();
                }
                case 2 -> {
                    adotanteSelecionado.alterarTelefone(novoValor);
                    linhaNova = "3 - " + adotanteSelecionado.getTelefone();
                }
                case 3 -> {
                    adotanteSelecionado.alterarEmail(novoValor);
                    linhaNova = "4 - " + adotanteSelecionado.getEmail();
                }
                default -> {
                    return "ERRO: Opção de alteração inválida.";
                }
            }

            boolean atualizouNoArquivo = adotanteRepository.atualizar(adotanteSelecionado, linhaNova);

            if (atualizouNoArquivo) {
                return "SUCESSO";
            } else {
                return "ERRO: Não foi possível atualizar o arquivo físico do adotante.";
            }

        } catch (IllegalArgumentException e) {
            return "ERRO: " + e.getMessage();
        }
    }

    private Adotante obterAdotantePorIndiceFiltrado(int numeroAdotante) {
        int index = numeroAdotante - 1;
        if (index >= 0 && index < listaFiltrada.size()) {
            return listaFiltrada.get(index);
        }
        return null;
    }

    public void limparCriterios() {
        criteriosAtivos.clear();
    }

    public Map<String, String> obterCriteriosParaExibicao() {
        Map<String, String> exibicao = new LinkedHashMap<>();
        exibicao.put("Nome", criteriosAtivos.getOrDefault(1, "Todos"));
        exibicao.put("CPF", criteriosAtivos.getOrDefault(2, "Todos"));
        return exibicao;
    }

    public void adicionarCriterio(int opcao, String valor) {
        if (valor != null && !valor.trim().isEmpty()) {
            criteriosAtivos.put(opcao, valor.trim());
        }
    }

    public List<String> obterDescricoesCriteriosAtivos() {
        List<String> descricoes = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : criteriosAtivos.entrySet()) {
            String nomeCrit = entry.getKey() == 1 ? "Nome" : "CPF";
            descricoes.add(nomeCrit + " = " + entry.getValue());
        }
        return descricoes;
    }

    public void removerCriterioPorIndice(int indice) {
        List<Integer> chaves = new ArrayList<>(criteriosAtivos.keySet());
        if (indice >= 0 && indice < chaves.size()) {
            criteriosAtivos.remove(chaves.get(indice));
        }
    }

    public String executarBuscaComCriteriosAtuais() {
        List<Adotante> todos = adotanteRepository.listarTodos();
        listaFiltrada.clear();

        for (Adotante a : todos) {
            boolean atendeCriterios = true;

            // Filtro por Nome (Opção 1)
            if (criteriosAtivos.containsKey(1)) {
                String buscaNome = criteriosAtivos.get(1).toLowerCase();
                if (!a.getNome().toLowerCase().contains(buscaNome)) {
                    atendeCriterios = false;
                }
            }

            // Filtro por CPF (Opção 2)
            if (criteriosAtivos.containsKey(2)) {
                String buscaCpf = criteriosAtivos.get(2).replaceAll("\\D", "");
                if (!a.getCpf().contains(buscaCpf)) {
                    atendeCriterios = false;
                }
            }

            if (atendeCriterios) {
                listaFiltrada.add(a);
            }
        }

        if (listaFiltrada.isEmpty()) {
            return "VAZIO";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listaFiltrada.size(); i++) {
            sb.append("\n").append(i + 1).append(listaFiltrada.get(i).toString());
        }
        return sb.toString();
    }
}