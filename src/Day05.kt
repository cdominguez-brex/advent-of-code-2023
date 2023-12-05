import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(lines: List<String>): Long {
        val input = Input.parse(lines)
        val finalLocations = input.rangeMaps.fold(input.seedNumbers) { sources, rangeMap ->
            sources.map { rangeMap.getDestination(it) }
        }
        return finalLocations.min()
    }

    fun part2(lines: List<String>): Long {
        val input = Input.parse(lines)
        val seedRanges = input.seedNumbers
            .asSequence()
            .windowed(2, 2)
            .map { (start, length) -> Range(start, length) }
            .toList()
        val finalLocationRanges = input.rangeMaps.fold(seedRanges) { sourceRanges, rangeMap ->
            rangeMap.getDestinationRanges(sourceRanges)
        }
        return finalLocationRanges.minOf { it.start }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private data class RangeMapElement(
    val destinationStart: Long,
    val sourceStart: Long,
    val rangeLength: Long,
) {
    fun isInSourceRange(number: Long) = number >= sourceStart && number < sourceStart + rangeLength
    fun getDestination(source: Long) = if (isInSourceRange(source)) source + (destinationStart - sourceStart) else null

    fun sourceRange() = Range(sourceStart, rangeLength)

    // First pair value is the mapped part of the original range (if any), second is a list of unmatched ranges (at most
    // two: one section of the range falling completely below the mapper range, and the other falling completely above)
    fun getDestinationRange(inputSourceRange: Range): Pair<Range?, List<Range>> {
        val mapperRange = this.sourceRange()

        if (inputSourceRange.end < mapperRange.start || inputSourceRange.start > mapperRange.end) {
            return null to listOf(inputSourceRange)
        }

        val matchedRange = Range.fromBoundsInclusive(
            getDestination(max(inputSourceRange.start, mapperRange.start))!!,
            getDestination(min(inputSourceRange.end, mapperRange.end))!!,
        )

        val lowerUnmatched = if (inputSourceRange.start < mapperRange.start) {
            Range.fromBoundsInclusive(inputSourceRange.start, mapperRange.start - 1)
        } else {
            null
        }
        val upperUnmatched = if (mapperRange.end < inputSourceRange.end) {
            Range.fromBoundsInclusive(mapperRange.end + 1, inputSourceRange.end)
        } else {
            null
        }

        return matchedRange to listOfNotNull(lowerUnmatched, upperUnmatched)
    }
}

private data class Range(val start: Long, val length: Long) {
    val end: Long
        get() = start + length - 1

    companion object {
        fun fromBoundsInclusive(lower: Long, upper: Long) = Range(lower, upper - lower + 1)
    }
}

private data class RangeMap(
    val elements: List<RangeMapElement>,
) {
    fun getDestination(source: Long) = elements.firstNotNullOfOrNull { it.getDestination(source) } ?: source
    fun getDestinationRanges(sourceRanges: List<Range>): List<Range> {
        val matched = mutableListOf<Range>()
        var unmatched = sourceRanges

        elements.forEach { elt ->
            val (newMatched, newUnmatched) = unmatched.map { elt.getDestinationRange(it) }.unzip()
            matched += newMatched.filterNotNull()
            unmatched = newUnmatched.flatten()
        }
        return matched + unmatched
    }
}

private data class Input(
    // The interpretation of this field depends on the part
    val seedNumbers: List<Long>,
    val rangeMaps: List<RangeMap>,
) {
    companion object {
        fun parse(lines: List<String>): Input {
            val iterator = lines.iterator()
            val seeds = iterator.next().split(": ")[1].split(" ").map { it.toLong() }
            iterator.next() // empty line

            val maps = mutableListOf<RangeMap>()
            while (iterator.hasNext()) {
                iterator.next() // map name
                val elements = mutableListOf<RangeMapElement>()
                while (iterator.hasNext()) {
                    val line = iterator.next()
                    if (line.isBlank()) break
                    val (destinationStart, sourceStart, rangeLength) = line.split(" ").map { it.toLong() }
                    elements += RangeMapElement(destinationStart, sourceStart, rangeLength)
                }
                maps += RangeMap(elements)
            }
            return Input(seeds, maps)
        }
    }
}
