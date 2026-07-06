package devKaua.projeto.application;

import devKaua.projeto.domain.Adotante;
import devKaua.projeto.domain.Endereco;
import devKaua.projeto.domain.Pet;
import devKaua.projeto.domain.Tutor;
import devKaua.projeto.infrastructure.AdotanteRepository;
import devKaua.projeto.infrastructure.PetRepository;

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

    public Optional<Adotante> buscarAdotantePorId(Long id) {
        return adotanteRepository.buscarPorId(id);
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

    public String obterNomeAdotante(int numeroAdotante) {
        Adotante adotante = obterAdotantePorIndiceFiltrado(numeroAdotante);
        return (adotante != null) ? adotante.getNome() : "INVALIDO";
    }

    public void removerAdotante(int numeroAdotante) {
        Adotante adotanteSelecionado = obterAdotantePorIndiceFiltrado(numeroAdotante);
        if (adotanteSelecionado != null) {
            // Remove do arquivo físico TXT através do repositório
            adotanteRepository.deletar(adotanteSelecionado);
            // Limpa da lista filtrada local da sessão
            listaFiltrada.remove(adotanteSelecionado);
        }
    }
    private List<Long> obterIdsComPets(PetService petService) {
        List<Long> idsComPets = new ArrayList<>();
        for (Pet pet : petService.obterListaDeObjetosPets()) {
            if (pet.getTutorId() != null && pet.getTutorId() > 0) {
                idsComPets.add(pet.getTutorId());
            }
        }
        return idsComPets;
    }

    public String obterNomeAdotantePorIndiceFiltrado(int numeroTutor) {
        int index = numeroTutor - 1;
        if (index >= 0 && index < listaFiltrada.size()) {
            return listaFiltrada.get(index).getNome();
        }
        return "INVALIDO";
    }

    public Long removerTutorEObterId(int numeroTutor) {
        int index = numeroTutor - 1;
        if (index >= 0 && index < listaFiltrada.size()) {
            Adotante alvo = listaFiltrada.get(index);
            Long idApanhado = alvo.getID();

            adotanteRepository.deletar(alvo);
            listaFiltrada.remove(index);

            return idApanhado; // Retorna o ID puro
        }
        return null;
    }

    public String executarAlteracaoTutor(int numeroTutor, int opcaoCampo, String novoValor, PetService petService) {
        if (numeroTutor < 1 || numeroTutor > listaFiltrada.size()) {
            return "ERRO:Número do tutor inválido.";
        }

        Adotante alvo = listaFiltrada.get(numeroTutor - 1);

        try {
            String linhaNova = "";

            switch (opcaoCampo) {
                case 1 -> {
                    alvo.alterarNome(novoValor);
                    linhaNova = "1 - " + alvo.getNome();
                }
                case 2 -> {
                    alvo.alterarTelefone(novoValor);
                    linhaNova = "3 - " + alvo.getTelefone();
                }
                case 3 -> {
                    alvo.alterarEmail(novoValor);
                    linhaNova = "4 - " + alvo.getEmail();
                }
                default -> {
                    return "ERRO:Opção inválida.";
                }
            }
            adotanteRepository.atualizar(alvo, linhaNova);
            Tutor tutorAtualizado = Tutor.promoverAdotante(alvo);

            for (Pet p : petService.obterListaDeObjetosPets()) {
                if (p.getTutorId() != null && p.getTutorId().equals(tutorAtualizado.getID())) {
                    tutorAtualizado.adicionarPet(p);
                }
            }

            return tutorAtualizado.toString();

        } catch (IllegalArgumentException e) {
            return "ERRO:" + e.getMessage();
        } catch (Exception e) {
            return "ERRO:Erro técnico ao atualizar arquivo: " + e.getMessage();
        }
    }

    // LISTAR TODOS OS ADOTANTES (Sem Pet)
    public String listarTodosAdotantesPuros(PetService petService) {
        List<Adotante> todos = adotanteRepository.listarTodos();
        List<Long> idsTutores = obterIdsComPets(petService);
        StringBuilder sb = new StringBuilder();
        int contador = 1;

        for (Adotante a : todos) {
            if (!idsTutores.contains(a.getID())) {
                sb.append("\n").append(contador++).append(a.toString());
            }
        }
        return sb.isEmpty() ? "VAZIO" : sb.toString();
    }

    // LISTAR TODOS OS TUTORES (Com Pet)
    public String listarTodosTutores(PetService petService) {
        List<Adotante> todos = adotanteRepository.listarTodos();
        List<Long> idsTutores = obterIdsComPets(petService);
        StringBuilder sb = new StringBuilder();
        int contador = 1;

        for (Adotante a : todos) {
            if (idsTutores.contains(a.getID())) {
                Tutor tutor = Tutor.promoverAdotante(a);
                for (Pet p : petService.obterListaDeObjetosPets()) {
                    if (p.getTutorId() != null && p.getTutorId().equals(tutor.getID())) {
                        tutor.adicionarPet(p);
                    }
                }
                sb.append("\n").append(contador++).append(". ").append(tutor.toString()).append("\n-------------------");
            }
        }
        return sb.isEmpty() ? "VAZIO" : sb.toString();
    }

    // BUSCA FILTRADA APENAS PARA TUTORES
    public String executarBuscaTutoresComCriterios(PetService petService) {
        List<Adotante> todos = adotanteRepository.listarTodos();
        List<Long> idsTutores = obterIdsComPets(petService);
        listaFiltrada.clear();

        for (Adotante a : todos) {
            // REGRA EXTRA: Precisa ser um tutor ativo
            if (!idsTutores.contains(a.getID())) continue;

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

        if (listaFiltrada.isEmpty()) return "VAZIO";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listaFiltrada.size(); i++) {
            Tutor tutor = Tutor.promoverAdotante(listaFiltrada.get(i));
            for (Pet p : petService.obterListaDeObjetosPets()) {
                if (p.getTutorId() != null && p.getTutorId().equals(tutor.getID())) {
                    tutor.adicionarPet(p);
                }
            }
            sb.append("\n").append(i + 1).append(". ").append(tutor.toString()).append("\n-------------------");
        }
        return sb.toString();
    }

    public Long obterIdAdotantePorIndiceFiltrado(int numeroTutor) {
        int index = numeroTutor - 1;
        if (index >= 0 && index < listaFiltrada.size()) {
            return listaFiltrada.get(index).getID();
        }
        return null;
    }
}