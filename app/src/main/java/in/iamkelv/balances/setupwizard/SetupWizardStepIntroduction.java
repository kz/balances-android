package in.iamkelv.balances.setupwizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.codepond.wizardroid.WizardStep;

import in.iamkelv.balances.R;

public class SetupWizardStepIntroduction extends WizardStep {

    public SetupWizardStepIntroduction() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_wizard_step_introduction, container, false);
        return v;
    }
}
