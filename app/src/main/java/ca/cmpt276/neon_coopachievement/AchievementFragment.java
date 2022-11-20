package ca.cmpt276.neon_coopachievement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AchievementFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Create the view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.achievement_layout, null);

        // Create a button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(which == DialogInterface.BUTTON_POSITIVE){
                    getActivity().finish();
                }
            }
        };

        // Return alert dialog
        AlertDialog.Builder achievementBuilder = new AlertDialog.Builder(getActivity());
        achievementBuilder.setPositiveButton(android.R.string.ok, listener);

        AlertDialog achievementDialog = achievementBuilder.create();

        achievementDialog.setTitle("Great job!");
        achievementDialog.setMessage(GameConfigActivity.achievement);
        achievementDialog.setView(v);

        return achievementDialog;
    }
}
