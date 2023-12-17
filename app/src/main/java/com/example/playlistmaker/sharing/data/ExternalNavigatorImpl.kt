package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R

class ExternalNavigatorImpl(
    private val context: Context
): ExternalNavigator {

    override fun shareApp() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.practicum_android_course_link))
            context.startActivity(this)
        }
    }

    override fun openTerms() {
        Intent(Intent.ACTION_VIEW).apply {
            val agreementURL = context.getString(R.string.practicum_user_agreement_link)
            data = Uri.parse(agreementURL)
            context.startActivity(this)
        }
    }

    override fun writeEmail() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_message_email)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_message_theme))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_message_body))
            context.startActivity(this)
        }
    }

}