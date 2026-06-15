package devKaua.projeto.application;

import devKaua.projeto.domain.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDadosTXT implements PersistenceUnit {
    private final String diretorioCaminho;
    private final List<Pet> listaPet = new ArrayList<>();
    public static final String SEM_DADOS = "NÃO INFORMADO";


    public PersistenciaDadosTXT(String diretorioCaminho) {
        this.diretorioCaminho = diretorioCaminho;
        File dir = new File(diretorioCaminho);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public boolean carregarDados() {
        // nao consegue ler a maioria pois so tem dois pet que foi criado com base no ID.
        // mas meu metodo deveria mostrar pelo menos esses 2 PETS.
        File dir = new File(getDiretorioCaminho());
        File[] arquivos = dir.listFiles();

        if (arquivos == null) {
            return false;
        }

        Long maiorIdEncontrado = 0L;
        for (File filePet : arquivos) {
            if (filePet.isFile() && filePet.getName().endsWith(".txt")) {
                try (FileReader fr = new FileReader(filePet)) {
                    BufferedReader br = new BufferedReader(fr);

                    // teste para verificar quais nao consegue ler
                    String linhaID = br.readLine();
                    if (linhaID == null || !linhaID.startsWith("ID - ")) {
                        System.out.println("Pulando arquivo antigo ou inválido: " + filePet.getName());
                        continue;
                    }
                    Long idPet = Long.parseLong(linhaID.split(" - ")[1]);

                    String linhaNome = br.readLine();
                    String nomePet = linhaNome.split(" - ")[1];

                    String linhaTipo = br.readLine();
                    String tipoString = linhaTipo.split(" - ")[1];
                    TipoAnimal tipoPet = TipoAnimal.valueOf(tipoString);

                    String linhaSexo = br.readLine();
                    String sexoString = linhaSexo.split(" - ")[1];
                    Sexo sexoPet = Sexo.valueOf(sexoString);

                    String linhaEndereco = br.readLine();
                    String dadosEndereco = linhaEndereco.substring(4);

                    String[] partesEndereco = dadosEndereco.split(", ");

                    String rua = partesEndereco[0];
                    String numero;
                    String cidade = partesEndereco[2];

                    if (partesEndereco.length > 1) {
                        numero = partesEndereco[1].trim();
                        if (numero.isEmpty()) {
                            numero = SEM_DADOS;
                        }
                    } else {
                        numero = SEM_DADOS;
                    }

                    Endereco enderecoPet = new Endereco(rua, numero, cidade);

                    String linhaIdade = br.readLine();
                    String idadePet = linhaIdade.split(" - ")[1].replace(" anos", "");

                    String linhaPeso = br.readLine();
                    String pesoPet = linhaPeso.split(" - ")[1].replace("kg", "");

                    String linhaRaca = br.readLine();
                    String racaPet = linhaRaca.split(" - ")[1];

                    if (idPet > maiorIdEncontrado) {
                        maiorIdEncontrado = idPet;
                    }

                    Pet novoPet = new Pet(idPet , nomePet, enderecoPet, sexoPet, tipoPet, idadePet, pesoPet, racaPet);

                    this.listaPet.add(novoPet);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
        Pet.atualizarGerador(maiorIdEncontrado);
        return true;
    }

    @Override
    public boolean salvar(Pet pet, String enderecoPetStr) {
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

        try (FileWriter fw = new FileWriter(filePet)) {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("ID - " + pet.getID());
            bw.newLine();
            bw.write("1 - " + pet.getNome());
            bw.newLine();
            bw.write("2 - " + pet.getTipoAnimal());
            bw.newLine();
            bw.write("3 - " + pet.getSexo());
            bw.newLine();
            bw.write("4 - " + enderecoPetStr);
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
        return true;
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
                try (FileReader fr = new FileReader(filePet)) {
                    BufferedReader br = new BufferedReader(fr);

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
                try (FileWriter fw = new FileWriter(filePet)) {
                    BufferedWriter bw = new BufferedWriter(fw);

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
    public boolean deletar(Pet pet) {
        File dir = new File(getDiretorioCaminho());
        File[] arquivos = dir.listFiles();

        if (arquivos == null) {
            return false;
        }

        for (File filePet : arquivos) {
            if (filePet.isFile() && filePet.getName().endsWith(".txt")) {
                boolean arquivoEncontrado = false;
                try (FileReader fr = new FileReader(filePet)) {
                    BufferedReader br = new BufferedReader(fr);

                    String linhaID = br.readLine();
                    if (linhaID != null && linhaID.startsWith("ID - ")) {
                        String idNoArquivo = linhaID.split(" - ")[1];

                        if (idNoArquivo.equals(String.valueOf(pet.getID()))) {
                            br.close();
                            arquivoEncontrado = true;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (arquivoEncontrado) {
                    this.listaPet.remove(pet);
                    return filePet.delete();
                }
            }
        }
        return false;
    }

    private String getDiretorioCaminho() {
        return diretorioCaminho;
    }

    public List<Pet> getListaPet() {
        return listaPet;
    }
}