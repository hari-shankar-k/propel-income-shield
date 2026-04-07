import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import { loginUser } from "../services/api";
import { getFirstIncompleteOnboardingStep, setAuthSession, clearOnboardingStatus, isLoggedIn } from "../utils/auth";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      setError(null);
      const response = await loginUser(email, password);
      console.log("Login successful:", response);

      const storedEmail = localStorage.getItem("userEmail");
      if (storedEmail && storedEmail !== email) {
        clearOnboardingStatus();
      }

      setAuthSession({
        token: response.token,
        userId: response.userId || response.id,
        email,
        name: response.name || response.userName || localStorage.getItem("userName") || email,
      });

      const nextRoute = getFirstIncompleteOnboardingStep();
      navigate(nextRoute);
    } catch (err) {
      console.error("Login failed:", err?.response?.data || err.message || err);
      const errMessage =
        err.response?.data?.message ||
        err.response?.data ||
        err.message ||
        "Login failed";
      setError(typeof errMessage === "string" ? errMessage : JSON.stringify(errMessage));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isLoggedIn()) {
      navigate(getFirstIncompleteOnboardingStep());
    }
  }, [navigate]);

  return (
    <div className="bg-black text-white min-h-screen">
      <Navbar />

      <div className="flex justify-center items-center py-12 lg:py-20 px-4">
        <div className="bg-gray-900 rounded-2xl p-8 lg:p-10 border border-gray-800 w-full max-w-md">
          <h1 className="text-3xl font-bold mb-2">Welcome Back</h1>
          <p className="text-gray-400 mb-8 text-sm lg:text-base">Sign in to your Propel Income Shield account</p>

          {error && (
            <div className="bg-red-900 border border-red-500 rounded-lg p-4 mb-6 text-red-200 text-sm">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
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

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-blue-600 px-4 py-2 rounded-lg hover:bg-blue-700 transition font-semibold disabled:opacity-50 text-sm"
            >
              {loading ? "Signing in..." : "Sign In"}
            </button>
          </form>

          <p className="text-gray-400 text-xs lg:text-sm text-center mt-6">
            Don't have an account?{" "}
            <Link to="/register" className="text-blue-500 hover:text-blue-400 font-semibold">
              Register here
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
