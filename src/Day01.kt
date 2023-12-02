fun main() {
    fun part1(input: List<String>) = input.sumOf {
        val first = digitsPt1[it.findAnyOf(digitsPt1.keys)!!.second]!!
        val last = digitsPt1[it.findLastAnyOf(digitsPt1.keys)!!.second]!!
        10 * first + last
    }

    fun part2(input: List<String>) = input.sumOf {
        val first = digitsPt2[it.findAnyOf(digitsPt2.keys)!!.second]!!
        val last = digitsPt2[it.findLastAnyOf(digitsPt2.keys)!!.second]!!
        10 * first + last
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

val digitsPt1 = mapOf(
    "1" to 1,
    "2" to 2,
    "3" to 3,
    "4" to 4,
    "5" to 5,
    "6" to 6,
    "7" to 7,
    "8" to 8,
    "9" to 9,
)

val digitsPt2 = digitsPt1 + mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)
