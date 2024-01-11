import aoc.Day
import java.util.Stack

enum class Connectors {
    NORTH, EAST, SOUTH, WEST;

    infix fun isConnecting(that: Connectors) =
        when (this) {
            NORTH -> that == SOUTH
            SOUTH -> that == NORTH
            EAST -> that == WEST
            WEST -> that == EAST
        }
}

sealed interface Pipe {
    val position: Pair<Int, Int>
    val connectors: List<Connectors>

    companion object {
        fun of(shape: String, position: Pair<Int, Int>) =
            when (shape) {
                "." -> Ground(position)
                "S" -> StartPipe(position)
                else -> ConnectedPipe(shape, position)
            }

    }

    infix fun conectsTo(that: Pipe) = when(position.first - that.position.first to position.second - that.position.second){
        0 to -1 -> connectors.contains(Connectors.EAST) && that.connectors.any { Connectors.EAST isConnecting it}
        0 to 1 -> connectors.contains(Connectors.WEST) && that.connectors.any { Connectors.WEST isConnecting it}
        -1 to 0 -> connectors.contains(Connectors.SOUTH) && that.connectors.any { Connectors.SOUTH isConnecting it}
        1 to 0 -> connectors.contains(Connectors.NORTH) && that.connectors.any { Connectors.NORTH isConnecting it}
        else -> false
    }
}

data class Ground(override val position: Pair<Int, Int>) : Pipe {
    override val connectors: List<Connectors>
        get() = emptyList()

}

data class StartPipe(override val position: Pair<Int, Int>) : Pipe {
    override val connectors: List<Connectors>
        get() = Connectors.values().toList()

}

data class ConnectedPipe(val shape: String, override val position: Pair<Int, Int>) : Pipe {
    override val connectors: List<Connectors>
        get() = when (shape) {
            "L" -> listOf(Connectors.NORTH, Connectors.EAST)
            "|" -> listOf(Connectors.NORTH, Connectors.SOUTH)
            "-" -> listOf(Connectors.WEST, Connectors.EAST)
            "J" -> listOf(Connectors.NORTH, Connectors.WEST)
            "7" -> listOf(Connectors.SOUTH, Connectors.WEST)
            "F" -> listOf(Connectors.SOUTH, Connectors.EAST)
            else -> emptyList()
        }
}

fun List<List<Pipe>>.findNext(pipe: Pipe, prev: Pipe = Ground(Pair(0, 0))): Pipe {
    var next: Pipe? =
        when {
            pipe.position.first > 0 && pipe conectsTo this[pipe.position.first - 1][pipe.position.second] && this[pipe.position.first - 1][pipe.position.second] != prev -> this[pipe.position.first - 1][pipe.position.second]
            pipe.position.first < this[pipe.position.first].size - 1 && pipe conectsTo this[pipe.position.first + 1][pipe.position.second] && this[pipe.position.first + 1][pipe.position.second] != prev -> this[pipe.position.first + 1][pipe.position.second]
            pipe.position.second > 0 && pipe conectsTo this[pipe.position.first][pipe.position.second - 1] && this[pipe.position.first][pipe.position.second - 1] != prev -> this[pipe.position.first][pipe.position.second - 1]
            pipe.position.second < this.size - 1 && pipe conectsTo this[pipe.position.first][pipe.position.second + 1] && this[pipe.position.first][pipe.position.second + 1] != prev -> this[pipe.position.first][pipe.position.second + 1]
            else -> null
        }
    return next ?: error("No Connection")
}

tailrec fun List<List<Pipe>>.findLoop(start: Pipe, path: List<Pipe> = listOf(start)): List<Pipe> {
    var prev = if (path.size > 1) path[path.size-2] else Ground(Pair(0,0))
    var next = findNext(start, prev)
    return if (next is StartPipe)
        path
    else findLoop(next,  path + next)
}

class Day10 : Day(10) {

    val start: StartPipe
    val grid: List<List<Pipe>> = puzzle.mapIndexed { lIdx, line ->
        line.mapIndexed { cIdx, c -> Pipe.of(c.toString(), Pair(lIdx, cIdx)) }
    }
    val animal:List<Pipe>
    val expanded: List<MutableList<Boolean>>
    init {
        start = grid.first { l -> l.any { it is StartPipe } }.first { it is StartPipe } as StartPipe

        animal = grid.findLoop(start)
        expanded = MutableList(grid.size * 3 ) {
            MutableList(grid.first().size * 3 ){
                false
            }
        }
        animal.forEach { pipe ->
            val connectedPipe = if(pipe is StartPipe){
                Pipe.of("-", pipe.position) as ConnectedPipe

//                Pipe.of("F", pipe.position) as ConnectedPipe
            }else {
                pipe as ConnectedPipe
            }
            val center = Pair(pipe.position.first * 3 +1, pipe.position.second * 3 + 1)
            expanded[center.first][center.second] = true

            when(connectedPipe.shape) {
                "L" -> {
                    expanded[center.first - 1][center.second] = true
                    expanded[center.first ][center.second + 1] = true
                }
                "|" -> {
                    expanded[center.first - 1][center.second] = true
                    expanded[center.first + 1][center.second] = true
                }
                "-" -> {
                    expanded[center.first][center.second - 1] = true
                    expanded[center.first][center.second+ 1] = true
                }
                "7" -> {
                    expanded[center.first + 1][center.second] = true
                    expanded[center.first][center.second - 1] = true
                }
                "J" -> {
                    expanded[center.first - 1][center.second] = true
                    expanded[center.first ][center.second - 1] = true
                }
                "F" -> {
                    expanded[center.first + 1][center.second] = true
                    expanded[center.first ][center.second + 1] = true
                }

            }
        }
    }

    fun solve2(){

        val fringe = Stack<Pair<Int,Int>>()
        fringe.push(Pair(0,0))

        while (fringe.isNotEmpty()){
            val act = fringe.pop()
            if ( act.first < 0 || act.second < 0 || act.first > expanded.size -1  || act.second > expanded[0].size -1){
                continue
            }
            if(expanded[act.first][act.second]){
                continue
            }
            expanded[act.first][act.second] = true
            fringe.push(Pair(act.first - 1, act.second))
            fringe.push(Pair(act.first + 1, act.second))
            fringe.push(Pair(act.first , act.second -1))
            fringe.push(Pair(act.first , act.second +1))
        }

        var n = 0
        for (x in 1..expanded.size  step 3){
            for (y in 1..expanded.size step 3){
                println("${x to y} ${expanded[x][y]}")
                if(!expanded[x][y]) n++;
            }
        }
//        println(n)
    }
}


fun main() {
    val day = Day10()
//    println(day.animal)
    day.grid.findLoop(day.start)
//    println(day.path)
    println(day.animal.size/2)
    day.expanded.forEach { line ->
        println("${line.map { if(it) "#" else "." }.joinToString("")}")
    }
    day.solve2()
    day.expanded.forEach { line ->
        println("${line.map { if(it) "#" else "." }.joinToString("")}")
    }
//    println( day.expanded.windowed(3,3).count { !it[1][1] })

}