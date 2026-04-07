import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import { registerUser } from "../services/api";
import { isLoggedIn, getFirstIncompleteOnboardingStep, clearOnboardingStatus } from "../utils/auth";

export default function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const response = await registerUser(name, email, password, phone);
      console.log("Registration successful:", response);
      clearOnboardingStatus();
      localStorage.setItem("userId", response.userId || response.id);
      localStorage.setItem("userEmail", email);
      localStorage.setItem("userName", name);
      navigate("/login");
    } catch (err) {
      const errMessage =
        err.response?.data?.message ||
        err.response?.data ||
        err.message ||
        "Registration failed";
      setError(typeof errMessage === "string" ? errMessage : JSON.stringify(errMessage));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isLoggedIn()) {
      window.location.replace(getFirstIncompleteOnboardingStep());
    }
  }, []);

  return (
    <div className="bg-black text-white min-h-screen">
      <Navbar />

      <div className="flex justify-center items-center py-12 lg:py-20 px-4">
        <div className="bg-gray-900 rounded-2xl p-8 lg:p-10 border border-gray-800 w-full max-w-md">
          <h1 className="text-3xl font-bold mb-2">Create Account</h1>
          <p className="text-gray-400 mb-8 text-sm lg:text-base">Join Propel Income Shield today</p>

          {error && (
            <div className="bg-red-900 border border-red-500 rounded-lg p-4 mb-6 text-red-200 text-sm">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-2">Full Name</label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="John Doe"
                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Email</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="you@example.com"
                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Phone</label>
              <input
                type="tel"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                placeholder="(555) 123-4567"
                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Password</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Confirm Password</label>
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-blue-500 transition text-sm"
                required
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-blue-600 px-4 py-2 rounded-lg hover:bg-blue-700 transition font-semibold disabled:opacity-50 text-sm"
            >
              {loading ? "Creating Account..." : "Create Account"}
            </button>
          </form>

          <p className="text-gray-400 text-xs lg:text-sm text-center mt-6">
            Already have an account?{" "}
            <Link to="/login" className="text-blue-500 hover:text-blue-400 font-semibold">
              Login here
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
