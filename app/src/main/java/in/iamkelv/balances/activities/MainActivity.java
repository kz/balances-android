package in.iamkelv.balances.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.lunchAmountTextView)
    TextView lunchAmountTextView;
    @Bind(R.id.tuckAmountTextView)
    TextView tuckAmountTextView;
    @Bind(R.id.lastUpdatedTextView)
    TextView lastUpdatedTextView;
    @Bind(R.id.refreshFab)
    FloatingActionButton refreshFab;
    @Bind(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.refreshFab)
    public void onClick() {
        fabProgressCircle.show();
    }
}
