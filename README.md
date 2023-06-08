# DijkstraAppPUC
Orientações
• Este é um projeto Interdisciplinar
• Data de Entrega: 10/06/2023
• Formato de trabalho: Individual, Duplas ou trios. Não serão aceitos trabalhos com mais de 3
integrantes.
• Forme sua equipe na disciplina de mobile teórica no CANVAS (Conjunto de grupos: PROJ-GRAFOS)
• Somente um aluno Equipe criada sobe os arquivos
• Em 10/06 surgirá uma tarefa no CANVAS da disciplina de Mobile teórica para sua equipe entregar
em formato compactado (exclusivamente .ZIP):
• Códigos do projeto (arquivo compactado em ZIP do projeto Android em Kotlin)
• Relatório descrevendo como sua equipe solucionou o problema e jus4ficando a estratégia
adotada.
• IMPORTANTE:
• Programas estritamente idên4cos serão desconsiderados. Considera-se programas iguais
aqueles que os fontes são muito parecidos até mesmo em nomeação de variáveis, classes,
componentes e comentários.
• A nota será compara4va, ou seja, o melhor trabalho irá 4rar a maior nota
• O resultado final do so1ware será avaliado pelos professores Mateus Dias e Sérgio
Marques
Problema
O Campus I da PUC-Campinas tem uma área territorial extensa e possui um número significa;vo de
prédios que são acessados pelos alunos que se deslocam para assis;r as aulas, elaborarem seus
projetos, pesquisas, inclusive se alimentarem durante os intervalos.
A universidade deseja definir os melhores caminhos para que seus alunos possam realizar para
chegar nos pontos (des;nos) desejados em menor tempo possível.
Projeto
Sua equipe é uma empresa. A missão dela é desenvolver um aplica;vo em Android/Kotlin na;vo
para determinar quais são os caminhos mais rápidos de um prédio ao outro e qual o tempo
associado a cada percurso.
Para resolver esse problema é necessário trabalhar com grafos. Sua equipe tem a responsabilidade
de encontrar a solução para este problema. Ela deve par;r do pressuposto que as informações
necessárias como nome dos prédios, caminhos entre eles, deverão ser armazenados em um Banco
de Dados no Firestore/Firebase.
Entrada
• X – O número prédios
• Y – Número de caminhos entre todos os prédios
• Tempo em minutos dos caminhos entre um prédio e outro
Saída: O tempo dos caminhos mais rápidos entre os prédios
Requisitos importantes de implementação
A equipe irá definir como efetuará a solução desse problema, para tal se desejar, pode usar e
adaptar algoritmos já existentes;
Escrever a aplicação usando linguagem Kotlin para Android.
Sempre que o estudante/usuário iniciar o aplica;vo, esse “grafo” será sempre reestruturado.
Afinal, pode surgir um novo prédio ou novo caminho inserido no banco de dados e o
“recálculo” deverá ser feito sempre que o aluno acessar o app.
Uma única tela: onde estou e para onde quero ir. O aplica;vo mostrar o MELHOR (menor)
caminho e outros que porventura possam exis;r.
Atenção: Não será necessário usar nenhuma API de Mapas como Google Maps. A ideia é que
você implemente a lógica do algoritmo e não serão avaliados aspectos gráficos como
primordiais ou interface linda e maravilhosa.
Como recomendação estudar como implementar grafos na prá;ca em Kotlin. Sugestão de
leitura:
hdps://medium.com/@piyushchauhan/basic-graph-algorithms-in-kotlin-8b84beda8907
Relatório
• Capa com nome e RA dos integrantes do trabalho
• Apresentação do problema, ressaltando os pontos que apresentaram maior dificuldade
• Discussão da solução, jus;ficando o que for relevante para tal. Se houver o uso de algum algoritmo
“clássico” jus;ficar a escolha do mesmo e como foi implementado. Se houve alguma modificação
no mesmo, esta deve ser explicada e jus;ficada
• Referência bibliográfica
