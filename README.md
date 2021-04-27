# Implementando serviços no Kubernetes Local através do Minikube para simulação de ambiente em Nuvem
> Este projeto tem o objetivo de construir um ambiente Kubernetes Local a fim de criar um cenário similar ao encontrado em ambiente em Nuvem, com o intuito de realizar procedimentos como deploy, debugs, end2end, entre outros.

[![Docker Badge](https://img.shields.io/badge/-Docker-informational?style=flat-square&logo=Docker&logoColor=white&link=https://www.docker.com/)](https://www.docker.com/)
[![K8S Badge](https://img.shields.io/badge/-K8S-blueviolet?style=flat-square&logo=Kubernetes&logoColor=white&link=https://kubernetes.io/pt-br/)](https://kubernetes.io/pt-br/)
[![Spring Badge](https://img.shields.io/badge/-Spring-brightgreen?style=flat-square&logo=Spring&logoColor=white&link=https://spring.io/)](https://spring.io/)


## Descrição da Aplicação
A aplicação é bem simples, usada somente para apresentar o host e o IP na qual a aplicação está rodando. Seu objetivo é apresentar ao usuário as informações de "hardware" da aplicação. Desta forma, serão apresentados informações quando ela estiver em execução em um container Docker e quando ela estiver em um cluster Kubernetes. 

Para isto, utilizaremos o Spring Frameworks para construir nossa aplicação e o gerenciador de banco de dados MySQL "containerzado" para armazenar os dados da aplicação. 

<center><img width="400px" height="300px" src="/images/cqrs_architecture.png"></center>

Desta forma, teremos dois artefatos, um criado pelo gerenciador de projetos Maven e o outro criado através do Docker que será nossa database.

## Para Executar a Aplicação
Através do terminal acesse o diretório onde estão os diretórios e arquivos do projeto que foram clonados ou baixados do Github. Depois digite o comando abaixo para construir o artefato da aplicação:
```sh
mvn clean install
```

Agora para executar a aplicação realize o seguinte comando:
```sh
java --enable-preview -jar target/java-kubernetes.jar
```

Já para criar um container Docker é necessário que o programa esteja instalado em sua máquina. Neste [link](https://docs.docker.com/get-started/) é possível acompanhar quais são os processos para a instalação em sua máquina, seja ela Windows, Mac ou Linux (Unix).
Ãpós a instalação realize o seguinte comando para criarmos um container com o gerenciador de banco de dados MySQL para interação com a nossa aplicação.
```sh
docker run --name mysql57 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_USER=java -e MYSQL_PASSWORD=1234 -e MYSQL_DATABASE=k8s_java -d mysql/mysql-server:5.7
```
sobre os parâmetros do comando ``docker run``, segue sua descrição na tabela abaixo:
| Parâmetro | Descrição |
|-----------|-----------|
| --name    | Nome do container |
| -p        | Porta do container mapeada (:) com a Porta da máquina |
| -e        | Variável de Ambiente (*Eviroment*) para a aplicação em container |
| -d 		| Para liberar o terminal após concluir sua construção rodando a instância em segundo plano |


Este comando procura a imagem mais sua tag ``mysql/mysql-server:5.7`` no repositório local da máquina para criar o container, caso não encontre, ele acessa o repositório central [Docker Hub](https://hub.docker.com/) para baixar uma cópia localmente. Para conhecer mais parãmetros possíveis a serem utilizados para manipulação de containers clique [aqui](https://stack.desenvolvedor.expert/appendix/docker/comandos.html).

Para visualizar se o container esta ativo na máquina execute:
```sh
docker ps
```

Para visualizar os processos que foram executados para criação do container com o SGBD execute: 
```sh
docker logs mysql57
```
> Se você tiver o MySQL instalado em sua máquina poderá ocorrer conflito com a porta presente no container

Agora para executarmos nossa aplicação para visualizar ela em ação executamos no endereço de um navegador:
```sh
http://localhost:8080/app/hello
```
<img align="center" width="400" height="300" src="/images/cqrs_architecture.png">

Observe que será apresentado o nome de sua máquina mais o IP que foi alocado para acesso a sua rede local.



## Sobre Encapsular a Aplicação em um Container Docker
É necessário utilizar um *Dockerfile* para encapsular nossa aplicação em um container, o Dockerfile é um meio tipo de script de programação utilizado para criar imagens de software próprias. Isto significa que, com ele é possível criarmos uma espécie de receita para construir um container, permitindo definir um ambiente personalizado e próprio para o projeto. Segue abaixo como está implementado o script:
```sh
FROM openjdk:14-alpine

RUN mkdir /usr/myapp

COPY target/java-kubernetes.jar /usr/myapp/app.jar
WORKDIR /usr/myapp

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java --enable-preview $JAVA_OPTS -jar app.jar" ]
```

Com o arquivo criado, inicia-se o terminal e acesse o diretório do projeto, na qual está o arquivo e o artefato criado pelo Maven, mas antes de executar o comando para encapsularmos a aplicação em um container, na tabela abaixo é apresentado as instruções que foi inserida no arquivo.
| Instrução | Descrição|
|-----------|----------|
| FROM      | Define qual será a imagem que será a base do container |
| RUN       | Define qual é o comando a ser executado na criação de camadas da imagem (Podem haver mais de uma instrução RUN) |
| COPY 		| Permite realizar a passagem de arquivos e/ou diretórios a serem copiados para a imagem |
| WORKDIR	| Define qual será o diretório a ser utilizado para passagem de comandos |
| EXPOSE	| Expõem a porta do container para acesso externo |
| ENTRYPOINT| Executa os comandos passados como parâmetros na qual aceita a passagem de variáveis de ambientes |

Agora para construir uma imagem personalizada Docker com a encapsulação da aplicação utilizando o seguinte comando:
```sh
docker build --force-rm -t java-k8s .
```

O comando [docker build] possibilita a construção de uma imagem a partir de um Dockerfile e do contexto passado, no caso, as instruções inseridas. O parâmetro ``--force-rm`` é para forçar a exclusão de uma imagem que tenha a mesma tag que é passado com o parâmetro ``-t "nome_tag"``. No contexto do build o conjunto de arquivos a ser utilizados pela a imagem estão localizada através da especificação de um ``PATH``. O PATH é o diretório no sistema de arquivos local, o comando [build] não trabalha com o caminho do arquivo, então é preciso informar o caminho do diretório ou, no caso, utilizamos a passagem do parâmetro ponto ``.``, que indica ao Docker que é o diretório atual.

Agora para executar a imagem recém criada para iniciar a aplicação em um container Docker digite o seguinte comando:
```sh
docker run --name myapp -p 8080:8080 -d -e DATABASE_SERVER_NAME=mysql57 --link mysql57:mysql57 java-k8s:latest
```

Com este comando conseguimos iniciar o container com a aplicação e passamos alguns parâmetros para lincar este container com o container do MySQL que está em execução. Na qual é passado o parâmetro *enviroment* DATABASE_SERVER_NAME e o parametro ``--link`` para informar que nosso parametro mysql57 estará mapeado ao container com o nome mysql57.

Agora para executarmos nossa aplicação para visualizar ela em ação executamos no endereço de um navegador:
```sh
http://localhost:8080/app/hello
```
<img align="center" width="400" height="300" src="/images/cqrs_architecture.png">

Observe que será apresentado o ID do container mais o IP que foi alocado pelo Docker para acesso a rede local.

## Orquestrando os Containers no K8S (Minikube)
Kubernetes conhecido pela comunidade como [K8S] é um projeto de código aberto que gerencia o processo de distribuição e controle de aplicativos com vários containers, trabalhando com o Docker, mas não se restrigindo somente a ele, pois atualmente no mercado existem vários outros sistemas para trabalhar com containers.

Em K8s, não nos referimos a contêineres, mas sim a ``pods``, onde cada pod representa uma única instância de um aplicativo e consiste em um ou mais contêineres. Basicamente, trata-se de abstrair recursos para inserir um contêiner dentro de um cluster, com um único endereço IP e porta atrelado a ele.

Já o [Minikube](https://minikube.sigs.k8s.io/) é uma forma de executar o Kubernetes localmente como um único nó através de uma máquina virtual, sendo uma boa alternativa para testar funcionalidades e aprender a trabalhar com o Kubernetes.
> Antes de instalar o Minikube é necessário habilitar a virtualização dos recursos de hardware no BIOS

Para realizar a instalação do Minikube clique [aqui](https://minikube.sigs.k8s.io/docs/start/), assim como, para a instalação da ferramenta [kubectl CLI](https://kubernetes.io/docs/tasks/tools/) que se comunica e modifique os detalhes da configuração do K8S.

Para iniciar o processo de implantação dos serviços no Minikube e do Kubectl CLI, primeiramente, paramos a execução as aplicações que estão em containers em nossa máquina.
```sh
docker stop myapp mysql57
```

Para iniciar um ``cluster`` local utilizando o comando ``START`` e passamos o driver de virtualização do [Docker] como pré-requisito inserimos o seguinte comando:
```sh
minikube -p dev.homolog start --vm-driver=docker --cpus 2 --memory=4098
```

<center><img src="/images/cqrs_architecture.png" width="400px" height="300px"></center>

Com este comando passamos o parâmetro -p ([Perfil]) dev.homolog, informamos o valor do *driver* de virtualização através do parâmetro --vm-driver, setamos o valor de 2 processadores através do parâmetro --cpus e setamos a memória do cluster para uso de 4 Giga Bytes (GB).

Agora adicionaremos o recurso de *Proxy Reverso* ao habilitar o uso do ``ingress`` através do Nginx, que é um servidor web de código aberto que não se restringui somente ao recurso de proxy reverso, mas permite também o balanceamento de carga (*Load Balance*) HTTP, POP3, SMTP e proxy de e-mail para IMAP.
```sh
minikube -p dev.homolog addons enable ingress
```

Para obter um fator de gerenciamento através de indicadores gráficos para facilitar a compreensão das métricas geradas através da disponibilização dos serviços presentes nos pods, incluiremos o recurso de métricas ao habilitar o ``metrics-server``
```sh
minikube -p dev.homolog addons enable metrics-server
```

Para concluir a configuração do Minikube para permitir o acesso a uma interface gráfica do nosso cluster utilizando o comando:
```sh
minikube -p dev.homolog dashboard
```

Na qual após conclusão do processamento é fornecido uma URL para acesso a interface gráfica via navegador web.

<img align="center" width="400" height="300" src="/images/cqrs_architecture.png">

Agora precisamos criar uma separação dos serviços que serão parametrizados ao cluster utilizando um *Namespace*. O namespace ajuda a organizarmos os pods que serão inclusos para orquestração.
```sh
kubectl create namespace dev-pods01
```
> Um pod é considerado a menor unidade no K8S, mesmo podendo ter inúmeros containers dentro dele, uma boa prática é manter apenas um container por pod

### Configuração através de Descritores (Receitas)



Esta aplicação foi realizada seu deploy no serviço Elastic Kubernetes Service [EKS] da Amazon Web Services [AWS], utilizando dois serviços do framework Quarkus que se comunicam através de um barramento assíncrono usando a plataforma Kafka para processamento de streams (mensageria). Desta forma, foram desenvolvidos manifestos do Kubernetes para implantação no EKS e parametrização das configurações necessárias para obter um ambiente como o de produção.

* Banco de Dados - RDS que roda uma instancia do PostgreSQL (Class - db.t2.micro);
* Cluster - EKS que contem o serviço de Kubernetes na qual no EC2 Experience estão presentes 3 máquinas rodando a aplicação, 1 máquina rodando um Kafka Server e 1 máquina rodando um K6 Performance Server para testes da aplicação.

<img align="center" width="400" height="300" src="/images/settings_panel.png">

Kubernetes (K8s) é um produto Open Source utilizado para automatizar a implantação, o dimensionamento e o gerenciamento de aplicativos em contêiner.
> Este texto foi obtido da própria documentação da plataforma

Para implantar os serviços externos:
```
docker-compose up -d --build
```
Ele implantará quatro contêineres docker em seu ambiente com MongoDB, PostgreSQL, Kafka e Zookepper (exigido por Kafka)

Depois de implantar o Kafka, você precisará [criar o tópico no cluster Kafka] (https://kafka.apache.org/quickstart).


### Construindo a Estrutura de Configurações do K8S
No Kubernetes (K8S) em um mesmo cluster podem ser exportados inúmeras aplicações, onde se faz necessário criar uma estrutura através da parametrização de um perfil e local onde encontramos os containers responsáveis por diponibilizarem as aplicações. 

Em cada serviço são criados scripts para configuração do ambiente em cloud, na qual será orquestrado pelo K8S. Desta forma, nos diretórios ``kubefiles/`` estão todos os scripts necessários para configuração, como:
* Namespace - cria um local específico para administração dos serviços em execução; 
* Service - cria um proxy a fim de orientar qual replica de serviço deve atender a requisição;
* Deployment - ele especifica em qual local (namespace) deve ser criado a replica do serviço (a aplicação), quantas replicas devem ser executadas, qual o modelo que deve ser seguido, a especificação do container com a imagem e porta para acesso e saída, entre outras configurações;

Desta forma, para realizar a configuração do cluster fornecido pela AWS utilizando o comando ``kubectl apply -f "file_name.yml"`` para automatizá-la.


## Agradecimentos

Obrigado por ter acompanhado aos meus esforços para desenvolver um ambiente controlado Kubernetes para testarmos aplicações em um ambiente local antes de passá-las para o ambiente em nuvem! :octocat:

Como diria um antigo mestre:
> *"Cedo ou tarde, você vai aprender, assim como eu aprendi, que existe uma diferença entre CONHECER o caminho e TRILHAR o caminho."*
>
> *Morpheus - The Matrix*