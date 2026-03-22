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