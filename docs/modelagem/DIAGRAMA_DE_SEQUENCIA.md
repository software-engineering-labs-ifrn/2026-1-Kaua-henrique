### SD-01: Inicialização do Sistema e Carga Dinâmica de Dados em Memória

```mermaid
sequenceDiagram
    autonumber
    actor Usuario
    participant Main as GeradorDaONG
    participant PetRepo as petRepository:PetRepositoryTXT
    participant AdotanteRepo as adotanteRepository:AdotanteRepositoryTXT
    participant File as File (Diretorio)
    participant BR as BufferedReader
    participant Pet as Pet (Classe)
    participant Pessoa as Pessoa (Classe)

    Usuario->>Main: Executa main(args)
    activate Main

%% CONTEXTO PETS
    Main->>PetRepo: new PetRepositoryTXT("petsCadastrados")
    activate PetRepo
    PetRepo-->>Main: Instancia do repositorio criada
    deactivate PetRepo

    Main->>PetRepo: carregarDados()
    activate PetRepo
    PetRepo->>File: listFiles()
    activate File
    File-->>PetRepo: Array de arquivos (.txt)
    deactivate File

    loop Para cada arquivo em arquivos
        PetRepo->>BR: new BufferedReader(FileReader(filePet))
        activate BR
        BR-->>PetRepo: Objeto br criado
        deactivate BR

        PetRepo->>BR: readLine()
        activate BR
        BR-->>PetRepo: Dados extraidos da linha
        deactivate BR

        PetRepo->>Pet: new Pet(idPet, nomePet, ..., tutorId)
        activate Pet
        Pet-->>PetRepo: Objeto novoPet instanciado
        deactivate Pet

        PetRepo->>PetRepo: listaPet.add(novoPet)
    end

    PetRepo->>Pet: atualizarGerador(maiorIdEncontrado)
    activate Pet
%% Método void: finaliza e desativa sem seta de retorno
    deactivate Pet

%% Fim do carregarDados (void): desativa sem seta de retorno
    deactivate PetRepo

%% CONTEXTO ADOTANTES
    Main->>AdotanteRepo: new AdotanteRepositoryTXT("adotantesCadastradosTXT")
    activate AdotanteRepo
    AdotanteRepo-->>Main: Instancia do repositorio criada
    deactivate AdotanteRepo

    Main->>AdotanteRepo: carregarDados()
    activate AdotanteRepo
    AdotanteRepo->>File: listFiles()
    activate File
    File-->>AdotanteRepo: Array de arquivos (.txt)
    deactivate File

    loop Para cada arquivo em arquivos
        AdotanteRepo->>BR: new BufferedReader(FileReader(fileAdotante))
        activate BR
        BR-->>AdotanteRepo: Objeto br criado
        deactivate BR

        AdotanteRepo->>BR: readLine()
        activate BR
        BR-->>AdotanteRepo: Dados extraidos da linha
        deactivate BR

        AdotanteRepo->>AdotanteRepo: listaAdotantes.add(adotante)
    end

    AdotanteRepo->>Pessoa: atualizarGerador(maiorIdEncontrado)
    activate Pessoa
%% Método void: finaliza e desativa sem seta de retorno
    deactivate Pessoa

%% Fim do carregarDados (void): desativa sem seta de retorno
    deactivate AdotanteRepo

    deactivate Main
```
### SD-02: Fluxo Padrão de Cadastro e Persistência de Pets
```mermaid
sequenceDiagram
    autonumber
    actor Usuario
    participant UI as << Boundary >><br/>ui:InterfaceDeUsuario
    participant Facade as << Control >><br/>PetFacade
    participant Service as << Control >><br/>petService:PetService
    participant PetClass as << Entity >><br/>Pet (Classe)
    participant Repo as << Control >><br/>repository:PetRepository

    Usuario->>UI: Solicita cadastro e insere dados do Pet
    activate UI

    UI->>Facade: executarAcaoPet(1)
    activate Facade
    Facade->>Facade: cadastrarPet()

    Facade->>Service: cadastrar(tipo, sexo, endArr, nome, raca, idade, peso)
    activate Service

    Note over Service: Bloco try { ... }
    Service->>Service: Instancia TipoAnimal, Sexo e Endereco

    Service->>PetClass: criar(nome, endereco, sexo, tipo, idade, peso, raca)
    activate PetClass
    Note over PetClass: Validações de Regex<br/>(Regras de Domínio)
    PetClass-->>Service: Instância de novoPet
    deactivate PetClass

    Service->>Repo: salvar(novoPet)
    activate Repo
    Note over Repo: Escreve no arquivo físico<br/>via BufferedWriter
    deactivate Repo

    Service-->>Facade: Retorna "SUCESSO"
    deactivate Service

    alt Se resposta for "SUCESSO"
        Note over Facade: Fluxo segue normalmente
    else Se capturar IllegalArgumentException
        Service-->>Facade: Retorna e.getMessage()
        Facade->>UI: erroSalvarObjPet()
        activate UI
        deactivate UI
    end

    deactivate Facade
    deactivate UI
```

