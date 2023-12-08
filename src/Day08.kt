import java.math.BigInteger

fun main() {
    fun part1(lines: List<String>): Int {
        val (steps, nodes) = parseInput(lines)

        var numSteps = 0
        var thisNode = "AAA"
        for (step in steps.repeatForever()) {
            val neighbors = nodes[thisNode]!!
            thisNode = if (step == 'L') neighbors.first else neighbors.second
            numSteps += 1

            if (thisNode == "ZZZ") break
        }
        return numSteps
    }

    fun part2(lines: List<String>): BigInteger {
        val (steps, nodes) = parseInput(lines)

        var totalSteps = BigInteger.ONE
        nodes.keys.filter { it.endsWith('A') }.forEach { initialNode ->
            var stepsUntilZ = 0
            var thisNode = initialNode
            for (step in steps.repeatForever()) {
                val neighbors = nodes[thisNode]!!
                thisNode = if (step == 'L') neighbors.first else neighbors.second
                stepsUntilZ += 1

                if (thisNode.endsWith('Z')) break
            }

            // Note: it's not obvious that this should work! One could imagine that the path from the
            // initialNode e.g. hits multiple Zs in a cycle, and that the length of the cycle is different from
            // the number of steps from the initialNode to the first Z. But... the input is cleverly crafted so
            // that this doesn't happen. (I checked, using some code that isn't here anymore.)
            val stepsBigInt = stepsUntilZ.toBigInteger()
            totalSteps *= stepsBigInt / (totalSteps.gcd(stepsBigInt))
        }
        return totalSteps
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(lines: List<String>): Pair<String, Map<String, Pair<String, String>>> {
    val steps = lines[0]

    val nodeRegex = """(...) = \((...), (...)\)""".toRegex()
    val nodes = lines.drop(2).associate {
        val (key, left, right) = nodeRegex.matchEntire(it)!!.destructured
        key to Pair(left, right)
    }
    return steps to nodes
}

private fun String.repeatForever() = sequence { while (true) { yieldAll(iterator()) } }
