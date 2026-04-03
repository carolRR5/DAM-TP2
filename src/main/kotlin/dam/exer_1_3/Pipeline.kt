package dam.exer_1_3

/*
 * Classe que represent um pipeline de transformação sequenciais sobre listas de strings.
 *
 * Internamente mantém uma lista ordenada de etapas, onde cada etapa é composta pelo seu nome e pela função
 * de transformação associada. Cada etapa recebe a lista resultante da etapa anterior e devolve uma nova lista
 * transformada, permitindo encadear operações de forma modular.
 */
class Pipeline {
    // Lista ordenada de etapas do pipeline. Cada etapa é um par (nome, função de transformação),
    // onde a função recebe uma lista de strings e devolve uma lista de strings transformada
    private val stagesList = mutableListOf<Pair<String, (List<String>) -> List<String>>>()

    // Função responsável por adicionar uma nova etapa ao pipeline, identificada pelo nome e pela função de transformação.
    // As etapas são executadas na ordem em que são adicionadas.
    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        stagesList.add(name to transform) // Adiciona um par (nome, função) ao fim da lista de etapas
    }

    // Executa todas as etapas do pipeline em ordem sobre a lista de entrada.
    // O resultado de cada etapa é passado como entrada à etapa seguinte.
    fun execute(input: List<String>): List<String> {
        var currentList = input // Começa com a lista de entrada original
        stagesList.forEach { step ->
            currentList = step.second(currentList) // Aplica a função de transformação da etapa atual à lista corrente
        }
        return currentList // Retorna a lista final após todas as transformações terem sido aplicadas
    }

    // Função responsável por imprimir o nome de cada etapa do pipeline, precedido pelo seu número de ordem com base 1.
    fun describe() {
        stagesList.forEachIndexed { index, stage -> // Percorre a lista de etapas, disponibilizando o índice e a etapa em cada iteração
            println("${index + 1}. ${stage.first}") // Imprime o número de ordem (índice + 1) seguido do nome da etapa.
        }
    }

    // Função responsável por combinar duas etapas existentes numa nova etapa composta.
    // A nova etapa aplica primeiro a transformação de name 1 e de seguida a de name2, sendo adicionada ao pipeline
    // com o nome newName.
    // Se alguma das etapas indicadas não for encontrada, a composição não é realizada.
    fun compose(name1: String, name2: String, newName: String) {
        val s1 = stagesList.find { it.first == name1 }?.second // Procura a etapa com o nome name1 e obtém a sua função de transformação. Devolve null se não existir
        val s2 = stagesList.find { it.first == name2 }?.second // Procura a etapa com o nome name2 e obtém a sua função de transformação. Devolve null se não existir

        if (s1 != null && s2 != null) { // Só prossegue se ambas as etapas foram encontradas
            addStage(newName) { input -> s2(s1(input))} // Cria uma nova etapa que aplica s1 ao input e passa o resultado a s2, compondo as duas transformações
        }
    }

    // Função responsável por executar o mesmo input em dois pipelines independentes.
    // Retorna um Pair em que o primeiro elemento é o resultado deste pipeline e o segundo é o
    // resultado do pipeline recebido como argumento.
    fun fork(other: Pipeline, input: List<String>) =
        this.execute(input) to other.execute(input)
}

/*
 * Função que permite construir um pipeline de forma declarativa.
 *
 * Recebe um lambda com Pipeline como recetor (Pipeline.() -> Unit), o que significa que dentro deste bloco
 * é possível chamar diretamente as funções da classe Pipeline (ex.: addStage), como se estivéssemos dentro
 * da própria classe.
 *
 * Pipeline.() -> Unit, indica que o lambda é uma extended function sobre Pipeline, o que significa que dentro
 * do bloco 'this' refere-se à instância de Pipeline permitindo chamar diretamente as suas funções sem escrever
 * 'this.' ou 'pipeline'
 *
 * O tipo de retorno Unit indica que o bloco existe apenas para configurar o pipeline, não para devolver nenhum resultado.
 *
 * Cria uma instância de Pipeline, aplica-lhe o bloco e devolve-a pronta a ser usada.
 */
fun buildPipeline(block: Pipeline.() -> Unit): Pipeline {
    val pipeline = Pipeline() // Cria uma nova instância vazia de Pipeline

    // Invoca o bloco lambda com pipeline como recetor (this).
    // Como block é do tipo Pipeline.() -> Unit (extension function) ao chamá-lo sobre uma instância
    // de Pipeline, todas as chamadas dentro do bloco (ex: addStage) são resolvidas no contexto dessa instância
    // (como se estivéssemos dentro da própria classe)
    pipeline.block()
    return pipeline // Devolve o pipeline já configurado com todas as etapas adicionadas pelo bloco
}

fun main() {
    // Lista de linhas de log
    val logs = listOf(
        " INFO: server started ",
        " ERROR: disk full ",
        " DEBUG: checking config ",
        " ERROR: out of memory ",
        " INFO: request received ",
        " ERROR: connection timeout"
    )

    // Constrói o pipeline utilizando buildPipeline.
    // Dentro do bloco, as funções de Pipelines sãp chamadas diretamente graças ao recetor Pipeline.() -> Unit.
    // As etapas são adicionadas e executadas na ordem em que aparecem.
    val myPipeline = buildPipeline {
        addStage("Trim") { list ->
            list.map { it.trim() } // Remove espaços em branco no início e no fim de cada linha da lista
        }

        addStage("Filter errors") { list ->
            list.filter { it.contains("ERROR") } // Mantém apenas as linhas que contém a palavra "ERROR", descartando as restantes
        }

        addStage("Uppercase") { list ->
            list.map {it.uppercase()} // Converte cada linha da lista para maiúsculas
        }

        addStage("Add index") { list ->
            list.mapIndexed { index, line -> "${index + 1}. $line" } // Prefixa cada linha com o seu número de ordem com base 1
        }
    }

    println("Pipeline stages:")
    myPipeline.describe() // Imprime os nomes de todas as etapas registadas no pipeline, pela ordem que foram adicionadas

    // Executa o pipeline sobre a lista de logs, aplicando as etapas sequencialmente
    val result = myPipeline.execute(logs)

    println("\nResult:")
    result.forEach { println(it) } // Imprime cada linha do resultado final, uma por linha

    println()

    // Cria uma etapa composta que combina as transformações de "Trim" e "Filter errors" numa só etapa chamada "Trim + Filter errors"
    myPipeline.compose("Trim", "Filter errors", "Trim + Filter errors")

    // Segundo pipeline independente, com apenas duas etapas: Trim e Uppercase
    val pipeline2 = buildPipeline {
        addStage("Trim") {list -> list.map { it.trim() }} // Remove espaços em branco do ínicio e no fim de cada linha
        addStage("Uppercase") {list -> list.map { it.uppercase() }} // Converte cada linha para maiúsculas
    }

    // Executa o mesmo input (logs) nos dois pipelines em paralelo e obtém ambos os resultados.
    // O resultado é desestruturado diretamente num par de variáveis (result1, result2).
    val (result1, result2) = myPipeline.fork(pipeline2, logs)
    println("Fork result 1: $result1") // Resultado produzido pelo myPipeline sobre os logs
    println("Fork result 2: $result2") // Resultado produzido pelo pipeline2 sobre os mesmos logs
}