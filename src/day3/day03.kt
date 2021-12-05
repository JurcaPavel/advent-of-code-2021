package day3

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val gammaRate = getGammaRate(input)
        val epsilonRate = getEpsilonRate(input)

        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        val oxygenGeneratorRating = getOxygenGeneratorRating(input)
        val co2ScrubberRating = getCo2ScrubberRating(input)

        return oxygenGeneratorRating * co2ScrubberRating
    }

    val exampleInput = readInput("day3/Day03Example")

    check(part1(exampleInput) == 198)
    check(part2(exampleInput) == 230)

    val input = readInput("day3/Day03")
    println(part1(input))
    println(part2(input))
}

private fun getGammaRate(input: List<String>): Int {
    val digitIndexes = input.first().indices
    val gammaRateString = StringBuilder()
    for (digitIndex in digitIndexes) {
        val zerosCount = input.count { binary -> binary[digitIndex] == '0' }
        val onesCount = input.count { binary -> binary[digitIndex] == '1' }
        if (zerosCount > onesCount) gammaRateString.append("0") else gammaRateString.append("1")
    }

    return gammaRateString.toString().toInt(2)
}

private fun getEpsilonRate(input: List<String>): Int =
    getGammaRate(input)
        .toString(2)
        .map { d -> if (d == '0') "1" else "0" }
        .joinToString("")
        .toInt(2)

private fun getOxygenGeneratorRating(input: List<String>): Int {
    val numberOfDigits = input.first().length
    var filteredHelperList = input
    for (index in 0 until numberOfDigits) {
        if (filteredHelperList.size == 1) break
        filteredHelperList = filterByMostCommonBit(filteredHelperList, index)
    }

    return filteredHelperList.first().toInt(2)
}

private fun getCo2ScrubberRating(input: List<String>): Int {
    val numberOfDigits = input.first().length
    var filteredHelperList = input
    for (index in 0 until numberOfDigits) {
        if (filteredHelperList.size == 1) break
        filteredHelperList = filterByLeastCommonBit(filteredHelperList, index)
    }

    return filteredHelperList.first().toInt(2)
}

private fun filterByLeastCommonBit(input: List<String>, index: Int): List<String> {
    val step: Map<Char, Int> = input.map { it.get(index) }.groupingBy { it }.eachCount()
    val leastCommonIndexBit = getMinOr1(step)

    return input.filter { s -> s.get(index) == leastCommonIndexBit }
}

private fun filterByMostCommonBit(input: List<String>, index: Int): List<String> {
    val step: Map<Char, Int> = input.map { it.get(index) }.groupingBy { it }.eachCount()
    val mostCommonIndexBit = getMaxOr1(step)

    return input.filter { s -> s.get(index) == mostCommonIndexBit }
}

private fun getMaxOr1(step: Map<Char, Int>): Char =
    if (step.get('0')!! > step.get('1')!!) '0'
    else '1'

private fun getMinOr1(step: Map<Char, Int>): Char =
    if (step.get('1')!! < step.get('0')!!) '1'
    else '0'
