fun main() {
    fun part1(lines: List<String>) = lines
        .map { it.parseHandAndBid() }
        .sortedBy { it.first }
        .withIndex()
        .sumOf { (ix, handAndBid) -> (ix + 1) * handAndBid.second }

    fun part2(lines: List<String>) = lines
        .map { it.parseHandAndBid(withJokers = true) }
        .sortedBy { it.first }
        .withIndex()
        .sumOf { (ix, handAndBid) -> (ix + 1) * handAndBid.second }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
}

private data class Hand(val cards: List<Int>) : Comparable<Hand> {
    private val type = computeTypeWithJokers()

    private fun computeTypeWithJokers(): HandType {
        val (jokers, otherCards) = cards.partition { it == 1 }
        if (jokers.size == 5) return HandType.FIVE_OF_A_KIND
        return jokers.fold(computeType(otherCards)) { type, _ -> type.applyJoker() }
    }

    private fun computeType(cards: List<Int>): HandType {
        val counts = cards.groupBy { it }.map { it.value.size }
        return when (counts.max()) {
            5 -> HandType.FIVE_OF_A_KIND
            4 -> HandType.FOUR_OF_A_KIND
            3 -> if (2 in counts) HandType.FULL_HOUSE else HandType.THREE_OF_A_KIND
            else -> when (counts.filter { it == 2 }.size) {
                2 -> HandType.TWO_PAIR
                1 -> HandType.ONE_PAIR
                else -> HandType.HIGH_CARD
            }
        }
    }

    private fun HandType.applyJoker() = when (this) {
        HandType.HIGH_CARD -> HandType.ONE_PAIR
        HandType.ONE_PAIR -> HandType.THREE_OF_A_KIND
        HandType.TWO_PAIR -> HandType.FULL_HOUSE
        HandType.THREE_OF_A_KIND -> HandType.FOUR_OF_A_KIND
        HandType.FULL_HOUSE -> HandType.FOUR_OF_A_KIND
        HandType.FOUR_OF_A_KIND -> HandType.FIVE_OF_A_KIND
        HandType.FIVE_OF_A_KIND -> HandType.FIVE_OF_A_KIND
    }

    override fun compareTo(other: Hand) = compareValuesBy(
        this,
        other,
        { it.type },
        { it.cards[0] },
        { it.cards[1] },
        { it.cards[2] },
        { it.cards[3] },
        { it.cards[4] },
    )
}

private fun String.parseHandAndBid(withJokers: Boolean = false): Pair<Hand, Int> {
    val cards = substringBefore(' ').map {
        when (it) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> if (withJokers) 1 else 11
            'T' -> 10
            else -> it.digitToInt()
        }
    }
    val bid = substringAfter(' ').toInt()
    return Hand(cards) to bid
}
