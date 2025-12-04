import { useState } from "react";
import { useAuth } from "../auth/AuthContext";
import { useNavigate } from "react-router-dom";


export default function LoginForm() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);





  async function handleSubmit(e) {
    e.preventDefault();
  setError("");
  setLoading(true);

  console.log("[LoginForm] handleSubmit called");

  try {
    const user = await login({ username, password });
    console.log("[LoginForm] user from login:", user);

    if (!user) {
      setError("Backend did not return user data.");
      return;
    }

    const role = String(user.role ?? "").toUpperCase();
    const isAdmin = role === "ADMIN" || role === "ROLE_ADMIN";

    console.log("[LoginForm] role:", role);
    console.log("[LoginForm] isAdmin:", isAdmin);

    navigate(isAdmin ? "/admin" : "/dashboard", { replace: true });

    console.log("[LoginForm] navigate called");
  } catch (err) {
    console.error("[LoginForm] login failed:", err);
    setError(err.message || "Login failed. Please check credentials.");
  } finally {
    setLoading(false);
  }

  }

  return (
    <form onSubmit={handleSubmit} style={{ display: "grid", gap: 10 }}>
      <h3>Login</h3>

      <label htmlFor="username">Username</label>
      <input
        id="username"
        name="username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        placeholder="Enter username"
        autoComplete="username"
        required
      />

      <label htmlFor="password">Password</label>
      <input
        id="password"
        name="password"
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Enter password"
        autoComplete="current-password"
        required
      />

      <button
        type="submit"
        disabled={loading}
        style={{
          padding: "8px 12px",
          marginTop: 6,
          background: "#1d4ed8",
          color: "white",
          border: "none",
          borderRadius: 6,
          cursor: "pointer",
        }}
      >
        {loading ? "Logging in..." : "Login"}
      </button>

      {error && (
        <div
          style={{
            marginTop: 10,
            color: "crimson",
            border: "1px solid crimson",
            background: "#fff1f1",
            padding: 8,
            borderRadius: 6,
          }}
        >
          {error}
        </div>
      )}
    </form>
  );
}
