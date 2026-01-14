// src/components/OverconsumptionAlerts.jsx
import { useOverconsumption } from "../ws/OverconsumptionContext";

export default function OverconsumptionAlerts() {
  const { alerts, status, clear } = useOverconsumption();

  return (
    <div style={{ border: "1px solid #ddd", padding: 12, borderRadius: 8, marginBottom: 16 }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h3 style={{ margin: 0 }}>Overconsumption alerts</h3>
        <div style={{ fontSize: 12 }}>
          WS: <b>{status}</b>
          {"  "}
          <button type="button" onClick={clear} style={{ marginLeft: 12 }}>
            Clear
          </button>
        </div>
      </div>

      {alerts.length === 0 ? (
        <p style={{ marginTop: 8 }}>No alerts yet.</p>
      ) : (
        <ul style={{ marginTop: 8 }}>
          {alerts.map((a, idx) => (
            <li key={a.receivedAt + "-" + idx}>
              <b>Device</b> {a.deviceId} — <b>{a.date}</b> @ <b>{a.hour}:00</b> —
              measured <b>{a.measuredKwh}</b> kWh (max <b>{a.maxAllowedKwh}</b> kWh)
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
