package com.example.mob3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;

public class Krestiki_noliki extends AppCompatActivity {

    private int winCountX = 0;
    private int winCountO = 0;
    private TextView winCounterX;
    private TextView winCounterO;
    private Button[][] buttons = new Button[3][3];
    private String currentPlayer = "X"; // Начинаем с "X"
    private HashSet<Integer> filledPositions = new HashSet<>();

    private static final String PREFERENCES_NAME = "appPreferences";
    private static final String KEY_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme(); // Устанавливаем тему до вызова setContentView
        setContentView(R.layout.activity_krestiki_noliki);

        winCounterX = findViewById(R.id.win_counter_x);
        winCounterO = findViewById(R.id.win_counter_o);
        GridLayout grid = findViewById(R.id.game_grid);

        // Получаем кнопки в массив
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = (Button) grid.getChildAt(i * 3 + j);
                buttons[i][j].setOnClickListener(view -> onButtonClick((Button) view));
                buttons[i][j].setTag((i * 3 + j) + 1);
            }
        }

        // Установка слушателя для смены темы
        ImageView imgChangeTheme = findViewById(R.id.imgChangeTheme);
        imgChangeTheme.setOnClickListener(view -> changeAppTheme());

        // Восстанавливаем состояние игры
        loadGameState();
    }

    private void onButtonClick(Button button) {
        if (!button.getText().toString().isEmpty()) {
            return; // Игнорируем клик, если кнопка уже нажата
        }

        button.setText(currentPlayer);
        filledPositions.add(Integer.parseInt(button.getTag().toString()));

        if (checkWin()) {
            if (currentPlayer.equals("X")) {
                winCountX++;
                winCounterX.setText("Wins X: " + winCountX);
            } else {
                winCountO++;
                winCounterO.setText("Wins O: " + winCountO);
            }
            resetBoard();
        } else if (filledPositions.size() == 9) {
            resetBoard(); // Сбрасываем при ничьей
        } else {
            currentPlayer = currentPlayer.equals("X") ? "O" : "X"; // Смена игрока
        }
    }

    private boolean checkWin() {
        // Логика проверки победителя
        for (int i = 0; i < 3; i++) {
            if ((buttons[i][0].getText().toString().equals(currentPlayer) &&
                    buttons[i][1].getText().toString().equals(currentPlayer) &&
                    buttons[i][2].getText().toString().equals(currentPlayer)) ||
                    (buttons[0][i].getText().toString().equals(currentPlayer) &&
                            buttons[1][i].getText().toString().equals(currentPlayer) &&
                            buttons[2][i].getText().toString().equals(currentPlayer))) {
                return true;
            }
        }
        return (buttons[0][0].getText().toString().equals(currentPlayer) &&
                buttons[1][1].getText().toString().equals(currentPlayer) &&
                buttons[2][2].getText().toString().equals(currentPlayer)) ||
                (buttons[0][2].getText().toString().equals(currentPlayer) &&
                        buttons[1][1].getText().toString().equals(currentPlayer) &&
                        buttons[2][0].getText().toString().equals(currentPlayer));
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        filledPositions.clear();
        currentPlayer = "X"; // Сброс к игроку X
    }

    private void setAppTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        boolean darkTheme = prefs.getBoolean(KEY_THEME, false);
        if (darkTheme) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Light);
        }
    }

    private void changeAppTheme() {
        saveGameState(); // Сохраняем состояние перед сменой темы
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        boolean darkTheme = prefs.getBoolean(KEY_THEME, false);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_THEME, !darkTheme);
        editor.apply();

        // Перезагрузка активности для применения новой темы
        recreate();
    }

    private void saveGameState() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("winCountX", winCountX);
        editor.putInt("winCountO", winCountO);

        // Сохранение состояния кнопок
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                editor.putString("button_" + i + "_" + j, buttons[i][j].getText().toString());
            }
        }
        editor.apply();
    }

    private void loadGameState() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        winCountX = prefs.getInt("winCountX", 0);
        winCountO = prefs.getInt("winCountO", 0);

        // Восстанавливаем состояние кнопок
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonText = prefs.getString("button_" + i + "_" + j, "");
                buttons[i][j].setText(buttonText);
            }
        }
        updateUI();  // Обновляем интерфейс
    }

    private void updateUI() {
        winCounterX.setText("Wins X: " + winCountX);
        winCounterO.setText("Wins O: " + winCountO);
    }
}