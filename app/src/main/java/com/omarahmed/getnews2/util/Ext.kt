package com.omarahmed.getnews2.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

fun TextView.setTimeAgo(timeString: String?) {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    try {
        timeString?.let {
            val time = format.parse(it)!!.time
            val now = System.currentTimeMillis()
            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
            this.text = ago.toString()
        }
    } catch (e: Exception) {
        throw e
    }
}

fun Fragment.showToastMessage(
    message: String,
    duration: Int = Toast.LENGTH_SHORT,
    context: Context = requireContext()
) {
    Toast.makeText(context,message,duration)
        .show()
}

fun Fragment.showShareBottomSheet(link: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, link)
        type = "text/plain"
    }
    requireActivity().startActivity(intent)
}

fun Fragment.showNews(link: String) {
    val uri = Uri.parse(link)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    requireActivity().startActivity(intent)
}

fun ViewPager2.setPageTransformer(pageLimit: Int, index: Int){
    val compositePaTransformer = CompositePageTransformer()
    compositePaTransformer.apply {
        addTransformer(MarginPageTransformer(30))
        addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
    }
    setPageTransformer(compositePaTransformer)
    offscreenPageLimit = pageLimit
    getChildAt(index).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
}
inline fun SearchView.onQueryTextSubmit(crossinline listener: (String) -> Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            listener(query.orEmpty())
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    })
}