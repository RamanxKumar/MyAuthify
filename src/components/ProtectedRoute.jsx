
import { Navigate } from "react-router-dom";
import { useContext } from "react";
import { AppContext } from "../context/AppContext";

const ProtectedRoute = ({ children }) => {
  const { isLoggedIn } = useContext(AppContext);

  return isLoggedIn ? children : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
