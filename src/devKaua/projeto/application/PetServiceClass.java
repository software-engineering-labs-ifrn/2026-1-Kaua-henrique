package devKaua.projeto.application;

import devKaua.projeto.domain.*;

import java.util.ArrayList;
import java.util.List;

public class PetServiceClass implements PetService {
    private AlteracoesPet alteracoesPet;
    private InterfaceUsarioCLI ui;
    private PersistenciaDadosTXT persistencia;
    public static final String SEM_DADOS = "NÃO INFORMADO";

    public PetServiceClass(InterfaceUsarioCLI ui, PersistenciaDadosTXT persistenciaDadosTXT) {
        this.ui = ui;
        this.persistencia = persistenciaDadosTXT;
        this.alteracoesPet = new AlteracoesPet();
    }

    private PersistenciaDadosTXT getPersistencia() {
        return persistencia;
    }

    private InterfaceUsarioCLI getUi() {
        return ui;
    }

    @Override
    public void cadastrar() {
        Pet atributos = null;
        Endereco enderecoPet = null;

        String nomePet = "";
        String racaPet = "";
        String idadePet = "";
        String pesoPet = "";
        TipoAnimal tipoPet = null;
        Sexo sexoPet = null;
        while (atributos == null) {
            try {
                int tipoPetInt = getUi().solicitarTipo();
                tipoPet = TipoAnimal.values()[tipoPetInt - 1];

                int sexoPetInt = getUi().solicitarSexo();
                sexoPet = Sexo.values()[sexoPetInt - 1];

                String[] enderecoPetU = getUi().solicitarEndereco();
                enderecoPet = new Endereco(enderecoPetU[0], enderecoPetU[1], enderecoPetU[2]);

                nomePet = getUi().solicitarNome();
                racaPet = getUi().solicitarRaca();
                idadePet = getUi().solicitarIdade();
                pesoPet = getUi().solicitarPeso();

                atributos = new Pet(null, nomePet,enderecoPet,sexoPet,tipoPet,idadePet,pesoPet,racaPet);
            } catch (IllegalArgumentException e) {
                getUi().erroSalvarObjPet();
            }
        }
        if (nomePet.isEmpty()) {
            nomePet = SEM_DADOS;
        }
        if (racaPet.isEmpty()) {
            racaPet = SEM_DADOS;
        }
        if (idadePet.isEmpty()) {
            idadePet = SEM_DADOS;
        }
        if (pesoPet.isEmpty()) {
            pesoPet = SEM_DADOS;
        }

        Pet novoPet = new Pet(null,nomePet,enderecoPet,sexoPet,tipoPet,idadePet,pesoPet,racaPet);
        String enderecoPetStr = enderecoPet.toFormatado();

        getPersistencia().salvar(novoPet, enderecoPetStr);
    }

