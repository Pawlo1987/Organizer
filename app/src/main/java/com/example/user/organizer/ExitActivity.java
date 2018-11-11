package com.example.user.organizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ExitActivity extends AppCompatActivity {

    final String ID_EXIT_DIALOG = "dialogExitConfirm";
    private ExitConfirmDialog exitDialog; // диалог подтверждения выхода из приложения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        exitDialog = new ExitConfirmDialog();

        exitDialog.show(getSupportFragmentManager(), ID_EXIT_DIALOG);

    }
}
