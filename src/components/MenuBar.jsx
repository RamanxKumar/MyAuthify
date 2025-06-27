import React, { useContext, useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { AppContext } from "../context/AppContext.jsx";
import { toast } from "react-toastify";
import { assets } from "../assets/assets.js";

const MenuBar = () => {
  const navigate = useNavigate();
  const { userData, backendURL, setUserData, setIsLoggedIn } = useContext(AppContext);
  const [dropDownOpen, setDropDownOpen] = useState(false);
  const dropDownRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropDownRef.current && !dropDownRef.current.contains(event.target)) {
        setDropDownOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleLogout = async () => {
    try {
      axios.defaults.withCredentials = true;
      const response = await axios.post(`${backendURL}/logout`);
      if (response.status === 200) {
        setIsLoggedIn(false);
        setUserData(null);
        navigate("/");
        toast.success("Logout successfully.")
      }
    } catch (error) {
      const message = error?.response?.data?.message || "Logout failed. Please try again.";
      toast.error(message);
    }
  };

  const sendVerificationOtp = async () => {
    try {
      axios.defaults.withCredentials = true;
      const response = await axios.post(`${backendURL}/send-otp`);
      if (response.status === 200) {
        navigate("/email-verify");
        toast.success("OTP has been sent successfully.");
      } else {
        toast.error("Unable to send the OTP.");
      }
    } catch (error) {
      toast.error(error?.response?.data?.message || "Unable to send the OTP.");
    }
  };

  return (
    <nav className="navbar bg-white px-5 py-4 d-flex justify-content-between align-items-center">
      <div className="d-flex align-items-center gap-2">
        <img src={assets.header} alt="Logo" width={32} height={32} />
        <span className="fw-bold fs-4 text-dark">MyAuthify</span>
      </div>

      {userData ? (
        <div className="position-relative" ref={dropDownRef}>
          <div
            className="bg-dark text-white rounded-circle d-flex justify-content-center align-items-center"
            style={{
              width: "40px",
              height: "40px",
              cursor: "pointer",
              userSelect: "none",
            }}
            onClick={() => setDropDownOpen((prev) => !prev)}
          >
            {userData?.name?.[0]?.toUpperCase() || "U"}
          </div>

          {dropDownOpen && (
            <div
              className="position-absolute shadow bg-white rounded p-2"
              style={{
                top: "50px",
                right: 0,
                zIndex: 100,
              }}
            >
              {!userData?.isAccountVerified && (
                <div
                  className="dropdown-item py-1 px-2"
                  style={{ cursor: "pointer" }}
                  onClick={sendVerificationOtp}
                >
                  Verify Email
                </div>
              )}

              <div
                className="dropdown-item py-1 px-2 text-danger"
                style={{ cursor: "pointer" }}
                onClick={handleLogout}
              >
                Logout
              </div>
            </div>
          )}
        </div>
      ) : (
        <div
          className="btn btn-outline-dark rounded-pill px-3"
          onClick={() => navigate("/login")}
        >
          Login <i className="bi bi-arrow-right ms-2"></i>
        </div>
      )}
    </nav>
  );
};

export default MenuBar;
