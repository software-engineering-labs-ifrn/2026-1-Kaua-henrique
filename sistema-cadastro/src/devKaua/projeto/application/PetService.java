package devKaua.projeto.application;

import devKaua.projeto.domain.*;
import devKaua.projeto.infrastructure.PetRepository;

import java.util.*;

public class PetService {
    private final PetFiltro petFiltro;
    private final PetRepository repository;

    private final List<Pet> subListaPetsDoTutor = new ArrayList<>();
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
            return e.getMessage();
        }
    }

    public String listarPetsDoTutor(Long idTutor) {
        subListaPetsDoTutor.clear();
        List<Pet> todosOsPets = repository.listarTodos();
        StringBuilder sb = new StringBuilder();
        int contador = 1;

        for (Pet pet : todosOsPets) {
            if (pet.getTutorId() != null && pet.getTutorId().equals(idTutor)) {
                subListaPetsDoTutor.add(pet);
                sb.append("\n").append(contador++).append(pet.toString());
            }
        }
        return sb.isEmpty() ? "VAZIO" : sb.toString();
    }

    public String executarDesvinculoUnico(Long idTutor, int numeroPetDaLista) {
        int index = numeroPetDaLista - 1;
        if (index < 0 || index >= subListaPetsDoTutor.size()) {
            return "Operação Abortada: Número sequencial do pet inválido.";
        }

        Pet petAlvo = subListaPetsDoTutor.get(index);

        if (petAlvo.getTutorId() == null || !petAlvo.getTutorId().equals(idTutor)) {
            return "Operação Abortada: O pet selecionado não pertence a este tutor.";
        }

        try {
            boolean atualizou = repository.atualizar(petAlvo, "8 - ");

            if (atualizou) {
                petAlvo.desvincularTutor();
            }

            return "SUCESSO";
        } catch (Exception e) {
            return "Erro técnico ao tentar desvincular o arquivo: " + e.getMessage();
        }
    }

    public String vincularTutorAoPet(Long idAdotante, Long idPet, AdotanteService adotanteService) {
        Optional<Adotante> adotanteOpt = adotanteService.buscarAdotantePorId(idAdotante);
        if (adotanteOpt.isEmpty()) {
            return "Operação Abortada: O ID do Adotante informado não existe no sistema.";
        }
        Adotante adotante = adotanteOpt.get();

        Optional<Pet> petOpt = repository.buscarPorId(idPet);
        if (petOpt.isEmpty()) {
            return "Operação Abortada: O ID do Pet informado não existe no sistema.";
        }
        Pet pet = petOpt.get();

        if (pet.getTutorId() != null && pet.getTutorId() > 0) {
            return "Operação Abortada: Este animal já possui um tutor vinculado!";
        }

        try {
            Tutor tutor = Tutor.promoverAdotante(adotante);
            tutor.adicionarPet(pet);

            pet.vincularTutor(idAdotante);

            repository.atualizar(pet, "8 - " + idAdotante);
            return "SUCESSO";

        } catch (Exception e) {
            return "Erro técnico ao tentar atualizar os arquivos: " + e.getMessage();
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

    public List<Pet> obterListaDeObjetosPets() {
        return repository.listarTodos(); // Retorna a lista de objetos, não a String!
    }

    public void desvincularPetsDoTutor(Long idTutor) {
        List<Pet> todosOsPets = repository.listarTodos();

        for (Pet pet : todosOsPets) {
            if (pet.getTutorId() != null && pet.getTutorId().equals(idTutor)) {
                repository.atualizar(pet, "8 - ");

                pet.alterarNome(pet.getNome());
            }
        }
    }
}