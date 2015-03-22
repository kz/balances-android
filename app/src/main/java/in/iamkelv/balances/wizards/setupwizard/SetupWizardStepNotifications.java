package in.iamkelv.balances.wizards.setupwizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.codepond.wizardroid.WizardStep;

import in.iamkelv.balances.PreferencesModel;
import in.iamkelv.balances.R;

public class SetupWizardStepNotifications extends WizardStep {

    public SetupWizardStepNotifications() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_wizard_step_notifications, container, false);

        final CheckBox chkNotificationsEnable = (CheckBox) v.findViewById(R.id.chkNotificationsEnable);
        final RelativeLayout notificationsLayout = (RelativeLayout) v.findViewById(R.id.notificationsLayout);
        final SeekBar sbLunch = (SeekBar) v.findViewById(R.id.sbLunch);
        final SeekBar sbTuck = (SeekBar) v.findViewById(R.id.sbTuck);
        final TextView txtLunchPound = (TextView) v.findViewById(R.id.txtLunchPound);
        final TextView txtTuckPound = (TextView) v.findViewById(R.id.txtTuckPound);

        // Set default values of strings
        txtLunchPound.setText(getString(R.string.pound_sign) + sbLunch.getProgress());
        txtTuckPound.setText(getString(R.string.pound_sign) + sbTuck.getProgress());

        // Set default values in preferences
        PreferencesModel preferences = new PreferencesModel(getActivity());
        preferences.setNotificationState(false);
        preferences.setLunchThreshold(10);
        preferences.setTuckThreshold(10);
        preferences.setNotificationHours(18);
        preferences.setNotificationMinutes(0);

        chkNotificationsEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    fadeIn(notificationsLayout);
                    PreferencesModel preferences = new PreferencesModel(getActivity());
                    preferences.setNotificationState(true);
                } else {
                    fadeOut(notificationsLayout);
                    PreferencesModel preferences = new PreferencesModel(getActivity());
                    preferences.setNotificationState(false);
                }
            }
        });

        sbLunch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    txtLunchPound.setText("Disabled");
                } else {
                    txtLunchPound.setText(getString(R.string.pound_sign) + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PreferencesModel preferences = new PreferencesModel(getActivity());
                preferences.setLunchThreshold(seekBar.getProgress());
            }
        });

        sbTuck.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    txtTuckPound.setText("Disabled");
                } else {
                    txtTuckPound.setText(getString(R.string.pound_sign) + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PreferencesModel preferences = new PreferencesModel(getActivity());
                preferences.setTuckThreshold(seekBar.getProgress());
            }
        });

        return v;
    }

    private void fadeIn(final View view) {
        final AlphaAnimation fadeOutAnimation = new AlphaAnimation(0.0F,  1.0F);
        fadeOutAnimation.setDuration(100);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(fadeOutAnimation);
    }

    private void fadeOut(final View view) {
        final AlphaAnimation fadeOutAnimation = new AlphaAnimation(1.0F,  0.0F);
        fadeOutAnimation.setDuration(100);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }
        });
        view.startAnimation(fadeOutAnimation);
    }

}
