package de.syntax.androidabschluss.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import de.syntax.androidabschluss.R

val assistantImageList = listOf(
    R.drawable.katze,
    R.drawable.pascal,
    R.drawable.phantom,
    R.drawable.testlogo,




)
enum class Status{
    LOADING,
    SUCCESS,
    ERROR

}
enum class NetworkStatus{
    Available,Unavailable
}

enum class  StatusResult {
    Added,
    Updated,
    Deleted
}

fun Context.hideKeyBoard(it: View) {
    try {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken,0)
    }catch (e:Exception){
        e.printStackTrace()
    }
}


fun Context.longToastShow(message: String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()

}
fun Context.copyToClipBoard(message: String) {
    val clipBoard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text",message)
    clipBoard.setPrimaryClip(clip)
    longToastShow("Copied")
}

 fun Context.shareMsg(message: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT,message)
    startActivity(Intent.createChooser(intent,"Share Message"))
}

