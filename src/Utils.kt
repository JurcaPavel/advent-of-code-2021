import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Splits given input txt file by empty lines
 */
fun readInputSplitByEmptyLine(name: String) = File("src", "$name.txt").readText().split("\r\n\r\n")

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
