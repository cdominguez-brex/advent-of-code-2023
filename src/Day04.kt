fun main() {
    fun part1(input: List<String>) = input.sumOf {
        it.parseScratchcard().pointsWon()
    }

    fun part2(input: List<String>): Int {
        val scratchcards = input.map { it.parseScratchcard() }.toTypedArray()

        for ((ix, scratchcard) in scratchcards.withIndex()) {
            for (i in 1..scratchcard.numbersInCommon().size) {
                scratchcards[ix + i].numPossessed += scratchcard.numPossessed
            }
        }

        return scratchcards.sumOf { it.numPossessed }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

private data class Scratchcard(
    val winningNumbers: Set<Int>,
    val yourNumbers: Set<Int>,
    var numPossessed: Int = 1,
) {
    fun pointsWon() = numbersInCommon().size.let { if (it == 0) 0 else 1 shl (it - 1) }
    fun numbersInCommon() = winningNumbers intersect yourNumbers
}

private fun String.parseScratchcard(): Scratchcard {
    val (_, numbers) = split(": ")
    val (winners, yours) = numbers.split(" | ")
    return Scratchcard(
        winningNumbers = winners.split(" ").mapNotNull { it.takeIf { it.isNotBlank() }?.toInt() }.toSet(),
        yourNumbers = yours.split(" ").mapNotNull { it.takeIf { it.isNotBlank() }?.toInt() }.toSet(),
    )
}
