import { createContext, useContext, useEffect, useState } from "react";
import { apiLogin } from "../api";

// ==== CONTEXT & HOOK ====
const AuthCtx = createContext({
  user: null,
  token: null,
  loading: true,
  login: async () => {},
  logout: () => {},
});

export function useAuth() {
  return useContext(AuthCtx);
}

// ==== PROVIDER ====
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);


  useEffect(() => {
    try {
      const raw = localStorage.getItem("auth");
      if (raw) {
        const { token, user } = JSON.parse(raw);
        setToken(token || null);
        setUser(user || null);
      }
    } catch (_) {}
    setLoading(false);
  }, []);

  // login -> ia { token, user } din apiLogin (care decodează JWT)
  async function login(credentials) {
    console.log(" AuthContext: login() called with", credentials);
    const { token, user } = await apiLogin(credentials);
    console.log(" AuthContext: apiLogin returned:", { token, user });

    
    localStorage.setItem("auth", JSON.stringify({ token, user }));
    setToken(token);
    setUser(user);
    return user; 
  }

  function logout() {
    localStorage.removeItem("auth");
    setToken(null);
    setUser(null);
  }

  const value = { user, token, loading, login, logout };
  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>;
}

export default AuthProvider; 
