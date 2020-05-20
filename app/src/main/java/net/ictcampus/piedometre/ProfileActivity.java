package net.ictcampus.piedometre;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {

    FloatingActionButton edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        edit = findViewById(R.id.floatingActionButtonEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog();
            }
        });
    }

    public void openEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set up the buttons (and a title), on Cancel dismiss()
        builder.setTitle(R.string.EditDialogTitle).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // call the method to read from the input and update the current information in the profile
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


/*
    // I'm using fragment here so I'm using getView() to provide ViewGroup
    // but you can provide here any other instance of ViewGroup from your Fragment / Activity
    View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_inpu_password, (ViewGroup) getView(), false);
    // Set up the input
    final EditText input = (EditText) viewInflated.findViewById(R.id.input);
    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    builder.setView(viewInflated);

    // Set up the buttons


    builder.show();
*/
}
