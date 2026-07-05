### US01 - Cadastrar Pet
- **Como funcionário**
- **Eu quero cadastrar um novo pet**
- **Para que eu possa acompanhar histórico do pet**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu principal e escolho a opção de cadastrar um novo pet.**
- [ ] **Quando eu informar sequencialmente os dados do pet**
  - [ ] Tipo do animal (Cachorro/Gato)
  - [ ] Sexo (Macho/Fêmea) 
  - [ ] Endereço (Rua e Cidade preenchidos; Número é opcional)
  - [ ] Nome e Sobrenome (Apenas letras e espaços)
  - [ ] Raça (Apenas letras)
  - [ ] Idade (Número entre 0.1 e 60)
  - [ ] Peso (Número entre 0.5 e 60)
- [ ] **Então o sistema deve retornar que o pet foi cadastrado com sucesso**

---

### US02 - Alterar Informações do Pet
- **Como funcionário**
- **Eu quero alterar as informações de um pet específico após listá-lo por critérios**
- **Para que eu possa manter os dados atualizados**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu principal e escolho a opção de alterar informações de um pet e eu executei uma busca por critérios e o sistema retornou uma lista de pets.**
- [ ] **Quando eu selecionar o número do pet desejado, escolher a opção para ser alterada e informar um valor válido**
- [ ] **Então o sistema deve atualizar o peso do pet no repositório e exibir os dados atualizados do animal na tela.**

---

### US03 - Deletar um Pet Cadastrado
- **Como funcionário**
- **Eu quero deletar o registro de um pet específico após listá-lo por critérios**
- **Para que eu possa remover dados incorretos ou de animais que já não fazem parte da ONG**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu principal e escolho a opção de deletar um pet**
- [ ] **E executo uma busca por critérios, ao escolher o pet desejado**
- [ ] **Devo digitar "SIM" na mensagem de confirmação de exclusão**
- [ ] **Então o sistema deve remover o pet do repositório, e exibir a mensagem "Pet removido com sucesso!"**

---

### US04 - Listar Pets Cadastrados Por Critérios
- **Como funcionário**
- **Eu quero listar os pets cadastrados adicionando critérios de busca**
- **Para que eu possa escolher o pet desejado**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu principal e escolho a opção de listar os pets cadastrados por critérios**
- [ ] **Quando eu informar um ou mais critérios de busca (nome, tipo, sexo, cidade)**
- [ ] **Então o sistema deve exibir uma lista de pets que correspondam aos critérios informados, e eu possa escolher o pet com base ao número**

--- 

### US05 - Listar Todos os Pets Cadastrados
- **Como funcionário**
- **Eu quero listar todos os pets cadastrados**
- **Para que eu possa ter uma visão geral dos animais registrados**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu principal e escolho a opção de listar todos os pets cadastrados**
- [ ] **Quando eu selecionar essa opção**
- [ ] **Então o sistema deve exibir uma lista completa de todos os dados dos pets cadastrados**

---

> User Stories para ser implementadas no sistema.

### US06 - Cadastrar Adotante

- **Como funcionário**
- **Eu quero cadastrar um novo adotante**
- **Para que eu possa registrar informações sobre pessoas interessadas em adotar animais**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu principal e escolho a opção de cadastrar um novo adotante.**
- [ ] **Quando eu informar sequencialmente os dados do adotante**
  - [ ] Nome completo (Apenas letras e espaços)
  - [ ] CPF (Formato válido)
  - [ ] Endereço (Logradouro)
  - [ ] Telefone (Apenas números)
  - [ ] Email (Formato válido)
- [ ] **Então** o sistema deve gerar um ID numérico único, salvar os dados no repositório TXT e retornar que o adotante foi cadastrado com sucesso.

---

### US07 - Alterar Informações do Adotante
- **Como funcionário**
- **Eu quero alterar as informações de um adotante específico após listá-lo por critérios**
- **Para que eu possa manter os dados atualizados**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu Adotante e escolho a opção de alterar informações de um adotante após listá-lo por critérios**
- [ ] **Quando eu selecionar o número do adotante desejado, escolher a opção para ser alterada e informar um valor válido**
- [ ] **Então** o sistema deve remover o adotante do repositório TXT e exibir a mensagem "Adotante removido com sucesso!".

### US08 - Deletar um Adotante Cadastrado
- **Como funcionário**
- **Eu quero deletar o registro de um adotante específico após listá-lo por critérios**
- **Para que eu possa remover dados incorretos ou de pessoas que quiserem retirar seu interesse em adotar um animal**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu Adotante e escolho a opção de deletar um adotante após listá-lo por critérios**
- [ ] **E executo uma busca por critérios, ao escolher o adotante desejado**
- [ ] **Devo digitar "SIM" na mensagem de confirmação de exclusão**
- [ ] **Então** o sistema deve remover o adotante do repositório TXT e exibir a mensagem "Adotante removido com sucesso!".

