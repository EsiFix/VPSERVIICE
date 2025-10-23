package com.vpserviice.app.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vpserviice.app.databinding.ActivityConnectBinding
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.core.ConfigParser
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VpnStatus
import java.io.InputStream
import java.io.InputStreamReader

class ConnectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConnectBinding
    private var currentProfile: VpnProfile? = null

    private val pickOvpn = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnImport.setOnClickListener { openFilePicker() }
        binding.btnConnect.setOnClickListener { startVpn() }
        binding.btnDisconnect.setOnClickListener { stopVpn() }

        VpnStatus.initLogCache(this.applicationContext)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, "Select .ovpn"), pickOvpn)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickOvpn && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) importOvpn(uri)
        }
    }

    private fun importOvpn(uri: Uri) {
        try {
            val input: InputStream? = contentResolver.openInputStream(uri)
            val cp = ConfigParser()
            cp.parseConfig(InputStreamReader(input))
            val profile = cp.convertProfile()
            profile.mName = "Imported-" + System.currentTimeMillis()
            // ذخیره در پروفایل منیجر
            val pm = ProfileManager.getInstance(this)
            pm.addProfile(profile)
            pm.saveProfile(this, profile)
            pm.saveProfileList(this)
            currentProfile = profile
            Toast.makeText(this, "OVPN imported.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Import error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun startVpn() {
        val profile = currentProfile
        if (profile == null) {
            Toast.makeText(this, "First import a .ovpn file.", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            ProfileManager.getInstance(this).saveProfile(this, profile)
            val intent = profile.prepareStartService(this)
            if (intent != null) {
                // نیاز به اجازه VpnService
                startActivityForResult(intent, 0)
                return
            }
            OpenVPNService.setNotificationActivityClass(ConnectActivity::class.java)
            profile.startVPN(this)
            startChrono(binding.chrono)
            binding.tvStatus.text = "Status: Connecting..."
        } catch (e: Exception) {
            Toast.makeText(this, "Start error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun stopVpn() {
        try {
            val stopIntent = Intent(this, OpenVPNService::class.java)
            stopIntent.action = OpenVPNService.STOP_VPN
            startService(stopIntent)
            stopChrono(binding.chrono)
            binding.tvStatus.text = "Status: Disconnected"
        } catch (e: Exception) {
            Toast.makeText(this, "Stop error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun startChrono(c: Chronometer) {
        c.base = SystemClock.elapsedRealtime()
        c.start()
        binding.tvStatus.text = "Status: Connected"
    }

    private fun stopChrono(c: Chronometer) {
        c.stop()
    }
}
