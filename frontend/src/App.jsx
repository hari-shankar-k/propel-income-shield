import "./index.css";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Home from "./pages/Home";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login";
import Register from "./pages/Register";
import CompleteProfile from "./pages/CompleteProfile";
import OnboardingRedirect from "./pages/OnboardingRedirect";
import { isLoggedIn } from "./utils/auth";

const PrivateRoute = ({ children }) => {
  return isLoggedIn() ? children : <Navigate to="/login" replace />;
};

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/complete-profile" element={<PrivateRoute><OnboardingRedirect /></PrivateRoute>} />
        <Route path="/onboarding" element={<PrivateRoute><Navigate to="/onboarding/company" replace /></PrivateRoute>} />
        <Route path="/onboarding/:step" element={<PrivateRoute><CompleteProfile /></PrivateRoute>} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
}