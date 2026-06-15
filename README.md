# Sistema de Cadastro de Pets

Um sistema de cadastro desenvolvido em Java que roda via terminal (CLI). O projeto realiza operações de **CRUD** (Cadastro, Consulta, Atualização e Remoção) de Pets.

A arquitetura foi estruturada utilizando os princípios de **DDD (Domain-Driven Design)**, garantindo a padronização das camadas e facilitando a manutenção e escalabilidade do projeto no futuro. O foco principal permanece na **lógica de manipulação de arquivos.txt** (persistência manual), validação de dados com **Regex** e tratamento de **exceções**.


## Referência e Desafio

> Este projeto foi desenvolvido como solução para o **Desafio de Cadastro**, proposto por **[Karilho](https://github.com/karilho/desafioCadastro/)**.

> Feito por **Kauã Henrique**, responsável pelo projeto e orientado por **Pedro Facchinetti**.
## Funcionalidades

- `[1]` **Cadastrar Pet:** Registra nome, tipo, sexo, endereço, idade, peso e raça com validações.
- `[2]` **Listar Pets:** Exibe todos os animais cadastrados lendo os arquivos da pasta do sistema.
- `[3]` **Buscar/Filtrar:** Localiza pets específicos pelo nome.
- `[4]` **Editar Informações:** Atualiza dados (como endereço ou peso) no objeto e reescreve o arquivo `.txt` automaticamente.
- `[5]` **Excluir:** Remove o pet da lista em memória e deleta o arquivo físico do disco.
- `[6]` **Encerrar:** Finaliza a execução do programa.

---

## Sumário

1. [Instalação e Configuração](#-instalação-e-configuração)
2. [Manual de Utilização](#-manual-de-utilização)

---

# Instalação e Configuração

> **Nota:** Este projeto foi desenvolvido em Java. Certifique-se de estar em um ambiente com o **JDK 17** ou superior instalado.

### 1. Instalação do Java (JDK 17) e Git

Escolha seu sistema operacional abaixo:

#### Linux (Ubuntu / Mint / Debian)
Abra seu terminal e execute os comandos para instalar tudo de uma vez:

```bash
      sudo apt update
      sudo apt install openjdk-17-jdk git -y
```

#### Windows / Mac
* **Java:** [Clique aqui para baixar o JDK 17 (Eclipse Adoptium)](https://adoptium.net/temurin/releases/?version=17)
* **Git:** [Clique aqui para baixar o Git](https://git-scm.com/downloads)

Após instalar, verifique se tudo está correto abrindo um novo terminal (CMD ou PowerShell):

```bash
      java -version   # Deve mostrar "openjdk 17..."
      git --version
```    

# Manual de Utilização

### 1. Clonar o repositório
Faça o download do projeto para sua máquina local. Abra o terminal na pasta onde deseja salvar o projeto e execute:

```bash
git clone https://github.com/Kaua-Henrique1/sistema-cadastro-java.git
```

### 2. Acessar a pasta

```bash
cd sistema-cadastro-java
```

### 3. Executar o projeto

```bash
# Compilar o código (Gera o arquivo .class)
javac Main.java

# Executar o programa
java Main
```
