package in.iamkelv.balances.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @OnClick(R.id.prevButton)
    public void onClick(ImageButton imageButton) {
        onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }
}
