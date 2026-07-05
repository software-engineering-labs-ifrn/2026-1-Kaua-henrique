package devKaua.projeto.infrastructure;

import devKaua.projeto.domain.Adotante;

import java.util.List;
import java.util.Optional;

public interface AdotanteRepository {
    void carregarDados();
    void salvar(Adotante adotante);
    boolean atualizar(Adotante adotante, String linhaNova);
    void deletar(Adotante adotante);
    List<Adotante> listarTodos();
    Optional<Adotante> buscarPorId(Long id);
}