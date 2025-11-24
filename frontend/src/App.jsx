import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./auth/AuthContext";
import AuthPage from "./pages/AuthPage";
import Dashboard from "./pages/Dashboard";
import AdminUsers from "./pages/AdminUsers";
import AdminDevices from "./pages/AdminDevices";
import AdminHome from "./pages/AdminHome";

// Gardian pentru user logat
function RequireAuth({ children }) {
  const { user, loading } = useAuth();
  if (loading) return <div style={{ padding: 24 }}>Loading…</div>;
  if (!user) return <Navigate to="/" replace />;
  return children;
}

// Gardian pentru admin
function RequireAdmin({ children }) {
  const { user, loading } = useAuth();

  if (loading) {
    return <div style={{ padding: 24 }}>Loading…</div>;
  }

  const role = String(user?.role ?? "").toUpperCase();
  const isAdmin = role === "ADMIN" || role === "ROLE_ADMIN";

  console.log("[RequireAdmin] user from context:", user);
  console.log("[RequireAdmin] role:", role, "isAdmin:", isAdmin);

  if (!user || !isAdmin) {
    return <Navigate to="/" replace />;
  }

  return children;
}

export default function App() {
  return (
    <Routes>
      {/* pagina de login / register */}
      <Route path="/" element={<AuthPage />} />

      {/* dashboard user normal */}
      <Route
        path="/dashboard"
        element={
          <RequireAuth>
            <Dashboard />
          </RequireAuth>
        }
      />

      {/* home admin (card cu butoane) */}
      <Route
        path="/admin"
        element={
          <RequireAdmin>
            <AdminHome />
          </RequireAdmin>
        }
      />

      {/* CRUD pe useri */}
      <Route
        path="/admin/users"
        element={
          <RequireAdmin>
            <AdminUsers />
          </RequireAdmin>
        }
      />

      {/* CRUD pe devices */}
      <Route
        path="/admin/devices"
        element={
          <RequireAdmin>
            <AdminDevices />
          </RequireAdmin>
        }
      />

      {/* fallback */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
