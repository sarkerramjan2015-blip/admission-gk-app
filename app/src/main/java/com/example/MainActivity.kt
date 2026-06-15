package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.GKRepository
import com.example.data.SeedData
import com.example.firebase.AuthManager
import com.example.firebase.BiometricAuthHelper
import com.example.ui.GKViewModel
import com.example.ui.MainApp
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var biometricHelper: BiometricAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        authManager = AuthManager(this)
        biometricHelper = BiometricAuthHelper(this)

        val db = AppDatabase.getDatabase(this)
        val localRepository = GKRepository(db.gkDao())

        SeedData.populateDatabase(this, localRepository)

        val fingerprintAvailable = biometricHelper.isBiometricAvailable() && authManager.fingerprintReady

        setContent {
            MyApplicationTheme {
                val uid = authManager.getUserId()
                val viewModel: GKViewModel = viewModel(
                    factory = GKViewModel.Factory(localRepository, uid)
                )
                MainApp(
                    viewModel = viewModel,
                    authManager = authManager,
                    fingerprintAvailable = fingerprintAvailable,
                    onFingerprintLogin = {
                        biometricHelper.authenticate(
                            activity = this@MainActivity as FragmentActivity,
                            onSuccess = {
                                authManager.loginWithFingerprint()
                                recreate()
                            },
                            onError = {}
                        )
                    }
                )
            }
        }
    }
}
