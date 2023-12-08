import aoc.Day
import aoc.findLCM

class Day8 : Day((8)) {

    val instructions: List<Int>
    val steps: Map<String, List<String>>

    init {
        instructions = puzzle.take(1).map { s -> s.map { if (it == 'L') 0 else 1 } }.flatten()
        steps = puzzle.drop(2).associate {
            it.substring(0, 3) to listOf(it.substring(7, 10), it.substring(12, 15))
        }
    }

    fun solve1(): Int {
        var pos = steps["AAA"]!!
        return generateSequence(0) { it + 1 }.first {
            val i = instructions[(it % instructions.size)]
            if (pos[i] != "ZZZ") {
                pos = steps[pos[i]]!!
                false
            } else {
                true
            }
        } + 1
    }

    fun solve1recursive(): Int {

        tailrec fun fn(i: Int, pos: List<String>): Int {
            val modi = instructions[(i % instructions.size)]
            return if (pos[modi] == "ZZZ") {
                i
            } else {
                fn(i + 1, steps[pos[modi]]!!)
            }
        }

        return fn(0, steps["AAA"]!!) + 1
    }

    fun solve2(): Long {
        val next = steps.filter { it.key.endsWith("A") }.values
        val result = next.map { step ->
            var pos = step
            generateSequence(0L) { it + 1 }.first {
                val i = instructions[(it % instructions.size).toInt()]
                if (!pos[i].endsWith("Z")) {
                    pos = steps[pos[i]]!!
                    false
                } else {
                    true
                }
            } + 1
        }
        return result.reduce { a, b -> findLCM(a, b) }
    }
}

fun main() {
    val day8 = Day8()
    println(day8.instructions)
    println(day8.steps)
    println("first seq ${day8.solve1()}")
    println("first rec ${day8.solve1recursive()}")
    println("second ${day8.solve2()}")
}