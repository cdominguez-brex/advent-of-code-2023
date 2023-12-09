fun main() {
    fun part1(lines: List<String>) = parseInput(lines).sumOf { extrapolate(it).elementAt(it.size) }
    fun part2(lines: List<String>) = parseInput(lines).sumOf { extrapolate(it.reversed()).elementAt(it.size) }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

private fun extrapolate(values: List<Int>): Sequence<Int> {
    if (values.all { it == 0 }) return generateSequence { 0 }
    val diffs = extrapolate(values.zipWithNext { a, b -> b - a })
    return diffs.runningFold(values[0]) { a, b -> a + b }
}

private fun parseInput(lines: List<String>) = lines.map { it.split(' ').map { it.toInt() } }
