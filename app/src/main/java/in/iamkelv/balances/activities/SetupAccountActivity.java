package in.iamkelv.balances.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;

public class SetupAccountActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nextButton)
    ImageButton nextButton;
    @Bind(R.id.prevButton)
    ImageButton prevButton;
    @Bind(R.id.signInButton)
    Button signInButton;
    @Bind(R.id.usernameEditText)
    EditText usernameEditText;
    @Bind(R.id.passwordEditText)
    EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        nextButton.setEnabled(false);
        nextButton.setAlpha((float) 0.3);

    }

    public void onSignInButtonClick() {
        signInButton.setEnabled(false);
        signInButton.setText(R.string.activity_setup_signed_in);

        usernameEditText.setFocusable(false);
        usernameEditText.setFocusableInTouchMode(false);
        usernameEditText.setClickable(false);
        passwordEditText.setFocusable(false);
        passwordEditText.setFocusableInTouchMode(false);
        passwordEditText.setClickable(false);

        nextButton.setAlpha((float) 1);
        nextButton.setEnabled(true);
    }

    public void onNextButtonClick() {

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

    @OnClick({R.id.signInButton, R.id.nextButton, R.id.prevButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signInButton:
                onSignInButtonClick();
                break;
            case R.id.nextButton:
                onNextButtonClick();
                break;
            case R.id.prevButton:
                onPrevButtonClick();
                break;
        }
    }
}
