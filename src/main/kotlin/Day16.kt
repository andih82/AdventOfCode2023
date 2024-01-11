import aoc.Day
import kotlin.math.max

class Day16 : Day(16) {


    val cave = Grid<Char>(puzzle) {
        it.map { c -> c }.toList()
    }

    fun solve2(): Int {
        val startpos = mutableListOf<Pair<Pair<Int,Int> , Pair<Int,Int>>>()
        for (i in 0..<cave.max.first) {
            startpos.add(Pair(Pair(i,0),Pair(i,-1)))
            startpos.add(Pair(Pair(i,cave.max.second-1),Pair(i, cave.max.second )))
        }
        for (i in 0..<cave.max.second) {
            startpos.add(Pair(Pair(0,i),Pair(-1,i)))
            startpos.add(Pair(Pair(cave.max.first-1, i),Pair(cave.max.second,i)))
        }
        return startpos.map { findEnergized(it.first,it.second) }.max()
    }

    fun solve1(): Int{
        return  findEnergized(Pair(0,0), Pair(0,-1))
    }
    private fun findEnergized(start: Pair<Int,Int> = Pair(0,0), prev : Pair<Int,Int> = Pair(0,-1)): Int {
        val fringe = mutableListOf(Pair(start,prev))

        val visited = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

        while (fringe.isNotEmpty()) {
            val aktNode = fringe.removeFirst()

            if (!aktNode.first.inBounds(cave.max) || aktNode in visited) {
                continue
            }
            visited.add(aktNode)
            val akt = aktNode.first
            val prev = aktNode.second
            fringe.addAll(cave.nextPos(akt) { next ->

                val result = akt.inBounds(cave.max) && when (cave[akt]) {
                    '/' -> {
                        when (prev comeFrom akt) {
                            Direction.UP -> akt goesTo next == Direction.LEFT
                            Direction.DOWN -> akt goesTo next == Direction.RIGHT
                            Direction.LEFT -> akt goesTo next == Direction.UP
                            Direction.RIGHT -> akt goesTo next == Direction.DOWN
                        }
                    }

                    '\\' ->
                        when (prev comeFrom akt) {
                            Direction.UP -> akt goesTo next == Direction.RIGHT
                            Direction.DOWN -> akt goesTo next == Direction.LEFT
                            Direction.LEFT -> akt goesTo next == Direction.DOWN
                            Direction.RIGHT -> akt goesTo next == Direction.UP
                        }

                    '-' -> when (prev comeFrom akt) {
                        Direction.UP ->
                            akt goesTo next == Direction.LEFT || akt goesTo next == Direction.RIGHT

                        Direction.DOWN ->
                            akt goesTo next == Direction.RIGHT || akt goesTo next == Direction.LEFT

                        Direction.LEFT -> akt goesTo next == Direction.RIGHT
                        Direction.RIGHT -> akt goesTo next == Direction.LEFT
                    }

                    '|' -> when (prev comeFrom akt) {
                        Direction.UP -> akt goesTo next == Direction.DOWN
                        Direction.DOWN -> akt goesTo next == Direction.UP
                        Direction.LEFT ->
                            akt goesTo next == Direction.UP || akt goesTo next == Direction.DOWN

                        Direction.RIGHT ->
                            akt goesTo next == Direction.UP || akt goesTo next == Direction.DOWN
                    }

                    else -> when (prev comeFrom akt) {
                        Direction.UP -> akt goesTo next == Direction.DOWN
                        Direction.DOWN -> akt goesTo next == Direction.UP
                        Direction.LEFT -> akt goesTo next == Direction.RIGHT
                        Direction.RIGHT -> akt goesTo next == Direction.LEFT
                    }

                }


                result

            }.map { Pair(it, akt) })

        }
//        this.print(visited)
        return visited.groupBy { it.first }.size
    }


    fun print(visited: MutableSet<Pair<Pair<Int,Int>, Pair<Int, Int>>>) {
        println("---------------------------------------------------------")
        (0..<cave.max.first).forEach { x ->
            (0..<cave.max.second).forEach { y ->
                if(visited.filter { it.second == Pair(x,y) }.isNotEmpty() ){
                    print("#")
                }
                else print(".")
            }
            println("")
        }
    }

}

fun main() {
    val day = Day16()
    println(day.solve1())
    println(day.solve2())
}