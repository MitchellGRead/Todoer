package com.example.todoer.sharing

import android.content.Intent

class ShareIntentFactory {

    fun createTextShareIntent(data: String): Intent {
        val sendIntent = createTextSendIntent(data)
        return Intent.createChooser(sendIntent, null)
    }

    private fun createTextSendIntent(data: String): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }
    }
}
