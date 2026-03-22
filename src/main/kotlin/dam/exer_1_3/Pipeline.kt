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

    fun compose(name1: String, name2: String, newName: String) {
        val s1 = stagesList.find { it.first == name1 }?.second
        val s2 = stagesList.find { it.first == name2 }?.second

        if (s1 != null && s2 != null) {
            addStage(newName) { input -> s2(s1(input))}
        }
    }

    fun fork(other: Pipeline, input: List<String>) =
        this.execute(input) to other.execute(input)
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

    println()
    myPipeline.compose("Trim", "Filter errors", "Trim + Filter errors")

    val pipeline2 = buildPipeline {
        addStage("Trim") {list -> list.map { it.trim() }}
        addStage("Uppercase") {list -> list.map { it.uppercase() }}
    }

    val (result1, result2) = myPipeline.fork(pipeline2, logs)
    println("Fork result 1: ${result1}")
    println("Fork result 2: ${result2}")
}