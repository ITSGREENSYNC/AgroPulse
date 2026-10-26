package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.data.AgroRepository
import com.example.model.AgWeatherData
import kotlinx.coroutines.flow.StateFlow

class WeatherViewModel(private val repository: AgroRepository) : ViewModel() {
    val weatherData: StateFlow<AgWeatherData> = repository.weatherState
}
