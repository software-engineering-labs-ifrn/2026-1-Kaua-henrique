package devKaua.projeto.domain;

import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pet {
    private static final AtomicLong idGenerator = new AtomicLong(1);
    private Long ID;

    private String nome;
    private Endereco endereco;
    private Sexo sexo;
    private TipoAnimal tipoAnimal;
    private String idade;
    private String peso;
    private String raca;
    public static final String SEM_DADOS = "NÃO INFORMADO";

    public Pet(Long id, String nome, Endereco endereco, Sexo sexo, TipoAnimal tipoAnimal, String idade, String peso, String raca) {
        if (id == null) {
            this.ID = idGenerator.getAndIncrement();
        } else {
            this.ID = id;
        }
        setNome(nome);
        setIdade(idade);
        setPeso(peso);
        setRaca(raca);
        this.endereco = endereco;
        this.sexo = sexo;
        this.tipoAnimal = tipoAnimal;
    }

    public Pet() {
        this.ID = idGenerator.getAndIncrement();
    }

    @Override
    public String toString() {
        return (". " + getID() + " - " + getNome() + " - " + getEndereco().toString() + " - " + getTipoAnimal() + " - "
                + getSexo() + " - " + getIdade() + " anos - " + getPeso() + "kg - " + getRaca()
        );
    }

    public Sexo getSexo() {
        return sexo;
    }

    public TipoAnimal getTipoAnimal() {
        return tipoAnimal;
    }

    public String getIdade() {
        return idade;
    }

    public String getPeso() {
        return peso;
    }

    public String getRaca() {
        return raca;
    }

    public String getNome() {
        return nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setNome(String nome) {
        if (nome.isEmpty()) {
            this.nome = SEM_DADOS;
            return;
        }

        String regexNome = "[A-Za-z]+(\\s)+[A-Za-z]+(\\s+|$)";
        Pattern regraNome = Pattern.compile(regexNome);
        Matcher condicionalNome = regraNome.matcher(nome);
        if (!condicionalNome.find()) {
            throw new IllegalArgumentException("Nome inválido! Use apenas letras e espaço.");
        }
        this.nome = nome;
    }

    public void setIdade(String idade) {
        if (idade.isEmpty()) {
            this.idade = SEM_DADOS;
            return;
        }
        String regexIdadePeso = "[0-9]+((\\\\.|,)[0-9]+)?";
        Pattern regraIdadePeso = Pattern.compile(regexIdadePeso);
        Matcher condicionalIdade = regraIdadePeso.matcher(idade);
        if (Double.parseDouble(idade) > 60 || !condicionalIdade.find()) {
            throw new IllegalArgumentException("Idade inválida! Escreva apenas números entre 0.1 Anos até 60 anos.");
        }
        this.idade = idade;
    }

    public void setPeso(String peso) {
        if (peso.isEmpty()) {
            this.peso = SEM_DADOS;
            return;
        }
        String pesoPet = peso.replace(',', '.');
        String regexPeso = "[0-9]+((\\\\.|,)[0-9]+)?";
        Pattern regraPeso = Pattern.compile(regexPeso);
        Matcher condicionalPeso = regraPeso.matcher(pesoPet);
        if (Double.parseDouble(pesoPet) > 60 || Double.parseDouble(pesoPet) < 0.5 || !condicionalPeso.find()) {
            throw new IllegalArgumentException("Peso inválido! Escreva apenas números entre 0.5kl até 60kl.");
        }
        this.peso = peso;
    }

    public void setRaca(String raca) {
        if (raca.isEmpty()) {
            this.raca = SEM_DADOS;
            return;
        }
        String regexRaca = "[a-z,A-Z]+";
        Pattern regraRaca = Pattern.compile(regexRaca);
        Matcher condicionalRaca = regraRaca.matcher(raca);
        if (!condicionalRaca.find()) {
            throw new IllegalArgumentException("Raça inválida! Escreva apenas letras.");
        }
        this.raca = raca;
    }

    public static void atualizarGerador(Long maiorIdEncontrado) {
        if (maiorIdEncontrado >= idGenerator.get()) {
            idGenerator.set(maiorIdEncontrado + 1);
        }
    }

    public Long getID() {
        return ID;
    }
}