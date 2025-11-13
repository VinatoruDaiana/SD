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
  if (loading) return <div style={{ padding: 24 }}>Loading…</div>;
  if (!user) return <Navigate to="/" replace />;
  const role = user.role;
  const isAdmin = role === "ADMIN" || role === "ROLE_ADMIN";
  if (!isAdmin) return <Navigate to="/dashboard" replace />;
  return children;
}

function AppHomeRedirect() {
  const { user } = useAuth();
  if (!user) return <Navigate to="/" replace />;

  if (user.role === "ADMIN" || user.role === "ROLE_ADMIN") {
    return <Navigate to="/admin" replace />;
  }

  return <Navigate to="/dashboard" replace />;
}

export default function App() {
  return (
    <Routes>
      {/* login/register */}
      <Route path="/" element={<AuthPage />} />

      {/* redirect după login */}
      <Route
        path="/home"
        element={
          <RequireAuth>
            <AppHomeRedirect />
          </RequireAuth>
        }
      />

      {/* dashboard user */}
      <Route
        path="/dashboard"
        element={
          <RequireAuth>
            <Dashboard />
          </RequireAuth>
        }
      />

      {/* admin home */}
      <Route
        path="/admin"
        element={
          <RequireAdmin>
            <AdminHome />
          </RequireAdmin>
        }
      />

      {/* admin users */}
      <Route
        path="/admin/users"
        element={
          <RequireAdmin>
            <AdminUsers />
          </RequireAdmin>
        }
      />

      {/* admin devices */}
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
