
import { useNavigate } from "react-router-dom";

export default function AdminHome() {
  const navigate = useNavigate();
   return (
    <div style={{ padding: "40px 16px" }}>
      <div
        style={{
          maxWidth: 600,
          margin: "0 auto",
          padding: 32,
          borderRadius: 16,
          boxShadow: "0 4px 16px rgba(0,0,0,0.1)",
          background: "white",
          textAlign: "center",
        }}
      >
        <h2 style={{ marginBottom: 32 }}>Admin — acțiuni</h2>

        <button
          style={{
            display: "block",
            width: "100%",
            padding: "12px 16px",
            marginBottom: 16,
            borderRadius: 999,
            border: "1px solid #ccc",
            background: "white",
            cursor: "pointer",
            fontWeight: 600,
            color: "purple",
          }}
          onClick={() => {
            console.log("[AdminHome] go to /admin/users");
            navigate("/admin/users");
          }}
        >
          Operații CRUD pe useri
        </button>

        <button
          style={{
            display: "block",
            width: "100%",
            padding: "12px 16px",
            borderRadius: 999,
            border: "1px solid #ccc",
            background: "white",
            cursor: "pointer",
            fontWeight: 600,
            color: "purple",
          }}
          onClick={() => {
            console.log("[AdminHome] go to /admin/devices");
            navigate("/admin/devices");
          }}
        >
          Operații CRUD pe devices
        </button>
      </div>
    </div>
  );
}
