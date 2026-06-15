package devKaua.projeto.infrastructure;

import devKaua.projeto.domain.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public void carregarDados() {
        File dir = new File(getDiretorioCaminho());
        File[] arquivos = dir.listFiles();

        if (arquivos == null) {
            return;
        }

        long maiorIdEncontrado = 0L;
        for (File filePet : arquivos) {
            if (filePet.isFile() && filePet.getName().endsWith(".txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(filePet))) {
                    String linhaID = br.readLine();
                    if (linhaID == null || !linhaID.startsWith("ID - ")) {
                        System.out.println("Pulando arquivo antigo ou inválido: " + filePet.getName());
                        continue;
                    }
                    long idPet = Long.parseLong(linhaID.split(" - ")[1]);

                    String linhaNome = br.readLine();
                    String nomePet = linhaNome.split(" - ")[1];

                    String linhaTipo = br.readLine();
                    String tipoString = linhaTipo.split(" - ")[1];
                    TipoAnimal tipoPet = TipoAnimal.valueOf(tipoString);

                    String linhaSexo = br.readLine();
                    String sexoString = linhaSexo.split(" - ")[1];
                    Sexo sexoPet = Sexo.valueOf(sexoString);

                    String linhaEndereco = br.readLine();
                    Endereco enderecoPet = extractEnderecoFromLine(linhaEndereco);

                    String linhaIdade = br.readLine();
                    String idadePet = linhaIdade.split(" - ")[1].replace(" anos", "");

                    String linhaPeso = br.readLine();
                    String pesoPet = linhaPeso.split(" - ")[1].replace("kg", "");

                    String linhaRaca = br.readLine();
                    String racaPet = linhaRaca.split(" - ")[1];

                    if (idPet > maiorIdEncontrado) {
                        maiorIdEncontrado = idPet;
                    }

                    Pet novoPet = new Pet(idPet, nomePet, enderecoPet, sexoPet, tipoPet, idadePet, pesoPet, racaPet);
                    this.listaPet.add(novoPet);

                } catch (Exception e) {
                    System.out.println("Erro ao ler arquivo: " + filePet.getName() + " - " + e.getMessage());
                }
            }
        }
        Pet.atualizarGerador(maiorIdEncontrado);
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
    public void salvar(Pet pet) {
        DateTimeFormatter formatada = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter formatadaMin = DateTimeFormatter.ofPattern("HHmm");
        LocalDateTime agora = LocalDateTime.now();
        String dataFormatada = agora.format(formatada);
        String dataFormatadaMin = agora.format(formatadaMin);

        String nomePetFile = pet.getNome().toUpperCase().trim().replace(" ", "");
        String nomeFile = dataFormatada + "T" + dataFormatadaMin + "-" + nomePetFile + pet.getID();

        File fileDir = new File(getDiretorioCaminho());
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }

        File filePet = new File(fileDir, nomeFile + ".txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePet))) {
            bw.write("ID - " + pet.getID());
            bw.newLine();
            bw.write("1 - " + pet.getNome());
            bw.newLine();
            bw.write("2 - " + pet.getTipoAnimal());
            bw.newLine();
            bw.write("3 - " + pet.getSexo());
            bw.newLine();
            bw.write("4 - " + pet.getEndereco().toFormatado());
            bw.newLine();
            bw.write("5 - " + pet.getIdade() + " anos");
            bw.newLine();
            bw.write("6 - " + pet.getPeso() + "kg");
            bw.newLine();
            bw.write("7 - " + pet.getRaca());
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

        if (arquivos == null) {
            return false;
        }

        for (File filePet : arquivos) {
            if (filePet.isFile() && filePet.getName().endsWith(".txt")) {
                String linhaID;
                String linhaNome;
                String linhaTipo;
                String linhaSexo;
                String linhaEndereco;
                String linhaIdade;
                String linhaPeso;
                String linhaRaca;

                try (BufferedReader br = new BufferedReader(new FileReader(filePet))) {
                    linhaID = br.readLine();
                    if (!linhaID.equals("ID - " + pet.getID())) {
                        continue;
                    }

                    linhaNome = br.readLine();
                    linhaTipo = br.readLine();
                    linhaSexo = br.readLine();
                    linhaEndereco = br.readLine();
                    linhaIdade = br.readLine();
                    linhaPeso = br.readLine();
                    linhaRaca = br.readLine();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                String[] linhasArquivo = {linhaID, linhaNome, linhaTipo, linhaSexo, linhaEndereco, linhaIdade, linhaPeso, linhaRaca};
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePet))) {
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
