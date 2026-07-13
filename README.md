# Sistema de Cadastro de Pets e Gestão de Adoções (ONG)

Um sistema de gestão e automação desenvolvido em Java focado em fluxos de acolhimento animal e processos de adoção. O software opera inteiramente via terminal (**CLI**), realizando operações completas de **CRUD** com regras de negócio complexas.

A arquitetura do projeto foi desenhada sob os princípios do **DDD (Domain-Driven Design)**, separando de forma estrita as responsabilidades do sistema. A documentação técnica e o mapeamento dos fluxos utilizam o padrão **BCE (Boundary-Control-Entity)**, garantindo expressividade ao negócio. O core técnico destaca-se pela **persistência manual e manipulação síncrona de arquivos `.txt`**, uso avançado de **Regex** para higienização e validação de dados de domínio, e tratamento robusto de **exceções**.

## Referência e Desafio

> Este projeto foi desenvolvido como solução para o **Desafio de Cadastro**, proposto por **[Karilho](https://github.com/karilho/desafioCadastro/)**.

> Feito por **Kauã Henrique**, responsável pelo projeto e orientado por **Pedro Facchinetti**.
## Funcionalidades

### 🐾 1. Contexto de Pets
- `[1]` **Cadastrar Pet:** Registra nome, tipo, sexo, endereço, idade, peso e raça com validações.
- `[2]` **Listar Pets:** Exibe todos os animais cadastrados lendo os arquivos da pasta do sistema.
- `[3]` **Buscar/Filtrar:** Localiza pets específicos pelo nome.
- `[4]` **Editar Informações:** Atualiza dados (como endereço ou peso) no objeto e reescreve o arquivo `.txt` automaticamente.
- `[5]` **Excluir:** Remove o pet da lista em memória e deleta o arquivo físico do disco.

### 👤 Contexto de Pessoas (Adotantes e Tutores)
- **Cadastrar Adotante:** Registra potenciais adotantes instanciando objetos de domínio com validações estritas de CPF, telefone e e-mail.
- **Gerenciamento de Critérios Dinâmicos:** Permite empilhar e remover filtros dinâmicos de Nome e CPF em memória para pesquisas rápidas.
- **Processo Especial de Adoção (Vincular Tutor ao Pet):** Fluxo transacional complexo que valida a existência de ambos os lados, promove dinamicamente o `Adotante` ao papel de `Tutor` e injeta seu ID direto na entidade `Pet`, atualizando o arquivo físico em disco.
- **Listar Adotantes Puros:** Filtra e exibe de forma exclusiva apenas os adotantes cadastrados no sistema que ainda não adotaram nenhum animal.
- **Listar e Buscar Tutores:** Mapeia a lista de pessoas cruzando dados com o `PetService` para montar e exibir instâncias completas de `Tutor` unificadas à listagem real dos seus respectivos Pets vinculados.
- **Alteração Evolutiva de Tutor:** Permite modificar dados cadastrais básicos de um tutor ativo, disparando uma re-promoção de papel em tempo real e re-vinculando instantaneamente os seus animais em memória para apresentação dos dados atualizados.
- **Remoção de Tutor e Desvínculo em Lote:** Deleta o registro físico da pessoa no sistema e aciona reativamente o `PetService` para limpar os IDs de tutor de todos os pets que estavam sob sua responsabilidade (retornando-os ao estado de disponíveis para adoção).
- ---

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

# Como Compilar e Executar o Projeto

Certifique-se de estar na raiz do repositório (`2026-1-Kaua-henrique`) antes de executar os comandos no terminal.

### No Windows (PowerShell)

Como o PowerShell possui regras restritas para caracteres como `@` e buscas recursivas, utilize os comandos nativos abaixo:

```powershell
# 1. Criar o diretório de saída para os binários
New-Item -ItemType Directory -Path .\out -Force

# 2. Compilar todos os arquivos .java recursivamente garantindo codificação UTF-8
javac -encoding utf8 -d out $(Get-ChildItem -Path .\sistema-cadastro\src\ -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)

# 3. Executar o sistema
java -cp out devKaua.projeto.presentation.GeradorDaONG
```
---

### No Linux / macOS (Bash)

```bash
# 1. Criar o diretório de saída para os binários
mkdir -p out

# 2. Buscar todos os arquivos .java e compilá-los em lote em UTF-8
find ./sistema-cadastro/src -name "*.java" | xargs javac -encoding utf8 -d out

# 3. Executar o sistema
java -cp out devKaua.projeto.presentation.GeradorDaONG
```

