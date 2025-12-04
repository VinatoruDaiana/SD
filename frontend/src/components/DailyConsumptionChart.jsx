// frontend/src/components/DailyConsumptionChart.jsx
import { useState } from "react";
import { apiGetDailyConsumption } from "../api";

export default function DailyConsumptionChart({ device }) {
  const [date, setDate] = useState(
    () => new Date().toISOString().slice(0, 10) // azi, in format YYYY-MM-DD
  );
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  async function handleLoad() {
    try {
      setLoading(true);
      setError(null);

      const result = await apiGetDailyConsumption(device.id, date);

      // ne asigurăm că sunt sortate după oră
      result.sort((a, b) => a.hour - b.hour);
      setData(result);
    } catch (e) {
      console.error(e);
      setError(e.message || "Failed to load consumption");
    } finally {
      setLoading(false);
    }
  }

  const hasData = data && data.length > 0;
  const maxEnergy = hasData
    ? Math.max(...data.map((p) => p.energyKwh ?? p.energyKWh ?? 0))
    : 0;

  return (
    <div
      style={{
        marginTop: 16,
        border: "1px solid #ddd",
        padding: 16,
        borderRadius: 8,
      }}
    >
      <h4>
        Hourly consumption for{" "}
        <span style={{ fontWeight: "bold" }}>{device.name}</span>
      </h4>

      <div
        style={{
          display: "flex",
          gap: 12,
          alignItems: "center",
          marginBottom: 12,
          flexWrap: "wrap",
        }}
      >
        <label>
          Select day:{" "}
          <input
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
          />
        </label>

        <button onClick={handleLoad} disabled={loading || !date}>
          {loading ? "Loading..." : "Load"}
        </button>
      </div>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {!loading && hasData && (
        <>
          {/* BAR CHART */}
          <div
            style={{
              display: "flex",
              alignItems: "flex-end",
              gap: 4,
              height: 200,
              borderBottom: "1px solid #ccc",
              paddingBottom: 8,
              marginBottom: 8,
              overflowX: "auto",
            }}
          >
            {data.map((p) => {
              const value = p.energyKwh ?? p.energyKWh ?? 0;
              const height = maxEnergy > 0 ? (value / maxEnergy) * 180 : 0;

              return (
                <div
                  key={p.hour}
                  style={{
                    flex: "0 0 24px",
                    textAlign: "center",
                  }}
                >
                  <div
                    style={{
                      margin: "0 auto",
                      width: "100%",
                      height,
                      borderRadius: 4,
                      border: "1px solid #4a4a4a",
                      background:
                        "linear-gradient(to top, rgba(0,150,255,0.7), rgba(0,150,255,0.3))",
                    }}
                    title={`${p.hour}:00 — ${value.toFixed(3)} kWh`}
                  />
                  <div style={{ marginTop: 4, fontSize: 10 }}>{p.hour}</div>
                </div>
              );
            })}
          </div>

          <div style={{ fontSize: 12, color: "#555" }}>
            <b>OX</b>: hours (0–23) &nbsp;|&nbsp; <b>OY</b>: energy value [kWh] for
            that hour
          </div>
        </>
      )}

      {!loading && !hasData && !error && (
        <p>No data for this day.</p>
      )}
    </div>
  );
}
