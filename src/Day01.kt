fun main() {
    fun part1(input: List<Int>): Int = input
        .zipWithNext { a, b -> b > a }
        .count { it }

    fun part2(input: List<Int>): Int = input
        .windowed(3) { it.sum() }
        .zipWithNext { a, b -> b > a }
        .count { it }

    val exampleInput = readInput("Day01Example").map { it.toInt() }
    check(part1(exampleInput) == 7)
    check(part2(exampleInput) == 5)

    val input = readInput("Day01").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
