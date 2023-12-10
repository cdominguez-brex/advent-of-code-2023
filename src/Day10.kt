import kotlin.math.ceil

fun main() {
    fun part1(lines: List<String>): Int {
        val pipeMap = parseInput(lines)
        val animalPipe = pipeMap.values.find { it.hasAnimal }!!
        return animalPipe
            .flowInFirstDirection(pipeMap)
            .takeWhile { it != animalPipe }
            .count()
            .let { ceil(it / 2.0).toInt() }
    }

    fun part2(lines: List<String>): Int {
        val pipeMap = parseInput(lines)
        val animalPipe = pipeMap.values.find { it.hasAnimal }!!
        animalPipe.isPartOfLoop = true
        animalPipe.flowInFirstDirection(pipeMap)
            .takeWhile { it != animalPipe }
            .forEach { it.isPartOfLoop = true }
        var amInsideLoopNorth = false
        var amInsideLoopSouth = false
        var area = 0
        for ((lineIx, line) in lines.withIndex()) {
            for ((charIx, _) in line.withIndex()) {
                val pipe = pipeMap[Point(lineIx, charIx)]
                if (pipe?.isPartOfLoop != true) {
                    if (amInsideLoopNorth && amInsideLoopSouth) area++
                } else {
                    val pipeDirections = pipe.directions.toList()
                    if (Direction.NORTH in pipeDirections) amInsideLoopNorth = !amInsideLoopNorth
                    if (Direction.SOUTH in pipeDirections) amInsideLoopSouth = !amInsideLoopSouth
                }
            }
        }
        return area
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

private data class Point(val line: Int, val char: Int) {
    operator fun plus(other: Point) = Point(line + other.line, char + other.char)
}

private enum class Direction(val normal: Point) {
    NORTH(Point(-1, 0)),
    SOUTH(Point(1, 0)),
    EAST(Point(0, 1)),
    WEST(Point(0, -1)),
    ;

    val opposite: Direction
        get() = when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
}

private fun Char.pipeDirections() = when (this) {
    '|' -> Direction.NORTH to Direction.SOUTH
    'L' -> Direction.NORTH to Direction.EAST
    'J' -> Direction.NORTH to Direction.WEST
    'F' -> Direction.EAST to Direction.SOUTH
    '-' -> Direction.EAST to Direction.WEST
    '7' -> Direction.SOUTH to Direction.WEST
    else -> throw IllegalArgumentException()
}

private data class Pipe(
    val location: Point,
    val directions: Pair<Direction, Direction>,
    val hasAnimal: Boolean,
    var isPartOfLoop: Boolean = false,
) {
    private fun otherDirection(dir: Direction): Direction {
        if (dir == directions.first) return directions.second
        if (dir == directions.second) return directions.first
        throw IllegalArgumentException()
    }

    fun flowInFirstDirection(pipeMap: Map<Point, Pipe>): Sequence<Pipe> = sequence {
        var directionFromPrevPipe = directions.second.opposite
        var thisPipe = this@Pipe
        while (true) {
            directionFromPrevPipe = thisPipe.otherDirection(directionFromPrevPipe.opposite)
            thisPipe = pipeMap[thisPipe.location + directionFromPrevPipe.normal]!!
            yield(thisPipe)
        }
    }
}

private fun parseInput(lines: List<String>) = buildMap {
    for ((lineIx, line) in lines.withIndex()) {
        for ((charIx, char) in line.withIndex()) {
            val point = Point(lineIx, charIx)
            val directions = when (char) {
                '.' -> continue
                'S' -> '|' // note: input-specific
                else -> char
            }.pipeDirections()
            put(point, Pipe(point, directions, char == 'S'))
        }
    }
}
