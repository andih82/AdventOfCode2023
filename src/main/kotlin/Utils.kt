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

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}