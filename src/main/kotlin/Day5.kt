import aoc.Day
import kotlin.time.measureTime


class Day5 : Day(5) {

    val seeds = mutableListOf<Long>()
    val seedsAsRange = mutableListOf<LongRange>()
    var backwardList: MutableList<MutableMap<LongRange, Long>>
    var forwardList: MutableList<MutableMap<LongRange, Long>>

    init {
        backwardList = mutableListOf()
        forwardList = mutableListOf()

        puzzle.forEach { line ->
            when {
                line.isEmpty() -> Unit
                line.startsWith("seeds:") -> {
                    val seedString = line.dropWhile { !it.isDigit() }.trim().split(" ").map { it.toLong() }
                    seeds.addAll(seedString)
                    seedsAsRange.addAll(seedString.windowed(2, step = 2).map { LongRange(it[0], it[0] + it[1] - 1) })
                }

                line[0].isDigit() -> {
                    val tempRange = line.split(" ").map { it.toLong() }
                    forwardList.last()[LongRange(tempRange[1], tempRange[1] + tempRange[2] - 1)] = tempRange[0] - tempRange[1]
                    backwardList.last()[LongRange(tempRange[0], tempRange[0] + tempRange[2] - 1)] = tempRange[1] - tempRange[0]

                }

                else -> {
                    backwardList.add(mutableMapOf()); forwardList.add(mutableMapOf())
                }
            }
        }
    }

    fun findLocation(): Long = seeds.map { findLocationFromList(it) }.min()
    fun findLocationBackwards(): Long {

        tailrec fun tf(loc: Long): Long {
            return if (seedsAsRange.any {
                    it.contains(findSeedFromList(loc))
                }) loc
            else tf(loc + 1)
        }

        return tf(0L)
    }

    fun findSeedFromList(location: Long): Long {
        var value = location
        for (m in backwardList.reversed()) {
            value = m.mapToOffset(value)
        }
        return value
    }

    fun findLocationFromList(seed: Long): Long {
        var value = seed
        for (m in forwardList) {
            value = m.mapToOffset(value)
        }
        return value
    }

}

fun MutableMap<LongRange, Long>.mapToOffset(k: Long): Long = k + (this[keys.firstOrNull { k in it }] ?: 0)

fun main() {
    val day5 = Day5()
    println(measureTime { println(day5.findLocation()) })
    println(measureTime { println(day5.findLocationBackwards()) })
}
