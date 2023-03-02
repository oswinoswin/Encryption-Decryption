package encryptdecrypt

import java.io.File

fun shiftChar(letter: Char, key: Int): Char {
    if(!letter.isLetter()) return letter
    val alphabetLength = 26
    return if (letter.isLowerCase()) {
        'a' + (letter.code + key - 'a'.code + alphabetLength)%alphabetLength
    }else {
        'A' + (letter.code + key - 'A'.code + alphabetLength)%alphabetLength
    }
}

fun unicodeChar(letter: Char, key: Int): Char {
    return (letter.code + key).toChar()
}

fun encrypt(message: String, key: Int, alg: String): String {
    var encrypted = ""
    val charEncryptionMethod = if (alg == "shift") ::shiftChar else ::unicodeChar
    for(letter in message) {
        encrypted += charEncryptionMethod(letter, key)
    }
    return encrypted
}

fun run(mode: String, key: Int, data: String, alg: String): String {
    var result = ""
    when(mode){
        "enc" -> result = encrypt(data, key, alg)
        "dec" -> result = encrypt(data, - key, alg)
    }
    return result
}

fun readFromFile(fileName: String): String {
    val content = File(fileName).readText()
    return content
}

fun writeToFile(fileName: String, message: String) {
    val file = File(fileName)
    file.writeText(message)
}
fun main(args: Array<String>) {
    var mode = "enc"
    var key = 0
    var data = ""
    var outFileName = "" // std or file
    var saveToFile = false
    var alg = "shift"
    for( i in 0 until args.size - 1){
        when(args[i]){
            "-mode" -> mode = args[i + 1]
            "-key" -> key = args[i + 1].toInt()
            "-in" -> data = readFromFile(args[i + 1])
            "-data" -> data = args[i + 1]
            "-alg" -> alg = args[i + 1]
            "-out" -> {
                outFileName = args[i + 1]
                saveToFile = true
            }
        }
    }

    val message = run(mode, key, data, alg)
    if (!saveToFile) {
        println(message)
    } else {
        writeToFile(outFileName, message)
    }

}