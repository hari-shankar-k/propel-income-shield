import { useState } from "react";
import { joinCompany, getApiErrorMessage, isAlreadySubmittedError } from "../services/api";
import { getCurrentUser, setOnboardingFlag } from "../utils/auth";

export default function StepCompany({ onNext }) {
  const [companyCode, setCompanyCode] = useState("");
  const [employeeId, setEmployeeId] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { userId } = getCurrentUser();
    if (!userId) {
      setError("User not logged in");
      return;
    }

    try {
      setLoading(true);
      setError(null);
      await joinCompany(userId, companyCode, employeeId);
      setOnboardingFlag("companyCompleted");
      onNext();
    } catch (err) {
      const errMessage = getApiErrorMessage(err);
      if (isAlreadySubmittedError(errMessage, "company")) {
        setOnboardingFlag("companyCompleted");
        onNext();
      } else {
        setError(errMessage);
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-2">Company Details</h1>
      <p className="text-gray-400 mb-6 text-sm">Enter your company information</p>

      {error && (
        <div className="bg-red-900 border border-red-500 rounded-lg p-4 mb-6 text-red-200 text-sm">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-2">Company Code</label>
          <input
            type="text"
            value={companyCode}
            onChange={(e) => setCompanyCode(e.target.value)}
            placeholder="Enter company code"
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">Employee ID</label>
          <input
            type="text"
            value={employeeId}
            onChange={(e) => setEmployeeId(e.target.value)}
            placeholder="Enter employee ID"
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
            required
          />
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 px-4 py-2 rounded-lg hover:bg-blue-700 transition font-semibold disabled:opacity-50 text-sm"
        >
          {loading ? "Joining Company..." : "Next"}
        </button>
      </form>
    </div>
  );
}