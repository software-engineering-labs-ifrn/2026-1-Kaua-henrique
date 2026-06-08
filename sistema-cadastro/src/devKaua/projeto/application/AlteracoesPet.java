package devKaua.projeto.application;

import devKaua.projeto.domain.*;

import java.util.ArrayList;
import java.util.List;

public class AlteracoesPet {
    private List<Pet> consultaNome(List<Pet> listaAtual, String nome) {
        List<Pet> listaFiltrada = new ArrayList<>();
        for (Pet pet : listaAtual) {
            if (pet.getNome().equalsIgnoreCase(nome)) {
                listaFiltrada.add(pet);
            }
        }
        return listaFiltrada;
    }

    private List<Pet> consultaIdade(List<Pet> listaAtual, String idade) {
        List<Pet> listaFiltrada = new ArrayList<>();
        for (Pet pet : listaAtual) {
            if (pet.getIdade().equals(idade)) {
                listaFiltrada.add(pet);
            }
        }
        return listaFiltrada;
    }

    private List<Pet> consultaRaca(List<Pet> listaAtual, String raca) {
        List<Pet> listaFiltrada = new ArrayList<>();
        for (Pet pet : listaAtual) {
            if (pet.getRaca().equalsIgnoreCase(raca)) {
                listaFiltrada.add(pet);
            }
        }
        return listaFiltrada;
    }

    private List<Pet> consultaPeso(List<Pet> listaAtual, String peso) {
        List<Pet> listaFiltrada = new ArrayList<>();
        for (Pet pet : listaAtual) {
            if (pet.getPeso().equals(peso)) {
                listaFiltrada.add(pet);
            }
        }
        return listaFiltrada;
    }

    private List<Pet> consultaSexo(List<Pet> listaAtual, String sexo)  {
        List<Pet> listaFiltrada = new ArrayList<>();
        for (Pet pet : listaAtual) {
            if (pet.getSexo().toString().equalsIgnoreCase(sexo)) {
                listaFiltrada.add(pet);
            }
        }
        return listaFiltrada;
    }

    public List<Pet> consultaEndereco(List<Pet> listaAtual, String cidade) {
        List<Pet> listaFiltrada = new ArrayList<>();
        for (Pet pet : listaAtual) {
            if (pet.getEndereco().getCidade().equalsIgnoreCase(cidade)) {
                listaFiltrada.add(pet);
            }
        }
        return listaFiltrada;
    }

    public List<Pet> consultaPet(List<Pet> listaRecebida, int consultaDesejada, String consulta) {
        List<Pet> resposta = new ArrayList<>();
        switch (consultaDesejada) {
            case 1:
                resposta = this.consultaNome(listaRecebida, consulta);
                break;
            case 2:
                resposta = this.consultaIdade(listaRecebida, consulta);
                break;
            case 3:
                resposta = this.consultaRaca(listaRecebida, consulta);
                break;
            case 4:
                resposta = this.consultaPeso(listaRecebida, consulta);
                break;
            case 5:
                resposta = this.consultaSexo(listaRecebida, consulta);
                break;
            case 6:
                resposta = this.consultaEndereco(listaRecebida, consulta);
                break;
        }
        return resposta;
    }
}