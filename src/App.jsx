import './App.css';
import { ToastContainer } from "react-toastify";
import { Routes, Route } from "react-router-dom";

import ResetPassword from './pages/ResetPassword';
import Login from "./pages/Login";
import EmailVerify from "./pages/EmailVerify";
import Home from "./pages/Home";

import ProtectedRoute from "./components/ProtectedRoute";

const App = () => {
  return (
    <div>
      <ToastContainer />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />

        {/* 🔐 Protected Routes */}
        <Route
          path="/email-verify"
          element={
            <ProtectedRoute>
              <EmailVerify />
            </ProtectedRoute>
          }
        />
        <Route
          path="/reset-password"
          element={
            <ProtectedRoute>
              <ResetPassword />
            </ProtectedRoute>
          }
        />
      </Routes>
    </div>
  );
};

export default App;
