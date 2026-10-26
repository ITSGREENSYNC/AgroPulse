package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AgroRepository
import com.example.data.SampleData
import com.example.model.CropDiseaseDetection
import com.example.model.SeverityLevel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScanUiState(
    val availableSpecimens: List<CropDiseaseDetection> = SampleData.sampleDetections,
    val selectedSpecimenIndex: Int = 0,
    val activeDetection: CropDiseaseDetection = SampleData.sampleDetections[0],
    val isScanningActive: Boolean = true,
    val isTorchEnabled: Boolean = false,
    val isAnalyzing: Boolean = false,
    val showDetailSheet: Boolean = false,
    val scanSavedNotification: String? = null
)

class ScanViewModel(private val repository: AgroRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun selectSpecimen(index: Int) {
        if (index in _uiState.value.availableSpecimens.indices) {
            val specimen = _uiState.value.availableSpecimens[index]
            _uiState.value = _uiState.value.copy(
                selectedSpecimenIndex = index,
                activeDetection = specimen,
                isAnalyzing = true
            )
            viewModelScope.launch {
                delay(600) // Realistic AI bounding box re-lock delay
                _uiState.value = _uiState.value.copy(isAnalyzing = false)
            }
        }
    }

    fun toggleTorch() {
        _uiState.value = _uiState.value.copy(isTorchEnabled = !_uiState.value.isTorchEnabled)
    }

    fun toggleScanning(active: Boolean) {
        _uiState.value = _uiState.value.copy(isScanningActive = active)
    }

    fun openDetailSheet() {
        _uiState.value = _uiState.value.copy(showDetailSheet = true)
    }

    fun closeDetailSheet() {
        _uiState.value = _uiState.value.copy(showDetailSheet = false)
    }

    fun saveCurrentScan() {
        val detection = _uiState.value.activeDetection
        viewModelScope.launch {
            repository.recordScan(detection, "Logged via AgroPulse HUD scanner")
            _uiState.value = _uiState.value.copy(
                scanSavedNotification = "Scan for ${detection.cropName} saved to farm logs!"
            )
            delay(3000)
            _uiState.value = _uiState.value.copy(scanSavedNotification = null)
        }
    }
}
