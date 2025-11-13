import { useState } from "react";
import { useAuth } from "../auth/AuthContext";
import { useNavigate } from "react-router-dom";

/**
 * Componenta LoginForm — formular de autentificare
 * Apelează funcția `login` din AuthContext și redirecționează utilizatorul
 * în funcție de rolul extras din JWT (ADMIN → /app/admin, altfel /app/dashboard).
 */
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

    try {
      const user = await login({ username, password });
      const role = String(user.role || "").toUpperCase();
      const isAdmin = role === "ADMIN" || role === "ROLE_ADMIN";

      navigate(isAdmin ? "/app/admin" : "/app/dashboard", { replace: true });
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
