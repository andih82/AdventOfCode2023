import aoc.Day

class Day6 : Day(6) {

    private var races: MutableList<Pair<Int, Int>> = mutableListOf()
    private var race2: Pair<Long, Long>

    init {
        races = mutableListOf()
        val times =
            puzzle[0].dropWhile { !it.isDigit() }.trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        val dist = puzzle[1].dropWhile { !it.isDigit() }.trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        for (idx in times.indices) {
            races.add(Pair(times[idx], dist[idx]))
        }
        race2 = Pair(
            times.joinToString("") { it.toString() }.toLong(),
            dist.joinToString("") { it.toString() }.toLong()
        )
    }

    fun solve(): Int =
        races.map { (0L..it.first).count { t -> timeGood(t, it.first.toLong(), it.second.toLong()) } }
            .reduce { a, b -> a * b }

    fun solve2(): Int =
        (0L..race2.first).count { t -> timeGood(t, race2.first, race2.second) }

    private fun timeGood(pressed: Long, time: Long, dist: Long): Boolean {
        val duration = time - pressed
        return pressed * duration > dist
    }

}

fun main() {
    val day6 = Day6()
    println(day6.solve())
    println(day6.solve2())

}