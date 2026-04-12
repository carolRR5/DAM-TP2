# Assignment TP2 - Kotlin Exercises

Course: Desenvolvimento de Aplicações Móveis <br>
Student: Carolina Raposo (51568) <br>
Date: 10/04/2026 <br>
Repository URL: https://github.com/carolRR5/DAM-TP2

---

## 1. Introduction  

Este relatório descreve a implementação dos exercícios de Kotlin do segundo trabalho prático da unidade 
curricular de Desenvolvimento de Aplicações Móveis (DAM). Os exercícios foram concebidos para aprofundar 
a compreensão das funcionalidades de programação funcional do Kotlin, nomeadamente sealed classes, 
extension functions, higher-order functions, genéricos, lambdas e sobrecarga de operadores.

## 2. System Overview

O projeto é uma aplicação Kotlin/JVM construída com o Apache Maven. Contém um grupo de 
exercícios (`exer_1`) com quatro componentes independentes, cada um abordando um conjunto 
específico de funcionalidades da linguagem Kotlin:

| Ficheiro | Conceitos Abordados |
|----------|-------------------|
| `Event.kt` | Sealed classes, extension functions, funções de ordem superior |
| `Cache.kt` | Genéricos, funções de ordem superior, parâmetros lambda |
| `Pipeline.kt` | Funções de ordem superior, padrão DSL builder, composição de funções |
| `Vec2.kt` | Data classes, sobrecarga de operadores, extension functions |

Cada exercício é implementado como um ficheiro Kotlin independente com uma função `main` 
para testar a implementação, e está organizado na seguinte estrutura de pacotes:

```
dam/
  exer_1_1/  — Processamento de Registos de Eventos (Event.kt)
  exer_1_2/  — Cache em Memória (Cache.kt)
  exer_1_3/  — Pipeline de Dados (Pipeline.kt)
  exer_1_4/  — Biblioteca de Vetores 2D (Vec2.kt)
```

## 3. Architecture and Design

O projeto segue a estrutura de diretórios padrão do Maven:

```
DAM_TP2/
├── pom.xml
└── src/
    └── main/
        └── kotlin/
            └── dam/
                ├── exer_1_1/
                │   └── Event.kt
                ├── exer_1_2/
                │   └── Cache.kt
                ├── exer_1_3/
                │   └── Pipeline.kt
                └── exer_1_4/
                    └── Vec2.kt
```

Todos os ficheiros de exercícios estão organizados sob o package `dam`, separados por subpackages 
correspondentes a cada exercício (`exer_1_1`, `exer_1_2`, `exer_1_3`, `exer_1_4`). Cada ficheiro é independente 
e inclui a sua própria função `main()` para demonstração autónoma. Não existem dependências entre os ficheiros de exercícios.

Decisões de design principais:
- `Vec2` é implementado como uma `data class` para tirar partido do suporte automático a `equals`, `hashCode`, `toString` e desestruturação fornecido pelo Kotlin.
- `Event` utiliza uma hierarquia de `sealed class` para modelar um conjunto fechado de tipos de eventos, permitindo expressões `when` exaustivas.
- `Cache` utiliza uma classe genérica com bounds de tipo (`K: Any, V: Any`) e um `MutableMap` internamente para armazenar os pares chave-valor.
- `Pipeline` utiliza um lambda com recetor (`Pipeline.() -> Unit`) na sua função DSL builder `buildPipeline`, permitindo uma sintaxe de construção limpa e declarativa.

## 4. Implementation

### Exercício 1.1 - `Vec2.kt`: Vetor 2D com Sobrecarga de Operadores

O vetor 2D é implementado como uma `data class Vec2(val x: Double, val y: Double)`. Foram sobrecarregados os operadores 
aritméticos principais, nomeadamente a adição e subtração entre vetores, a multiplicação por um escalar do lado direito, e a 
negação unária. Foi também implementado o operador `compareTo` para permitir a comparação de vetores pela sua magnitude euclidiana,
calculada através da fórmula `sqrt(x² + y²)`, o que possibilita o uso direto dos operadores `<` e `>` entre instâncias de `Vec2`.

Para além dos operadores, foram implementadas as funções `dot` para o produto escalar e `normalized` para obter o vetor 
unitário na mesma direção, lançando uma `IllegalArgumentException` caso o vetor seja nulo. O operador `get` foi também sobrecarregado 
para permitir o acesso às componentes por índice.

Como complemento, foi definida uma extension function sobre `Double` para suportar a multiplicação escalar pelo lado esquerdo 
(por exemplo `2.0 * v`). Como `Vec2` é uma `data class`, o Kotlin gera automaticamente as funções `component1()` e `component2()`, 
tornando possível a desestruturação do vetor sem qualquer implementação adicional.

---

### Exercício 1.2 - `Event.kt`: Processamento de Eventos com Sealed Classes

