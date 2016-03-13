package in.iamkelv.balances.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import in.iamkelv.balances.R;

public class ThresholdPickerPreference extends DialogPreference {

    SharedPreferences mSettings;
    SeekBar mSeekBar;
    String mThresholdPreferenceValue;
    TextView mThresholdTextView;


    public ThresholdPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_thresholdpicker);

        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        mThresholdPreferenceValue = mSettings.getString(getKey(), "10");
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);

        if (restorePersistedValue) {
            persistString(getPersistedString(mThresholdPreferenceValue));
        } else {
            persistString((String) defaultValue);
        }
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        // Bind view components
        mThresholdTextView = (TextView) view.findViewById(R.id.thresholdTextView);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);

        // Update view components
        updateThresholdTextView();
        mSeekBar.setProgress(Integer.valueOf(mThresholdPreferenceValue));

        // Set seekbar listener
        mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistString(mThresholdPreferenceValue);
            callChangeListener(mThresholdPreferenceValue);
            notifyChanged();
        }
    }


    protected void updateThresholdTextView() {
        Integer threshold = Integer.valueOf(mThresholdPreferenceValue);
        if (threshold == 0) {
            mThresholdTextView.setText(getContext().getString(R.string.threshold_picker_threshold_disabled));
        } else {
            String thresholdPickerPlaceholder = getContext().getString(R.string.threshold_picker_threshold_amount);
            mThresholdTextView.setText(String.format(thresholdPickerPlaceholder, threshold));
        }
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mThresholdPreferenceValue = String.valueOf(progress);
            updateThresholdTextView();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

}
