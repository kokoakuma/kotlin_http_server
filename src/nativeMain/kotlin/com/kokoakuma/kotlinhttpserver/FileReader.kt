package com.kokoakuma.kotlinhttpserver

import com.kokoakuma.kotlinhttpserver.exceptions.NotFoundException
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import platform.posix.perror

class FileReader(private val path: String) {
    private var loaded = false
    private var content: String = ""
    fun content(): String {
        if (loaded) return content
        val file = fopen(path, "r")
        try {
            if (file == null) {
                perror("cannot open file: $file")
                throw NotFoundException()
            }
            memScoped {
                val bufferLength = 64 * 1024
                val buffer = allocArray<ByteVar>(bufferLength)
                while (true) {
                    val nextLine = fgets(buffer, bufferLength, file)?.toKString()
                    if (nextLine == null || nextLine.isEmpty()) break
                    content += nextLine
                }
                loaded = true
            }
            return content
        } finally {
            fclose(file)
        }
    }
}