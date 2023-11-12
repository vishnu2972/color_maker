package edu.fz.cs411.color_maker

import android.graphics.Color
import android.graphics.Color.rgb
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import kotlin.math.roundToInt

private const val TAG = "MainActivity"
private const val SAVE_RED_SWITCH = "redSwitch"
private const val SAVE_GREEN_SWITCH = "greenSwitch"
private const val SAVE_BLUE_SWITCH = "blueSwitch"
private const val SAVE_RED_SEEKBAR = "redSeekBar"
private const val SAVE_GREEN_SEEKBAR = "GreenSeekBar"
private const val SAVE_BLUE_SEEKBAR = "BlueSeekBar"

class MainActivity : AppCompatActivity() {
    private lateinit var resetButton: Button
    private lateinit var yellowButton: Button
    private lateinit var pinkButton: Button
    private lateinit var cyanButton: Button
    private lateinit var colorView: View
    lateinit var redSwitch: Switch
    lateinit var greenSwitch: Switch
    lateinit var blueSwitch: Switch
    lateinit var redSeekBar: SeekBar
    lateinit var blueSeekBar: SeekBar
    lateinit var greenSeekBar: SeekBar
    lateinit var redEditText: EditText
    lateinit var greenEditText: EditText
    lateinit var blueEditText: EditText
    private var rescaledValue = 0.01
    private var newValue: Double = 0.00
    private var startNum = 0
    private var endNum = 0
    var colorRed = 0
    var colorBlue = 0
    var colorGreen = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectViewPointers()
        resetButtonCallback()
        redSwitchCallback()
        blueSwitchCallback()
        greenSwitchCallback()
        redSeekBarCallback()
        greenSeekBarCallback()
        blueSeekBarCallback()
        redEditTextCallback()
        greenEditTextCallback()
        blueEditTextCallback()
        Log.d(TAG, "onCreate(Bundle?) called")
        redSeekBar.isEnabled = false
        greenSeekBar.isEnabled = false
        blueSeekBar.isEnabled = false
        redEditText.isEnabled = false
        blueEditText.isEnabled = false
        greenEditText.isEnabled = false
        viewModel.loadState(this)
        viewModel.loadRedSeekBarValue()
        viewModel.loadGreenSeekBarValue()
        viewModel.loadBlueSeekBarValue()
        redSeekBar.progress = viewModel.getRedSeekBarValue()
        greenSeekBar.progress = viewModel.getGreenSeekBarValue()
        blueSeekBar.progress = viewModel.getBlueSeekBarValue()
        colorRed = viewModel.getRedSeekBarValue()
        colorBlue = viewModel.getBlueSeekBarValue()
        colorGreen = viewModel.getGreenSeekBarValue()
        viewModel.setRedSeekBarState(
            savedInstanceState?.getInt(SAVE_RED_SEEKBAR, 0) ?: 0
        )
        viewModel.setGreenSeekBarState(
            savedInstanceState?.getInt(SAVE_GREEN_SEEKBAR, 0) ?: 0
        )
        viewModel.setBlueSeekBarState(
            savedInstanceState?.getInt(SAVE_BLUE_SEEKBAR, 0) ?: 0
        )
        if(!redSwitch.isChecked && !greenSwitch.isChecked && !blueSwitch.isChecked) {
            Log.d(TAG, "Inside reset")
            colorView.background = resources.getDrawable(viewModel.resetBackgroundImage())
        }
        colorView.setBackgroundColor(rgb(colorRed, colorGreen, colorBlue))
        Log.d(TAG, "...")
        yellowButton.setOnClickListener {
            yellowColor()
        }
        pinkButton.setOnClickListener {
            pinkColor()
        }
        cyanButton.setOnClickListener {
            cyanColor()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun connectViewPointers() {
        resetButton = this.findViewById(R.id.reset_button)
        yellowButton = this.findViewById(R.id.yellow_Button)
        pinkButton = this.findViewById(R.id.pink_Button)
        cyanButton = this.findViewById(R.id.cyan_Button)
        colorView = this.findViewById(R.id.color_view)
        redSwitch = this.findViewById(R.id.red_switch)
        redSeekBar = this.findViewById(R.id.red_seekBar)
        redEditText = this.findViewById(R.id.red_editTextNumberDecimal)
        greenSwitch = this.findViewById(R.id.green_switch)
        greenSeekBar = this.findViewById(R.id.green_seekBar)
        greenEditText = this.findViewById(R.id.green_editTextNumberDecimal)
        blueSwitch = this.findViewById(R.id.blue_switch)
        blueSeekBar = this.findViewById(R.id.blue_seekBar)
        blueEditText = this.findViewById(R.id.blue_editTextNumberDecimal)

    }

    private fun resetButtonCallback() {
        resetButton.setOnClickListener {
            if(redSwitch.isChecked) {
                redSwitch.toggle()
                viewModel.setRedSwitchState(false)
            }
            if(greenSwitch.isChecked) {
                greenSwitch.toggle()
                viewModel.setGreenSwitchState(false)
            }
            if(blueSwitch.isChecked) {
                blueSwitch.toggle()
                viewModel.setBlueSwitchState(false)
            }
            viewModel.setRedSeekBarState(0)
            viewModel.setGreenSeekBarState(0)
            viewModel.setBlueSeekBarState(0)
            redEditText.setText("")
            greenEditText.setText("")
            blueEditText.setText("")
            redSeekBar.isEnabled = false
            redEditText.isEnabled = false
            greenSeekBar.isEnabled = false
            greenEditText.isEnabled = false
            blueSeekBar.isEnabled = false
            blueEditText.isEnabled = false
            colorView.background = resources.getDrawable(viewModel.resetBackgroundImage())
        }
    }

    private fun redSwitchCallback() {
        redSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                viewModel.setRedSwitchState(true)
                redSeekBar.isEnabled = true
                redEditText.isEnabled = true
                if(redSeekBar.progress == 0)
                    redEditText.setText("0.0")
                val getRedColorValue = redEditText.text
                if(getRedColorValue!=null && getRedColorValue.isNotEmpty())
                    colorRed = viewModel.convertEditTextColorValue(getRedColorValue)
            } else {
                viewModel.setRedSwitchState(false)
                redSeekBar.isEnabled = false
                redEditText.isEnabled = false
                viewModel.setRedSeekBarState(0)
                colorRed = viewModel.getRedSeekBarValue()
            }
            colorView.setBackgroundColor(rgb(colorRed, colorGreen, colorBlue))
        }
    }

    private fun greenSwitchCallback() {
        greenSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                viewModel.setGreenSwitchState(true)
                greenSeekBar.isEnabled = true
                greenEditText.isEnabled = true
                if(greenSeekBar.progress == 0)
                    greenEditText.setText("0.0")
                val getGreenColorValue = greenEditText.text
                if(getGreenColorValue!=null && getGreenColorValue.isNotEmpty())
                    colorGreen = viewModel.convertEditTextColorValue(getGreenColorValue)
            } else {
                viewModel.setGreenSwitchState(false)
                greenSeekBar.isEnabled = false
                greenEditText.isEnabled = false
                colorGreen = 0
            }
            colorView.setBackgroundColor(rgb(colorRed, colorGreen, colorBlue))
        }
    }

    private fun blueSwitchCallback() {
        blueSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                viewModel.setBlueSwitchState(true)
                blueSeekBar.isEnabled = true
                blueEditText.isEnabled = true
                if(blueSeekBar.progress == 0)
                    blueEditText.setText("0.0")
                val getBlueColorValue = blueEditText.text
                if(getBlueColorValue!=null && getBlueColorValue.isNotEmpty())
                    colorBlue = viewModel.convertEditTextColorValue(getBlueColorValue)
            } else {
                viewModel.setBlueSwitchState(false)
                blueSeekBar.isEnabled = false
                blueEditText.isEnabled = false
                colorBlue = 0
            }
            colorView.setBackgroundColor(rgb(colorRed, colorGreen, colorBlue))
        }
    }

    private fun redSeekBarCallback() {
        redSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                newValue = p1 * rescaledValue
                val roundOffValue = (newValue * 1000.0).roundToInt() / 1000.0
                redEditText.setText(roundOffValue.toString())
                colorRed = p1
                val color1 = Color.rgb((255*colorRed),(255*colorGreen),(255*colorBlue))
                colorView.setBackgroundColor(color1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                startNum = redSeekBar.progress
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                endNum = redSeekBar.progress
            }
        })
    }

    private fun greenSeekBarCallback() {
        greenSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                newValue = p1*rescaledValue
                val roundOffValue = (newValue * 1000.0).roundToInt() / 1000.0
                greenEditText.setText(roundOffValue.toString())
                colorGreen = p1
                val color1 = Color.rgb((255*colorRed),(255*colorGreen),(255*colorBlue))
                colorView.setBackgroundColor(color1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                startNum = greenSeekBar.progress
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                endNum = greenSeekBar.progress
            }
        })
    }

    private fun blueSeekBarCallback() {
        blueSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                newValue = p1*rescaledValue
                val roundOffValue = (newValue * 1000.0).roundToInt() / 1000.0
                blueEditText.setText(roundOffValue.toString())
                colorBlue = p1
                val color1 = Color.rgb((255*colorRed),(255*colorGreen),(255*colorBlue))
                colorView.setBackgroundColor(color1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                startNum = blueSeekBar.progress
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                endNum = blueSeekBar.progress
            }
        })
    }

    private fun redEditTextCallback() {
        redEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                redEditText.filters = arrayOf(InputFilter.LengthFilter(5))
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.isNotEmpty()){
                    viewModel.setRedSeekBarState(viewModel.convertEditTextColorValue(s))
                }
                colorView.setBackgroundColor(rgb(colorRed, colorGreen, colorBlue))
            }
        })
    }

    private fun blueEditTextCallback() {
        blueEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                blueEditText.filters = arrayOf(InputFilter.LengthFilter(5))

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.isNotEmpty()){
                    viewModel.setBlueSeekBarState(viewModel.convertEditTextColorValue(s))
                }
                colorView.setBackgroundColor(rgb(colorRed, colorGreen, colorBlue))
            }
        })
    }

    private fun greenEditTextCallback() {
        greenEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                greenEditText.filters = arrayOf(InputFilter.LengthFilter(5))
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.isNotEmpty()){
                    viewModel.setGreenSeekBarState(viewModel.convertEditTextColorValue(s))
                }
                colorView.setBackgroundColor(rgb(colorRed, colorGreen, colorBlue))
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "The counter value is saved")
        outState.putBoolean(SAVE_RED_SWITCH, viewModel.getRedSwitchState())
        outState.putBoolean(SAVE_GREEN_SWITCH, viewModel.getGreenSwitchState())
        outState.putBoolean(SAVE_BLUE_SWITCH, viewModel.getBlueSwitchState())
        outState.putInt(SAVE_RED_SEEKBAR, viewModel.getRedSeekBarValue())
        outState.putInt(SAVE_GREEN_SEEKBAR, viewModel.getGreenSeekBarValue())
        outState.putInt(SAVE_BLUE_SEEKBAR, viewModel.getBlueSeekBarValue())
    }

    private val viewModel: ColorViewModel by lazy {
        PreferencesRepository.initialize(this)
        ViewModelProvider(this)[ColorViewModel::class.java]
    }
    private fun updateColor() {
        var red = if (redSwitch.isChecked) redSeekBar.progress / 100.0 else 0.0
        var green = if (greenSwitch.isChecked) greenSeekBar.progress / 100.0 else 0.0
        var blue = if (blueSwitch.isChecked) blueSeekBar.progress / 100.0 else 0.0

        val color = Color.rgb(
            (255 * red).toInt(),
            (255 * green).toInt(),
            (255 * blue).toInt()
        )
        colorView.setBackgroundColor(color)

    }
    // Function for yellow button to produce yellow color
    private fun yellowColor(){
        redSwitch.isChecked = true
        greenSwitch.isChecked = true
        blueSwitch.isChecked = false
        redSeekBar.progress = 100
        greenSeekBar.progress = 100
        blueSeekBar.progress = 0
        redEditText.setText("1")
        greenEditText.setText("1")
        blueEditText.setText("0")
        updateColor()
    }
    // Function for pink button to produce pink color
    private fun pinkColor(){
        redSwitch.isChecked = true
        greenSwitch.isChecked = false
        blueSwitch.isChecked = true
        redSeekBar.progress = 100
        greenSeekBar.progress = 0
        blueSeekBar.progress = 100
        redEditText.setText("1")
        greenEditText.setText("0")
        blueEditText.setText("1")
        updateColor()
    }
    // Function for cyan button to produce cyan color
    private fun cyanColor(){
        redSwitch.isChecked = false
        greenSwitch.isChecked = true
        blueSwitch.isChecked = true
        redSeekBar.progress = 0
        greenSeekBar.progress = 100
        blueSeekBar.progress = 100
        redEditText.setText("0")
        greenEditText.setText("1")
        blueEditText.setText("1")
        updateColor()
    }
}




