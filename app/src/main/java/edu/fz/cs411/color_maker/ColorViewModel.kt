package edu.fz.cs411.color_maker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import kotlin.math.roundToInt

private const val TAG = "ViewModel"

class ColorViewModel: ViewModel() {

    private var redSwitchState: Boolean = false
    private var greenSwitchState: Boolean = false
    private var blueSwitchState: Boolean = false
    private var redSeekBarValue: Int = 0
    private var greenSeekBarValue: Int = 0
    private var blueSeekBarValue: Int = 0

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "View Model instance about to be destroyed")
    }

    private val prefs = PreferencesRepository.getRepository()

    private fun saveRedSwitchState() {
        viewModelScope.launch {
            prefs.saveRedSwitchState(redSwitchState)
            Log.d(TAG, "Done saving red switch State $redSwitchState")
        }
    }

    private fun saveGreenSwitchState() {
        viewModelScope.launch {
            prefs.saveGreenSwitchState(greenSwitchState)
            Log.d(TAG, "Done saving green switch State $greenSwitchState")
        }
    }

    private fun saveBlueSwitchState() {
        viewModelScope.launch {
            prefs.saveBlueSwitchState(blueSwitchState)
            Log.d(TAG, "Done saving blue switch State $blueSwitchState")
        }
    }

    private fun saveRedSeekBarValue() {
        viewModelScope.launch {
            prefs.saveRedSeekBarValue(redSeekBarValue)
            Log.d(TAG, "Done saving red seekbar State $redSeekBarValue")
        }
    }

    private fun saveGreenSeekBarValue() {
        viewModelScope.launch {
            prefs.saveGreenSeekBarValue(greenSeekBarValue)
            Log.d(TAG, "Done saving green seekbar State $greenSeekBarValue")
        }
    }

    private fun saveBlueSeekBarValue() {
        viewModelScope.launch {
            prefs.saveBlueSeekBarValue(blueSeekBarValue)
            Log.d(TAG, "Done saving blue seekbar State $blueSeekBarValue")
        }
    }

    fun loadState(act: MainActivity) {
        viewModelScope.launch {
            prefs.redSwitchState.collectLatest {
                act.redSwitch.isChecked = it
                Log.v(TAG, "Done collecting redSwitch state")
            }
        }
        viewModelScope.launch {
            prefs.greenSwitchState.collectLatest {
                act.greenSwitch.isChecked = it
                Log.v(TAG, "Done collecting greenSwitch state")
            }
        }
        viewModelScope.launch {
            prefs.blueSwitchState.collectLatest {
                act.blueSwitch.isChecked = it
                Log.v(TAG, "Done collecting blueSwitch state")
            }
        }
        sleep(1000)
    }

    fun loadRedSeekBarValue() {
        GlobalScope.launch {
            prefs.redSeekBarValue.collectLatest {
                redSeekBarValue = it
                Log.v(TAG, "Done collecting redSeekBar $redSeekBarValue")
            }
        }
        sleep(1000)
    }

    fun loadGreenSeekBarValue() {
        GlobalScope.launch {
            prefs.greenSeekBarValue.collectLatest {
                greenSeekBarValue = it
                Log.v(TAG, "Done collecting greenSeekBar $greenSeekBarValue")
            }
        }
        sleep(1000)
    }

    fun loadBlueSeekBarValue() {
        GlobalScope.launch {
            prefs.blueSeekBarValue.collectLatest {
                blueSeekBarValue = it
                Log.v(TAG, "Done collecting blueSeekbar $blueSeekBarValue")
            }
        }
        sleep(1000)
    }

    fun setRedSwitchState(state: Boolean) {
        this.redSwitchState = state
        if(!this.redSwitchState) {
            redSeekBarValue = 0
        }
        saveRedSwitchState()
    }

    fun getRedSwitchState(): Boolean {
        return this.redSwitchState
    }

    fun setGreenSwitchState(state: Boolean) {
        this.greenSwitchState = state
        saveGreenSwitchState()
    }

    fun getGreenSwitchState(): Boolean {
        return this.greenSwitchState
    }
    fun setBlueSwitchState(state: Boolean) {
        this.blueSwitchState = state
        saveBlueSwitchState()
    }

    fun getBlueSwitchState(): Boolean {
        return this.blueSwitchState
    }

    fun setRedSeekBarState(value: Int) {
        this.redSeekBarValue = value
        saveRedSeekBarValue()
    }
    fun getRedSeekBarValue(): Int {
        return this.redSeekBarValue
    }
    fun setGreenSeekBarState(value: Int) {
        this.greenSeekBarValue = value
        saveGreenSeekBarValue()
    }
    fun getGreenSeekBarValue(): Int {
        return this.greenSeekBarValue
    }
    fun setBlueSeekBarState(value: Int) {
        this.blueSeekBarValue = value
        saveBlueSeekBarValue()
    }
    fun getBlueSeekBarValue(): Int {
        return this.blueSeekBarValue
    }
    fun resetBackgroundImage(): Int {
        return android.R.color.black
    }
    fun convertEditTextColorValue(value: Any) : Int {
        return value.toString().toDouble().times(255).roundToInt()
    }
}