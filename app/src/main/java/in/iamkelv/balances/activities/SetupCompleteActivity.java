package in.iamkelv.balances.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;

public class SetupCompleteActivity extends AppCompatActivity {

    @Bind(R.id.nextButton)
    Button nextButton;
    @Bind(R.id.prevButton)
    ImageButton prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_complete);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void onNextButtonClick() {
        Intent nextActivityIntent = new Intent(this, MainActivity.class);
        nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(nextActivityIntent);
        finish();
    }

    public void onPrevButtonClick() {
        onBackPressed();
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
