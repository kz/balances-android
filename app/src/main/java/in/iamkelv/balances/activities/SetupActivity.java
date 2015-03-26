package in.iamkelv.balances.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import in.iamkelv.balances.R;
import in.iamkelv.balances.fragments.SetupTimePickerFragment;


public class SetupActivity extends FragmentActivity {

    SetupActivity mActivity;
    public TextView mTxtScheduledTimeHours;
    public TextView mTxtScheduledTimeMinutes;

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
        SetupTimePickerFragment newFragment = new SetupTimePickerFragment();
        newFragment.setParent(mActivity);
        newFragment.show(fm, "timePicker");
    }
}
