package devKaua.projeto.domain;

import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public abstract class Pessoa {

    private static final AtomicLong idGenerator = new AtomicLong(1);
    public static final String SEM_DADOS = "NÃO INFORMADO";

    private final Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private final Endereco endereco;

    protected Pessoa(String nome, String cpf, String telefone, String email, Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço é obrigatório.");
        }

        // INTERCEPTAÇÃO: Se o endereço foi criado com "NÃO INFORMADO" (SEM_DADOS), a Pessoa recusa!
        if (endereco.getNumero().equals("NÃO INFORMADO")) {
            throw new IllegalArgumentException("Para cadastrar uma pessoa (Adotante/Tutor), o número do endereço é obrigatório!");
        }

        this.id = idGenerator.getAndIncrement();
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setEmail(email);
        this.endereco = endereco;
    }

    // --- Construtor para Reconstituição (Leitura do TXT - Não incrementa ID) ---
    protected Pessoa(Long id, String nome, String cpf, String telefone, String email, Endereco endereco) {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório para reconstituição.");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço é obrigatório.");
        }
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setEmail(email);
        this.endereco = endereco;
    }

    private void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        // Aceita letras de A-Z (incluindo acentos) e espaços
        String regexNome = "^[A-Za-zÀ-ÿ]+(\\s+[A-Za-zÀ-ÿ]+)+ *$";
        if (!nome.matches(regexNome)) {
            throw new IllegalArgumentException("Nome inválido! Use apenas letras e sobrenome separado por espaço.");
        }
        this.nome = nome.trim();
    }

    private void setCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório.");
        }
        String cpfLimpo = cpf.replaceAll("\\D", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF inválido! Deve conter exatamente 11 dígitos numéricos.");
        }
        this.cpf = cpfLimpo;
    }

    private void setTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            this.telefone = SEM_DADOS;
            return;
        }
        String apenasNumeros = telefone.replaceAll("\\D", "");
        if (apenasNumeros.length() < 10 || apenasNumeros.length() > 11) {
            throw new IllegalArgumentException("Telefone inválido! Deve conter DD + número (10 ou 11 dígitos apenas numéricos).");
        }
        this.telefone = apenasNumeros;
    }

    private void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        // Padrão RFC 5322 para e-mails válidos
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regexEmail);
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("E-mail inválido! Informe um formato correto (exemplo@dominio.com).");
        }
        this.email = email.trim().toLowerCase();
    }

    // --- Getters ---
    public Long getID() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public Endereco getEndereco() { return endereco; }

    // --- Métodos de Alteração (Abertos para o Service/Facade) ---
    public void alterarNome(String nome) { setNome(nome); }
    public void alterarTelefone(String telefone) { setTelefone(telefone); }
    public void alterarEmail(String email) { setEmail(email); }

    // --- Sincronização do Gerador com o TXT ---
    public static void atualizarGerador(Long maiorIdEncontrado) {
        if (maiorIdEncontrado >= idGenerator.get()) {
            idGenerator.set(maiorIdEncontrado + 1);
        }
    }
}