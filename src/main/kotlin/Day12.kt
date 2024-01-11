import aoc.Day

class Day12 : Day(12) {


    fun solve1(): Long {

        println(example1.map {
            Pair(
                "${it.takeWhile { c -> c != ' ' }}",
                it.dropWhile { c -> !c.isDigit() }.split(",").map { c -> c.toInt() }
            )
        }.map { it.first.check(it.second, false) })

        val list = puzzle.map {
            Pair(
                "${it.takeWhile { c -> c != ' ' }}.",
                it.dropWhile { c -> !c.isDigit() }.split(",").map { c -> c.toInt() }
            )
        }
        println(list.map { it.first.check(it.second, false) })

        return list.sumOf { it.first.check(it.second, false) }
    }


    // Does not work
    fun solve2(): Long {


        println(example1.map {
            Pair(
                "${it.takeWhile { c -> c != ' ' }}",
                it.dropWhile { c -> !c.isDigit() }.split(",").map { c -> c.toInt() }
            )
        }.map { orig ->
            var unfold = ""
            val unfold2 = mutableListOf<Int>()
            repeat(5) { unfold += "${orig.first}?" }
            repeat(5) { unfold2.addAll(orig.second) }
            Pair(unfold.dropLast(1) + ".", unfold2)
        }.map { it.first.check(it.second, false) }.sumOf { it })

        val list = puzzle.map {
            Pair(
                "${it.takeWhile { c -> c != ' ' }}",
                it.dropWhile { c -> !c.isDigit() }.split(",").map { c -> c.toInt() }
            )
        }.map { orig ->
            var unfold = ""
            val unfold2 = mutableListOf<Int>()
            repeat(times = 5) {
                unfold += "${orig.first}?"
                unfold2.addAll(orig.second)
            }
//            23907367315543
//              24124374405631
//            4614991196971
            Pair(unfold.dropLast(1) + ".", unfold2.toList())
        }


        println(list.map { it.first.check(it.second, false) })
        return list.sumOf { it.first.check(it.second, false) }
    }

    private val cache = mutableMapOf<Pair<String, List<Int>>, Long>()
    fun String.check(n: List<Int>, open: Boolean): Long =
        cache.getOrPut(this to n) {
            when {
//                n.any { it < 0 } -> 0
                isEmpty() -> if (n.isEmpty() || n.singleOrNull() == 0) 1 else 0
                else -> {
                    when (this[0]) {
                        '#' ->
//                            when {
//                                n.isEmpty() -> 0
//                                n[0] < 1 -> 0
//                                else -> {
                                    drop(1).check(listOf(n[0] - 1) + n.drop(1), true)
//                                }
//                            }

                        '.' ->
                            if (!open) {
                                drop(1).check(n, false)
                            } else when (n[0]) {
                                0 -> if (n.size == 1) 1 else drop(1).check(n.drop(1), false)
                                else -> 0
                            }

                        else -> ("#${drop(1)}".check(n, open) + ".${drop(1)}".check(n, open))

                    }
                }
            }
        }

}


//                ("#${drop(1)}".check(n, open) + ".${drop(1)}".check(n, open))


fun main() {
    // Does not work, only solved part 1 without cache
    val day = Day12();
    println(day.solve1())
    println(day.solve2())
}