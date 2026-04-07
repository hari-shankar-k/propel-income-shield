import { useState } from "react";
import { uploadKyc, getApiErrorMessage, isAlreadySubmittedError } from "../services/api";
import { getCurrentUser, setOnboardingFlag } from "../utils/auth";

export default function StepKYC({ onNext, onPrev }) {
  const [aadhaarNumber, setAadhaarNumber] = useState("");
  const [panNumber, setPanNumber] = useState("");
  const [aadhaarImage, setAadhaarImage] = useState("");
  const [panImage, setPanImage] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleFileChange = (e, setter) => {
    const file = e.target.files[0];
    if (file) {
      setter(file.name);
    } else {
      setter("");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { userId } = getCurrentUser();
    if (!userId) {
      setError("User not logged in");
      return;
    }

    const aadhaarImageName = aadhaarImage || null;
    const panImageName = panImage || null;

    try {
      setLoading(true);
      setError(null);
      await uploadKyc(userId, aadhaarNumber, panNumber, aadhaarImageName, panImageName);
      setOnboardingFlag("kycCompleted");
      onNext();
    } catch (err) {
      const errMessage = getApiErrorMessage(err);
      if (isAlreadySubmittedError(errMessage, "kyc")) {
        setOnboardingFlag("kycCompleted");
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
      <h1 className="text-2xl font-bold mb-2">KYC Details</h1>
      <p className="text-gray-400 mb-6 text-sm">Upload your identification documents</p>

      {error && (
        <div className="bg-red-900 border border-red-500 rounded-lg p-4 mb-6 text-red-200 text-sm">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-2">Aadhaar Number</label>
          <input
            type="text"
            value={aadhaarNumber}
            onChange={(e) => setAadhaarNumber(e.target.value)}
            placeholder="Enter Aadhaar number"
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">PAN Number</label>
          <input
            type="text"
            value={panNumber}
            onChange={(e) => setPanNumber(e.target.value)}
            placeholder="Enter PAN number"
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">Aadhaar Image</label>
          <input
            type="file"
            accept="image/*"
            onChange={(e) => handleFileChange(e, setAadhaarImage)}
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">PAN Image</label>
          <input
            type="file"
            accept="image/*"
            onChange={(e) => handleFileChange(e, setPanImage)}
            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
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
            {loading ? "Uploading..." : "Next"}
          </button>
        </div>
      </form>
    </div>
  );
}