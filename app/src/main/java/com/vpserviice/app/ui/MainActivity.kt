package com.vpserviice.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vpserviice.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.text = "VPSERVIICE"
        binding.subtitle.text = "Android VPN Client"

        binding.btnLogin.setOnClickListener {
            // TODO: صفحه Login → Cloudius/RADIUS
        }

        binding.btnServers.setOnClickListener {
    startActivity(Intent(this, ConnectActivity::class.java))
        }
    }
}
