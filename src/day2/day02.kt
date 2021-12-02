package day2

import readInput

fun main() {
    fun part1(input: List<String>): Int = input
        .fold(Pair(0, 0)) { position, command -> performCommand(command, position) }
        .let { position -> position.first * position.second }

    fun part2(input: List<String>): Int = input
        .fold(Triple(0, 0, 0)) { position, command -> performCommandPart2(command, position) }
        .let { position -> position.first * position.second }

    val exampleInput = readInput("day2/Day02Example")
    check(part1(exampleInput) == 150)
    check(part2(exampleInput) == 900)

    val input = readInput("day2/Day02")
    println(part1(input))
    println(part2(input))
}

private fun performCommand(command: String, position: Pair<Int, Int>): Pair<Int, Int> {
    val horizontal = position.first
    val depth = position.second

    return when {
        isForward(command) -> Pair(horizontal + getMoveValue(command), depth)
        isDown(command) -> Pair(horizontal, depth + getMoveValue(command))
        isUp(command) -> Pair(horizontal, depth - getMoveValue(command))
        else -> position
    }
}

private fun performCommandPart2(command: String, position: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
    val horizontal = position.first
    val depth = position.second
    val aim = position.third

    return when {
        isForward(command) -> Triple(horizontal + getMoveValue(command), depth + (getMoveValue(command) * aim), aim)
        isDown(command) -> Triple(horizontal, depth, aim + getMoveValue(command))
        isUp(command) -> Triple(horizontal, depth, aim - getMoveValue(command))
        else -> position
    }
}

private fun isForward(command: String): Boolean = """forward (\d)""".toRegex().matches(command)
private fun isDown(command: String): Boolean = """down (\d)""".toRegex().matches(command)
private fun isUp(command: String): Boolean = """up (\d)""".toRegex().matches(command)
private fun getMoveValue(command: String): Int = command.substringAfter(" ").toInt()
