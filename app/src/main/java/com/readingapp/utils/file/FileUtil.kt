package com.readingapp.utils.file

import android.content.Context
import android.net.Uri
import java.io.BufferedReader
import java.io.InputStreamReader

object FileUtil {
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

    fun readTextFileLines(context: Context, uri: Uri): List<String> {
        val lines = mutableListOf<String>()
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    line?.let { lines.add(it) }
                }
            }
        }
        return lines
    }

    fun getFileName(context: Context, uri: Uri): String? {
        val result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) {
                        return cursor.getString(index)
                    }
                }
            }
        }
        return result
    }

    fun getFileExtension(fileName: String): String {
        val lastDot = fileName.lastIndexOf('.')
        return if (lastDot >= 0) fileName.substring(lastDot + 1).lowercase() else ""
    }
}
