import kotlin.math.abs


class Grid<T>(src: List<String>, initFn: (line: String) -> List<T>) {

    val grid: List<List<T>>

    val max: Pair<Int, Int>


    init {
        grid = src.map { line ->
            initFn(line)
        }
        max = Pair(grid.size, grid[0].size)
    }

    operator fun get(pos: Pair<Int, Int>) = grid[pos.first][pos.second]
    operator fun get(x: Int, y :Int) = grid[x][y]

    fun nextPos(akt: Pair<Int, Int> = Pair(0, 0)) =
        listOf(
            Pair(akt.first - 1, akt.second),
            Pair(akt.first, akt.second + 1),
            Pair(akt.first + 1, akt.second),
            Pair(akt.first, akt.second - 1)
        )

    fun nextPosInBounds(akt: Pair<Int, Int> = Pair(0, 0), max: Pair<Int, Int> = Pair(grid.size, grid[0].size)) =
        nextPos(akt).filter { it.inBounds(max) }

    fun nextPos(akt: Pair<Int, Int> = Pair(0, 0), restriction: (Pair<Int, Int>) -> Boolean) =
        nextPos(akt).filter { restriction(it) }

    fun nextPos(akt: Pair<Int, Int> = Pair(0, 0), blocking: List<Pair<Int, Int>>) =
        nextPos(akt).filter { it !in blocking }

    override fun toString(): String {
        val sb = StringBuffer()
        for (i in 0..< max.first){
            for (j in 0 ..< max.second){
                sb.append(this[i,j])
            }
            sb.appendLine()
        }
        return sb.toString()
    }

}

enum class Direction {
    UP, DOWN, LEFT, RIGHT;
}

fun Pair<Int, Int>.inBounds(max: Pair<Int, Int>): Boolean =
    this.first > -1 && this.second > -1 && this.first < max.first && this.second < max.second

fun Pair<Int, Int>.euclidenDistance(that : Pair<Int, Int>) : Int = abs(first -that.first) + abs(second -that.second)

infix fun Pair<Int, Int>.comeFrom(prev: Pair<Int, Int>): Direction = when {
    prev.first < first -> Direction.DOWN
    prev.first > first -> Direction.UP
    prev.second < second -> Direction.RIGHT
    prev.second > second -> Direction.LEFT
    else -> error("Cant obtain direction.")
}

infix fun Pair<Int, Int>.goesTo(next: Pair<Int, Int>): Direction = when {
    next.first < first  -> Direction.UP
    next.first > first -> Direction.DOWN
    next.second < second -> Direction.LEFT
    next.second > second -> Direction.RIGHT
    else -> error("Cant obtain direction.")
}
