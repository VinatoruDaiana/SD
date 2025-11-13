import { useEffect, useState } from "react";
import { useAuth } from "../auth/AuthContext";
import { apiGetUserByUsername, apiListDevicesByUser } from "../api";


export default function MyDevices() {
  const { user } = useAuth(); // { username, role, ... }
  const [loading, setLoading] = useState(true);
  const [devices, setDevices] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        setError(null);

        // 1) ia userId după username
        const u = await apiGetUserByUsername(user.username); // { id, ... }

        // 2) ia device-urile userului
        const list = await apiListDevicesByUser(u.id);
        setDevices(list);
      } catch (e) {
        setError(e.message || String(e));
      } finally {
        setLoading(false);
      }
    })();
  }, [user?.username]);

  if (loading) return <p>Se încarcă device-urile…</p>;
  if (error) return <p style={{color:"crimson"}}>Eroare: {error}</p>;
  if (!devices.length) return <p>Nu ai device-uri asignate.</p>;

  return (
    <table style={{ width: "100%", borderCollapse: "collapse" }}>
      <thead>
        <tr>
          <th align="left">ID</th>
          <th align="left">Name</th>
          <th align="left">Max</th>
          <th align="left">Description</th>
        </tr>
      </thead>
      <tbody>
        {devices.map(d => (
          <tr key={d.id}>
            <td>{d.id}</td>
            <td>{d.name}</td>
            <td>{d.maxConsumption}</td>
            <td>{d.description}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
