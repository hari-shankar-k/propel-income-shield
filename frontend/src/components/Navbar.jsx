import { Link, useNavigate } from "react-router-dom";
import { logout } from "../utils/auth";
import { useAuth } from "../hooks/useAuth";

export default function Navbar() {
  const navigate = useNavigate();
  const { isAuthenticated, currentUser } = useAuth();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav className="flex flex-wrap justify-between items-center px-6 lg:px-10 py-4 bg-black border-b border-gray-800">
      <div className="flex items-center space-x-3">
        <h1 className="text-xl font-bold">
          Propel <span className="text-blue-500">Income Shield</span>
        </h1>
        {isAuthenticated && currentUser.name && (
          <span className="text-gray-400 text-sm">Hi, {currentUser.name}</span>
        )}
      </div>

      <div className="flex flex-wrap items-center gap-3">
        <Link to="/" className="text-gray-300 hover:text-white transition">
          Home
        </Link>
        <a href="#how-it-works" className="text-gray-300 hover:text-white transition">
          How it Works
        </a>
        {isAuthenticated ? (
          <>
            <Link to="/dashboard" className="text-gray-300 hover:text-white transition">
              Dashboard
            </Link>
            <button
              onClick={handleLogout}
              className="text-gray-300 hover:text-white transition"
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login" className="text-gray-300 hover:text-white transition">
              Login
            </Link>
            <Link
              to="/register"
              className="bg-white text-black px-4 py-2 rounded-xl font-semibold hover:bg-gray-200 transition"
            >
              Register
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}
