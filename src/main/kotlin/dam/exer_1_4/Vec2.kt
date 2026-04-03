package dam.exer_1_4

import kotlin.math.sqrt

/*
 * Classe que representa um vetor bidimensional com coordenadas x e y do tipo Double.
 *
 * Por ser uma data class, o Kotlin gera automaticamente as funções toString(), equals(), hashCode(),
 * copy(), component1() e component2(), sem necessidade de as escrever manualmente.
 *
 * Implementa Comparable<Vec2> para que dois vetores possam ser comparados pelo seu comprimento,
 * permitindo usar os operadores <, > entre vetores e funções como max() e min() em listas.
 */
data class Vec2(val x: Double, val y: Double): Comparable<Vec2> {
    // Operador de soma. Soma este vetor com outro vetor.
    // A palavra 'operator' permite utilizar o símbolo '+' diretamente entre dois Vec2, em vez de chamar a função pelo nome.
    operator fun plus(other: Vec2): Vec2 {
        return Vec2(x + other.x, y + other.y) // Soma separadamente as componentes x e as componentes y.
    }

    // Operador de subtração. Subtrai outro vetor a este vetor.
    // A palavra 'operator' permite utilizar o símbolo '-' diretamente entre dois Vec2.
    operator fun minus(other: Vec2): Vec2 {
        return Vec2(x - other.x, y - other.y) // Subtrai separadamente as componentes x e as componentes y.
    }

    // Operador de multiplicação escalar, sendo que multiplica este vetor por um número, ampliando ou reduzindo o seu comprimento.
    operator fun times(scalar: Double): Vec2 {
        return Vec2(x * scalar, y * scalar) // Multiplica cada componente pelo número fornecido.
    }

    // Operador de negação. Inverte a direção do vetor, negando ambas as componentes.
    operator fun unaryMinus(): Vec2 {
        return Vec2(-x, -y)  // Troca o sinal de x e de y.
    }

    // Calcula o comprimento (magnitude) do vetor, ou seja, a distância da origem ao ponto (x,y).
    // É utilizado o teorema de Pitágoras: comprimento = raiz quadrada de (x^2 + y^2).
    fun magnitude(): Double {
        return sqrt(x * x + y * y) // Eleva x e y ao quadrado, soma-os e calcula a raiz quadrada
    }

    // Calcula e devolve o produto escalar entre este vetor e outro.
    // O produto escalar multiplica as componentes correspondentes e soma os resultados.
    fun dot(other: Vec2): Double {
        return x * other.x + y * other.y // Multiplica x com x e y com y, depois soma os dois resultados.
    }

    // Devolve a versão normalizada deste vetor, ou seja, um vetor com a mesma direção mas com comprimento igual a 1.
    // Para normalizar, divide-se cada componente pelo comprimento do vetor.
    // Se o vetor for o vetor zero (magnitude == 0), lança uma exceção, pois não é possível normalizar.
    fun normalized(): Vec2 {
        val mag = magnitude() // Calcula a magnitude do vetor atual
        // Um vetor zero não tem direção definida, pelo que não pode ser normalizado
        if (mag == 0.0) throw IllegalArgumentException("Cannot normalize a zero vector")
        return Vec2(x / mag, y / mag) // Divide cada componente pelo comprimento, obtendo um vetor de comprimento 1
    }

    // Permite comparar dois vetores pelo seu comprimento, usando os operadores < e >.
    // Ao implementar esta função da interface Comparable, o Kotlin permite utilizar automaticamente
    // os operadores <, >, <= e >= entre vetores, e funções como max() e min() em listas de vetores.
    override fun compareTo(other: Vec2): Int {
        return magnitude().compareTo(other.magnitude()) // Compara os comprimentos dos dois vetores. Devolve negativo, zero ou positivo.
    }

    // Permite aceder às componentes do vetor por índice, como se fosse uma lista.
    // v[0] devolve x e v[1] devolve y. Qualquer outro índice lança uma exceção.
    operator fun get(index: Int): Double = when (index) {
        0 -> x // índice 0 corresponde a x
        1 -> y // índice 1 corresponde a y
        else -> throw IndexOutOfBoundsException("Index $index is out bounds for Vec2") // Qualquer outro índice não existe num vetor 2D.
    }
}

// Permite utilizar a multiplicação escalar com o número à esquerda.
// Esta operação não pode ser definida dentro da classe Vec2 porque o lado esquerdo não é um Vec2. Ao escrever
// esta função fora da classe como uma extensão de Double, ensinamos ao Kotlin o que fazer quando um Double é multiplicado por Vec2.
operator fun Double.times(other: Vec2): Vec2 {
    return Vec2(this * other.x, this * other.y) // this é o Double à esquerda. Multiplica-o por cada componente do vetor.
}

fun main() {
    val a = Vec2(3.0, 4.0) // Cria o vetor a com x=3 e y=4
    val b = Vec2(1.0, 2.0) // Cria o vetor b com x=1 e y=2

    println("a = $a") // Imprime Vec2(x=3.0, y=4.0), formato gerado automaticamente pela data class
    println("b = $b") // Imprime Vec2(x=1.0, y=2.0)
    println("a + b = ${a + b}") // Chama o operador plus -> Vec2(4.0, 6.0)
    println("a - b = ${a - b}") // Chama o operador minus -> Vec2(2.0, 2.0)
    println("a * 2.0 = ${a * 2.0}") // Chama o operador times -> Vec2(6.0, 8.0)
    println("-a = ${-a}") // Chama o operador unaryMinus -> Vec2(-3.0, -4.0)
    println("|a| = ${a.magnitude()}") // Comprimento de a -> sqrt(9+16) = 5.0
    println("a dot b = ${a.dot(b)}") // Produto escalar -> 3*1 + 4*2 = 11.0
    println("norm(a) = ${a.normalized()}") // Vetor normalizado de a -> Vec2(0.6, 0.8)
    println("a[0] = ${a[0]}") // Chama o operador get com índice 0 -> devolve x = 3.0
    println("a[1] = ${a[1]}") // Chama o operador get com índice 1 -> devolve y = 4.0
    println("a > b = ${a > b}") // Compara os comprimentos: |a|=5.0 > |b|=2.24 -> true
    println("a < b = ${a < b}") // Compara os comprimentos: |a|=5.0 < |b|=2.24 -> false

    println("\n2.0 * a = ${2.0 * a}") // Chama a extensão de Double -> Vec2(6.0, 8.0)

    // A data class gera automaticamente component1() que devolve x e component2() que devolve y.
    val (x,y) = a // x recebe a.component1()=3.0 e y recebe a.component2()=4.0
    println("Destructuring: x=$x, y=$y") // Imprime os valores separados do vetor a

    println()

    // Lista com três vetores de comprimentos diferentes
    val vectors = listOf(Vec2(1.0, 0.0), Vec2(3.0, 4.0), Vec2(0.0, 2.0))
    println("Longest = ${vectors.max()}") // max() utiliza compareTo para encontrar o vetor mais longo -> Vec2(3.0, 4.0) com comprimento 5.0
    println(" Shortest = ${vectors.min()}") // min() usa compareTo para encontrar o vetor mais curto -> Vec2(1.0, 0.0) com comprimento 1,0
}