### SD-03: Processo de Adoção (Vincular Tutor ao Pet)
```mermaid
sequenceDiagram
    autonumber
    actor Usuario
    participant UI as << Boundary >><br/>ui:InterfaceDeUsuario
    participant Facade as << Control >><br/>PetFacade
    participant AdotanteServ as << Control >><br/>adotanteService:AdotanteService
    participant PetServ as << Control >><br/>petService:PetService
    participant TutorClass as << Entity >><br/>Tutor (Classe)
    participant PetClass as << Entity >><br/>pet:Pet (Instancia)
    participant PetRepo as << Control >><br/>repository:PetRepository

    Usuario->>UI: Escolhe a opcao de Vinculo / Adocao
    activate UI
    UI->>Facade: executarAcaoPessoa(opcao)
    activate Facade

    Facade->>Facade: vincularPetAdotante()

%% PASSO 1: LOCALIZAR ADOTANTE
    Note over Facade, AdotanteServ: --- PASSO 1: Localizar o Adotante Desejado ---
    Facade->>Facade: gerenciarCriteriosFluxoAdotantes()
    Facade->>AdotanteServ: executarBuscaComCriteriosAtuais()
    activate AdotanteServ
    AdotanteServ-->>Facade: listagemAdotantes (String)
    deactivate AdotanteServ
    Facade->>UI: exibirListaAdotantes(listagemAdotantes)
    Facade->>UI: solicitarIdAdotante()
    UI-->>Facade: idAdotante

%% PASSO 2: LOCALIZAR PET
    Note over Facade, PetServ: --- PASSO 2: Localizar o Pet Desejado ---
    Facade->>Facade: gerenciarCriteriosFluxoPets()
    Facade->>PetServ: executarBuscaComCriteriosAtuais()
    activate PetServ
    PetServ-->>Facade: listagemPets (String)
    deactivate PetServ
    Facade->>UI: exibirListaPets(listagemPets)
    Facade->>UI: solicitarIdPet()
    UI-->>Facade: idPet

%% PASSO 3: EXECUTAR VÍNCULO E VALIDAÇÕES
    Note over Facade, PetServ: --- PASSO 3: Executar Vinculo e Validacoes ---
    Facade->>PetServ: vincularTutorAoPet(idAdotante, idPet, adotanteService)
    activate PetServ

    PetServ->>AdotanteServ: buscarAdotantePorId(idAdotante)
    activate AdotanteServ
    AdotanteServ-->>PetServ: Optional<Adotante>
    deactivate AdotanteServ

    PetServ->>PetRepo: buscarPorId(idPet)
    activate PetRepo
    PetRepo-->>PetServ: Optional<Pet>
    deactivate PetRepo

    alt Se Pet ja possuir tutor (pet.getTutorId() > 0)
        PetServ-->>Facade: Retorna "Operação Abortada: Este animal já possui um tutor..."
    else Caminho Feliz: Validacoes OK
        PetServ->>TutorClass: promoverAdotante(adotante)
        activate TutorClass
        TutorClass-->>PetServ: tutor (Objeto)
        deactivate TutorClass

        PetServ->>TutorClass: adicionarPet(pet)
        activate TutorClass
        deactivate TutorClass

        PetServ->>PetClass: vincularTutor(idAdotante)
        activate PetClass
        deactivate PetClass

        PetServ->>PetRepo: atualizar(pet, "8 - " + idAdotante)
        activate PetRepo
        Note over PetRepo: Atualiza o arquivo físico txt
        PetRepo-->>PetServ: true
        deactivate PetRepo

        PetServ-->>Facade: Retorna "SUCESSO"
    end
    deactivate PetServ

    alt Se resultado for "SUCESSO"
        Facade->>UI: exibirSucesso("Adotante promovido a Tutor e Pet vinculado...")
    else Caso Contrario
        Facade->>UI: errorExibir(resultado)
    end

    deactivate Facade
    deactivate UI
```

