package utils

const val ANSI_RESET = "\u001B[0m"

enum class Color(val ansiCode: String) {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    YELLOW("\u001B[33m")
}

/**
 * Prints a message to the console, optionally applying an ANSI color.
 *
 * @param message The string to print.
 * @param color The Color enum to use for formatting (optional).
 */
fun log(message: String, color: Color? = null) {
    if (color != null) {
        val coloredMessage = "${color.ansiCode}$message$ANSI_RESET"
        println(coloredMessage)
    } else {
        println(message)
    }
}