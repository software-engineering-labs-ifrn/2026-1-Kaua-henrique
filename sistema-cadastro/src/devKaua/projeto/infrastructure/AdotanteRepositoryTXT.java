package devKaua.projeto.infrastructure;

import devKaua.projeto.domain.Adotante;
import devKaua.projeto.domain.Endereco;
import devKaua.projeto.domain.Pessoa;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AdotanteRepositoryTXT implements AdotanteRepository {

    private final String diretorioCaminho;
    private final List<Adotante> listaAdotantes = new ArrayList<>();

    public AdotanteRepositoryTXT(String diretorioCaminho) {
        this.diretorioCaminho = diretorioCaminho;
        File dir = new File(diretorioCaminho);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void carregarDados() {
        File dir = new File(getDiretorioCaminho());
        File[] arquivos = dir.listFiles();

        if (arquivos == null) {
            return;
        }

        long maiorIdEncontrado = 0L;
        for (File fileAdotante : arquivos) {
            if (fileAdotante.isFile() && fileAdotante.getName().endsWith(".txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(fileAdotante))) {
                    String linhaID = br.readLine();
                    if (linhaID == null || !linhaID.startsWith("ID - ")) {
                        System.out.println("Pulando arquivo antigo ou inválido: " + fileAdotante.getName());
                        continue;
                    }
                    long idAdotante = Long.parseLong(linhaID.split(" - ")[1]);

                    String linhaNome = br.readLine();
                    String nomeAdotante = linhaNome.split(" - ")[1];

                    String linhaCpf = br.readLine();
                    String cpfAdotante = linhaCpf.split(" - ")[1];

                    String linhaTelefone = br.readLine();
                    String telefoneAdotante = linhaTelefone.split(" - ")[1];

                    String linhaEmail = br.readLine();
                    String emailAdotante = linhaEmail.split(" - ")[1];

                    String linhaEndereco = br.readLine();
                    Endereco enderecoAdotante = extractEnderecoFromLine(linhaEndereco);

                    if (idAdotante > maiorIdEncontrado) {
                        maiorIdEncontrado = idAdotante;
                    }

                    Adotante adotante = new Adotante(idAdotante, nomeAdotante, cpfAdotante, telefoneAdotante, emailAdotante, enderecoAdotante);
                    this.listaAdotantes.add(adotante);

                } catch (Exception e) {
                    System.out.println("Erro ao ler arquivo: " + fileAdotante.getName() + " - " + e.getMessage());
                }
            }
        }
        Pessoa.atualizarGerador(maiorIdEncontrado);
    }

    private static Endereco extractEnderecoFromLine(String linhaEndereco) {
        // Corta os 4 primeiros caracteres ("5 - ") para pegar apenas a string do endereço
        String dadosEndereco = linhaEndereco.substring(4);
        String[] partesEndereco = dadosEndereco.split(", ");

        String rua = partesEndereco[0];
        String numero = partesEndereco[1].trim();
        String cidade = partesEndereco[2];

        if (numero.isEmpty()) {
            numero = Pessoa.SEM_DADOS;
        }

        return new Endereco(rua, numero, cidade);
    }

    @Override
    public void salvar(Adotante adotante) {
        DateTimeFormatter formatada = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter formatadaMin = DateTimeFormatter.ofPattern("HHmm");
        LocalDateTime agora = LocalDateTime.now();
        String dataFormatada = agora.format(formatada);
        String dataFormatadaMin = agora.format(formatadaMin);

        String nomeAdotanteFile = adotante.getNome().toUpperCase().trim().replace(" ", "");
        String nomeFile = dataFormatada + "T" + dataFormatadaMin + "-" + nomeAdotanteFile + adotante.getID();

        File fileDir = new File(getDiretorioCaminho());
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }

        File fileAdotante = new File(fileDir, nomeFile + ".txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileAdotante))) {
            bw.write("ID - " + adotante.getID());
            bw.newLine();
            bw.write("1 - " + adotante.getNome());
            bw.newLine();
            bw.write("2 - " + adotante.getCpf());
            bw.newLine();
            bw.write("3 - " + adotante.getTelefone());
            bw.newLine();
            bw.write("4 - " + adotante.getEmail());
            bw.newLine();
            bw.write("5 - " + adotante.getEndereco().toFormatado());
            bw.flush();

            this.listaAdotantes.add(adotante);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // --- MÉTODOS NOVOS ADICIONADOS COM BASE NO PET REPOSITORY ---

    @Override
    public boolean atualizar(Adotante adotante, String linhaNova) {
        File dir = new File(getDiretorioCaminho());
        File[] arquivos = dir.listFiles();

        if (arquivos == null) {
            return false;
        }

        for (File fileAdotante : arquivos) {
            if (fileAdotante.isFile() && fileAdotante.getName().endsWith(".txt")) {
                String linhaID;
                String linhaNome;
                String linhaCpf;
                String linhaTelefone;
                String linhaEmail;
                String linhaEndereco;

                try (BufferedReader br = new BufferedReader(new FileReader(fileAdotante))) {
                    linhaID = br.readLine();
                    if (!linhaID.equals("ID - " + adotante.getID())) {
                        continue;
                    }

                    linhaNome = br.readLine();
                    linhaCpf = br.readLine();
                    linhaTelefone = br.readLine();
                    linhaEmail = br.readLine();
                    linhaEndereco = br.readLine();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Ajustado para o modelo de 6 linhas do Adotante
                String[] linhasArquivo = {linhaID, linhaNome, linhaCpf, linhaTelefone, linhaEmail, linhaEndereco};
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileAdotante))) {
                    String linhaNovaCortada = linhaNova.substring(0, 3);

                    for (String linha : linhasArquivo) {
                        String linhaForCortada = linha.substring(0, 3);
                        if (linhaForCortada.equals(linhaNovaCortada)) {
                            bw.write(linhaNova);
                            bw.newLine();
                        } else {
                            bw.write(linha);
                            bw.newLine();
                        }
                    }
                    bw.flush();
                    return true;

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    @Override
    public void deletar(Adotante adotante) {
        File dir = new File(getDiretorioCaminho());
        File[] arquivos = dir.listFiles();

        if (arquivos == null) {
            return;
        }

        for (File fileAdotante : arquivos) {
            if (fileAdotante.isFile() && fileAdotante.getName().endsWith(".txt")) {
                boolean arquivoEncontrado = isAdotanteInFile(adotante, fileAdotante);
                if (arquivoEncontrado) {
                    this.listaAdotantes.remove(adotante);
                    fileAdotante.delete();
                    return;
                }
            }
        }
    }

    private static boolean isAdotanteInFile(Adotante adotante, File fileAdotante) {
        boolean arquivoEncontrado = false;
        try (BufferedReader br = new BufferedReader(new FileReader(fileAdotante))) {
            String linhaID = br.readLine();
            if (linhaID != null && linhaID.startsWith("ID - ")) {
                String idNoArquivo = linhaID.split(" - ")[1];
                if (idNoArquivo.equals(String.valueOf(adotante.getID()))) {
                    arquivoEncontrado = true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return arquivoEncontrado;
    }

    // -----------------------------------------------------------

    @Override
    public List<Adotante> listarTodos() {
        return Collections.unmodifiableList(listaAdotantes);
    }

    @Override
    public Optional<Adotante> buscarPorId(Long id) {
        return listaAdotantes.stream()
                .filter(a -> a.getID().equals(id))
                .findFirst();
    }

    private String getDiretorioCaminho() {
        return diretorioCaminho;
    }
}