fun main() {
    fun part1(input: List<String>) = input.sumOf {
        val game = it.parseGame()
        if (game.isPossible(12, 13, 14)) game.id else 0
    }

    fun part2(input: List<String>) = input.sumOf {
        it.parseGame().minDiceRequired().power()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private data class Game(
    val id: Int,
    val draws: List<RGB>,
) {
    fun isPossible(red: Int, green: Int, blue: Int) = draws.all {
        it.red <= red && it.green <= green && it.blue <= blue
    }

    fun minDiceRequired() = RGB(
        red = draws.maxOf { it.red },
        green = draws.maxOf { it.green },
        blue = draws.maxOf { it.blue },
    )
}

private data class RGB(
    var red: Int = 0,
    var green: Int = 0,
    var blue: Int = 0,
) {
    fun power() = red * green * blue
}

private fun String.parseGame(): Game {
    val (game, draws) = split(": ")
    return Game(
        id = game.split(" ")[1].toInt(),
        draws = draws.split("; ").map {
            RGB().apply {
                it.split(", ").forEach {
                    val (num, color) = it.split(" ")
                    when (color) {
                        "red" -> red = num.toInt()
                        "green" -> green = num.toInt()
                        "blue" -> blue = num.toInt()
                        else -> throw IllegalArgumentException("got illegal color $color")
                    }
                }
            }
        },
    )
}
