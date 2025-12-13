package com.application.ui.feature_home_wrapper.live_club

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.data.repository.ClubStatusRepository
import com.application.domain.model.ClubStatus
import com.application.domain.model.ZoneStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

data class LiveClubState(
    val clubStatus: ClubStatus? = null,
    val isLoading: Boolean = false
) : UiState {
    val hasData: Boolean
        get() = clubStatus != null
}

sealed class LiveClubEvent : UiEvent {
    object OnScreenShown : LiveClubEvent()
    object OnReserveNowClicked : LiveClubEvent()
}

sealed class LiveClubEffect : UiEffect {
    object NavigateToReserveSeat : LiveClubEffect()
}

@HiltViewModel
class LiveClubViewModel @Inject constructor(
    private val clubStatusRepository: ClubStatusRepository
) : MviViewModel<LiveClubEvent, LiveClubState, LiveClubEffect>() {

    private var simulationJob: Job? = null

    override fun createInitialState(): LiveClubState = LiveClubState()

    override fun handleEvent(event: LiveClubEvent) {
        when (event) {
            is LiveClubEvent.OnScreenShown -> {
                loadClubStatus()
                startSimulation()
            }
            is LiveClubEvent.OnReserveNowClicked -> {
                setEffect { LiveClubEffect.NavigateToReserveSeat }
            }
        }
    }

    private fun loadClubStatus() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                // Инициализируем данные если их нет
                val currentStatus = clubStatusRepository.getClubStatus().first()
                if (currentStatus == null) {
                    val defaultStatus = getDefaultStatus()
                    clubStatusRepository.saveClubStatus(defaultStatus)
                }
            } catch (e: Exception) {
                // Если ошибка, используем дефолтные значения
                val defaultStatus = getDefaultStatus()
                setState { 
                    copy(
                        clubStatus = defaultStatus,
                        isLoading = false
                    ) 
                }
                return@launch
            }
            
            // Подписываемся на изменения
            clubStatusRepository.getClubStatus().collect { status ->
                setState { 
                    copy(
                        clubStatus = status ?: getDefaultStatus(),
                        isLoading = false
                    ) 
                }
            }
        }
    }

    private fun getDefaultStatus(): ClubStatus {
        return ClubStatus(
            id = "club_status",
            pcFree = 12,
            pcBusy = 8,
            zoneAStatus = ZoneStatus.FREE,
            zoneBStatus = ZoneStatus.BUSY,
            zoneCStatus = ZoneStatus.BUSY,
            estimatedWaitTimeMinutes = 12,
            lastUpdated = System.currentTimeMillis()
        )
    }

    private fun startSimulation() {
        // Останавливаем предыдущую симуляцию если есть
        simulationJob?.cancel()
        
        simulationJob = viewModelScope.launch {
            while (true) {
                delay(5000) // Обновление каждые 5 секунд
                simulateStatusUpdate()
            }
        }
    }

    private suspend fun simulateStatusUpdate() {
        val currentStatus = clubStatusRepository.getClubStatus().first() ?: getDefaultStatus()
        val random = Random()
        
        // Симулируем изменения в статусе
        val newPcFree = (currentStatus.pcFree + random.nextInt(5) - 2).coerceIn(0, 20)
        val newPcBusy = (20 - newPcFree).coerceIn(0, 20)
        
        // Симулируем изменения зон
        val zoneAStatus = if (random.nextBoolean()) ZoneStatus.FREE else ZoneStatus.BUSY
        val zoneBStatus = if (random.nextBoolean()) ZoneStatus.FREE else ZoneStatus.BUSY
        val zoneCStatus = if (random.nextBoolean()) ZoneStatus.FREE else ZoneStatus.BUSY
        
        // Рассчитываем примерное время ожидания на основе занятости
        val estimatedWaitTime = if (newPcBusy > 0) {
            // Среднее время сессии ~60 минут, делим на количество занятых ПК
            (60 / newPcBusy.coerceAtLeast(1)).coerceIn(5, 30)
        } else {
            0
        }
        
        val updatedStatus = currentStatus.copy(
            pcFree = newPcFree,
            pcBusy = newPcBusy,
            zoneAStatus = zoneAStatus,
            zoneBStatus = zoneBStatus,
            zoneCStatus = zoneCStatus,
            estimatedWaitTimeMinutes = estimatedWaitTime,
            lastUpdated = System.currentTimeMillis()
        )
        
        clubStatusRepository.updateClubStatus(updatedStatus)
    }

    override fun onCleared() {
        super.onCleared()
        simulationJob?.cancel()
    }
}

