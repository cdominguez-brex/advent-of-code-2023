fun main() {
    fun part1(input: List<String>) = input.sumOf { line ->
        val springString = line.substringBefore(' ')
        val groups = line.substringAfter(' ').split(',').map { it.toInt() }

        springString.numPossibleAllocations(groups)
    }

    fun part2(input: List<String>) = input.sumOf { line ->
        val springString = line.substringBefore(' ')
        val expandedSpringString = List(5) { springString }.joinToString("?")

        val groupString = line.substringAfter(' ')
        val expandedGroupString = List(5) { groupString }.joinToString(",")
        val groups = expandedGroupString.split(',').map { it.toInt() }

        expandedSpringString.numPossibleAllocations(groups)
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

private val npaCache = mutableMapOf<Pair<String, List<Int>>, Long>()

private fun String.numPossibleAllocations(groups: List<Int>): Long = npaCache.getOrPut(this to groups) {
    if (groups.isEmpty()) return@getOrPut if (all { it != '#' }) 1 else 0
    if (groups.sum() + groups.size - 1 > length) return@getOrPut 0
    val fromShiftWithoutAllocation = if (first() != '#') substring(1).numPossibleAllocations(groups) else 0
    val fromAllocation = attemptToAllocate(groups.first())?.numPossibleAllocations(groups.drop(1)) ?: 0
    return@getOrPut fromShiftWithoutAllocation + fromAllocation
}

private fun String.attemptToAllocate(num: Int): String? {
    if (length < num) return null
    if (substring(0, num).any { it == '.' }) return null
    if (length == num) return ""
    if (get(num) != '#') return substring(num + 1)
    return null
}
