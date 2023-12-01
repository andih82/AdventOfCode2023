import java.io.File

enum class digits {
    zero, one, two, three, four, five, six, seven, eight, nine;
}

fun main() {
    println(firstPuzzle())
    println(secondPuzzle())
}

fun firstPuzzle() = File("src/main/resources/day1_1.txt").readLines().map { line ->
    val first = line.first { it.isDigit() }
    val sec = line.last { it.isDigit() }
    "$first$sec".toInt()
}.sum()

fun secondPuzzle() =
    File("src/main/resources/day1_1.txt").readLines().map { line ->
        val first = line.firstDigitIn(digits.entries.map { it.toString() })
        val sec = line.lastDigitIn(digits.entries.map { it.toString() })
        "$first$sec".toInt()
    }.sum()

fun String.firstDigitIn(that: List<String>): Char {
    val firstLetter = this.findAnyOf(that)
    val idxOfFirstdigit = this.indexOfFirst { it.isDigit() }
    return if (firstLetter == null) {
        this[idxOfFirstdigit]
    }else if (firstLetter.first < idxOfFirstdigit ){
        digits.valueOf(firstLetter.second).ordinal.digitToChar()
    } else {
        this[idxOfFirstdigit]
    }
}

fun String.lastDigitIn(that: List<String>): Char {
    val lastLetter = this.findLastAnyOf(that)
    val idxOfLastdigit = this.indexOfLast { it.isDigit() }
    return if (lastLetter == null) {
        this[idxOfLastdigit]
    } else if (lastLetter.first < idxOfLastdigit) {
        this[idxOfLastdigit]
    } else {
        digits.valueOf(lastLetter.second).ordinal.digitToChar()
    }

}
