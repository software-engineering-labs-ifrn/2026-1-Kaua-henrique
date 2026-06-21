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
- [ ] **Então o sistema deve retornar que o adotante foi cadastrado com sucesso**

---

### US07 - Alterar Informações do Adotante
- **Como funcionário**
- **Eu quero alterar as informações de um adotante específico após listá-lo por critérios**
- **Para que eu possa manter os dados atualizados**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu Adotante e escolho a opção de alterar informações de um adotante após listá-lo por critérios**
- [ ] **Quando eu selecionar o número do adotante desejado, escolher a opção para ser alterada e informar um valor válido**
- [ ] **Então o sistema deve atualizar os dados do adotante no repositório e exibir os dados atualizados na tela.**

### US08 - Deletar um Adotante Cadastrado
- **Como funcionário**
- **Eu quero deletar o registro de um adotante específico após listá-lo por critérios**
- **Para que eu possa remover dados incorretos ou de pessoas que quiserem retirar seu interesse em adotar um animal**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu Adotante e escolho a opção de deletar um adotante após listá-lo por critérios**
- [ ] **E executo uma busca por critérios, ao escolher o adotante desejado**
- [ ] **Devo digitar "SIM" na mensagem de confirmação de exclusão**
- [ ] **Então o sistema deve remover o adotante do repositório, e exibir a mensagem "Adotante removido com sucesso!"**

### US09 - Listar Adotantes Cadastrados Por Critérios
- **Como funcionário**
- **Eu quero listar os adotantes cadastrados adicionando critérios de busca**
- **Para que eu possa escolher o adotante desejado**

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu Adotante e escolho a opção de listar os adotantes cadastrados por critérios**
- [ ] **Quando eu informar um ou mais critérios de busca (nome, CPF, cidade)**
- [ ] **Então o sistema deve exibir uma lista de adotantes que correspondam aos critérios informados, e eu possa escolher o adotante com base ao número**

### US10 - Vincular Adotante a um Pet
- **Como funcionário**
- **Eu quero vincular uma pessoa a um ou mais pets**
- **Para que eu possa registrar e acompanhar como os animais estão após adotados** 

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu Adotante e escolho a opção de vincular um adotante a um pet após listá-los por critérios**
- [ ] **Quando eu selecionar o número do adotante desejado, escolher a opção de vincular a um pet e selecionar o número do pet desejado**
- [ ] **O pet deve ser removido da lista de pets disponíveis para adoção**
- [ ] **Então o sistema deve vincular o adotante ao pet, mudar a pessoa para tutor e exibir uma mensagem de sucesso "Cadastro de adoção realizado com sucesso!"**

### US11 - Desvincular Tutor de um Pet
- **Como funcionário**
- **Eu quero desvincular um tutor de um ou mais pets**
- **Para que eu possa atualizar o status dos animais após a desvinculação** 

**Critérios de Aceitação:**
- [ ] **Dado que eu estou no menu Adotante e escolho a opção de desvincular um tutor de um pet após listá-los por critérios**
- [ ] **Quando eu selecionar o número do adotante desejado, escolher a opção de desvincular e selecionar o número do pet desejado**
- [ ] **O pet deve ser adicionado à lista de pets disponíveis para adoção**
- [ ] **Então o sistema deve desvincular o tutor do pet, mudar a pessoa para adotante e exibir uma mensagem de sucesso "Desvinculação realizada com sucesso!"**