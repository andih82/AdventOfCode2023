import aoc.Day
import kotlin.time.Duration.Companion.seconds

class Day14 : Day(14) {

    val solidRocks: List<Pair<Int,Int>>
    val roundRocks: List<Pair<Int,Int>>
    val lines : Int
    val length : Int

    init {
        val list = puzzle
        length = list[0].length - 1
        lines = list.size -1
        solidRocks = list.mapIndexed { lIdx, line ->
            line.mapIndexed { cIdx, c ->
                if (c == '#')
                    Pair(lIdx,cIdx)
                else
                    Pair(-1,-1)
            }.filter { it.first != -1 }
        }.flatten()
        roundRocks = list.mapIndexed { lIdx, line ->
            line.mapIndexed { cIdx, c ->
                if (c == 'O')
                 Pair(lIdx,cIdx)
                else
                    Pair(-1,-1)
            }.filter { it.first != -1 }
        }.flatten()
    }


    fun solve1() : Int =
        roundRocks.tilt(Pair(-1,0), Pair(lines, length),solidRocks).groupBy { it.first }.map { (k, v) -> (lines  - k + 1) * v.size }.sum()

    fun solve2() : Int {
        var tilted = roundRocks
        var n = 0
        repeat(1000){
            tilted = tilted.tilt(Pair(-1,0), Pair(lines, length),solidRocks)
            tilted = tilted.tilt(Pair(0,-1), Pair(lines, length),solidRocks)
            tilted = tilted.tilt(Pair(1,0), Pair(lines, length),solidRocks)
            tilted = tilted.tilt(Pair(0,1), Pair(lines, length),solidRocks)
            println("${tilted.groupBy { it.first }.map { (k, v) -> (lines  - k + 1 ) * v.size }.sum()} ${++n}")
//            print(solidRocks,tilted)
        }
        return tilted.groupBy { it.first }.map { (k, v) -> (lines  - k + 1)  * v.size }.sum()
    }


    fun print(solid: List<Pair<Int, Int>>, round: List<Pair<Int, Int>>){
        println("---------------------------------------------------------")
        (0..lines).forEach { x ->
            (0..length).forEach { y ->
                if (solid.contains(Pair(x,y)))
                    print("#")
                else if (round.contains(Pair(x,y)))
                    print("O")
                else
                    print(".")
            }
            println("    ${lines - x + 1}")
        }

    }

}
tailrec fun Pair<Int, Int>.step(dir : Pair<Int,Int> = Pair(-1,0), max  : Pair<Int, Int>,rocks: List<Pair<Int,Int>>) : Pair<Int, Int> {
    val next= Pair(first + dir.first, second + dir.second)

    return if (next.first < 0 || next.second < 0 || next.first > max.first || next.second > max.second || rocks.any { it == next })
        this
    else
        next.step(dir,max,rocks)
}

fun List<Pair<Int,Int>>.tilt(dir : Pair<Int,Int> = Pair(-1,0), max: Pair<Int, Int>, solid: List<Pair<Int,Int>>) : List<Pair<Int, Int>>  {
    var result = this.sortedBy {
        when(dir){
            Pair(-1,0) -> it.first
            Pair(0,1) -> - it.second
            Pair(1,0) -> - it.first
            Pair(0,-1) ->it.second
            else -> it.first
        }
    }.toMutableList()
    for (rock in result.indices){
        result[rock] = result[rock].step(dir, max,result + solid)
    }
    return result
}
fun main(){
val day = Day14()

    println(day.solve1())

    var i = 1000000000
    while (i > 100){
        i -= 51
    }
    println(i+ 51)   // value at 1000000000
//    println(day.print(day.solidRocks,day.roundRocks))
    println(day.solve2())
}
