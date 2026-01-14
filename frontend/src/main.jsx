import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import { AuthProvider } from "./auth/AuthContext";
import { OverconsumptionProvider } from "./ws/OverconsumptionContext";


ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <OverconsumptionProvider>
          <App />
        </OverconsumptionProvider>
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);