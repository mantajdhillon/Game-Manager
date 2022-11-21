package ca.cmpt276.neon_coopachievement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Achievement Fragment
 * <p>
 * - Used to create AlertDialog with photos and animation
 * - Returns an alert dialog from a dialog builder with
 *   text and animation, inflated from achievement_layout
 */

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
        YoYo.with(Techniques.Tada).duration(500).repeat(YoYo.INFINITE).playOn(v);

        AlertDialog.Builder achievementBuilder = new AlertDialog.Builder(getActivity());
        achievementBuilder.setPositiveButton(android.R.string.ok, listener);
        achievementBuilder.setView(v);
        achievementBuilder.setTitle(R.string.great_job);
        String message = getString(R.string.your_rank) + " " + GameConfigActivity.achievement;
        achievementBuilder.setMessage(message);

        AlertDialog achievementDialog = achievementBuilder.create();

        return achievementDialog;
    }

}
