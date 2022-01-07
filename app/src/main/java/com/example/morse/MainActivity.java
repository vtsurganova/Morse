package com.example.morse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import java.lang.Thread;


import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //region Nearby
    int nrbPoint = 100;
    int nrbDash = 300;
    int nrbPause = 300;
    int nrbSplit = 700;
    //endregion

    int avgPoint = 200;
    int avgDash = 600;
    int avgPause = 600;
    int avgSplit = 1400;

    int farPoint = 400;
    int farDash = 1200;
    int farPause = 1200;
    int farSplit = 2400;

    int elemPause;

    int speed;
    int pointPause;
    int dashPause;


    CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        Vibrator vibro = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        EditText normalText = (EditText) findViewById(R.id.editText);
        TextView morseCode = (TextView) findViewById(R.id.morseCode);
        TextView error = (TextView) findViewById(R.id.errorText);
        CheckBox checkFlash = (CheckBox) findViewById(R.id.flashBox);
        CheckBox checkVibro= (CheckBox) findViewById(R.id.vibroBox);
        Button start = (Button) findViewById(R.id.startButton);
        Button reset = (Button) findViewById(R.id.resetButton);

        RadioButton nrbDist = (RadioButton) findViewById(R.id.nrbRadio);
        RadioButton avgDist = (RadioButton) findViewById(R.id.avgRadio);
        RadioButton farDist = (RadioButton) findViewById(R.id.farRadio);

        char[] letters = { ' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        String[] morseLetters = { " ", ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----", "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.", "-----", ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.." };


        normalText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String textToChange = "";
                String newText = "";
                error.setText("");

                textToChange = normalText.getText().toString();
                textToChange = textToChange.toLowerCase(Locale.ROOT);

                for (int i = 0; i < textToChange.length(); i++)
                {
                    for (short j = 0; j < 63; j++)
                    {
                        if (textToChange.charAt(i) == letters[j])
                        {
                            newText += morseLetters[j];
                            newText += " ";
                            morseCode.setText(newText);
                        }
                    }
                }
                morseCode.setText(newText);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                error.setText("");

                String textToChange = "";
                String newText = "";

                textToChange = normalText.getText().toString();
                textToChange = textToChange.toLowerCase(Locale.ROOT);

                for (int i = 0; i < textToChange.length(); i++)
                {
                    for (short j = 0; j < 63; j++)
                    {
                        if (textToChange.charAt(i) == letters[j])
                        {
                            newText += morseLetters[j];
                            newText += " ";
                        }
                    }
                }


                if (checkFlash.isChecked() & !checkVibro.isChecked())
                {
                    if (nrbDist.isChecked() || avgDist.isChecked() || farDist.isChecked())
                    {
                        if (textToChange.matches(".*\\w.*"))
                        {
                            for (char c : newText.toCharArray())
                            {
                                if (farDist.isChecked())
                                {
                                    speed = farPoint;
                                    elemPause = speed;
                                }
                                else if (avgDist.isChecked())
                                {
                                    speed = avgPoint;
                                    elemPause = speed;
                                }
                                else if (nrbDist.isChecked())
                                {
                                    speed = nrbPoint;
                                    elemPause = speed;
                                }


                                if (c == '.')
                                {
                                    if (farDist.isChecked())
                                    {
                                        speed = farPoint;
                                    }
                                    else if (avgDist.isChecked())
                                    {
                                        speed = avgPoint;
                                    }
                                    else if (nrbDist.isChecked())
                                    {
                                        speed = nrbPoint;
                                    }

                                    try {
                                        cameraManager.setTorchMode("0", true);
                                        Thread.sleep(speed);
                                        cameraManager.setTorchMode("0", false);
                                        Thread.sleep(elemPause);
                                    } catch (CameraAccessException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if (c == '-')
                                {
                                    if (farDist.isChecked())
                                    {
                                        speed = farDash;
                                    }
                                    else if (avgDist.isChecked())
                                    {
                                        speed = avgDash;
                                    }
                                    else if (nrbDist.isChecked())
                                    {
                                        speed = nrbDash;
                                    }

                                    try {
                                        cameraManager.setTorchMode("0", true);
                                        Thread.sleep(speed);
                                        cameraManager.setTorchMode("0", false);
                                        Thread.sleep(elemPause);
                                    } catch (CameraAccessException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if (c == ' ')
                                {
                                    if (farDist.isChecked())
                                    {
                                        speed = farPause;
                                    }
                                    else if (avgDist.isChecked())
                                    {
                                        speed = avgPause;
                                    }
                                    else if (nrbDist.isChecked())
                                    {
                                        speed = nrbPause;
                                    }

                                    if (c == ' ' + ' ')
                                    {
                                        if (farDist.isChecked())
                                        {
                                            speed = farSplit;
                                        }
                                        else if (avgDist.isChecked())
                                        {
                                            speed = avgSplit;
                                        }
                                        else if (nrbDist.isChecked())
                                        {
                                            speed = nrbSplit;
                                        }
                                    }

                                    try {
                                        Thread.sleep(speed);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        else
                        {
                            error.setText("ERROR: Write some text!!");
                        }
                    }
                    else
                    {
                        error.setText("ERROR: Choose distance!");
                    }
                }
                else if (!checkFlash.isChecked() & checkVibro.isChecked())
                {
                    if (nrbDist.isChecked() || avgDist.isChecked() || farDist.isChecked())
                    {
                        if (textToChange.matches(".*\\w.*"))
                        {
                            for (char c : newText.toCharArray())
                            {
                                if (farDist.isChecked())
                                {
                                    speed = farPoint;
                                    elemPause = speed;
                                }
                                else if (avgDist.isChecked())
                                {
                                    speed = avgPoint;
                                    elemPause = speed;
                                }
                                else if (nrbDist.isChecked())
                                {
                                    speed = nrbPoint;
                                    elemPause = speed;
                                }


                                if (c == '.')
                                {
                                    //region Point
                                    if (farDist.isChecked())
                                    {
                                        speed = farPoint;
                                        pointPause = farPoint;
                                    }
                                    else if (avgDist.isChecked())
                                    {
                                        speed = avgPoint;
                                        pointPause = avgPoint;
                                    }
                                    else if (nrbDist.isChecked())
                                    {
                                        speed = nrbPoint;
                                        pointPause = nrbPoint;
                                    }
                                    //endregion

                                    vibro.vibrate(VibrationEffect.createOneShot(speed, VibrationEffect.DEFAULT_AMPLITUDE));
                                    try {
                                        Thread.sleep(speed);
                                        Thread.sleep(elemPause);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else if (c == '-')
                                {
                                    //region Dash
                                    if (farDist.isChecked())
                                    {
                                        speed = farDash;
                                        dashPause = farDash;
                                    }
                                    else if (avgDist.isChecked())
                                    {
                                        speed = avgDash;
                                        dashPause = avgDash;
                                    }
                                    else if (nrbDist.isChecked())
                                    {
                                        speed = nrbDash;
                                        dashPause = nrbDash;
                                    }
                                    //endregion

                                    vibro.vibrate(VibrationEffect.createOneShot(speed, VibrationEffect.DEFAULT_AMPLITUDE));
                                    try {
                                        Thread.sleep(speed);
                                        Thread.sleep(elemPause);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if (c == ' ')
                                {
                                    //region Pause
                                    if (farDist.isChecked())
                                    {
                                        speed = farPause;
                                    }
                                    else if (avgDist.isChecked())
                                    {
                                        speed = avgPause;
                                    }
                                    else if (nrbDist.isChecked())
                                    {
                                        speed = nrbPause;
                                    }
                                    //endregion

                                    if (c == ' ' + ' ')
                                    {
                                        if (farDist.isChecked())
                                        {
                                            speed = farSplit;
                                        }
                                        else if (avgDist.isChecked())
                                        {
                                            speed = avgSplit;
                                        }
                                        else if (nrbDist.isChecked())
                                        {
                                            speed = nrbSplit;
                                        }
                                    }

                                    try {
                                        Thread.sleep(speed);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        else
                        {
                            error.setText("ERROR: Write some text!!");
                        }
                    }
                    else
                    {
                        error.setText("ERROR: Choose distance!");
                    }
                }
                else if (checkFlash.isChecked() & checkVibro.isChecked())
                {
                    if (nrbDist.isChecked() || avgDist.isChecked() || farDist.isChecked())
                    {
                        if (textToChange.matches(".*\\w.*"))
                        {
                            for (char c : newText.toCharArray())
                            {
                                if (farDist.isChecked())
                                {
                                    speed = farPoint;
                                    elemPause = speed;
                                }
                                else if (avgDist.isChecked())
                                {
                                    speed = avgPoint;
                                    elemPause = speed;
                                }
                                else if (nrbDist.isChecked())
                                {
                                    speed = nrbPoint;
                                    elemPause = speed;
                                }


                                if (c == '.')
                                {
                                    //region Point
                                    if (farDist.isChecked())
                                    {
                                        speed = farPoint;
                                        pointPause = farPoint;
                                    }
                                    else if (avgDist.isChecked())
                                    {
                                        speed = avgPoint;
                                        pointPause = avgPoint;
                                    }
                                    else if (nrbDist.isChecked())
                                    {
                                        speed = nrbPoint;
                                        pointPause = nrbPoint;
                                    }
                                    //endregion

                                    try {
                                        cameraManager.setTorchMode("0", true);
                                        vibro.vibrate(VibrationEffect.createOneShot(speed, VibrationEffect.DEFAULT_AMPLITUDE));
                                        Thread.sleep(speed);
                                        cameraManager.setTorchMode("0", false);
                                        Thread.sleep(elemPause);
                                    } catch (CameraAccessException | InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else if (c == '-')
                                {
                                    //region Dash
                                    if (farDist.isChecked())
                                    {
                                        speed = farDash;
                                        dashPause = farDash;
                                    }
                                    else if (avgDist.isChecked())
                                    {
                                        speed = avgDash;
                                        dashPause = avgDash;
                                    }
                                    else if (nrbDist.isChecked())
                                    {
                                        speed = nrbDash;
                                        dashPause = nrbDash;
                                    }
                                    //endregion

                                    try {
                                        cameraManager.setTorchMode("0", true);
                                        vibro.vibrate(VibrationEffect.createOneShot(speed, VibrationEffect.DEFAULT_AMPLITUDE));
                                        Thread.sleep(speed);
                                        cameraManager.setTorchMode("0", false);
                                        Thread.sleep(elemPause);
                                    } catch (CameraAccessException | InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else if (c == ' ')
                                {
                                    //region Pause
                                    if (farDist.isChecked())
                                    {
                                        speed = farPause;
                                    }
                                    else if (avgDist.isChecked())
                                    {
                                        speed = avgPause;
                                    }
                                    else if (nrbDist.isChecked())
                                    {
                                        speed = nrbPause;
                                    }
                                    //endregion

                                    if (c == ' ' + ' ')
                                    {
                                        if (farDist.isChecked())
                                        {
                                            speed = farSplit;
                                        }
                                        else if (avgDist.isChecked())
                                        {
                                            speed = avgSplit;
                                        }
                                        else if (nrbDist.isChecked())
                                        {
                                            speed = nrbSplit;
                                        }
                                    }

                                    try {
                                        Thread.sleep(speed);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                        else
                        {
                            error.setText("ERROR: Write some text!!");
                        }
                    }
                    else
                    {
                        error.setText("ERROR: Choose distance!");
                    }
                }
                else if (!checkFlash.isChecked() & !checkVibro.isChecked() )
                {
                    error.setText("ERROR: Choose mode!");
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                normalText.setText("");
                morseCode.setText("");

                checkFlash.setChecked(false);
                checkVibro.setChecked(false);

                nrbDist.setChecked(false);
                avgDist.setChecked(false);
                farDist.setChecked(false);

            }
        });


    }
}