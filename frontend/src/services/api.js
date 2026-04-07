import axios from "axios";

// Create Axios instance with base URL for Vite proxy
const API = axios.create({
  baseURL: "/api",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

API.interceptors.request.use((config) => {
  const token = localStorage.getItem("authToken");
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  console.log("API request", config.method, config.url, config.data || config.params || {});
  return config;
});

API.interceptors.response.use(
  (response) => {
    console.log("API response", response.config.url, response.status, response.data);
    return response;
  },
  (error) => {
    console.error("API response error", error?.response?.config?.url, error?.response?.status, error?.response?.data || error.message);
    return Promise.reject(error);
  }
);

export const getApiErrorMessage = (error) => {
  const data = error?.response?.data;
  if (typeof data === "string") return data;
  if (data?.message) return data.message;
  if (data?.error) return data.error;
  if (typeof data === "object") return JSON.stringify(data);
  return error?.message || "An unexpected error occurred.";
};

export const isAlreadySubmittedError = (message, step) => {
  if (!message) return false;
  const lower = message.toString().toLowerCase();
  if (step === "company") {
    return /already linked|company already linked|already.*company/.test(lower);
  }
  if (step === "kyc") {
    return /already submitted|kyc already submitted|already.*kyc/.test(lower);
  }
  if (step === "bank") {
    return /already added|bank already added|already.*bank/.test(lower);
  }
  return /already linked|already submitted|already added/.test(lower);
};

// API Functions

/**
 * Calculate premium based on work type and inactivity hours
 * @param {string} workType - Type of work (e.g., DELIVERY)
 * @param {number} avgInactivityHours - Average inactivity hours
 * @returns {Promise} Premium calculation response
 */
export const calculatePremium = async (
  workType = "DELIVERY",
  avgInactivityHours = 3
) => {
  try {
    console.log("Requesting premium calculation", { workType, avgInactivityHours });
    const response = await API.post("/premium/calculate", null, {
      params: {
        workType,
        avgInactivityHours,
      },
    });
    console.log("Premium response", response.data);
    return response.data;
  } catch (error) {
    console.error("Error calculating premium:", error?.response?.data || error.message || error);
    throw error;
  }
};

/**
 * Login user with email and password
 * @param {string} email - User email
 * @param {string} password - User password
 * @returns {Promise} Login response with token
 */
export const loginUser = async (email, password) => {
  try {
    console.log("Login request", { email });
    const response = await API.post(
      "/auth/login",
      {
        email,
        password,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    console.log("Login response", response.data);
    return response.data;
  } catch (error) {
    console.error("Error logging in:", error?.response?.data || error.message || error);
    throw error;
  }
};

/**
 * Register new user
 * @param {string} name - User name
 * @param {string} email - User email
 * @param {string} password - User password
 * @param {string} phone - User phone number
 * @returns {Promise} Registration response
 */
export const registerUser = async (name, email, password, phone) => {
  try {
    console.log("Register request", { name, email, phone });
    const response = await API.post(
      "/auth/register",
      {
        name,
        email,
        password,
        phone,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    console.log("Register response", response.data);
    return response.data;
  } catch (error) {
    console.error("Error registering:", error?.response?.data || error.message || error);
    throw error;
  }
};

/**
 * Join company
 * @param {string} userId - User ID
 * @param {string} companyCode - Company code
 * @param {string} employeeId - Employee ID
 * @returns {Promise} Join company response
 */
export const joinCompany = async (userId, companyCode, employeeId) => {
  try {
    console.log("Join company request", { userId, companyCode, employeeId });
    const response = await API.post(
      "/company/join",
      {
        userId,
        companyCode,
        employeeId,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    console.log("Join company response", response.data);
    return response.data;
  } catch (error) {
    console.error("Error joining company:", error?.response?.data || error.message || error);
    throw error;
  }
};

/**
 * Upload KYC
 * @param {string} userId - User ID
 * @param {string} aadhaarNumber - Aadhaar number
 * @param {string} panNumber - PAN number
 * @param {string} aadhaarImage - Aadhaar image filename or placeholder
 * @param {string} panImage - PAN image filename or placeholder
 * @returns {Promise} Upload KYC response
 */
export const uploadKyc = async (userId, aadhaarNumber, panNumber, aadhaarImage, panImage) => {
  try {
    console.log("Upload KYC request", { userId, aadhaarNumber, panNumber });
    const response = await API.post(
      "/kyc/upload",
      {
        userId,
        aadhaarNumber,
        panNumber,
        aadhaarImage,
        panImage,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    console.log("Upload KYC response", response.data);
    return response.data;
  } catch (error) {
    console.error("Error uploading KYC:", error?.response?.data || error.message || error);
    throw error;
  }
};

/**
 * Add bank details
 * @param {string} userId - User ID
 * @param {string} accountNumber - Account number
 * @param {string} ifscCode - IFSC code
 * @param {string} bankName - Bank name
 * @param {string} accountHolderName - Account holder name
 * @returns {Promise} Add bank response
 */
export const addBankDetails = async (userId, accountNumber, ifscCode, bankName, accountHolderName) => {
  try {
    console.log("Add bank request", { userId, accountNumber, ifscCode, bankName, accountHolderName });
    const response = await API.post(
      "/bank/add",
      {
        userId,
        accountNumber,
        ifscCode,
        bankName,
        accountHolderName,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    console.log("Add bank response", response.data);
    return response.data;
  } catch (error) {
    console.error("Error adding bank:", error?.response?.data || error.message || error);
    throw error;
  }
};

export default API;
