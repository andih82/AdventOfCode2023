import aoc.Day

class Day15 : Day(15) {

    val initSequence = puzzle.map { it.split(",") }.flatten()

    fun hash(n: Int, char: Char): Int {
        return ((n + char.code) * 17) % 256
    }

    fun encode(s: String) = s.fold(0) { acc, c -> hash(acc, c) }

    fun solve1() = initSequence.sumOf { s -> encode(s) }

    fun solve2() =
        initSequence.map { encode(it.takeWhile { c -> c != '-' && c != '=' }) to it }
            .fold(mapOf<Int, List<String>>()) { acc, pair ->
                val l = acc[pair.first]?.toMutableList() ?: mutableListOf()
                val idx = l.indexOfFirst { it.startsWith(pair.second.takeWhile { c -> c != '-' && c != '=' }) }
                when {
                    pair.second.contains("=") -> {
                        if (idx > -1) {
                            l[idx] = pair.second
                        } else {
                            l.add(pair.second)
                        }
                    }

                    pair.second.contains("-") -> {
                        if (idx > -1) {
                            l.removeAt(idx)
                        }
                    }
                }
                acc + (pair.first to l)
            }.map { (k, v) ->
                v.mapIndexed { index, s ->
                    (k + 1) * (index + 1) * s.takeLastWhile { it.isDigit() }.toInt()
                }.sum()
            }.sum()

}

fun main() {
    val day = Day15()
    println(day.solve1())
    println(day.solve2())
}