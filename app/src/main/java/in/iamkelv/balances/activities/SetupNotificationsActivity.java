package in.iamkelv.balances.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;
import in.iamkelv.balances.fragments.NotificationPreferencesFragment;

public class SetupNotificationsActivity extends AppCompatActivity {

    @Bind(R.id.nextButton)
    ImageButton nextButton;
    @Bind(R.id.prevButton)
    ImageButton prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_notifications);
        ButterKnife.bind(this);
        getFragmentManager().beginTransaction().replace(R.id.relativeLayout, new NotificationPreferencesFragment()).commit();
    }


    public void onNextButtonClick() {
/*      Intent nextActivityIntent = new Intent(this, SetupNotificationsActivity.class);
        startActivity(nextActivityIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/
    }

    public void onPrevButtonClick() {
        onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @OnClick({R.id.nextButton, R.id.prevButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton:
                onNextButtonClick();
                break;
            case R.id.prevButton:
                onPrevButtonClick();
                break;
        }
    }
}
