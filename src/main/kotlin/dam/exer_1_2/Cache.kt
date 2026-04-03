package dam.exer_1_2

/**
 * Classe genérica que implementa uma cache em memória, associando chaves do tipo [K] a valores do tipo [V].
 *
 * O parâmetro 'K' representa o tipo de chave e o 'V' o tipo de valor.
 * A restrição ': Any' garante que os parâmetros não podem ser null, evitando ambiguidades.
 *
 * @param K Tipo de chave
 * @param V Tipo de valor
 */
class Cache<K: Any, V: Any> () {
    // Mapa mutável que serve de armazenamento interno para guardar os pares chave-valor.
    // É privado para que o exterior não consiga modificar a cache diretamente,
    // forçando todo o acesso a passar pelas funções públicas da classe.
    private val map = mutableMapOf<K, V>()

    /**
     *  Função responsável por inserir ou substituir o valor associado a key.
     *  Se a chave já existir, o valor anterior é sobrescrito.
     *
     * @param key Chave a inserir ou substituir.
     * @param value Valor a associar a chave.
     */
    fun put (key: K, value: V){
        map[key] = value // Se a chave já existir, o valor é sobrescrito
    }

    /**
     * Função responsável por obter o valor associado a uma chave.
     *
     * O tipo de retorno V? permite distinguir dois tipos de cenários:
     *     - A chave existe, sendo que devolve o valor armazenado, do tipo V.
     *     - A chave não existe, sendo que devolve null.
     *
     * Sem o '?' o compilador não permitiria devolver null, pois V puro não aceita esse valor.
     *
     * @param key Chave a procurar.
     * @return Valor associado à chave, ou null caso a chave não existir.
     */
    fun get(key: K): V?{
        return map[key] // Devolve null automaticamente se a chave não existir
    }

    /**
     * Função responsável por remover uma entrada da cache com base na chave.
     *
     * @param key Chave a remover.
     */
    fun evict(key: K) {
        map.remove(key) // Remove o par chave-valor. Se a chave não existir, não faz nada
    }

    /**
     * Função responsável por devolver o número de elementos atualmente armazenados na cache.
     *
     * @return Número de pares chave-valor existentes.
     */
    fun size(): Int{
        return map.size
    }

    /**
     * Devolve o valor existente para key ou, se a chave ainda não existir, invoca default para calcular um novo valor,
     * armazena-o e devolve-o.
     *
     * O lambda default só é invocado quando necessário, evitando computações desnecessárias quando a chave
     * já está presente.
     *
     * @param key Chave a procurar ou inserir.
     * @param default Lambda invocada para calcular o valor caso a chave não exista.
     * @return Valor existente ou recém-calculado.
     */
    fun getOrPut (key: K, default: () -> V): V{
        val cacheValue = get(key) // Tenta obter o valor atual da cache para a chave fornecida

        if (cacheValue != null) {
            return cacheValue // Caso a chave já existe, devolve o valor armazenado sem invocar o lambda
        } else {
            val defaultValue = default() // Caso contrário, invoca o lambda para calcular o valor por omissão
            put(key, defaultValue) // Armazena o valor calculado para que as futuras chamadas não precisem de o recalcular
            return defaultValue // Devolve o valor calculado e armazenado
        }
    }

    /**
     * Função responsável por aplicar uma transformação ao valor associado a uma chave, caso esta exista.
     *
     * @param key Chave cujo valor se pretende transformar.
     * @param action Lambda que recebe o valor atual e devolve o novo valor transformado.
     * @return true se a chave existia e a transformação foi aplicada. false caso contrário.
     */
    fun transform(key: K, action: (V) -> V): Boolean {
        val cacheValue = get(key) // Obtém o valor atual associado à chave, ou null caso não existir

        if (cacheValue != null) { // Só aplica a transformação se a chave existir na cache
            val newValue = action(cacheValue) // Aplica o lambda ao valor atual, produzindo o novo valo
            put(key, newValue) // Substitui o valor antigo pelo novo valor transformado
            return true // Indica que a transformação foi aplicada com sucesso
        }
        return false // Caso a chave não existe, não altera a cache e indica insucesso
    }

    /**
     * Função responsável por devolver uma cópia imutável do conteúdo da cache.
     *
     * @return Cópia imutável de todas as entradas da cache.
     */
    fun snapshot(): Map<K, V> {
        return map.toMap() // toMap() cria uma cópia independente e imutável do mapa interno
    }

    /**
     * Função responsável por devolver uma cópia imutável contendo apenas as entradas cujos valores satisfazem
     * o predicado fornecido.
     *
     * @param predicate Lambda que recebe um valor e devolve true se a entrada deve ser incluída.
     * @return Mapa imutável com as entradas filtradas.
     */
    fun filterValues(predicate: (V) -> Boolean): Map<K, V> =
        map.filter { predicate(it.value) } // Mantém apenas as entradas cujo valor satisfaz o predicado
            .toMap() // Converte para Map imutável

}

fun main(){
    // Cache de frequências. A chave é a palavra e o valor é o número de ocorrências
    var cache1 = Cache<String, Int>()
    cache1.put("kotlin", 1) // Insere "kotlin" com frequência inicial 1
    cache1.put("scala", 1) // Insere "scala" com frequência inicial 1
    cache1.put("haskell", 1) // Insere "haskell" com frequência inicial 1

    println(
        "--- Word frequency cache ---" +
        "\nSize: ${cache1.size()}" + // 3 entradas inseridas até este ponto
        "\nFrequency of \"kotlin\": ${cache1.get("kotlin")}" + // "kotlin" existe, ou seja, devolve 1
        "\ngetOrPut \"kotlin\": ${cache1.getOrPut("kotlin") { 0 }}" + // "kotlin" já existe, por isso devolve 1 sem invocar o lambda
        "\ngetOrPut \"java\":  ${cache1.getOrPut("java") { 0 }}" + // "java" não existe, logo invoca o lambda, armazena 0 e devolve 0
        "\nSize after getOrPut: ${cache1.size()}" + // "java" foi adicionado, logo o tamanho passa a 4
        "\nTransform \"kotlin\" (+1): ${cache1.transform("kotlin") { it + 1 }}" + // "kotlin" existe, então incrementa de 1 para 2, devolve true
        "\nTransform \"cobol\" (+1): ${cache1.transform("cobol") { it + 1 }}" + // "cobol" não existe, logo a cache não vai ser alterada, devolvendo false
        "\nSnapshot: ${cache1.snapshot()}" // cópia imutável do estado atual da cache
    )

    println() // Linha em branco para separar as duas secções de output

    // Cache de identificadores. A chave é um id numérico e o valor é um nome associado
    var cache2 = Cache<Int, String>()
    cache2.put(1, "Alice") // Associa o id 1 ao nome "Alice"
    cache2.put(2, "Bob") // Associa o id 2 ao nome "Bob"

    println(
        "--- Id registry cache ---" +
        "\nId 1 -> ${cache2.get(1)}" +
        "\nId 2 -> ${cache2.get(2)}"
    )

    cache2.evict(1) // Remove a entrada com id 1. A partir daqui, get(1) devolverá null
    println("After evict id 1, size: ${cache2.size()}" + // Apenas a entrada 2 permanece (size = 1)
            "\nId 1 after evict -> ${cache2.get(1)}") // A chave 1 foi removida por isso devolve null

    println() // Linha em branco para separar a secção do challenge

    // Imprime apenas as palavras com frequência superior a zero, excluindo "java" (count=0)
    println("Words with count > 0: ${cache1.filterValues { it > 0}}")
}