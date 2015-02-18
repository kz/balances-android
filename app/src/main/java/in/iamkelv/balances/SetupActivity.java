package in.iamkelv.balances;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;


public class SetupActivity extends FragmentActivity {

    SetupActivity mActivity;
    TextView mTxtScheduledTimeHours;
    TextView mTxtScheduledTimeMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    public void showTimePickerDialog(View v) {
        mTxtScheduledTimeHours = (TextView) findViewById(R.id.txtScheduledTimeHours);
        mTxtScheduledTimeMinutes = (TextView) findViewById(R.id.txtScheduledTimeMinutes);
        FragmentManager fm = this.getFragmentManager();
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setParent(mActivity);
        newFragment.show(fm, "timePicker");
    }
}
