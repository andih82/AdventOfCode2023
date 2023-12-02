package aoc

import java.io.File

open class Day(val day: Int) {

    val puzzle
        get() = File("src/main/resources/day$day.txt").readLines()

    val example1
        get() = File("src/main/resources/day${day}_1ex.txt").readLines()

    val example2
        get() = File("src/main/resources/day${day}_2ex.txt").readLines()


}