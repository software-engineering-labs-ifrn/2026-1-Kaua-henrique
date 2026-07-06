package devKaua.projeto.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tutor extends Pessoa {
    private final List<Pet> meusPets = new ArrayList<>();

    public Tutor(Long id, String nome, String cpf, String telefone, String email, Endereco endereco) {
        super(id, nome, cpf, telefone, email, endereco);
    }

    public static Tutor promoverAdotante(Adotante adotante) {
        return new Tutor(
                adotante.getID(),
                adotante.getNome(),
                adotante.getCpf(),
                adotante.getTelefone(),
                adotante.getEmail(),
                adotante.getEndereco()
        );
    }

    public void adicionarPet(Pet pet) {
        if (pet == null) {
            throw new IllegalArgumentException("Não é possível vincular um pet nulo ao tutor.");
        }
        if (!meusPets.contains(pet)) {
            this.meusPets.add(pet);
        }
    }

    public void removerPet(Pet pet) {
        this.meusPets.remove(pet);
    }

    public List<Pet> getMeusPets() {
        return Collections.unmodifiableList(meusPets);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tutor ID: ").append(getID())
                .append(" - ").append(getNome())
                .append(" - CPF: ").append(getCpf())
                .append("\nPets sob responsabilidade:");

        if (meusPets.isEmpty()) {
            sb.append(" [Nenhum pet vinculado ainda]");
        } else {
            for (Pet pet : meusPets) {
                sb.append("\n  ↳ ID: ").append(pet.getID()).append(" - ").append(pet.getNome()).append(" (").append(pet.getRaca()).append(")");
            }
        }
        return sb.toString();
    }
}