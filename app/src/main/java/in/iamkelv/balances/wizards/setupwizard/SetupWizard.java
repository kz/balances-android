package in.iamkelv.balances.wizards.setupwizard;

import android.content.Intent;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;

import in.iamkelv.balances.MainActivity;
import in.iamkelv.balances.SetupActivity;

public class SetupWizard extends BasicWizardLayout {

    public SetupWizard() {
        super();
    }

    @Override
    public WizardFlow onSetup() {
        return new WizardFlow.Builder()
                .addStep(SetupWizardStepIntroduction.class)
                .addStep(SetupWizardStepLogin.class, true)
                .addStep(SetupWizardStepNotifications.class)
                .addStep(SetupWizardStepFinish.class)
                .create();
    }

    @Override
    public void onWizardComplete() {
        super.onWizardComplete();   //Make sure to first call the super method before anything else
        Intent mainIntent= new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(mainIntent);
        getActivity().finish();     //Terminate the wizard
    }

}
