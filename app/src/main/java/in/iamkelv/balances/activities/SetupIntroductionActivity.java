package in.iamkelv.balances.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;

public class SetupIntroductionActivity extends AppCompatActivity {

    @Bind(R.id.nextButton)
    ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_introduction);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @OnClick(R.id.nextButton)
    public void onClick(ImageButton imageButton) {
        Intent nextActivityIntent = new Intent(this, SetupAccountActivity.class);
        startActivity(nextActivityIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
}
