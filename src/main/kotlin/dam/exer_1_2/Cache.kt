package dam.exer_1_2

class Cache<K: Any, V: Any> () {
    private val map = mutableMapOf<K, V>()

    fun put (key: K, value: V){
        map[key] = value
    }
}