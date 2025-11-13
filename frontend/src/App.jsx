import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./auth/AuthContext";
import AuthPage from "./pages/AuthPage";
import Dashboard from "./pages/Dashboard";
import AdminUsers from "./pages/AdminUsers";
import AdminDevices from "./pages/AdminDevices";
import AdminHome from "./pages/AdminHome";

// =====================
// Gardian pentru user logat
// =====================
function RequireAuth({ children }) {
  const { user, loading } = useAuth();
  if (loading) return <div style={{ padding: 24 }}>Loading…</div>;
  if (!user) return <Navigate to="/" replace />;
  return children;
}

// =====================
// Gardian pentru admin
// =====================
function RequireAdmin({ children }) {
  const { user, loading } = useAuth();
  if (loading) return <div style={{ padding: 24 }}>Loading…</div>;
  if (!user) return <Navigate to="/" replace />;
  const role = user.role;
  const isAdmin = role === "ADMIN" || role === "ROLE_ADMIN";
  if (!isAdmin) return <Navigate to="/app" replace />;
  return children;
}

function AppHomeRedirect() {
  const { user } = useAuth();
  if (!user) return <Navigate to="/" replace />;
  // dacă e admin -> du-l la pagina cu cele 2 butoane (AdminHome)
  if (user.role === "ADMIN" || user.role === "ROLE_ADMIN") {
    return <Navigate to="/app/admin" replace />;
  }
  // dacă e user normal, du-l la dashboard
  return <Navigate to="/app/dashboard" replace />;
}

// =====================
// Componenta principală
// =====================
export default function App() {
  return (
    <Routes>
      {/* mereu login/register la / */}
      <Route path="/" element={<AuthPage />} />

      {/* dashboard protejat (pentru user logat) */}
      <Route
        path="/app"
        element={
          <RequireAuth>
            <AppHomeRedirect />
          </RequireAuth>
        }
      />


      <Route
        path="/app/dashboard"
        element={
          <RequireAuth>
            <Dashboard />
          </RequireAuth>
        }
      />

      <Route
        path="/app/admin"
        element={
          <RequireAdmin>
            <AdminHome />
          </RequireAdmin>
        }
     />

      {/* CRUD utilizatori doar pentru admin */}
      <Route
        path="/app/admin/users"
        element={
          <RequireAdmin>
            <AdminUsers />
          </RequireAdmin>
        }

      />

      <Route
        path="/app/admin/devices"
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
