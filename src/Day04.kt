fun main() {
    fun part1(input: List<String>) = input.sumOf {
        it.parseScratchcard().pointsWon()
    }

    fun part2(input: List<String>): Int {
        val scratchcards = input.associate {
            val scratchcard = it.parseScratchcard()
            scratchcard.id to scratchcard
        }

        for (scratchcard in scratchcards.values) {
            for (i in 1..scratchcard.numbersInCommon().size) {
                scratchcards[scratchcard.id + i]!!.numPossessed += scratchcard.numPossessed
            }
        }

        return scratchcards.values.sumOf { it.numPossessed }
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
    val id: Int,
    val winningNumbers: Set<Int>,
    val yourNumbers: Set<Int>,
    var numPossessed: Int = 1,
) {
    fun pointsWon() = numbersInCommon().size.let { if (it == 0) 0 else 1 shl (it - 1) }
    fun numbersInCommon() = winningNumbers intersect yourNumbers
}

private fun String.parseScratchcard(): Scratchcard {
    val (card, numbers) = split(": ")
    val (winners, yours) = numbers.split(" | ")
    return Scratchcard(
        id = card.split(" ").last().toInt(),
        winningNumbers = winners.split(" ").mapNotNull { it.takeIf { it.isNotBlank() }?.toInt() }.toSet(),
        yourNumbers = yours.split(" ").mapNotNull { it.takeIf { it.isNotBlank() }?.toInt() }.toSet(),
    )
}
