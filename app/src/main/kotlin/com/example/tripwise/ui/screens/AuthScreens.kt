package com.example.tripwise.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.auth.GoogleAuthHelper
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("karimnoman97@gmail.com") }
    var password by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var showForgotDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleAuthHelper = remember { GoogleAuthHelper(context) }

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    if (showForgotDialog) {
        ForgotPasswordDialog(
            onDismiss = { showForgotDialog = false },
            onSubmit = { resetEmail ->
                showForgotDialog = false
                viewModel.showMessage("Password reset instructions sent to $resetEmail")
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
            .testTag("login_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(AppColors.PrimaryBlue.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = "Logo",
                tint = AppColors.PrimaryBlueDark,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome Back!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextDark
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Sign in to manage and plan your trips",
            fontSize = 13.sp,
            color = AppColors.TextGrey
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Google Sign-In Option
        OutlinedButton(
            onClick = {
                scope.launch {
                    val result = googleAuthHelper.signInWithGoogle()
                    result.onSuccess { name ->
                        viewModel.showMessage("Signed in with Google as $name")
                        viewModel.navigateTo(Screen.Home)
                    }.onFailure { err ->
                        if (err !is androidx.credentials.exceptions.GetCredentialCancellationException) {
                            errorMessage = err.message ?: "Google sign-in failed"
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("google_signin_button"),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, AppColors.BorderGrey),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.TextDark)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Google",
                tint = AppColors.PrimaryBlueDark,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Sign in with Google",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = AppColors.TextDark
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = AppColors.BorderGrey)
            Text(
                text = "  OR EMAIL  ",
                fontSize = 11.sp,
                color = AppColors.TextGrey,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = AppColors.BorderGrey)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AppColors.TextGrey) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_email_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.PrimaryBlue,
                unfocusedBorderColor = AppColors.BorderGrey
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AppColors.TextGrey) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password",
                        tint = AppColors.TextGrey
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_password_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.PrimaryBlue,
                unfocusedBorderColor = AppColors.BorderGrey
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = AppColors.PrimaryBlue)
                )
                Text("Remember Me", fontSize = 12.sp, color = AppColors.TextDark)
            }

            Text(
                text = "Forgot Password?",
                color = AppColors.PrimaryBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { showForgotDialog = true }
                    .padding(4.dp)
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage!!, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (email.isBlank() || !email.contains("@")) {
                    errorMessage = "Please enter a valid email address."
                } else if (password.length < 6) {
                    errorMessage = "Password must be at least 6 characters."
                } else {
                    viewModel.login(email)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("login_submit_button"),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?", fontSize = 13.sp, color = AppColors.TextGrey)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Sign Up",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.PrimaryBlue,
                modifier = Modifier.clickable { viewModel.navigateTo(Screen.Signup) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
            Text("Continue as Guest", color = AppColors.TextGrey, fontSize = 13.sp)
        }
    }
}

@Composable
fun SignupScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreedToTerms by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleAuthHelper = remember { GoogleAuthHelper(context) }

    BackHandler {
        viewModel.navigateTo(Screen.Login)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
            .testTag("signup_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(AppColors.Green.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = "Logo",
                tint = AppColors.Green,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Create Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Join TripWise for smart travel planning",
            fontSize = 13.sp,
            color = AppColors.TextGrey
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Google Sign-In Option
        OutlinedButton(
            onClick = {
                scope.launch {
                    val result = googleAuthHelper.signInWithGoogle()
                    result.onSuccess { googleName ->
                        viewModel.showMessage("Signed in with Google as $googleName")
                        viewModel.navigateTo(Screen.Home)
                    }.onFailure { err ->
                        if (err !is androidx.credentials.exceptions.GetCredentialCancellationException) {
                            errorMessage = err.message ?: "Google sign-in failed"
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("google_signup_button"),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, AppColors.BorderGrey),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.TextDark)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Google",
                tint = AppColors.PrimaryBlueDark,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Sign in with Google",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = AppColors.TextDark
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = AppColors.BorderGrey)
            Text(
                text = "  OR FILL DETAILS  ",
                fontSize = 11.sp,
                color = AppColors.TextGrey,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = AppColors.BorderGrey)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Name
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                errorMessage = null
            },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AppColors.TextGrey) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_name_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AppColors.TextGrey) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_email_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Phone
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
                errorMessage = null
            },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = AppColors.TextGrey) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_phone_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("Password (min 6 chars)") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AppColors.TextGrey) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password",
                        tint = AppColors.TextGrey
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_password_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agreedToTerms,
                onCheckedChange = { agreedToTerms = it },
                colors = CheckboxDefaults.colors(checkedColor = AppColors.PrimaryBlue)
            )
            Text(
                text = "I agree to the Terms of Service & Privacy Policy",
                fontSize = 12.sp,
                color = AppColors.TextDark
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage!!, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (name.isBlank()) {
                    errorMessage = "Please enter your name."
                } else if (email.isBlank() || !email.contains("@")) {
                    errorMessage = "Please enter a valid email."
                } else if (password.length < 6) {
                    errorMessage = "Password must be at least 6 characters."
                } else if (!agreedToTerms) {
                    errorMessage = "Please accept the Terms of Service."
                } else {
                    viewModel.signup(name, email, phone)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("signup_submit_button"),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Create Account", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Already have an account?", fontSize = 13.sp, color = AppColors.TextGrey)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Sign In",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.PrimaryBlue,
                modifier = Modifier.clickable { viewModel.navigateTo(Screen.Login) }
            )
        }
    }
}

@Composable
fun ForgotPasswordDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reset Password", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    text = "Enter your email address and we'll send you instructions to reset your password.",
                    fontSize = 13.sp,
                    color = AppColors.TextGrey
                )
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (email.isNotBlank()) onSubmit(email) },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
            ) {
                Text("Send Instructions")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
