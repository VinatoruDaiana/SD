import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import LoginForm from "../components/LoginForm";
import RegisterForm from "../components/RegisterForm";
import { apiRegister } from "../api";

export default function AuthPage() {
  const [mode, setMode] = useState("login");   
  const [regStatus, setRegStatus] = useState(null); 
  const navigate = useNavigate();
  const { login } = useAuth();


 async function handleLoginSubmit(e) {
  e.preventDefault();
  const fd = new FormData(e.currentTarget);
  const credentials = {
    username: fd.get("username"),
    password: fd.get("password"),
  };

  try {
    const me = await login(credentials);       
    const isAdmin = me.role === "ADMIN" || me.role === "ROLE_ADMIN";
    navigate(isAdmin ? "/app/admin" : "/app/dashboard", { replace: true });
  } catch (err) {
    alert(err.message || "Login failed");
  }
}



  async function handleRegisterSubmit(e) {
  e.preventDefault();
  setRegStatus(null);
  setMode("login");
  const form = e.currentTarget;             
  const fd = new FormData(form);

  const payload = {
    username: String(fd.get("username") || "").trim(),
    email: String(fd.get("email") || "").trim(),
    password: fd.get("password"),
    role: (fd.get("role") || "USER").toString().toUpperCase(),
  };

  try {
    await apiRegister(payload);
    setRegStatus({ type: "success", text: "Cont creat cu succes!" });
    form.reset();                            
  } catch (err) {
    setRegStatus({ type: "error", text: err.message || "Ceva n-a mers corect." });
  }
}


  return (
    <div style={{ display: "grid", placeItems: "center", minHeight: "100vh" }}>
      <div style={{ width: 420, padding: 24, border: "1px solid #ddd", borderRadius: 8 }}>
        <h2 style={{ marginTop: 0 }}>Welcome</h2>

        <div style={{ display: "flex", gap: 12, marginBottom: 12 }}>
          <button onClick={() => setMode("login")} disabled={mode === "login"}>Login</button>
          <button onClick={() => setMode("register")} disabled={mode === "register"}>Register</button>
        </div>

        {mode === "login" ? (
          <LoginForm onSubmit={handleLoginSubmit} />
        ) : (
          <>
            <RegisterForm onSubmit={handleRegisterSubmit} />
            {regStatus && (
              <div
                style={{
                  marginTop: 12,
                  padding: 12,
                  border: "1px solid",
                  borderColor: regStatus.type === "success" ? "green" : "crimson",
                  background: regStatus.type === "success" ? "#f1fff1" : "#fff1f1",
                }}
              >
                {regStatus.text}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}
