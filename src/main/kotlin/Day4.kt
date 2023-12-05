import aoc.Day
import kotlin.math.min
import kotlin.math.pow

fun String.mapNumbers() = this.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }

data class Card(val id: Int, val winningNumber: List<Int>, val myNumber: List<Int>) {

    constructor(id: Int, winningNumber: String, myNumber: String) :
            this(id,
                winningNumber.mapNumbers(),
                myNumber.mapNumbers()
            )

    var copies = 1

    val winningPoint
        get() = myNumber.count { it in winningNumber }

    fun calcPart1() =
        (2.0).pow(winningPoint - 1.0).toInt()

}

class Day4 : Day(4) {

    private fun toCard(line: String): Card {
        val (l, r) = line.dropWhile { it != ':' }.drop(1).split("|")
        return Card(line.drop(4).takeWhile { it != ':' }.trim().toInt(), l, r)
    }

    fun solvePart1() = puzzle.sumOf { toCard(it).calcPart1() }

    fun solvePart2() =
        puzzle.map { toCard(it) }
            .apply {
                forEachIndexed { idx, card ->
                    val wp = card.winningPoint
                    subList(idx + 1, min(idx + 1 + wp, size))
                        .forEach { it.copies += card.copies }
                }
            }.sumOf { it.copies }
}

fun main() {
    val day4 = Day4()
    println(day4.solvePart1())
    println(day4.solvePart2())
}