O sistema de eventos é modelado através de uma `sealed class Event` com três subtipos: `Login`, `Purchase` e `Logout`. A utilização
de uma sealed class garante que todas as expressões `when` sobre `Event` são exaustivas, ou seja, o compilador obriga a tratar todos 
os subtipos sem necessidade de um ramo `else`.

Foram implementadas duas extension functions sobre `List<Event>`: a função `filterByUser` que filtra os eventos de um utilizador e
specífico, e a função `totalSpent` que calcula o total gasto por um utilizador somando os valores de todos os seus eventos `Purchase`. 
Foi também implementada a função de ordem superior `processEvents`, que recebe um lambda como parâmetro e o aplica a cada evento da 
lista, tornando o comportamento de processamento completamente configurável pelo chamador.

---

### Exercício 1.3 - `Cache.kt`: Cache Genérica de Chave-Valor

A cache é implementada como uma classe genérica `Cache<K: Any, V: Any>`, onde o bound `: Any` garante que nem as chaves, 
nem os valores podem ser nulos. Internamente, os pares chave-valor são armazenados num `MutableMap` privado, assegurando que 
todo o acesso ao estado da cache passa pelas funções públicas da classe.

Foram implementadas as operações básicas de inserção, consulta e remoção de entradas, bem como funções mais avançadas 
como `getOrPut`, que devolve o valor existente ou calcula e armazena um novo utilizando um lambda, e `transform`, que aplica 
uma transformação ao valor associado a uma chave e devolve um booleano a indicar se a operação foi bem-sucedida. Para proteger
o estado interno, as funções `snapshot` e `filterValues` devolvem sempre cópias imutáveis do conteúdo da cache.

---

### Exercício 1.4 - `Pipeline.kt`: Pipeline de Processamento de Texto com DSL Builder

O pipeline é implementado na classe `Pipeline`, que mantém internamente uma lista ordenada de etapas de transformação,
onde cada etapa é composta pelo seu nome e por uma função do tipo `(List<String>) -> List<String>`. As etapas são executadas 
sequencialmente, sendo que o resultado de cada uma serve de entrada à seguinte.

Para além das operações básicas de adição e execução de etapas, foram implementadas as funções `compose`, que combina duas 
etapas existentes numa nova etapa composta, e `fork`, que executa o mesmo input em dois pipelines independentes e devolve ambos 
os resultados num `Pair`. A função de topo `buildPipeline`, que utiliza um lambda com recetor (`Pipeline.() -> Unit`), permite 
construir pipelines de forma declarativa e legível.

Como exemplo de utilização, foi construído no `main()` um pipeline com quatro etapas sequenciais — Trim, Filter errors, 
Uppercase e Add index — aplicado a uma lista de strings de log.

## 5. Testing and Validation

Cada exercício foi testado utilizando os dados de exemplo fornecidos no enunciado. O output foi verificado
e comparado com os resultados esperados especificados no enunciado.

## 6. Usage Instructions

1. Abrir o projeto no IntelliJ IDEA.
2. Navegar até ao pacote do exercício pretendido (`dam.exer_1_1`, `dam.exer_1_2`, etc.).
3. Executar a função `main` do ficheiro correspondente.
4. Verificar o output na consola.

---

## 12. Version Control and Commit History

O repositório foi gerido utilizando Git e GitHub. Os commits foram realizados regularmente ao longo do processo de 
desenvolvimento, correspondendo a passos específicos de implementação de cada exercício.

## 13. Difficulties and Lessons Learned

- **Exercício 1.1** — Compreender como as sealed classes garantem a exaustividade nas expressões `when` 
e como as extension functions permitem adicionar comportamento a classes existentes sem as modificar;
- **Exercício 1.2** — Compreender o papel do bound `: Any` nas classes genéricas e como usar `null` como sentinela para chaves ausentes;
- **Exercício 1.3** — Compreender como usar lambdas com recetor (`Pipeline.() -> Unit`) para permitir 
uma construção declarativa do pipeline;
- **Exercício 1.4** — Compreender como funciona a sobrecarga de operadores em Kotlin e como as extension 
functions sobre tipos externos (como `Double`) podem ser usadas para suportar a sobrecarga de operadores pelo lado esquerdo;

## 14. Future Improvements

As soluções atuais dos exercícios constituem uma base sólida nos fundamentos do Kotlin. As 
melhorias futuras irão focar-se em elevar o código a padrões mais profissionais, nomeadamente através 
da implementação de testes unitários formais e da exploração de padrões arquiteturais mais avançados, 
como, por exemplo, tornar a execução do `Pipeline` assíncrona recorrendo às Coroutines do Kotlin.

## 15. AI Usage Disclosure (Mandatory)

Nesta parte do trabalho, a utilização de ferramentas de IA para geração de código foi explicitamente proibida. 
Por isso, todo o código presente neste repositório foi implementado manualmente pela aluna.

No entanto, foram utilizadas ferramentas de IA apenas para a melhoria da redação do relatório README, conforme permitido 
pelas diretrizes do trabalho. A aluna assume total responsabilidade pela exatidão e conteúdo desta documentação.