import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Navbar from "../components/Navbar";
import StepCompany from "../components/StepCompany";
import StepKYC from "../components/StepKYC";
import StepBank from "../components/StepBank";
import {
  getFirstIncompleteOnboardingStep,
  isLoggedIn,
  isOnboardingCompleted,
  getOnboardingStatus,
} from "../utils/auth";

const steps = ["company", "kyc", "bank"];

export default function CompleteProfile() {
  const navigate = useNavigate();
  const { step } = useParams();
  const activeStepIndex = steps.indexOf(step);
  const activeStep = activeStepIndex >= 0 ? step : null;
  const onboardingStatus = getOnboardingStatus();

  useEffect(() => {
    if (!isLoggedIn()) {
      navigate("/login");
      return;
    }

    if (isOnboardingCompleted()) {
      navigate("/dashboard");
      return;
    }

    if (!activeStep) {
      navigate(getFirstIncompleteOnboardingStep());
      return;
    }

    if (activeStep === "company" && onboardingStatus.companyCompleted) {
      navigate(getFirstIncompleteOnboardingStep());
    }
    if (activeStep === "kyc" && onboardingStatus.kycCompleted) {
      navigate(getFirstIncompleteOnboardingStep());
    }
    if (activeStep === "bank" && onboardingStatus.bankCompleted) {
      navigate(getFirstIncompleteOnboardingStep());
    }
  }, [activeStep, navigate, onboardingStatus]);

  const renderStep = () => {
    switch (activeStep) {
      case "company":
        return <StepCompany onNext={() => navigate("/onboarding/kyc")} />;
      case "kyc":
        return <StepKYC onNext={() => navigate("/onboarding/bank")} onPrev={() => navigate("/onboarding/company")} />;
      case "bank":
        return <StepBank onNext={() => navigate("/dashboard")} onPrev={() => navigate("/onboarding/kyc")} />;
      default:
        return null;
    }
  };

  const stepNumber = activeStepIndex + 1;
  const stepTitle =
    activeStep === "company"
      ? "Company Details"
      : activeStep === "kyc"
      ? "KYC Details"
      : "Bank Details";

  return (
    <div className="bg-black text-white min-h-screen">
      <Navbar />
      <div className="flex justify-center items-center py-12 lg:py-20 px-4">
        <div className="bg-gray-900 rounded-2xl p-8 lg:p-10 border border-gray-800 w-full max-w-md">
          <div className="mb-6">
            <div className="flex justify-between items-center mb-4">
              {steps.map((item, index) => (
                <span
                  key={item}
                  className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold ${index <= activeStepIndex ? "bg-blue-600" : "bg-gray-700"}`}
                >
                  {index + 1}
                </span>
              ))}
            </div>
            <p className="text-gray-400 text-sm text-center">
              Step {stepNumber} of 3: {stepTitle}
            </p>
          </div>
          {renderStep()}
        </div>
      </div>
    </div>
  );
}
