fun main() {
    fun part1(input: List<String>) = parseInput(input).sumOf { (rows, cols) ->
        rows.findSymmetry()?.let { return@sumOf 100 * it }
        cols.findSymmetry()?.let { return@sumOf it }
        throw IllegalArgumentException()
    }

    fun part2(input: List<String>) = parseInput(input).sumOf { (rows, cols) ->
        rows.findSymmetry(::almostEquals)?.let { return@sumOf 100 * it }
        cols.findSymmetry(::almostEquals)?.let { return@sumOf it }
        throw IllegalArgumentException()
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.findSymmetry(equals: (List<String>, List<String>) -> Boolean = { l1, l2 -> l1 == l2 }): Int? {
    for (i in 1 until size) {
        if (i <= size / 2) {
            if (equals(subList(0, i), subList(i, 2 * i).reversed())) return i
        } else {
            if (equals(subList(i, size), subList(2 * i - size, i).reversed())) return i
        }
    }
    return null
}

private fun almostEquals(l1: List<String>, l2: List<String>): Boolean {
    val s1 = l1.joinToString("")
    val s2 = l2.joinToString("")
    return s1.zip(s2).count { it.first != it.second } == 1
}

private fun parseInput(input: List<String>): Sequence<Pair<List<String>, List<String>>> = sequence {
    var nextRows = mutableListOf<String>()
    input.forEach {
        if (it.isBlank()) {
            yield(nextRows)
            nextRows = mutableListOf()
        } else {
            nextRows.add(it)
        }
    }
    if (nextRows.isNotEmpty()) yield(nextRows)
}.map { rows ->
    val cols = List(rows[0].length) { colNumber -> rows.map { it[colNumber] }.joinToString("") }
    rows to cols
}
