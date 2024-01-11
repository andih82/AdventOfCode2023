import aoc.Day

class Day17 : Day(17) {

    val city = Grid(puzzle) {
        it.map { c -> c.toString().toInt()  }.toList()
    }
    fun  solve1(){
        findPath()
    }
    fun solve2() {
        findPath2()
    }
    fun findPath(start: Pair<Int,Int> = Pair(0,0), prev : Pair<Int,Int> = Pair(-1,0)): Triple<Pair<Int, Int>, Pair<Int, Int>, Int>? {
        val fringe = mutableMapOf(0 to mutableListOf( Triple(start, prev, 0)))
        val visited = mutableSetOf<  Triple<Pair<Int, Int>, Pair<Int, Int>, Int>>()

        while (fringe.filter { (_,v) -> v.isNotEmpty() }.isNotEmpty()){
            val best = fringe .filter { (_,v) ->
                v.isNotEmpty()
            }.keys.min()
            val akt = fringe[best]!!.removeFirst()
            if(fringe[best]!!.isEmpty()){
                fringe -= best
            }

            if (akt in visited)
                continue

            if(akt.first == Pair(city.max.first-1, city.max.second-1) ){
                println(best)
                return akt
            }
            visited.add(akt)

            val aktPos = akt.first
            val prevPos = akt.second

            val nextSteps = city.nextPos(aktPos) { step ->
                step.inBounds(city.max) &&
                step != prevPos
                        && !(aktPos comeFrom prevPos == step comeFrom aktPos && akt.third >= 3)
            }
            nextSteps.forEach {
                val heat = city[it] + best
                fringe.computeIfAbsent(heat) { mutableListOf() }
                    .add(Triple(it, aktPos, if(aktPos comeFrom prevPos == it comeFrom aktPos ) akt.third + 1 else 1))
            }

        }
         return null
    }

    fun findPath2(start: Pair<Int,Int> = Pair(0,0), prev : Pair<Int,Int> = Pair(0,-1)): Triple<Pair<Int, Int>, Pair<Int, Int>, Int>? {
        val fringe = mutableMapOf(0 to mutableListOf( Triple(start, prev, 0)))
        val visited = mutableSetOf<  Triple<Pair<Int, Int>, Pair<Int, Int>, Int>>()

        while (fringe.filter { (_,v) -> v.isNotEmpty() }.isNotEmpty()){
            val best = fringe .filter { (_,v) ->
                v.isNotEmpty()
            }.keys.min()
            val akt = fringe[best]!!.removeFirst()
            if(fringe[best]!!.isEmpty()){
                fringe -= best
            }

            if (akt in visited)
                continue

            if(akt.first == Pair(city.max.first-1, city.max.second-1) && akt.third >3 ){
                println(best)
                return akt
            }
            visited.add(akt)

            val aktPos = akt.first
            val prevPos = akt.second

            val nextSteps = city.nextPos(aktPos) { step ->
                step.inBounds(city.max) &&
                        step != prevPos
                        && when(akt.third){
                            in 0..3 -> aktPos comeFrom prevPos == step comeFrom aktPos
                            in 4..10 -> true
                            in 10..10 -> aktPos comeFrom prevPos != step comeFrom aktPos
                            else -> false
                        }
            }
            nextSteps.forEach {
                val heat = city[it] + best
                fringe.computeIfAbsent(heat) { mutableListOf() }
                    .add(Triple(it, aktPos, if(aktPos comeFrom prevPos == it comeFrom aktPos ) akt.third + 1 else 1))
            }

        }
        return null
    }
}

fun main(){
    val day = Day17()
    day.solve1()
    day.solve2()
}