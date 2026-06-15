package com.example.firebase

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthHelper(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("auto") // Let Google Play services auto-resolve the client ID from google-services.json
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun getUserId(): String? = auth.currentUser?.uid

    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }

    suspend fun signInWithGoogle(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return suspendCancellableCoroutine { continuation ->
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result.user!!)
                    } else {
                        continuation.resumeWithException(task.exception ?: Exception("Sign-in failed"))
                    }
                }
        }
    }

    fun getSignInIntent(): Intent = googleSignInClient.signInIntent

    fun handleSignInResult(data: Intent?, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { onSuccess(it) }
        } catch (e: ApiException) {
            onError(e.localizedMessage ?: "Sign-in failed")
        }
    }
}
