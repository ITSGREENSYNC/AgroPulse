package com.example.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AgroRepository
import com.example.data.db.FarmFieldEntity
import com.example.data.db.GrowthMilestoneEntity
import com.example.data.storage.LocalPhotoStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GrowthLogUiState(
    val selectedField: FarmFieldEntity? = null,
    val showAddMilestoneSheet: Boolean = false,
    val showAddFieldSheet: Boolean = false,
    val showComparisonModal: Boolean = false,
    val comparisonMilestoneA: GrowthMilestoneEntity? = null,
    val comparisonMilestoneB: GrowthMilestoneEntity? = null,
    // Add Milestone Form state
    val inputStage: String = "Vegetative Stage",
    val inputHeightCm: Int = 30,
    val inputHealthScore: Int = 9,
    val inputNotes: String = "",
    // Add Field Form state
    val newFieldName: String = "",
    val newCropType: String = "",
    val newAcreage: Double = 2.5
)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class GrowthLogViewModel(private val repository: AgroRepository) : ViewModel() {

    val fields: StateFlow<List<FarmFieldEntity>> = repository.fieldsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(GrowthLogUiState())
    val uiState: StateFlow<GrowthLogUiState> = _uiState.asStateFlow()

    val currentMilestones: StateFlow<List<GrowthMilestoneEntity>> = _uiState
        .flatMapLatest { state ->
            val fieldId = state.selectedField?.id ?: fields.value.firstOrNull()?.id
            if (fieldId != null) {
                repository.getMilestonesForField(fieldId)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectField(field: FarmFieldEntity) {
        _uiState.value = _uiState.value.copy(selectedField = field)
    }

    fun openAddMilestoneSheet() {
        _uiState.value = _uiState.value.copy(showAddMilestoneSheet = true)
    }

    fun closeAddMilestoneSheet() {
        _uiState.value = _uiState.value.copy(showAddMilestoneSheet = false)
    }

    fun openAddFieldSheet() {
        _uiState.value = _uiState.value.copy(showAddFieldSheet = true)
    }

    fun closeAddFieldSheet() {
        _uiState.value = _uiState.value.copy(showAddFieldSheet = false)
    }

    fun updateMilestoneForm(
        stage: String = _uiState.value.inputStage,
        heightCm: Int = _uiState.value.inputHeightCm,
        healthScore: Int = _uiState.value.inputHealthScore,
        notes: String = _uiState.value.inputNotes
    ) {
        _uiState.value = _uiState.value.copy(
            inputStage = stage,
            inputHeightCm = heightCm,
            inputHealthScore = healthScore,
            inputNotes = notes
        )
    }

    fun updateNewFieldForm(
        name: String = _uiState.value.newFieldName,
        crop: String = _uiState.value.newCropType,
        acreage: Double = _uiState.value.newAcreage
    ) {
        _uiState.value = _uiState.value.copy(
            newFieldName = name,
            newCropType = crop,
            newAcreage = acreage
        )
    }

    fun saveMilestone(customBitmap: Bitmap? = null) {
        val field = _uiState.value.selectedField ?: fields.value.firstOrNull() ?: return
        val bmp = customBitmap ?: LocalPhotoStorage.createPlaceholderGrowthBitmap(
            _uiState.value.inputStage,
            _uiState.value.inputHeightCm
        )

        viewModelScope.launch {
            repository.saveGrowthMilestone(
                fieldId = field.id,
                fieldName = field.name,
                photoBitmap = bmp,
                stage = _uiState.value.inputStage,
                plantHeightCm = _uiState.value.inputHeightCm,
                healthScore = _uiState.value.inputHealthScore,
                notes = _uiState.value.inputNotes,
                sowingDateMillis = field.sowingDateMillis
            )
            _uiState.value = _uiState.value.copy(
                showAddMilestoneSheet = false,
                inputNotes = ""
            )
        }
    }

    fun createField() {
        val name = _uiState.value.newFieldName.trim()
        val crop = _uiState.value.newCropType.trim()
        if (name.isBlank() || crop.isBlank()) return

        viewModelScope.launch {
            repository.createField(
                name = name,
                cropType = crop,
                acreage = _uiState.value.newAcreage,
                sowingDateMillis = System.currentTimeMillis()
            )
            _uiState.value = _uiState.value.copy(
                showAddFieldSheet = false,
                newFieldName = "",
                newCropType = ""
            )
        }
    }

    fun openComparisonModal(milestoneA: GrowthMilestoneEntity, milestoneB: GrowthMilestoneEntity) {
        _uiState.value = _uiState.value.copy(
            showComparisonModal = true,
            comparisonMilestoneA = milestoneA,
            comparisonMilestoneB = milestoneB
        )
    }

    fun setComparisonMilestoneA(milestone: GrowthMilestoneEntity) {
        _uiState.value = _uiState.value.copy(comparisonMilestoneA = milestone)
    }

    fun setComparisonMilestoneB(milestone: GrowthMilestoneEntity) {
        _uiState.value = _uiState.value.copy(comparisonMilestoneB = milestone)
    }

    fun closeComparisonModal() {
        _uiState.value = _uiState.value.copy(showComparisonModal = false)
    }

    fun deleteMilestone(milestoneId: String) {
        viewModelScope.launch {
            repository.deleteMilestone(milestoneId)
        }
    }
}
