package day5

import readInput
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val pipes = input.map { getPipeFromString(it) }
        val maxX = pipes.maxOf { max(it.firstPoint.x, it.lastPoint.x) }
        val maxY = pipes.maxOf { max(it.firstPoint.y, it.lastPoint.y) }

        val diagram = getInitialDiagram(maxX, maxY)
        for (pipe in pipes) {
             val pipePoints: List<Point> = when {
                 isHorizontalPipe(pipe) -> getAllHorizontalPipePoints(pipe)
                 isVerticalPipe(pipe) -> getAllVerticalPipePoints(pipe)
                 else -> continue
            }
            pipePoints.forEach { diagram[it.y][it.x]++ }
        }

        return countAtLeastTwoPointsOverlap(diagram)
    }

    fun part2(input: List<String>): Int {
        val pipes = input.map { getPipeFromString(it) }
        val maxX = pipes.maxOf { max(it.firstPoint.x, it.lastPoint.x) }
        val maxY = pipes.maxOf { max(it.firstPoint.y, it.lastPoint.y) }

        val diagram = getInitialDiagram(maxX, maxY)
        for (pipe in pipes) {
            val pipePoints: List<Point> = getAllPipePoints(pipe)
            pipePoints.forEach { diagram[it.y][it.x]++ }
        }

        return countAtLeastTwoPointsOverlap(diagram)
    }

    val exampleInput = readInput("day5/Day05Example")

    check(part1(exampleInput) == 5)
    check(part2(exampleInput) == 12)

    val input = readInput("day5/Day05")
    println(part1(input))
    println(part2(input))
}

data class Pipe(val firstPoint: Point, val lastPoint: Point)
data class Point(val x: Int, val y: Int)
private val pipeMapRegex: Regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
private fun getPipeFromString(line: String): Pipe {
    val (x1, y1, x2, y2) = pipeMapRegex.find(line)!!.destructured
    val firstPoint = Point(x1.toInt(), y1.toInt())
    val lastPoint = Point(x2.toInt(), y2.toInt())
    return Pipe(firstPoint, lastPoint)
}
private fun isHorizontalPipe(pipe: Pipe): Boolean = pipe.firstPoint.y == pipe.lastPoint.y
private fun isVerticalPipe(pipe: Pipe): Boolean = pipe.firstPoint.x == pipe.lastPoint.x
private fun isDiagonalPipe(pipe: Pipe): Boolean {
    if (pipe.firstPoint.x < pipe.lastPoint.x) {
        val xDiff = pipe.lastPoint.x - pipe.firstPoint.x
        return isXYRatioCorrect(pipe, xDiff)
    } else {
        val xDiff = pipe.firstPoint.x - pipe.lastPoint.x
        return isXYRatioCorrect(pipe, xDiff)
    }
}

private fun isXYRatioCorrect(pipe: Pipe, xDiff: Int): Boolean {
    return if (pipe.firstPoint.y < pipe.lastPoint.y) {
        val yDiff = pipe.lastPoint.y - pipe.firstPoint.y
        xDiff == yDiff
    } else {
        val yDiff = pipe.firstPoint.y - pipe.lastPoint.y
        xDiff == yDiff
    }
}

private fun getAllPipePoints(pipe: Pipe): List<Point> {
    return when {
        isHorizontalPipe(pipe) -> getAllHorizontalPipePoints(pipe)
        isVerticalPipe(pipe) -> getAllVerticalPipePoints(pipe)
        isDiagonalPipe(pipe) -> getAllDiagonalPipePoints(pipe)
        else -> emptyList()
    }
}

private fun getAllHorizontalPipePoints(pipe: Pipe): List<Point> {
    val y = pipe.firstPoint.y
    return if (pipe.lastPoint.x < pipe.firstPoint.x) {
        (pipe.firstPoint.x downTo pipe.lastPoint.x).map { Point(it, y) }
    } else {
        (pipe.firstPoint.x .. pipe.lastPoint.x).map { Point(it, y) }
    }
}

private fun getAllVerticalPipePoints(pipe: Pipe): List<Point> {
    val x = pipe.firstPoint.x
    return if (pipe.lastPoint.y < pipe.firstPoint.y) {
        (pipe.firstPoint.y downTo pipe.lastPoint.y).map { Point(x, it) }
    } else (pipe.firstPoint.y .. pipe.lastPoint.y).map { Point(x, it) }
}

fun getAllDiagonalPipePoints(pipe: Pipe): List<Point> {
    if (pipe.firstPoint.x < pipe.lastPoint.x) {
        val xRange = pipe.firstPoint.x .. pipe.lastPoint.x
        return getDiagonalPipePointsWithXRange(pipe, xRange)
    } else {
        val xRange = pipe.firstPoint.x downTo  pipe.lastPoint.x
        return getDiagonalPipePointsWithXRange(pipe, xRange)
    }
}

private fun getDiagonalPipePointsWithXRange(pipe: Pipe, xRange: IntProgression): List<Point> {
    return if (pipe.firstPoint.y < pipe.lastPoint.y) {
        val yRange = pipe.firstPoint.y..pipe.lastPoint.y
        xRange.zip(yRange).map { (x, y) -> Point(x, y) }
    } else {
        val yRange = pipe.firstPoint.y downTo pipe.lastPoint.y
        xRange.zip(yRange).map { (x, y) -> Point(x, y) }
    }
}

private fun getInitialDiagram(maxX: Int, maxY: Int): MutableList<MutableList<Int>> =
    (0 .. maxY).map {
        (0 .. maxX).map { 0 }.toMutableList()
    }.toMutableList()

private fun countAtLeastTwoPointsOverlap(diagram: MutableList<MutableList<Int>>): Int =
    diagram.flatten().count { it >= 2 }
