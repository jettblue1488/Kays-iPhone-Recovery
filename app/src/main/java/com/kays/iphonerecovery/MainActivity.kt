package com.kays.iphonerecovery

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.hardware.usb.UsbManager
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.*
import android.graphics.drawable.GradientDrawable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var root: LinearLayout
    private lateinit var status: TextView
    private val red = Color.rgb(222, 32, 48)
    private fun label(s: String, size: Float = 17f): TextView = TextView(this).apply {
        text = s; textSize = size; setTextColor(Color.WHITE); setPadding(8, 12, 8, 12)
    }
    private fun button(s: String, action: () -> Unit) {
        val b = Button(this).apply {
            text = s; setTextColor(Color.WHITE); isAllCaps = false
            background = GradientDrawable().apply { setColor(red); cornerRadius = 22f }
            setOnClickListener { action() }
        }
        root.addView(b, LinearLayout.LayoutParams(-1, 110).apply { bottomMargin = 16 })
    }
    private fun save(s: String) {
        val p = getSharedPreferences("history", MODE_PRIVATE)
        val stamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
        p.edit().putString("log", "[$stamp] $s\n" + p.getString("log", "")).apply()
    }
    private fun show(title: String, text: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(text).setPositiveButton("OK", null).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.BLACK
        val scroll = ScrollView(this).apply { setBackgroundColor(Color.rgb(12, 12, 14)) }
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; setPadding(28, 30, 28, 30)
        }
        scroll.addView(root)
        setContentView(scroll)
        root.addView(label("🕷 BLACK WIDOW", 30f).apply {
            gravity = Gravity.CENTER; setTextColor(red); setTypeface(null, Typeface.BOLD)
        })
        root.addView(label("KAY'S iPHONE RECOVERY", 21f).apply { gravity = Gravity.CENTER })
        root.addView(label("iPhone 13 / mini / Pro / Pro Max\nSafe guided recovery • Offline instructions", 15f).apply { gravity = Gravity.CENTER })
        status = label("Connection: not checked")
        root.addView(status)
        button("Detect connected USB devices") {
            val manager = getSystemService(Context.USB_SERVICE) as UsbManager
            val devices = manager.deviceList.values.toList()
            val apple = devices.filter { it.vendorId == 0x05AC }
            val result = if (apple.isNotEmpty()) "Apple USB device detected (not yet verified as iPhone 13). Product IDs: " + apple.joinToString { "0x" + it.productId.toString(16) }
                else if (devices.isEmpty()) "No USB devices detected. Check OTG support and cable."
                else "USB device(s) detected, but none identified as Apple. Vendor IDs: " + devices.joinToString { "0x" + it.vendorId.toString(16) }
            status.text = result; save(result)
            show("USB diagnostic", result + "\n\nDetection does not mean firmware recovery is supported.")
        }
        button("Fix stuck Apple logo — guided Update") {
            save("Opened update instructions")
            show("Try Update before Restore", "1. Connect iPhone to a Mac or Windows computer with a data-capable cable.\n2. Quickly press Volume Up, then Volume Down.\n3. Hold the Side button until the connect-to-computer recovery screen appears; keep holding past the Apple logo.\n4. On Mac use Finder; on Windows use Apple Devices (or iTunes where applicable).\n5. Choose UPDATE, not Restore, to attempt reinstalling iOS without erasing data.\n\nIf downloading takes too long and the iPhone leaves recovery mode, repeat the button sequence. Back up if possible.")
        }
        button("Factory Restore — erase warning") {
            AlertDialog.Builder(this).setTitle("Restore erases iPhone data")
                .setMessage("Factory Restore deletes data and reinstalls iOS. It may require the original Apple Account for Activation Lock. This app does NOT perform a restore directly. Continue to official instructions?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Show instructions") { _, _ ->
                    save("Viewed factory restore instructions")
                    show("Official restore", "Connect to a Mac or Windows computer, enter recovery mode using Volume Up, Volume Down, then hold Side button until recovery screen. Open Finder / Apple Devices and choose RESTORE only if you accept data erasure. Do not disconnect during installation.")
                }.show()
        }
        button("Computer-assisted recovery") {
            show("Computer companion", "Use a Mac with Finder or Windows with Apple Devices. The companion remote-control feature is not yet implemented; this button provides safe setup guidance only.")
        }
        button("Recovery history") {
            show("Saved history", getSharedPreferences("history", MODE_PRIVATE).getString("log", "No activity recorded yet.") ?: "")
        }
        root.addView(label("Version 0.1 • USB identification and guided recovery only. Direct iOS flashing, one-tap repair, and computer remote control are not implemented.", 13f))
    }
}
