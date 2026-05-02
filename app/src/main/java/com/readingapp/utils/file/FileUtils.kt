package com.readingapp.utils.file

import android.content.Context
import android.net.Uri
import java.io.BufferedReader
import java.io.InputStreamReader

object FileUtils {

    fun readTextFile(context: Context, uri: Uri): String {
        val content = StringBuilder()
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    content.append(line).append("\n")
                }
            }
        }
        return content.toString()
    }

    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex >= 0) {
                result = cursor.getString(nameIndex)
            }
        }
        return result ?: "Unknown"
    }

    fun getFileExtension(fileName: String): String {
        val lastDot = fileName.lastIndexOf('.')
        return if (lastDot >= 0) {
            fileName.substring(lastDot + 1).lowercase()
        } else {
            ""
        }
    }

    fun isTextFile(fileName: String): Boolean {
        val extension = getFileExtension(fileName)
        return extension == "txt"
    }

    fun isPdfFile(fileName: String): Boolean {
        val extension = getFileExtension(fileName)
        return extension == "pdf"
    }

    fun isEpubFile(fileName: String): Boolean {
        val extension = getFileExtension(fileName)
        return extension == "epub"
    }
}
