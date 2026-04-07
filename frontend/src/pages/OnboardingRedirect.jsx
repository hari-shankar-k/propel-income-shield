import { Navigate } from "react-router-dom";
import { getFirstIncompleteOnboardingStep, isLoggedIn } from "../utils/auth";

export default function OnboardingRedirect() {
  if (!isLoggedIn()) {
    return <Navigate to="/login" replace />;
  }

  const nextStep = getFirstIncompleteOnboardingStep();
  return <Navigate to={nextStep} replace />;
}
