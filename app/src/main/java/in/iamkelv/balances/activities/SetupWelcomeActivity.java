package in.iamkelv.balances.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;

public class SetupWelcomeActivity extends AppCompatActivity {

    @Bind(R.id.titleTextView)
    TextView titleTextView;
    @Bind(R.id.subtitleTextView)
    TextView subtitleTextView;
    @Bind(R.id.proceedFab)
    FloatingActionButton proceedFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_welcome);
        ButterKnife.bind(this);

        // Animations
        Animation slideInFromTopAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_top);
        Animation slideInFromBottom = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom);
        titleTextView.startAnimation(slideInFromTopAnim);
        subtitleTextView.startAnimation(slideInFromTopAnim);
        proceedFab.startAnimation(slideInFromBottom);

    }

    @OnClick(R.id.proceedFab)
    public void onClick(FloatingActionButton floatingActionButton) {
        Intent nextActivityIntent = new Intent(this, SetupIntroductionActivity.class);
        startActivity(nextActivityIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
}
