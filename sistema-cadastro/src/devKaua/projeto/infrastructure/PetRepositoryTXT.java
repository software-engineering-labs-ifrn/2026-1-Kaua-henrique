package devKaua.projeto.infrastructure;

import devKaua.projeto.domain.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PetRepositoryTXT implements PetRepository {

    private final String diretorioCaminho;
    private final List<Pet> listaPet = new ArrayList<>();

    public PetRepositoryTXT(String diretorioCaminho) {
        this.diretorioCaminho = diretorioCaminho;
        File dir = new File(diretorioCaminho);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    @Override
    public Optional<Pet> buscarPorId(Long id) {
        return listaPet.stream()
                .filter(pet -> pet.getID().equals(id))
                .findFirst();
    }

    @Override
    public void carregarDados() {
        File dir = new File(getDiretorioCaminho());
        File[] arquivos = dir.listFiles();

        if (arquivos == null) return;

        long maiorIdEncontrado = 0L;
        for (File filePet : arquivos) {
            if (filePet.isFile() && filePet.getName().endsWith(".txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(filePet))) {
                    String linhaID = br.readLine();
                    if (linhaID == null || !linhaID.startsWith("ID - ")) continue;
                    long idPet = Long.parseLong(linhaID.split(" - ")[1]);

                    String nomePet = br.readLine().split(" - ")[1];
                    TipoAnimal tipoPet = TipoAnimal.valueOf(br.readLine().split(" - ")[1]);
                    Sexo sexoPet = Sexo.valueOf(br.readLine().split(" - ")[1]);
                    Endereco enderecoPet = extractEnderecoFromLine(br.readLine());
                    String idadePet = br.readLine().split(" - ")[1].replace(" anos", "");
                    String pesoPet = br.readLine().split(" - ")[1].replace("kg", "");
                    String racaPet = br.readLine().split(" - ")[1];

                    // Tenta ler a linha do Tutor se ela existir no arquivo
                    Long tutorId = null;
                    String linhaTutor = br.readLine();
                    if (linhaTutor != null && linhaTutor.startsWith("8 - ")) {
                        tutorId = Long.parseLong(linhaTutor.split(" - ")[1]);
                    }

                    if (idPet > maiorIdEncontrado) {
                        maiorIdEncontrado = idPet;
                    }

                    // Instancia o Pet usando o construtor completo com Tutor
                    Pet novoPet = new Pet(idPet, nomePet, enderecoPet, sexoPet, tipoPet, idadePet, pesoPet, racaPet, tutorId);
                    this.listaPet.add(novoPet);

                } catch (Exception e) {
                    System.out.println("Erro ao ler arquivo: " + filePet.getName() + " - " + e.getMessage());
                }
            }
        }
        Pet.atualizarGerador(maiorIdEncontrado);
    }

    @Override
    public void salvar(Pet pet) {
        DateTimeFormatter formatada = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter formatadaMin = DateTimeFormatter.ofPattern("HHmm");
        LocalDateTime agora = LocalDateTime.now();
        String dataFormatada = agora.format(formatada);
        String dataFormatadaMin = agora.format(formatadaMin);

        String nomePetFile = pet.getNome().toUpperCase().trim().replace(" ", "");
        String nomeFile = dataFormatada + "T" + dataFormatadaMin + "-" + nomePetFile + pet.getID();

        File fileDir = new File(getDiretorioCaminho());
        if (!fileDir.exists()) fileDir.mkdir();

        File filePet = new File(fileDir, nomeFile + ".txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePet))) {
            bw.write("ID - " + pet.getID()); bw.newLine();
            bw.write("1 - " + pet.getNome()); bw.newLine();
            bw.write("2 - " + pet.getTipoAnimal()); bw.newLine();
            bw.write("3 - " + pet.getSexo()); bw.newLine();
            bw.write("4 - " + pet.getEndereco().toFormatado()); bw.newLine();
            bw.write("5 - " + pet.getIdade() + " anos"); bw.newLine();
            bw.write("6 - " + pet.getPeso() + "kg"); bw.newLine();
            bw.write("7 - " + pet.getRaca()); bw.newLine();

            // Grava a linha do tutor apenas se ele possuir um tutor
            if (pet.getTutorId() != null) {
                bw.write("8 - " + pet.getTutorId());
                bw.newLine();
            }
            bw.flush();
            this.listaPet.add(pet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean atualizar(Pet pet, String linhaNova) {
        File dir = new File(getDiretorioCaminho());
        File[] arquivos = dir.listFiles();

        if (arquivos == null) return false;

        for (File filePet : arquivos) {
            if (filePet.isFile() && filePet.getName().endsWith(".txt")) {
                List<String> linhasArquivo = new ArrayList<>();
                boolean arquivoAlvo = false;

                // 1. Lê todas as linhas existentes dinamicamente
                try (BufferedReader br = new BufferedReader(new FileReader(filePet))) {
                    String linha = br.readLine();
                    if (linha != null && linha.equals("ID - " + pet.getID())) {
                        arquivoAlvo = true;
                        linhasArquivo.add(linha);
                        while ((linha = br.readLine()) != null) {
                            linhasArquivo.add(linha);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (!arquivoAlvo) continue;

                // 2. Grava de volta substituindo o campo atualizado ou adicionando se for novo (como o Tutor)
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePet))) {
                    String linhaNovaCortada = linhaNova.substring(0, 3); // Ex: "8 - "
                    boolean linhaSubstituida = false;

                    for (String linha : linhasArquivo) {
                        String linhaForCortada = linha.substring(0, 3);
                        if (linhaForCortada.equals(linhaNovaCortada)) {
                            bw.write(linhaNova);
                            linhaSubstituida = true;
                        } else {
                            bw.write(linha);
                        }
                        bw.newLine();
                    }

                    if (!linhaSubstituida) {
                        bw.write(linhaNova);
                        bw.newLine();
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

    private static Endereco extractEnderecoFromLine(String linhaEndereco) {
        String dadosEndereco = linhaEndereco.substring(4);
        String[] partesEndereco = dadosEndereco.split(", ");

        String rua = partesEndereco[0];
        String numero;
        String cidade = partesEndereco[2];

        numero = partesEndereco[1].trim();
        if (numero.isEmpty()) {
            numero = Pet.SEM_DADOS;
        }

        return new Endereco(rua, numero, cidade);
    }

    @Override
    public void deletar(Pet pet) {
        File dir = new File(getDiretorioCaminho());
        File[] arquivos = dir.listFiles();

        if (arquivos == null) {
            return;
        }

        for (File filePet : arquivos) {
            if (filePet.isFile() && filePet.getName().endsWith(".txt")) {
                boolean arquivoEncontrado = isPetInFile(pet, filePet);
                if (arquivoEncontrado) {
                    this.listaPet.remove(pet);
                    filePet.delete();
                    return;
                }
            }
        }
    }

    private static boolean isPetInFile(Pet pet, File filePet) {
        boolean arquivoEncontrado = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePet))) {
            String linhaID = br.readLine();
            if (linhaID != null && linhaID.startsWith("ID - ")) {
                String idNoArquivo = linhaID.split(" - ")[1];
                if (idNoArquivo.equals(String.valueOf(pet.getID()))) {
                    arquivoEncontrado = true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return arquivoEncontrado;
    }

    @Override
    public List<Pet> listarTodos() {
        return Collections.unmodifiableList(listaPet);
    }

    private String getDiretorioCaminho() {
        return diretorioCaminho;
    }
}
