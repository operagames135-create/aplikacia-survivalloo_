package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object ClipboardHelper {
    fun copyToClipboard(context: Context, label: String, text: String, customToastMessage: String? = null) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        
        val message = customToastMessage ?: "Skopírované do schránky: $text"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun openUrl(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Nepodarilo sa otvoriť odkaz: $url", Toast.LENGTH_SHORT).show()
        }
    }
}
