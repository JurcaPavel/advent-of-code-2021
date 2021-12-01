fun main() {
    fun part1(input: List<Int>): Int {
        return input.zipWithNext { a, b -> b > a }.count { it }
    }

    fun part2(input: List<Int>): Int {
        val threeSumsHaha = mutableListOf<Int>()

        for ((index, value) in input.withIndex()) {
            if (index + 2 <= input.lastIndex) {
                threeSumsHaha.add(value + input[index + 1] + input[index + 2])
            }
        }

        return threeSumsHaha.zipWithNext { a, b -> b > a }.count { it }
    }

    val input = readInput("Day01").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
