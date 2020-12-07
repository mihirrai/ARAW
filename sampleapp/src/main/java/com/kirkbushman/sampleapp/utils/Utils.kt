package com.kirkbushman.sampleapp.utils

import android.content.Context
import com.kirkbushman.sampleapp.TestCredentials
import com.kirkbushman.sampleapp.R
import org.xmlpull.v1.XmlPullParser

object Utils {

    fun loadCredentials(context: Context): TestCredentials {

        val isTravis = System.getenv("TRAVIS") != null && System.getenv("TRAVIS")!!.toBoolean()
        return if (isTravis) {
            loadCredentialsFromTravis()
        } else {
            loadCredentialsFromXml(context)
        }
    }

    private fun loadCredentialsFromTravis(): TestCredentials {

        val clientId = System.getenv("clientId")
        val redirectUrl = System.getenv("redirectUrl")

        val scriptClientId = System.getenv("scriptClientId")
        val scriptClientSecret = System.getenv("scriptClientSecret")

        val username = System.getenv("username")
        val password = System.getenv("password")

        val scopes = ArrayList<String>()
        scopes.addAll(System.getenv("scopes")?.split(',') ?: emptyList())

        return TestCredentials(
            clientId = clientId!!,
            redirectUrl = redirectUrl!!,

            scriptClientId = scriptClientId!!,
            scriptClientSecret = scriptClientSecret!!,
            username = username!!,
            password = password!!,

            scopes
        )
    }

    private fun loadCredentialsFromXml(context: Context): TestCredentials {
        val xpp = context.resources.getXml(R.xml.credentials)

        var clientId = ""
        var redirectUrl = ""

        var scriptClientId = ""
        var scriptClientSecret = ""
        var username = ""
        var password = ""

        val scopes = ArrayList<String>()

        while (xpp.eventType != XmlPullParser.END_DOCUMENT) {

            when (xpp.eventType) {

                XmlPullParser.START_TAG -> {

                    when (xpp.name) {
                        "clientId" -> clientId = xpp.nextText()
                        "redirectUrl" -> redirectUrl = xpp.nextText()
                        "scope" -> scopes.add(xpp.nextText())
                        "scriptClientId" -> scriptClientId = xpp.nextText()
                        "scriptClientSecret" -> scriptClientSecret = xpp.nextText()
                        "username" -> username = xpp.nextText()
                        "password" -> password = xpp.nextText()
                    }
                }
            }

            xpp.next()
        }

        return TestCredentials(
            clientId,
            redirectUrl,

            scriptClientId,
            scriptClientSecret,
            username,
            password,

            scopes
        )
    }
}
