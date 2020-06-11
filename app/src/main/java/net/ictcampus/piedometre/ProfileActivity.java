package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * <h3> Profile Activity </h3>
 * It shows the last entered profile information of the user
 *
 * extends the AppCompatActivity and therefore overrides the methods of the Android lifecycle
 *
 * @author doriera & luetolfre
 * @version 1.0
 * @since 2020-06-11
 */
public class ProfileActivity extends AppCompatActivity {


    /**
     * variables are used to get values out of the edit dialog and stored in the SharedPreferences
     */
    private static final String PROFILE_SHARED_PREFS = "profile";
    private static final String NAME = "name";
    private static final String WEIGHT = "weight";
    private static final String STEP_LENGTH = "stepLength";
    private static final String DAILY_STEPS = "dailySteps";



    /**
     * onCreate starts the activity_profile to display the information of the use
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // get the TextViews from here
        updateTextView();

        // the button to start editing information
        FloatingActionButton edit = findViewById(R.id.floatingActionButtonEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog();
            }
        });
    }

    /**
     * due to the onClick on the floating action button, the AlertDialog gets called to display the text input
     */
    private void openEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // input fields
        final EditText inputName = new EditText(this);
        final EditText inputWeight = new EditText(this);
        final EditText inputStepLength = new EditText(this);
        final EditText inputDailySteps = new EditText(this);


        // Set up the buttons (and a title), on Cancel dismiss()
        builder.setTitle(R.string.EditDialogTitle).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // puts the SharedPreferences profile onto the editor
                SharedPreferences profile = getSharedPreferences(PROFILE_SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = profile.edit();

                // read from the input and update the current information in the SharedPreferences profile
                editor.putString(NAME, inputName.getText().toString());
                editor.putString(WEIGHT, inputWeight.getText().toString());
                editor.putString(STEP_LENGTH, inputStepLength.getText().toString());
                editor.putString(DAILY_STEPS, inputDailySteps.getText().toString());
                editor.apply();

                // handle the stored information in method updateTextView to display them
                updateTextView();
                // with updated activity_profile, the dialog can get closed
                dialog.cancel();

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            /**
             * onClick cancel, Dialog dismisses
             * @param dialog DialogInterface
             * @param which int
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // instance to access SharedPreferences
        SharedPreferences profile = getSharedPreferences(PROFILE_SHARED_PREFS, Context.MODE_PRIVATE);

        // linear layout to set the inputs
        final LinearLayout inputs = new LinearLayout(this);
        inputs.setOrientation(LinearLayout.VERTICAL);

        // Set up the text input field for the TextInput to change profile information

        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        inputName.setText(profile.getString(NAME, "Your Name"));
        inputName.setHint("Name or Nickname");
        inputs.addView(inputName);

        inputWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputWeight.setText(profile.getString(WEIGHT, "Weight in kg"));
        inputWeight.setHint("Weight in kg");
        inputs.addView(inputWeight);

        inputStepLength.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputStepLength.setText(profile.getString(STEP_LENGTH, "Step length in cm"));
        inputStepLength.setHint("Step length in cm");
        inputs.addView(inputStepLength);

        inputDailySteps.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputDailySteps.setText(profile.getString(DAILY_STEPS, "Steps a Day"));
        inputDailySteps.setHint("Steps a Day");
        inputs.addView(inputDailySteps);

        builder.setView(inputs);
        builder.show();
    }

    /**
     * update is responsible for setting the new values from input into the activity_profile text views
     */
    private void updateTextView() {
        // instance of SharedPreferences
        SharedPreferences profile = getSharedPreferences(PROFILE_SHARED_PREFS, Context.MODE_PRIVATE);

        // look for text views which should be updated
        TextView nameView = findViewById(R.id.textViewName);
        TextView weightView = findViewById(R.id.textViewWeight);
        TextView stepLengthView = findViewById(R.id.textViewStepLength);
        TextView dailyStepsView = findViewById(R.id.textViewDailySteps);

        // set the input values to the text view in activity_profile
        nameView.setText(profile.getString(NAME, "NAME"));
        weightView.setText(profile.getString(WEIGHT, "70"));
        stepLengthView.setText(profile.getString(STEP_LENGTH, "70"));
        dailyStepsView.setText(profile.getString(DAILY_STEPS, "2000"));
    }
}
