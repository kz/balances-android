package in.iamkelv.balances;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


public class SetupActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    public void showTimePickerDialog(View v) {
        FragmentManager fm = this.getFragmentManager();
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(fm, "timePicker");
    }
}