    @Override
    public void alterar() {
        List<Pet> listaParaAlteraPet = listarPetsPorCriterio();
        int numeroPet = getUi().numeroPetListFiltrada();

        int contador = 0;
        for (Pet pets : listaParaAlteraPet) {
            contador++;
            if (numeroPet == contador) {
                int consultaDesejada;
                do {
                    consultaDesejada = getUi().solicitarOpcaoAlterar();
                } while (consultaDesejada < 1 || consultaDesejada > 4);

                switch (consultaDesejada) {
                    case 1:
                        boolean nomeValido = false;
                        while (!nomeValido) {
                            try {
                                String nomeNovo = getUi().solicitarNome();

                                pets.setNome(nomeNovo);
                                String novoNomeTxt = "1 - " + nomeNovo;
                                boolean isTrue = getPersistencia().atualizar(pets, novoNomeTxt);

                                if (isTrue) {
                                    getUi().exibirPet(pets);
                                    getUi().exibirMensagemAlteracaoConcluida();
                                }
                                nomeValido = true;

                            } catch (IllegalArgumentException e) {
                                getUi().errorExibir(e.getMessage());
                            }
                        }
                        break;
                    case 2:
                        boolean idadeValido = false;
                        while (!idadeValido) {
                            try {
                                String idadeNovo = getUi().solicitarIdade();

                                pets.setIdade(idadeNovo);
                                String novoIdadeTxt = "5 - " + idadeNovo + " anos";
                                boolean isTrue = getPersistencia().atualizar(pets, novoIdadeTxt);

                                if (isTrue) {
                                    getUi().exibirPet(pets);
                                    getUi().exibirMensagemAlteracaoConcluida();
                                }
                                idadeValido = true;

                            } catch (IllegalArgumentException e) {
                                getUi().errorExibir(e.getMessage());
                            }
                        }
                        break;
                    case 3:
                        boolean racaValido = false;
                        while (!racaValido) {
                            try {
                                String racaNovo = getUi().solicitarRaca();

                                pets.setRaca(racaNovo);
                                String novoRacaTxt = "7 - " + racaNovo;
                                boolean isTrue = getPersistencia().atualizar(pets, novoRacaTxt);

                                if (isTrue) {
                                    getUi().exibirPet(pets);
                                    getUi().exibirMensagemAlteracaoConcluida();
                                }
                                racaValido = true;

                            } catch (IllegalArgumentException e) {
                                getUi().errorExibir(e.getMessage());
                            }
                        }
                        break;
                    case 4:
                        boolean pesoValido = false;
                        while (!pesoValido) {
                            try {
                                String pesoNovo = getUi().solicitarPeso();

                                pets.setPeso(pesoNovo);
                                String novoPesoTxt = "6 - " + pesoNovo+ "kg";
                                boolean isTrue = getPersistencia().atualizar(pets, novoPesoTxt);

                                if (isTrue) {
                                    getUi().exibirPet(pets);
                                    getUi().exibirMensagemAlteracaoConcluida();
                                }
                                pesoValido = true;

                            } catch (IllegalArgumentException e) {
                                getUi().errorExibir(e.getMessage());
                            }
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void remover() {
        List<Pet> listaParaDeletarPet = listarPetsPorCriterio();
        int numeroPet = getUi().numeroPetListFiltrada();

        int contador = 0;
        String respostaDeletarPet;
        for (Pet pet : listaParaDeletarPet) {
            contador++;
            if (numeroPet == contador) {
                do {
                    respostaDeletarPet = getUi().confirmacaoDeletarPet(pet);
                } while (!respostaDeletarPet.equalsIgnoreCase("SIM") && !respostaDeletarPet.equalsIgnoreCase("NÃO"));

                if (respostaDeletarPet.equalsIgnoreCase("SIM")) {
                    getPersistencia().deletar(pet);
                    getPersistencia().getListaPet().remove(pet);
                    getUi().mensagemDeletarPet();
                } else {
                    return;
                }
            }
        }
    }

    @Override
    public List<Pet> listarPetsPorCriterio() {
        int consultaDesejadaInterface;
        String consultaInterface;

        List<Pet> listaFiltrada = new ArrayList<>();
        int respostaTipoAnimal;
        do {
            respostaTipoAnimal = getUi().consultaCachorroOuGato();
        } while (respostaTipoAnimal < 1 || respostaTipoAnimal > 2);

        for (Pet petOpcoes : getPersistencia().getListaPet()) {
            if (respostaTipoAnimal == 1 && petOpcoes.getTipoAnimal() == TipoAnimal.CACHORRO) {
                listaFiltrada.add(petOpcoes);
            } else if (respostaTipoAnimal == 2 && petOpcoes.getTipoAnimal() == TipoAnimal.GATO) {
                listaFiltrada.add(petOpcoes);
            }
        }

        if (listaFiltrada.isEmpty()) {
            getUi().exibirMensagemErrorConsulta();
            return listaFiltrada;
        }
        getUi().exibirListaPets(listaFiltrada);


        do {
            consultaDesejadaInterface = getUi().solicitarOpcaoFiltro();
        } while (consultaDesejadaInterface < 1 || consultaDesejadaInterface > 6);
        consultaInterface = getUi().solicitarTextoBusca();

        List<Pet> listFiltrada2 = alteracoesPet.consultaPet(listaFiltrada, consultaDesejadaInterface, consultaInterface);

        if (listFiltrada2.isEmpty()) {
            getUi().exibirMensagemErrorConsulta();
            return listaFiltrada;
        }
        getUi().exibirListaPets(listFiltrada2);

        int adicionarConsulta;
        do {
            adicionarConsulta = getUi().solicitarConfirmacaoSimNao();
        } while (adicionarConsulta < 1 || adicionarConsulta > 2);

        if (adicionarConsulta != 1) {
            return listFiltrada2;
        }

        do {
            consultaDesejadaInterface = getUi().solicitarOpcaoFiltro();
        } while (consultaDesejadaInterface < 1 || consultaDesejadaInterface > 6);
        consultaInterface = getUi().solicitarTextoBusca();

        List<Pet> listFiltrada3 = alteracoesPet.consultaPet(listFiltrada2, consultaDesejadaInterface, consultaInterface);

        if (listFiltrada3.isEmpty()) {
            getUi().exibirMensagemErrorConsulta();
            return listaFiltrada;
        }
        getUi().exibirListaPets(listFiltrada3);
        return listFiltrada3;
    }

    @Override
    public void listarPetsCompleta() {
        getUi().exibirListaPets(getPersistencia().getListaPet());
    }
}