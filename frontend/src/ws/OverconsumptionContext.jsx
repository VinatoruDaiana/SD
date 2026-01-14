// src/ws/OverconsumptionContext.jsx
import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { createOverconsumptionClient } from "./overconsumptionWs";

const OverCtx = createContext(null);

export function OverconsumptionProvider({ children }) {
  const [alerts, setAlerts] = useState([]);
  const [status, setStatus] = useState("disconnected"); // connected|disconnected|error

  useEffect(() => {
    const client = createOverconsumptionClient({
      onStatus: setStatus,
      onAlert: (alert) => {
        // alert: { deviceId, date, hour, measuredKwh, maxAllowedKwh }
        setAlerts((prev) => [{ ...alert, receivedAt: Date.now() }, ...prev].slice(0, 50));
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  const value = useMemo(() => ({ alerts, status, clear: () => setAlerts([]) }), [alerts, status]);

  return <OverCtx.Provider value={value}>{children}</OverCtx.Provider>;
}

export function useOverconsumption() {
  const ctx = useContext(OverCtx);
  if (!ctx) throw new Error("useOverconsumption must be used inside OverconsumptionProvider");
  return ctx;
}
