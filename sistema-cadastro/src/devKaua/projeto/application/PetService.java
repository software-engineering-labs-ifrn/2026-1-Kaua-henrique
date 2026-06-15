package devKaua.projeto.application;

import devKaua.projeto.domain.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PetService {

    private final PetFiltro petFiltro;
    private final InterfaceDeUsario ui;
    private final PetRepository repository;

    public PetService(InterfaceDeUsario ui, PetRepository repository) {
        this.ui = ui;
        this.repository = repository;
        this.petFiltro = new PetFiltro();
    }

    private PetRepository repository() {
        return repository;
    }

    private InterfaceDeUsario ui() {
        return ui;
    }

    public void cadastrar() {
        Pet novoPet = null;

        while (novoPet == null) {
            try {
                int tipoPetInt = ui().solicitarTipo();
                TipoAnimal tipoPet = TipoAnimal.fromValor(tipoPetInt);

                int sexoPetInt = ui().solicitarSexo();
                Sexo sexoPet = Sexo.fromValor(sexoPetInt);

                String[] enderecoPetU = ui().solicitarEndereco();
                Endereco enderecoPet = new Endereco(enderecoPetU[0], enderecoPetU[1], enderecoPetU[2]);

                String nomePet = ui().solicitarNome();
                String racaPet = ui().solicitarRaca();
                String idadePet = ui().solicitarIdade();
                String pesoPet = ui().solicitarPeso();

                novoPet = Pet.criar(nomePet, enderecoPet, sexoPet, tipoPet, idadePet, pesoPet, racaPet);
            } catch (IllegalArgumentException e) {
                ui().erroSalvarObjPet();
            }
        }

        repository().salvar(novoPet);
    }

    public void alterar() {
        List<Pet> listaParaAlteraPet = listarPetsPorCriterio();
        if (listaParaAlteraPet.isEmpty()) {
            return;
        }

        int numeroPet = ui().numeroPetListFiltrada();

        int contador = 0;
        for (Pet pets : listaParaAlteraPet) {
            contador++;
            if (numeroPet == contador) {
                int consultaDesejada;
                do {
                    consultaDesejada = ui().solicitarOpcaoAlterar();
                } while (consultaDesejada < 1 || consultaDesejada > 4);

                switch (consultaDesejada) {
                    case 1:
                        alterarCampoNome(pets);
                        break;
                    case 2:
                        alterarCampoIdade(pets);
                        break;
                    case 3:
                        alterarCampoRaca(pets);
                        break;
                    case 4:
                        alterarCampoPeso(pets);
                        break;
                }
            }
        }
    }

    private void alterarCampoNome(Pet pet) {
        boolean valido = false;
        while (!valido) {
            try {
                String nomeNovo = ui().solicitarNome();
                pet.alterarNome(nomeNovo);
                String novoNomeTxt = "1 - " + nomeNovo;
                boolean atualizado = repository().atualizar(pet, novoNomeTxt);
                if (atualizado) {
                    ui().exibirPet(pet);
                    ui().exibirMensagemAlteracaoConcluida();
                }
                valido = true;
            } catch (IllegalArgumentException e) {
                ui().errorExibir(e.getMessage());
            }
        }
    }

    private void alterarCampoIdade(Pet pet) {
        boolean valido = false;
        while (!valido) {
            try {
                String idadeNovo = ui().solicitarIdade();
                pet.alterarIdade(idadeNovo);
                String novoIdadeTxt = "5 - " + idadeNovo + " anos";
                boolean atualizado = repository().atualizar(pet, novoIdadeTxt);
                if (atualizado) {
                    ui().exibirPet(pet);
                    ui().exibirMensagemAlteracaoConcluida();
                }
                valido = true;
            } catch (IllegalArgumentException e) {
                ui().errorExibir(e.getMessage());
            }
        }
    }

    private void alterarCampoRaca(Pet pet) {
        boolean valido = false;
        while (!valido) {
            try {
                String racaNovo = ui().solicitarRaca();
                pet.alterarRaca(racaNovo);
                String novoRacaTxt = "7 - " + racaNovo;
                boolean atualizado = repository().atualizar(pet, novoRacaTxt);
                if (atualizado) {
                    ui().exibirPet(pet);
                    ui().exibirMensagemAlteracaoConcluida();
                }
                valido = true;
            } catch (IllegalArgumentException e) {
                ui().errorExibir(e.getMessage());
            }
        }
    }

    private void alterarCampoPeso(Pet pet) {
        boolean valido = false;
        while (!valido) {
            try {
                String pesoNovo = ui().solicitarPeso();
                pet.alterarPeso(pesoNovo);
                String novoPesoTxt = "6 - " + pesoNovo + "kg";
                boolean atualizado = repository().atualizar(pet, novoPesoTxt);
                if (atualizado) {
                    ui().exibirPet(pet);
                    ui().exibirMensagemAlteracaoConcluida();
                }
                valido = true;
            } catch (IllegalArgumentException e) {
                ui().errorExibir(e.getMessage());
            }
        }
    }

    public void remover() {
        List<Pet> listaParaDeletarPet = listarPetsPorCriterio();
        if (listaParaDeletarPet.isEmpty()) {
            return;
        }

        int numeroPet = ui().numeroPetListFiltrada();

        int contador = 0;
        String respostaDeletarPet;
        for (Pet pet : listaParaDeletarPet) {
            contador++;
            if (numeroPet == contador) {
                do {
                    respostaDeletarPet = ui().confirmacaoDeletarPet(pet);
                } while (!respostaDeletarPet.equalsIgnoreCase("SIM") && !respostaDeletarPet.equalsIgnoreCase("NÃO"));

                if (respostaDeletarPet.equalsIgnoreCase("SIM")) {
                    repository().deletar(pet);
                    ui().mensagemDeletarPet();
                } else {
                    return;
                }
            }
        }
    }

    public List<Pet> listarPetsPorCriterio() {
        List<Pet> base = repository().listarTodos();

        if (base.isEmpty()) {
            ui().exibirMensagemErrorConsulta();
            return base;
        }

        Map<CriterioFiltro, String> criterios = new LinkedHashMap<>();

        while (true) {
            int acao = ui().solicitarAcaoGerenciamentoCriterios(criterios);

            switch (acao) {
                case 1:
                    adicionarCriterio(criterios);
                    break;
                case 2:
                    removerCriterio(criterios);
                    break;
                case 3:
                    return executarBusca(base, criterios);
                case 4:
                    return base;
                default:
                    break;
            }
        }
    }

    private void adicionarCriterio(Map<CriterioFiltro, String> criterios) {
        CriterioFiltro criterio = ui().solicitarCriterioFiltro();

        String valor;
        if (criterio == CriterioFiltro.SEXO) {
            Sexo sexo = ui().solicitarSexoParaFiltro();
            valor = sexo.tipo();
        } else if (criterio == CriterioFiltro.TIPO) {
            TipoAnimal tipo = ui().solicitarTipoAnimalParaFiltro();
            valor = tipo.animal();
        } else {
            valor = ui().solicitarTextoBusca();
        }

        criterios.put(criterio, valor);
    }

    private void removerCriterio(Map<CriterioFiltro, String> criterios) {
        CriterioFiltro criterioParaRemover = ui().solicitarCriterioParaRemover(criterios);
        criterios.remove(criterioParaRemover);
    }

    private List<Pet> executarBusca(List<Pet> baseLista, Map<CriterioFiltro, String> criterios) {
        if (criterios.isEmpty()) {
            // Sem critérios adicionais: exibe a lista base ao executar a busca explicitamente.
            ui().exibirListaPets(baseLista);
            return baseLista;
        }

        List<Pet> resultado = petFiltro.filtrar(baseLista, criterios);

        if (resultado.isEmpty()) {
            ui().exibirMensagemErrorConsulta();
            return baseLista;
        }

        ui().exibirListaPets(resultado);
        return resultado;
    }

    public void listarPetsCompleta() {
        ui().exibirListaPets(repository().listarTodos());
    }
}
