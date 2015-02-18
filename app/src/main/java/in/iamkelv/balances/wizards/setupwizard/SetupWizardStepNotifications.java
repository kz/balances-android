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

import org.codepond.wizardroid.WizardStep;

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

        chkNotificationsEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    fadeIn(notificationsLayout);
                } else {
                    fadeOut(notificationsLayout);
                }
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
