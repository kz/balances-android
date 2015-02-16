package in.iamkelv.balances.wizards.setupwizard;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;

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
        getActivity().finish();     //Terminate the wizard
    }

}
