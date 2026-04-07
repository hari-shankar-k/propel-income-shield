import { useEffect, useState } from "react";
import { getCurrentUser, isLoggedIn, getOnboardingStatus } from "../utils/auth";

export function useAuth() {
  const [authState, setAuthState] = useState({
    isAuthenticated: isLoggedIn(),
    currentUser: getCurrentUser(),
    onboarding: getOnboardingStatus(),
  });

  useEffect(() => {
    const update = () => {
      setAuthState({
        isAuthenticated: isLoggedIn(),
        currentUser: getCurrentUser(),
        onboarding: getOnboardingStatus(),
      });
    };

    window.addEventListener("storage", update);
    return () => window.removeEventListener("storage", update);
  }, []);

  return authState;
}
