import { useState } from "react";
import { addBankDetails, getApiErrorMessage, isAlreadySubmittedError } from "../services/api";
import { getCurrentUser, setOnboardingFlag } from "../utils/auth";

export default function StepBank({ onNext, onPrev }) {
  const [accountHolderName, setAccountHolderName] = useState("");
  const [accountNumber, setAccountNumber] = useState("");
  const [ifscCode, setIfscCode] = useState("");
  const [bankName, setBankName] = useState("");
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
      await addBankDetails(userId, accountNumber, ifscCode, bankName, accountHolderName);
      setOnboardingFlag("bankCompleted");
      onNext();
    } catch (err) {
      const errMessage = getApiErrorMessage(err);
      if (isAlreadySubmittedError(errMessage, "bank")) {
        setOnboardingFlag("bankCompleted");
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
      <h1 className="text-2xl font-bold mb-2">Bank Details</h1>
      <p className="text-gray-400 mb-6 text-sm">Enter your bank account information</p>

      {error && (
        <div className="bg-red-900 border border-red-500 rounded-lg p-4 mb-6 text-red-200 text-sm">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-2">Account Holder Name</label>
          <input
            type="text"
            value={accountHolderName}
            onChange={(e) => setAccountHolderName(e.target.value)}
            placeholder="Enter account holder name"
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">Account Number</label>
          <input
            type="text"
            value={accountNumber}
            onChange={(e) => setAccountNumber(e.target.value)}
            placeholder="Enter account number"
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">IFSC Code</label>
          <input
            type="text"
            value={ifscCode}
            onChange={(e) => setIfscCode(e.target.value)}
            placeholder="Enter IFSC code"
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">Bank Name</label>
          <input
            type="text"
            value={bankName}
            onChange={(e) => setBankName(e.target.value)}
            placeholder="Enter bank name"
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
            required
          />
        </div>

        <div className="flex space-x-4">
          <button
            type="button"
            onClick={onPrev}
            className="w-1/2 bg-gray-600 px-4 py-2 rounded-lg hover:bg-gray-700 transition font-semibold text-sm"
          >
            Previous
          </button>
          <button
            type="submit"
            disabled={loading}
            className="w-1/2 bg-blue-600 px-4 py-2 rounded-lg hover:bg-blue-700 transition font-semibold disabled:opacity-50 text-sm"
          >
            {loading ? "Adding Bank..." : "Complete Setup"}
          </button>
        </div>
      </form>
    </div>
  );
}