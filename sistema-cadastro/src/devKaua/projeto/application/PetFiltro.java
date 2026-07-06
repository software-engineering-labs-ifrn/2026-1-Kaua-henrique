package devKaua.projeto.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PetFiltro {

    public List<Pet> filtrar(List<Pet> lista, Map<CriterioFiltro, String> criterios) {
        List<Pet> resultado = new ArrayList<>(lista);
        for (Map.Entry<CriterioFiltro, String> entry : criterios.entrySet()) {
            resultado = filtrar(resultado, entry.getKey(), entry.getValue());
        }
        return resultado;
    }

    public List<Pet> filtrar(List<Pet> lista, CriterioFiltro criterio, String busca) {
        List<Pet> resultado = new ArrayList<>();
        for (Pet pet : lista) {
            String valorDoCampo = extrairCampo(pet, criterio);
            if (corresponde(criterio, valorDoCampo, busca)) {
                resultado.add(pet);
            }
        }
        return resultado;
    }

    private boolean corresponde(CriterioFiltro criterio, String valorDoCampo, String busca) {
        return switch (criterio) {
            case NOME, RACA, CIDADE -> valorDoCampo.toLowerCase().contains(busca.toLowerCase());
            case IDADE, PESO, SEXO, TIPO -> valorDoCampo.equalsIgnoreCase(busca);
        };
    }

    private String extrairCampo(Pet pet, CriterioFiltro criterio) {
        return switch (criterio) {
            case NOME -> pet.getNome();
            case IDADE -> pet.getIdade();
            case RACA -> pet.getRaca();
            case PESO -> pet.getPeso();
            case SEXO -> pet.getSexo().tipo();
            case CIDADE -> pet.getEndereco().getCidade();
            case TIPO -> pet.getTipoAnimal().animal();
        };
    }
}
