package edu.uoc.pac3.tools

import android.app.Activity
import android.content.Intent
import edu.uoc.pac3.R

fun Activity.playGoAnimation(){
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun Activity.playBackAnimation(){
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}

inline fun <reified T : Activity> Activity.goToActivity(noinline init: Intent.() -> Unit = {}){
    val intent = Intent(this, T::class.java)
    intent.init() //Es para poder introducir más cosas sobre el intent como putExtras, flags...
    startActivity(intent)
}