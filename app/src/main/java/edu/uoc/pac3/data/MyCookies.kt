package edu.uoc.pac3.data

import android.content.Context
import android.webkit.CookieManager


object MyCookies {
    fun clearCookies(context: Context?) {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }
}