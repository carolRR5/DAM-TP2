package dam.exer_1_3

class Pipeline {
    private val stagesList = mutableListOf<Pair<String, (List<String>) -> List<String>>>()

    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        stagesList.add(name to transform)
    }

    fun execute(input: List<String>): List<String> {
        var currentList = input
        stagesList.forEach { step ->
            currentList = step.second(currentList)
        }
        return currentList
    }

    fun describe() {
        stagesList.forEachIndexed { index, stage ->
            println("${index + 1}. ${stage.first}")
        }
    }
}

fun buildPipeline(block: Pipeline.() -> Unit): Pipeline {
    return Pipeline().apply(block)
}

fun main() {
    val logs = listOf(
        " INFO: server started ",
        " ERROR: disk full ",
        " DEBUG: checking config ",
        " ERROR: out of memory ",
        " INFO: request received ",
        " ERROR: connection timeout"
    )

    val myPipeline = buildPipeline {
        addStage("Trim") { list ->
            list.map { it.trim() }
        }

        addStage("Filter errors") { list ->
            list.filter { it.contains("ERROR") }
        }

        addStage("Uppercase") { list ->
            list.map {it.uppercase()}
        }

        addStage("Add index") { list ->
            list.mapIndexed { index, line -> "${index + 1}. $line" }
        }
    }

    println("Pipeline stages:")
    myPipeline.describe()

    val result = myPipeline.execute(logs)

    println("\nResult:")
    result.forEach { println(it) }
}