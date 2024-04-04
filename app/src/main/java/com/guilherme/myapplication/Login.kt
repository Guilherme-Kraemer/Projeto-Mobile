package com.guilherme.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity


class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login);

        val button: Button = findViewById(R.id.botaologin)
        button.setOnClickListener(View.OnClickListener {
            val it = Intent(this@Login, MainActivity::class.java)
            startActivity(it)
        })
    };
    }
