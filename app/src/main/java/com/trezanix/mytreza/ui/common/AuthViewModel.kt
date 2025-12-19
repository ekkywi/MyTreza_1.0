package com.trezanix.mytreza.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trezanix.mytreza.data.local.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userSession: UserSession
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            delay(1000)

            userSession.isLoggedIn.collect { isLoggedIn ->
                _isUserLoggedIn.value = isLoggedIn
                _isLoading.value = false
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            userSession.saveLoginState(true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userSession.saveLoginState(false)
        }
    }
}