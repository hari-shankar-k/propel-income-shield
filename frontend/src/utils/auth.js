const AUTH_KEYS = {
  authToken: "authToken",
  userId: "userId",
  userEmail: "userEmail",
  userName: "userName",
  companyCompleted: "companyCompleted",
  kycCompleted: "kycCompleted",
  bankCompleted: "bankCompleted",
};

export const getCurrentUser = () => {
  return {
    userId: localStorage.getItem(AUTH_KEYS.userId),
    email: localStorage.getItem(AUTH_KEYS.userEmail),
    name: localStorage.getItem(AUTH_KEYS.userName),
    token: localStorage.getItem(AUTH_KEYS.authToken),
  };
};

export const isLoggedIn = () => {
  return !!localStorage.getItem(AUTH_KEYS.authToken);
};

export const setAuthSession = ({ token, userId, email, name }) => {
  if (token) localStorage.setItem(AUTH_KEYS.authToken, token);
  if (userId) localStorage.setItem(AUTH_KEYS.userId, userId.toString());
  if (email) localStorage.setItem(AUTH_KEYS.userEmail, email);
  if (name) localStorage.setItem(AUTH_KEYS.userName, name);
};

export const clearAuthSession = () => {
  localStorage.removeItem(AUTH_KEYS.authToken);
  localStorage.removeItem(AUTH_KEYS.userId);
  localStorage.removeItem(AUTH_KEYS.userEmail);
  localStorage.removeItem(AUTH_KEYS.userName);
  clearOnboardingStatus();
};

export const setOnboardingFlag = (flagName) => {
  if (Object.values(AUTH_KEYS).includes(flagName)) {
    localStorage.setItem(flagName, "true");
  }
};

export const clearOnboardingStatus = () => {
  localStorage.removeItem(AUTH_KEYS.companyCompleted);
  localStorage.removeItem(AUTH_KEYS.kycCompleted);
  localStorage.removeItem(AUTH_KEYS.bankCompleted);
};

export const getOnboardingStatus = () => {
  return {
    companyCompleted: localStorage.getItem(AUTH_KEYS.companyCompleted) === "true",
    kycCompleted: localStorage.getItem(AUTH_KEYS.kycCompleted) === "true",
    bankCompleted: localStorage.getItem(AUTH_KEYS.bankCompleted) === "true",
  };
};

export const isOnboardingCompleted = () => {
  const status = getOnboardingStatus();
  return status.companyCompleted && status.kycCompleted && status.bankCompleted;
};

export const getFirstIncompleteOnboardingStep = () => {
  const { companyCompleted, kycCompleted, bankCompleted } = getOnboardingStatus();
  if (!companyCompleted) return "/onboarding/company";
  if (!kycCompleted) return "/onboarding/kyc";
  if (!bankCompleted) return "/onboarding/bank";
  return "/dashboard";
};

export const logout = () => {
  clearAuthSession();
};