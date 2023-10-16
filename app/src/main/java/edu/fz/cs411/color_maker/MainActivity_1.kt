package edu.fz.cs411.color_maker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.widget.*
import android.view.View

class MainActivity : AppCompatActivity() {
    // Creating variables for Switches
    private lateinit var redSwitch: Switch
    private lateinit var greenSwitch: Switch
    private lateinit var blueSwitch: Switch
    // Creating variables for SeekBars
    private lateinit var redSlider: SeekBar
    private lateinit var greenSlider: SeekBar
    private lateinit var blueSlider: SeekBar
    // Creating variables for Text Boxes
    private lateinit var redValue: EditText
    private lateinit var greenValue: EditText
    private lateinit var blueValue: EditText
    // Creating variable for Color Box
    private lateinit var colorPreview: View
    // Creating variable for Reset Button
    private lateinit var resetButton: Button
    private lateinit var yellowButton: Button
    private lateinit var pinkButton: Button
    private lateinit var cyanButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing the UI elements by using their ID's from activity_main.xml
        // Initializing the Switches
        redSwitch = findViewById(R.id.redSwitch)
        greenSwitch = findViewById(R.id.greenSwitch)
        blueSwitch = findViewById(R.id.blueSwitch)
        // Initializing the SeekBars
        redSlider = findViewById(R.id.redSlider)
        greenSlider = findViewById(R.id.greenSlider)
        blueSlider = findViewById(R.id.blueSlider)
        // Initializing the Text Boxes
        redValue = findViewById(R.id.redValue)
        greenValue = findViewById(R.id.greenValue)
        blueValue = findViewById(R.id.blueValue)
        // Initialing the ColorBox
        colorPreview = findViewById(R.id.colorPreview)
        // Initializing the Reset Button
        resetButton = findViewById(R.id.resetButton)
        yellowButton = findViewById(R.id.yellowButton)
        pinkButton = findViewById(R.id.pinkButton)
        cyanButton = findViewById(R.id.cyanButton)

        // Setting the Switches to be off initially
        redSwitch.isChecked = false
        greenSwitch.isChecked = false
        blueSwitch.isChecked = false
        // Disabling the SeekBars initially
        redSlider.isEnabled = false
        greenSlider.isEnabled = false
        blueSlider.isEnabled = false

        // Setting the Listeners for Switches, SeekBars and Text Boxes
        setSwitchListener(redSwitch, redSlider, redValue)
        setSwitchListener(greenSwitch, greenSlider, greenValue)
        setSwitchListener(blueSwitch, blueSlider, blueValue)

        // Click listener for Reset, yellow, pink and cyan Buttons
        resetButton.setOnClickListener {
            resetControls()
        }
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
    // Function to set listeners for switches, seekBars, and Text Boxes
    private fun setSwitchListener(switch: Switch, slider: SeekBar, value: EditText) {
        // Switch Listener
        switch.setOnCheckedChangeListener { _, isChecked ->
            slider.isEnabled = isChecked
            value.isEnabled = isChecked
            if (!isChecked) {
                slider.progress = 0
                value.setText("0")
            }
            updateColor()
        }
        // SeekBar Listener
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                value.setText((progress / 100.0).toString())
                updateColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        // Text Box Listener
        value.setOnEditorActionListener { _, _, _ ->
            val floatValue = value.text.toString().toFloatOrNull() ?: 0f
            slider.progress = (floatValue * 100).toInt()
            updateColor()
            false
        }
    }
    // Function to reset switches, seekbars and text boxes to initial state
    private fun resetControls() {
        redSwitch.isChecked = false
        greenSwitch.isChecked = false
        blueSwitch.isChecked = false
        redSlider.progress = 0
        greenSlider.progress = 0
        blueSlider.progress = 0
        redValue.setText("0")
        greenValue.setText("0")
        blueValue.setText("0")
        updateColor()
    }
    // Function to update the color box according to the changes made by the user
    private fun updateColor() {
        var red = if (redSwitch.isChecked) redSlider.progress / 100.0 else 0.0
        var green = if (greenSwitch.isChecked) greenSlider.progress / 100.0 else 0.0
        var blue = if (blueSwitch.isChecked) blueSlider.progress / 100.0 else 0.0

        val color = Color.rgb(
            (255 * red).toInt(),
            (255 * green).toInt(),
            (255 * blue).toInt()
        )
        colorPreview.setBackgroundColor(color)

    }
    // Function for yellow button to produce yellow color
    private fun yellowColor(){
        redSwitch.isChecked = true
        greenSwitch.isChecked = true
        blueSwitch.isChecked = false
        redSlider.progress = 100
        greenSlider.progress = 100
        blueSlider.progress = 0
        redValue.setText("1")
        greenValue.setText("1")
        blueValue.setText("0")
        updateColor()
    }
    // Function for pink button to produce pink color
    private fun pinkColor(){
        redSwitch.isChecked = true
        greenSwitch.isChecked = false
        blueSwitch.isChecked = true
        redSlider.progress = 100
        greenSlider.progress = 0
        blueSlider.progress = 100
        redValue.setText("1")
        greenValue.setText("0")
        blueValue.setText("1")
        updateColor()
    }
    // Function for cyan button to produce cyan color
    private fun cyanColor(){
        redSwitch.isChecked = false
        greenSwitch.isChecked = true
        blueSwitch.isChecked = true
        redSlider.progress = 0
        greenSlider.progress = 100
        blueSlider.progress = 100
        redValue.setText("0")
        greenValue.setText("1")
        blueValue.setText("1")
        updateColor()
    }

}



