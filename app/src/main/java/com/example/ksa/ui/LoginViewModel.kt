package com.example.ksa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ksa.KsaApplication
import com.example.ksa.classes.CurrentMember
import com.example.ksa.classes.LoginData
import com.example.ksa.data.KsaRepository
import com.example.ksa.data.WorkManagerApiRepository
import com.example.ksa.network.KsaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.logging.Logger

class LoginViewModel(
    private val ksaRepository: KsaRepository,
    private val workManagerApiRepository: WorkManagerApiRepository,

    ) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun sendNotification(memberId: Int) {
        Logger.getLogger("LoginViewModel").warning("sendNotification($memberId))")
        workManagerApiRepository.sendRecentAnnouncementNotification(memberId)
    }

    fun getApiData(memberId: Int) {
        sendNotification(memberId)
        viewModelScope.launch {
            try {
                val members = KsaApi.retrofitService.getMembers()
                val groups = KsaApi.retrofitService.getGroups()
                val announcements = KsaApi.retrofitService.getAnnouncements(memberId)
                val translations = KsaApi.retrofitService.getTranslations()
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        ksaRepository.clearDatabase()
                        groups.forEach { group ->
                            ksaRepository.addGroup(group)
                        }
                        announcements.forEach { announcements ->
                            ksaRepository.addAnnouncement(announcements)
                        }
                        translations.forEach { translation ->
                            ksaRepository.addTranslation(translation)
                        }
                        members.forEach { member ->
                            ksaRepository.addMember(member)
                        }
                    }
                }
            } catch (e: IOException) {
                _uiState.update { currentState -> currentState.copy(error = "Geen connectie met de server") }
            } catch (e: SocketTimeoutException) {
                _uiState.update { currentState -> currentState.copy(error = "Geen connectie met de server") }
            } catch (e: HttpException) {
                _uiState.update { currentState -> currentState.copy(error = "Geen connectie met de server") }
            }
        }
    }

    fun onEmailChange(it: String) {
        _uiState.update { currentState -> currentState.copy(emailText = it) }
    }

    fun onPasswordChange(it: String) {
        _uiState.update { currentState -> currentState.copy(passwordText = it) }
    }

    fun login() {
        viewModelScope.launch {
            try {
                val currentAccount = KsaApi.retrofitService.login(LoginData(_uiState.value.emailText, _uiState.value.passwordText))

                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        ksaRepository.login(currentAccount)
                    }
                }
            } catch (e: HttpException) {
                _uiState.update { currentState -> currentState.copy(error = "Verkeerde inloggegevens") }
            } catch (e: IOException) {
                _uiState.update { currentState -> currentState.copy(error = "Geen connectie met de server") }
            } catch (e: SocketTimeoutException) {
                _uiState.update { currentState -> currentState.copy(error = "Geen connectie met de server") }
            }
        }
    }

    fun getCurrentAccount(): Flow<CurrentMember> {
        return ksaRepository.getCurrentAccount()
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KsaApplication)
                LoginViewModel(
                    KsaRepository(application.ksaDatabase.ksaDao()),
                    workManagerApiRepository = WorkManagerApiRepository(application.applicationContext)
                )
            }
        }
    }
}