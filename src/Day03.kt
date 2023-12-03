fun main() {
    fun part1(input: List<String>): Int {
        val positionsWithNumbers = positionsWithNumbers(input)

        return symbolPositions(input)
            .flatMap { it.adjacencies() }
            .mapNotNull { positionsWithNumbers[it] }
            .distinct()
            .sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        val positionsWithNumbers = positionsWithNumbers(input)

        return symbolPositions(input).sumOf {
            val adjacentNumbers = it.adjacencies().mapNotNull { positionsWithNumbers[it] }.distinct()
            if (adjacentNumbers.size == 2) adjacentNumbers[0].number * adjacentNumbers[1].number else 0
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

private data class FilePosition(val lineNumber: Int, val linePosition: Int) {
    fun adjacencies() = listOf(
        FilePosition(lineNumber - 1, linePosition - 1),
        FilePosition(lineNumber - 1, linePosition),
        FilePosition(lineNumber - 1, linePosition + 1),
        FilePosition(lineNumber, linePosition - 1),
        FilePosition(lineNumber, linePosition + 1),
        FilePosition(lineNumber + 1, linePosition - 1),
        FilePosition(lineNumber + 1, linePosition),
        FilePosition(lineNumber + 1, linePosition + 1),
    )
}

private data class NumberPosition(val number: Int, val startPosition: FilePosition)

private fun positionsWithNumbers(inputLines: List<String>): Map<FilePosition, NumberPosition> {
    val numberRegex = Regex("""\d+""")
    val numberPositions = mutableMapOf<FilePosition, NumberPosition>()
    inputLines.forEachIndexed { lineNumber, line ->
        numberRegex.findAll(line).forEach {
            val startPosition = FilePosition(lineNumber, it.range.start)
            val numberPosition = NumberPosition(it.value.toInt(), startPosition)
            it.range.forEach {
                numberPositions += FilePosition(lineNumber, it) to numberPosition
            }
        }
    }
    return numberPositions
}

private fun symbolPositions(inputLines: List<String>): List<FilePosition> {
    val symbolRegex = Regex("""[^\d.]""")
    return inputLines.flatMapIndexed { lineNumber, line ->
        symbolRegex.findAll(line).map { FilePosition(lineNumber, it.range.start) }
    }
}