### US09 - Listar Adotantes Cadastrados Por Critérios
- **Como funcionário**
- **Eu quero listar os adotantes cadastrados adicionando critérios de busca**
- **Para que eu possa escolher o adotante desejado**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu Adotante e escolho a opção de listar os adotantes cadastrados por critérios**
- [ ] **Quando eu informar um ou mais critérios de busca (nome, CPF, cidade ou Email)**
- [ ] **Então** o sistema deve utilizar o `UsuarioFiltro` para exibir uma lista numerada de adotantes correspondentes, permitindo que eu selecione o adotante desejado com base no seu número da lista.

---
  
## US10: Vincular Pet (Promover Adotante a Tutor)
**Como** funcionário da ONG  
**Eu quero** buscar um adotante existente e vinculá-lo a um  ou mais pets  
**Para que** ele seja promovido a Tutor, tornando-se o responsável legal por aquele animal

### Critérios de Aceitação:
- [ ] **Dado** que eu estou no menu principal e escolho a opção de vincular um pet a um adotante.
- [ ] **Quando** o sistema solicitar um critério de busca (Nome, CPF, Cidade ou Email) para localizar o adotante.
- [ ] **E** eu informar o termo de busca, o sistema deve listar todos os adotantes correspondentes utilizando o filtro de usuários.
- [ ] **Então** o sistema deve solicitar que eu digite o **ID do Adotante** escolhido e o **ID do Pet** que será vinculado.
- [ ] **E** o sistema deve:
  - Validar se o ID do Pet e o ID do Adotante existem.
  - Validar se o Pet escolhido já não possui outro tutor vinculado.
- [ ] **Ao passar nas validações**, o sistema deve migrar internamente o perfil de Adotante para Tutor, associar o Pet ao seu ID, e atualizar as informações nos arquivos TXT.
- [ ] **Se** qualquer ID for inválido ou o animal já estiver ocupado, o sistema deve exibir uma mensagem de erro e abortar a operação.

---

### US11 - Desvincular Tutor de um Pet
- **Como funcionário**
- **Eu quero desvincular um tutor de um ou mais pets**
- **Para que eu possa atualizar o status dos animais após a desvinculação**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu Adotante e escolho a opção de desvincular um tutor de um pet após listá-los por critérios**
- [ ] **Quando eu selecionar o número do adotante desejado, escolher a opção de desvincular e selecionar o número do pet desejado**
- [ ] **O pet deve ser adicionado à lista de pets disponíveis para adoção**
- [ ] **Então o sistema deve desvincular o tutor do pet, mudar a pessoa para adotante e exibir uma mensagem de sucesso "Desvinculação realizada com sucesso!"**

---

### US12 - Deletar um Tutor Cadastrado (Desvincular Responsabilidade)
**Como** funcionário da ONG  
**Eu quero** deletar o registro de um tutor específico após listá-lo por critérios  
**Para que** eu possa remover o registro e disponibilizar seus pets para adoção novamente

#### Critérios de Aceitação:
- [ ] **Dado** que eu estou no menu Tutor e escolho a opção de deletar um tutor.
- [ ] **Quando** eu filtrar os tutores, selecionar o número do tutor desejado e digitar "SIM" na mensagem de confirmação.
- [ ] **Então** o sistema deve remover o tutor do repositório TXT, **alterar o status dos seus pets vinculados de volta para "Disponível/Sem Tutor"**, e exibir a mensagem "Tutor removido com sucesso e pets desvinculados!".

---

### US13 - Listar Tutores Cadastrados Por Critérios
**Como** funcionário da ONG  
**Eu quero** listar os tutores cadastrados adicionando critérios de busca  
**Para que** eu possa visualizar os responsáveis e quais animais pertencem a eles

#### Critérios de Aceitação:
- [ ] **Dado** que eu estou no menu Tutor e escolho a opção de listar tutores por critérios.
- [ ] **Quando** eu informar os parâmetros de busca (Nome, CPF, Cidade ou ID do Pet).
- [ ] **Então** o sistema deve exibir os tutores correspondentes e, **ao lado de cada tutor, listar os nomes e IDs dos pets sob a responsabilidade dele**.

---

### US14  - Alterar Informações do Tutor
**Como** funcionário da ONG  
**Eu quero** alterar as informações de um tutor específico após listá-lo por critérios  
**Para que** eu possa manter os dados cadastrais atualizados sem afetar seus pets vinculados

#### Critérios de Aceitação:
- [ ] **Dado** que eu estou no menu Tutor e escolho a opção de alterar informações de um tutor.
- [ ] **Quando** eu executar uma busca por critérios, selecionar o tutor desejado, escolher o campo a ser alterado e informar o novo valor válido.
- [ ] **Então** o sistema deve atualizar os dados do tutor no repositório TXT, garantindo que a sua lista de pets vinculados permaneça intacta, e exibir os novos dados na tela.
