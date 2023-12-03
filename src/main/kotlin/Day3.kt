import aoc.Day

data class Pos(val line: Int, val pos: Int, val value: String) {

    infix fun isAdjacent(that: Pos) =
        line in (that.line - 1)..(that.line + 1) && pos in (that.pos - 1)..(that.pos + that.value.length)
}

class Day3 : Day(3) {
    val symbols = mutableListOf<Pos>()
    val potentialParts = mutableListOf<Pos>()

    val identifiedParts
        get() = potentialParts.filter { symbols.any { s -> s isAdjacent it } }

    val identifedGears
        get() = symbols.filter { it.value == "*" }
            .filter { potentialParts.count { part -> it isAdjacent part  } == 2 }
            .map { Pair(
                potentialParts.find {  part -> it isAdjacent part  },
                potentialParts.findLast {  part -> it isAdjacent part  }
                )
            }

    operator fun invoke(lines: List<String>) = run {
        lines.forEachIndexed { idx, line ->
            val endedLine = "$line."
            var value = ""
            endedLine.forEachIndexed { pos, c ->
                when {
                    c == '.' -> if (!value.isEmpty()) {
                        potentialParts.add(Pos(idx, pos - value.length, value))
                        value = ""
                    }
                    c.isDigit() -> value += c
                    !c.isDigit() -> {
                        if (!value.isEmpty()) {
                            potentialParts.add(Pos(idx, pos - value.length, value))
                            value = ""
                        }
                        symbols.add(Pos(idx, pos, c.toString()))
                    }
                }
            }
        }
        this
    }

}

fun main() {
    val day3 = Day3()
    day3(day3.puzzle)
    println(day3.symbols)
    println(day3.potentialParts)
    println(day3.identifiedParts.sumOf { it.value.toInt() })
    println(day3.identifedGears.sumOf { (first, second) -> (first?.value?.toInt()?:0) * (second?.value?.toInt()?:0) })
}

