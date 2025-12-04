import { Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import MyDevices from "../components/MyDevices";

export default function Dashboard() {
  const { user } = useAuth();

  return (
    <div style={{ padding: 24 }}>
      <h1>Dashboard</h1>
      <p>Salut, <b>{user?.username}</b> ({user?.role})</p>

      {user?.role === "ADMIN" && (
        <>
          <hr />
          <h3>Admin</h3>
          <ul>
            <li><Link to="/app/admin/users">Manage Users (CRUD)</Link></li>
          </ul>
        </>
      )}

      <hr />
      <h3>Device-urile mele</h3>
      <MyDevices />
    </div>
  );
}

