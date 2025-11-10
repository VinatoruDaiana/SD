// src/pages/AdminHome.jsx
import { Link } from "react-router-dom";

export default function AdminHome() {
  return (
    <div style={{
      minHeight: "100vh",
      display: "grid",
      placeItems: "center",
      background: "#f7f7f7",
      padding: 24
    }}>
      <div style={{
        width: 420,
        maxWidth: "90vw",
        background: "white",
        border: "1px solid #e5e5e5",
        borderRadius: 12,
        padding: 32,
        boxShadow: "0 10px 30px rgba(0,0,0,0.06)",
        textAlign: "center"
      }}>
        <h2 style={{ marginBottom: 24 }}>Admin — acțiuni</h2>

        <div style={{ display: "grid", gap: 16 }}>
          <Link
            to="/app/admin/users"
            style={{
              display: "block",
              padding: "14px 18px",
              borderRadius: 10,
              border: "1px solid #ddd",
              textDecoration: "none",
              fontWeight: 600
            }}
          >
            Operații CRUD pe useri
          </Link>

          <Link
            to="/app/admin/devices"
            style={{
              display: "block",
              padding: "14px 18px",
              borderRadius: 10,
              border: "1px solid #ddd",
              textDecoration: "none",
              fontWeight: 600
            }}
          >
            Operații CRUD pe devices
          </Link>
        </div>
      </div>
    </div>
  );
}
