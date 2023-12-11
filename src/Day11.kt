import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part2(lines: List<String>, expandEmptiesBy: Long) = parseImage(lines).run {
        galaxyPairs().sumOf { distanceBetween(it.first, it.second, expandEmptiesBy) }
    }

    val testInput = readInput("Day11_test")
    check(part2(testInput, expandEmptiesBy = 2) == 374L)
    check(part2(testInput, expandEmptiesBy = 10) == 1030L)
    check(part2(testInput, expandEmptiesBy = 100) == 8410L)

    val input = readInput("Day11")
    part2(input, expandEmptiesBy = 2).println()
    part2(input, expandEmptiesBy = 1000000).println()
}

// didn't realize I couldn't use the same private class name twice, even in different scopes :-/
private data class Point2(val line: Int, val column: Int)

private data class Image(
    val linesWithoutGalaxies: Set<Int>,
    val columnsWithoutGalaxies: Set<Int>,
    val galaxies: List<Point2>,
) {
    fun galaxyPairs() = sequence {
        for (i in galaxies.indices) {
            for (j in (i + 1) until galaxies.size) {
                yield(galaxies[i] to galaxies[j])
            }
        }
    }

    fun distanceBetween(x: Point2, y: Point2, expandEmptiesBy: Long): Long {
        val emptyLines = linesWithoutGalaxies.count { it.isBetween(x.line, y.line) }
        val emptyColumns = columnsWithoutGalaxies.count { it.isBetween(x.column, y.column) }
        return abs(x.line - y.line) + abs(x.column - y.column) + (expandEmptiesBy - 1) * (emptyLines + emptyColumns)
    }
}

private fun Int.isBetween(x: Int, y: Int) = this > min(x, y) && this < max(x, y)

private fun parseImage(lines: List<String>): Image {
    val linesWithoutGalaxies = lines.indices.toMutableSet()
    val columnsWithoutGalaxies = lines[0].indices.toMutableSet()
    val galaxies = mutableListOf<Point2>()
    for ((lineIx, line) in lines.withIndex()) {
        for ((columnIx, char) in line.withIndex()) {
            if (char != '#') continue
            linesWithoutGalaxies.remove(lineIx)
            columnsWithoutGalaxies.remove(columnIx)
            galaxies.add(Point2(lineIx, columnIx))
        }
    }
    return Image(linesWithoutGalaxies, columnsWithoutGalaxies, galaxies)
}
