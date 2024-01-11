import aoc.Day

class Day9 : Day(9) {

    private val history: List<List<Int>> = puzzle.map { it.split(" ").map { s -> s.toInt() } }

    fun solve1() :Int {
        var aktList = mutableListOf<MutableList<Int>>()
        return  history.map { line ->
            aktList.add(line.toMutableList())
            //reduce
            while (aktList.last().any { it != 0} ){
                aktList.add(aktList.last().reduceToDiffs())
            }

            //extrapolate
            val last= aktList.reversed().mapIndexed { idx, l ->
                if (idx > 0){
                    aktList.reversed()[idx].add(l.last() + aktList.reversed()[idx-1].last())
                }
              l.last()
            }
            aktList.clear()
            last
        }.sumOf { it.last() }
    }

    fun solve2() :Int {
        var aktList = mutableListOf<MutableList<Int>>()
        return  history.map { line ->
            aktList.add(line.toMutableList())
            //reduce
            while (aktList.last().any { it != 0} ){
                aktList.add(aktList.last().reduceToDiffs())
            }

            //extrapolate
            val last= aktList.reversed().mapIndexed { idx, l ->
                if (idx > 0){
                    aktList.reversed()[idx].add(0, l.first() - aktList.reversed()[idx-1].first())
                }
                l.first()
            }

            aktList.clear()
            last
        }.sumOf { it.last()}
    }

    fun List<Int>.reduceToDiffs() =
        this.foldIndexed(mutableListOf<Int>())
        { idx, list, b -> if (idx > 0) list.add(b - (this[idx - 1])); list }



}


fun main() {
    val day = Day9()
    println(day.solve1())
    println(day.solve2())
}