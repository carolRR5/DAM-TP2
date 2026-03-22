package dam.exer_1_3

class Pipeline {
    private val stagesList = mutableListOf<Pair<String, (List<String>) -> List<String>>>()

    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        stagesList.add(name to transform)
    }
}