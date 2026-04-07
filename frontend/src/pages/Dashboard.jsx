import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { calculatePremium } from "../services/api";
import { getCurrentUser, getOnboardingStatus, isLoggedIn, isOnboardingCompleted, getFirstIncompleteOnboardingStep } from "../utils/auth";

const fallbackData = {
  suggestedPremium: 210,
  minPremium: 150,
  maxPremium: 280,
  riskLevel: "HIGH",
  reason: "Heavy rain and poor air quality in your area",
  recommendedPlan: "Pro Shield",
  weeklyUsage: 7,
  usedHours: 0,
  earningsProtected: 0,
  autoPayoutsReceived: 0,
  coins: 0,
};

export default function Dashboard() {
  const [data, setData] = useState(fallbackData);
  const [loading, setLoading] = useState(true);
  const [apiError, setApiError] = useState(null);
  const navigate = useNavigate();
  const user = getCurrentUser();
  const onboarding = getOnboardingStatus();

  useEffect(() => {
    if (!isLoggedIn()) {
      navigate("/login");
      return;
    }

    if (!isOnboardingCompleted()) {
      navigate(getFirstIncompleteOnboardingStep());
      return;
    }

    const fetchPremiumData = async () => {
      try {
        setLoading(true);
        setApiError(null);
        const response = await calculatePremium("DELIVERY", 3);
        if (response) {
          setData((prev) => ({ ...prev, ...response }));
        }
      } catch (err) {
        console.error("Failed to fetch premium:", err);
        const errorMessage = err.response?.data?.message || err.response?.data || err.message || "Unable to load premium data";
        setApiError(errorMessage);
        if (errorMessage.includes("KYC") || errorMessage.includes("not found") || errorMessage.includes("not completed")) {
          navigate(getFirstIncompleteOnboardingStep());
          return;
        }
      } finally {
        setLoading(false);
      }
    };

    fetchPremiumData();
  }, [navigate]);

  return (
    <div className="bg-black text-white min-h-screen">
      <Navbar />

      <div className="px-6 lg:px-16 py-12 lg:py-20 space-y-8">
        <div className="flex flex-col gap-4 lg:gap-0 lg:flex-row lg:justify-between lg:items-end">
          <div>
            <p className="text-sm text-gray-400">Welcome back</p>
            <h1 className="text-3xl lg:text-4xl font-bold">Hello, {user.name || user.email || "Partner"}</h1>
            <p className="text-gray-400 mt-2">Partner ID: {user.userId || "N/A"}</p>
          </div>
          <div className="flex flex-col sm:flex-row gap-3">
            <div className="bg-gray-900 border border-gray-800 rounded-2xl px-5 py-4">
              <p className="text-xs uppercase tracking-widest text-gray-400">Plan</p>
              <p className="font-semibold mt-2">No Active Plan</p>
            </div>
            <div className="bg-yellow-900 border border-yellow-800 rounded-2xl px-5 py-4">
              <p className="text-xs uppercase tracking-widest text-yellow-300">Verification</p>
              <p className="font-semibold mt-2">{onboarding.companyCompleted && onboarding.kycCompleted && onboarding.bankCompleted ? "Verified" : "Pending"}</p>
            </div>
          </div>
        </div>

        {apiError && (
          <div className="bg-red-900 border border-red-500 rounded-2xl p-5 text-red-200">
            <p className="font-semibold">Limited data available</p>
            <p className="text-sm mt-2">{apiError}</p>
          </div>
        )}

        <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
          <div className="bg-gray-900 rounded-3xl p-8 border border-gray-800">
            <p className="text-gray-400 text-sm">Weekly Usage</p>
            <p className="text-5xl font-bold mt-4">{data.weeklyUsage} hrs</p>
            <div className="mt-4 text-gray-400 text-sm">
              <p>Used: {data.usedHours} hrs</p>
              <p>Remaining: {Math.max(data.weeklyUsage - data.usedHours, 0)} hrs</p>
            </div>
          </div>

          <div className="bg-gray-900 rounded-3xl p-8 border border-gray-800">
            <p className="text-gray-400 text-sm">Propel Coins</p>
            <p className="text-5xl font-bold mt-4">{data.coins}</p>
            <p className="mt-4 text-gray-400 text-sm">Total wallet balance from unused hours</p>
          </div>

          <div className="bg-gray-900 rounded-3xl p-8 border border-gray-800">
            <p className="text-gray-400 text-sm">Auto Payouts Received</p>
            <p className="text-5xl font-bold mt-4">₹{data.autoPayoutsReceived}</p>
            <p className="mt-4 text-gray-400 text-sm">Automatic payouts triggered by coverage</p>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 bg-gradient-to-br from-blue-900 to-blue-800 rounded-3xl p-8 border border-blue-500">
            <p className="text-gray-300 text-sm">Recommended Plan</p>
            <div className="mt-4 flex flex-wrap items-center gap-4">
              <p className="text-4xl font-bold">{data.recommendedPlan}</p>
              <span className="rounded-full bg-white/10 px-3 py-1 text-sm text-white/80">High priority</span>
            </div>
            <p className="mt-4 text-gray-300">{data.reason}</p>
            <div className="mt-8 grid grid-cols-2 gap-4">
              <div className="bg-black/30 rounded-2xl p-4">
                <p className="text-gray-400 text-xs uppercase tracking-widest">Estimated</p>
                <p className="text-2xl font-semibold mt-2">₹{data.suggestedPremium}</p>
              </div>
              <div className="bg-black/30 rounded-2xl p-4">
                <p className="text-gray-400 text-xs uppercase tracking-widest">Risk</p>
                <p className="text-2xl font-semibold mt-2">{data.riskLevel}</p>
              </div>
            </div>
          </div>

          <div className="bg-gray-900 rounded-3xl p-8 border border-gray-800">
            <p className="text-gray-300 text-sm mb-4">Location Status</p>
            <div className="space-y-3 text-sm text-gray-400">
              <p>Weather monitoring is active.</p>
              <p>Location updates will adjust premiums automatically.</p>
              <p className="text-green-400">Fallback values displayed when API is unavailable.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
