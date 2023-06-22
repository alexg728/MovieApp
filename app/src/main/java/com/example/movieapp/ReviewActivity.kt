package com.example.movieapp

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ReviewActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var shareButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val position = intent.getIntExtra("position", -1)
        if (position != -1) {
            editText = findViewById(R.id.editText)
            val shareButton = findViewById<Button>(R.id.shareButton)

            shareButton.setOnClickListener {
                val text = editText.text.toString()

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, text)

                val chooser = Intent.createChooser(shareIntent, "Share Review")

                if (shareIntent.resolveActivity(packageManager) != null) {
                    startActivity(chooser)
                } else {
                    Toast.makeText(this, "No app available to share", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}