### SD-04: Consulta Dinâmica com Filtros Avançados de Pets
```mermaid
sequenceDiagram
    autonumber
    actor Usuario
    participant UI as << Boundary >><br/>ui:InterfaceDeUsuario
    participant Facade as << Control >><br/>PetFacade
    participant Service as << Control >><br/>petService:PetService
    participant Filtro as << Control >><br/>petFiltro:PetFiltro
    participant Criterio as << Entity >><br/>CriterioFiltro
    participant Repo as << Control >><br/>repository:PetRepository

    Usuario->>UI: Solicita filtragem de Pets
    activate UI
    UI->>Facade: listarPetsPorCriterio()
    activate Facade

%% GERENCIAMENTO DE CRITÉRIOS
    Note over Facade, UI: Loop de Configuração dos Critérios
    Facade->>Service: limparCriterios()

    Facade->>Service: obterCriteriosParaExibicao()
    activate Service
    Service-->>Facade: dadosExibicao (Map)
    deactivate Service

    Facade->>UI: solicitarAcaoGerenciamentoCriterios(dadosExibicao)
    UI-->>Facade: acao (1 - Adicionar, 3 - Filtrar)

    alt Acao = 1 (Adicionar Criterio)
        Facade->>UI: solicitarCriterioFiltro()
        UI-->>Facade: opcaoCrit
        Facade->>UI: solicitarTextoBusca()
        UI-->>Facade: valorRaw
        Facade->>Service: adicionarCriterio(opcaoCrit, valorRaw)
        activate Service
        Service->>Criterio: fromValor(opcaoCrit)
        activate Criterio
        Criterio-->>Service: crit (Enum)
        deactivate Criterio
        Note over Service: Trata conversões se for SEXO ou TIPO<br/>e adiciona no mapa criteriosAtivos
        deactivate Service
    end

%% EXECUÇÃO DA BUSCA FILTRADA
    Note over Facade, Repo: --- Execução da Busca Dinâmica ---
    Facade->>Service: executarBuscaComCriteriosAtuais()
    activate Service

    Service->>Repo: listarTodos()
    activate Repo
    Repo-->>Service: base (List<Pet>)
    deactivate Repo

    alt Se criteriosAtivos estiver vazio
        Note over Service: resultado = base
    else Possui critérios (criteriosAtivos.isEmpty() == false)
        Service->>Filtro: filtrar(base, criteriosAtivos)
        activate Filtro

        loop Para cada Map.Entry no mapa de criterios
            Filtro->>Filtro: filtrar(resultado, criterio, busca)
            activate Filtro

            loop Para cada Pet na lista
                Filtro->>Filtro: extrairCampo(pet, criterio)
                Note over Filtro: switch(criterio) para obter<br/>Nome, Idade, Raça, etc.
                Filtro->>Filtro: corresponde(criterio, valorDoCampo, busca)
                Note over Filtro: switch(criterio) com contains()<br/>ou equalsIgnoreCase()

                alt Se corresponde == true
                    Note over Filtro: Adiciona o pet na sublista resultado
                end
            end

            deactivate Filtro
        end

        Filtro-->>Service: resultado (List<Pet>)
        deactivate Filtro
    end

    alt Se resultado.isEmpty()
        Service-->>Facade: "VAZIO"
        Facade->>UI: exibirMensagemErrorConsulta()
    else Caso contrário
        Service->>Service: formatarListaParaTexto(resultado)
        Service-->>Facade: listagem (String)
        Facade->>UI: exibirListaPets(listagem)
    end
    deactivate Service
    UI-->>Usuario: Apresenta o resultado visual na tela
    deactivate Facade
    deactivate UI
```