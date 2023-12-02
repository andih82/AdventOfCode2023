package aoc

import kotlin.math.max

data class Game(val id: Int) {
    var green = 0;
    var red = 0;
    var blue = 0;

    operator fun invoke(sets: List<String>): Game {
        sets.map { e ->
            e.split(',').map {
                val (v, k) = it.trim().split(" ")
                when (k) {
                    "green" -> green = max(green, v.toInt())
                    "red" -> red = max(red, v.toInt())
                    "blue" -> blue = max(blue, v.toInt())
                    else -> Unit
                }
            }
        }
        return this
    }
}

class Day2 : Day(2)

fun main() {
    val day2 = Day2()
    val games = day2.puzzle.map { line ->
        val game = Game(line.drop(5).takeWhile { it.isDigit() }.toInt())
        game(line.dropWhile { it != ':' }.drop(1).split(";"))
    }.toList()
    //part1
    println(games.filter { g ->
        g.green <= 13 && g.red <= 12 && g.blue <= 14
    }.map { it.id }.sum())
    //part2
    println(games.map { g ->
        g.green * g.blue * g.red
    }.sum())
}

