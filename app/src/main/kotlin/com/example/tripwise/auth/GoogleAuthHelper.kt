package com.example.tripwise.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.tripwise.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class GoogleAuthHelper(private val context: Context) {
    private val credentialManager = CredentialManager.create(context)
    private val auth = Firebase.auth

    suspend fun signInWithGoogle(): Result<String> {
        return try {
            val serverClientId = context.getString(R.string.default_web_client_id)
            val googleIdOption = GetSignInWithGoogleOption.Builder(serverClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            if (credential is androidx.credentials.CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                val user = authResult.user
                Result.success(user?.displayName ?: user?.email ?: "Traveler")
            } else {
                Result.failure(Exception("Unrecognized credential type"))
            }
        } catch (e: GetCredentialCancellationException) {
            Log.w("GoogleAuth", "Sign-in was cancelled by user", e)
            Result.failure(e)
        } catch (e: GetCredentialException) {
            Log.e("GoogleAuth", "Credential error: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Authentication error: ${e.message}", e)
            Result.failure(e)
        }
    }
}
