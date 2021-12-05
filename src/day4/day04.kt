package day4

import readInputSplitByEmptyLine

private data class Table(val rows: List<Row>)
private data class Row(val numbers: MutableList<Pair<Int, Boolean>>)

fun main() {
    fun part1(input: List<String>): Int {
        val bingoNumbers= input.first().split(",").map { it.toInt() }
        val bingoTables = input
            .drop(1)
            .map { tableString -> createTableFromString(tableString) }

        var lastNumberCalled = 0

        for (number in bingoNumbers) {
            markNumberInTables(number, bingoTables)
            if (isBingo(bingoTables)) {
                lastNumberCalled = number
                break
            }
        }

        val winningBoard = getAllWinningTables(bingoTables).first()
        val sum = getSumOfUnmarkedNumbersOnWinningBoard(winningBoard)

        return sum * lastNumberCalled
    }

    fun part2(input: List<String>): Int {
        val bingoNumbers = input.first().split(",").map { it.toInt() }
        val bingoTables = input
            .drop(1)
            .map { tableString -> createTableFromString(tableString) }
            .toMutableList()
        val winningTables: MutableList<Table> = mutableListOf()
        val numbersThatSecuredBingo: MutableList<Int> = mutableListOf()

        for (number in bingoNumbers) {
            markNumberInTables(number, bingoTables)
            if (isBingo(bingoTables)) {
                val allCurrentlyWinningTables = getAllWinningTables(bingoTables)
                winningTables.addAll(allCurrentlyWinningTables)
                bingoTables.removeAll { allCurrentlyWinningTables.contains(it) }
                numbersThatSecuredBingo.add(number)
            }
        }
        val sum = getSumOfUnmarkedNumbersOnWinningBoard(winningTables.last())

        return sum * numbersThatSecuredBingo.last()
    }

    val exampleInput = readInputSplitByEmptyLine("day4/Day04Example")

    check(part1(exampleInput) == 4512)
    check(part2(exampleInput) == 1924)

    val input = readInputSplitByEmptyLine("day4/Day04")
    println(part1(input))
    println(part2(input))
}

private fun getAllWinningTables(bingoTables: List<Table>): List<Table> = bingoTables.filter { isWinningTable(it) }

private fun getSumOfUnmarkedNumbersOnWinningBoard(winningBoard: Table): Int =
    winningBoard.rows
        .sumOf { row ->
            row.numbers
                .filter { numberMarkPair -> !numberMarkPair.second }
                .sumOf { it.first }
        }

private fun markNumberInTables(bingoNumber: Int, bingoTables: List<Table>) {
    for ((tableIndex, table) in bingoTables.withIndex()) {
        for ((rowIndex, row) in table.rows.withIndex()) {
            for ((numberIndex, nr) in row.numbers.withIndex()) {
                if (bingoNumber == nr.first) {
                    bingoTables[tableIndex].rows[rowIndex].numbers[numberIndex]= Pair(nr.first, true)
                }
            }
        }
    }
}

private fun isBingo(checkTables: List<Table>): Boolean = checkTables.any { table -> isWinningTable(table) }

private fun isWinningTable(table: Table): Boolean {
    for (row in table.rows) {
        if (row.numbers.any { !it.second }) continue
        else return true
    }
    for (index in 0 until 5) {
        val isColumnWin = table.rows.map { it.numbers[index] }.all { it.second }
        if (isColumnWin)
            return true
    }

    return false
}

private fun createTableFromString(tableString: String): Table {
    val stringRows: List<String> = tableString.split("\r\n")
        .map { it.trim() }
        .map { it.replace("  ", " ") }
    val rows: List<Row> = stringRows.map { createRowFromString(it) }
    return Table(rows)
}

private fun createRowFromString(rowString: String): Row =
    Row(rowString.split(" ")
        .map { Pair(it.toInt(), false) }
        .toMutableList())
