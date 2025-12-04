import { useEffect, useState } from "react";
import { useAuth } from "../auth/AuthContext";
import { apiGetUserByUsername, apiListDevicesByUser } from "../api";
import DailyConsumptionChart from "./DailyConsumptionChart";

export default function MyDevices() {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [devices, setDevices] = useState([]);
  const [error, setError] = useState(null);

  const [selectedDevice, setSelectedDevice] = useState(null);

  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        setError(null);

        // luăm user-ul curent după username
        const u = await apiGetUserByUsername(user.username);

        // luăm device-urile asignate user-ului
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
  if (error) return <p style={{ color: "crimson" }}>Eroare: {error}</p>;
  if (!devices.length) return <p>Nu ai device-uri asignate.</p>;

  return (
    <>
      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th align="left">ID</th>
            <th align="left">Name</th>
            <th align="left">Max</th>
            <th align="left">Description</th>
            <th align="left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {devices.map((d) => (
            <tr key={d.id}>
              <td>{d.id}</td>
              <td>{d.name}</td>
              <td>{d.maxConsumption}</td>
              <td>{d.description}</td>
              <td>
                <button type="button" onClick={() => setSelectedDevice(d)}>
                  View chart
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedDevice && <DailyConsumptionChart device={selectedDevice} />}
    </>
  );
}
