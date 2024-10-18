package com.example.mob3;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_NAME = "appPreferences";
    private static final String KEY_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_main);

        Button btnClicker = findViewById(R.id.btnClicker);
        Button btnTicTacToe = findViewById(R.id.btnTicTacToe);
        ImageView btnChangeTheme = findViewById(R.id.imgChangeTheme);


        btnClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Clicker.class));
            }
        });

        btnTicTacToe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Krestiki_noliki.class));
            }
        });

        btnChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppTheme();
            }
        });

    }

    // Устанавливаем тему приложения в зависимости от сохраненного значения
    private void setAppTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        boolean darkTheme = prefs.getBoolean(KEY_THEME, false);
        if (darkTheme) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Light);
        }
    }

    // Меняем тему и сохраняем выбор в SharedPreferences
    private void changeAppTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        boolean darkTheme = prefs.getBoolean(KEY_THEME, false);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_THEME, !darkTheme);
        editor.apply();
        recreate();  // Перезагружаем активность для применения новой темы
    }
}