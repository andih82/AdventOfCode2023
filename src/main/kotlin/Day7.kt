import aoc.Day
import kotlin.math.min

open class Hand(private val cards: String, private val bid: Int) : Comparable<Hand> {

    var type = mutableMapOf<Char, Int>()

    init {
        cards.forEach {
            type[it] = type.getOrDefault(it, 0) + 1
        }
    }

    override operator fun compareTo(other: Hand): Int {
        return if (this.type.size > other.type.size) -1
        else if (this.type.size < other.type.size) 1
        else if (this.type.values.max() > other.type.values.max()) 1
        else if (this.type.values.max() < other.type.values.max()) -1
        else {
            this.cards compareTo other.cards
        }
    }

    operator fun invoke(i: Int) = i * bid
}

data class Hand1(val cards: String, val bid: Int) : Hand(cards, bid) {}

data class Hand2(val cards: String, val bid: Int) : Hand(cards, bid) {
    init {
        if (type.size > 1) {
            val joker = type.remove('1') ?: 0

            for ((k, _) in type) {
                type[k] = min(type[k]!! + joker, 5)
            }
        }
    }
}


class Day7 : Day(7) {

    var hands1: List<Hand1>
    var hands2: List<Hand2>

    init {
        hands1 = puzzle.map {
            val (l, r) = it.replace("A", "Z").replace("K", "Y").replace("T", "B").split(" ")
            Hand1(l, r.toInt())
        }

        hands2 = puzzle.map {
            // part2
            val (l, r) = it.replace("J", "1").replace("A", "Z").replace("K", "Y").replace("T", "B").split(" ")
            Hand2(l, r.toInt())
        }

    }
}

fun main() {
    val day7 = Day7()
    println(day7.hands1.sorted().mapIndexed { idx, hand -> hand(idx)  }.sum())
    println(day7.hands2.sorted().mapIndexed { idx, hand -> hand(idx) }.sum())

}