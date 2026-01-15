// src/ws/OverconsumptionContext.jsx
import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { createOverconsumptionClient } from "./overconsumptionWs";
import { useAuth } from "../auth/AuthContext";
import { apiGetUserByUsername } from "../api";

const OverCtx = createContext(null);

export function OverconsumptionProvider({ children }) {
  const { user } = useAuth();
  const [alerts, setAlerts] = useState([]);
  const [status, setStatus] = useState("disconnected");
  const [userId, setUserId] = useState(null);

  // Obține userId-ul
  useEffect(() => {
    if (user?.username) {
      apiGetUserByUsername(user.username)
        .then((userData) => {
          console.log("✅ Got userId:", userData.id);
          setUserId(userData.id);
        })
        .catch((err) => console.error("❌ Failed to get userId:", err));
    }
  }, [user]);

  // Conectează WebSocket când avem userId
  useEffect(() => {
    if (!userId) {
      console.log("⏳ Waiting for userId...");
      return;
    }

    console.log("🔌 Connecting WebSocket for user:", userId);

    const client = createOverconsumptionClient({
      userId: userId,
      onStatus: setStatus,
      onAlert: (alert) => {
        console.log("🚨 New alert received:", alert);
        setAlerts((prev) => [{ ...alert, receivedAt: Date.now() }, ...prev].slice(0, 50));
      },
    });

    client.activate();

    return () => {
      console.log("🔌 Disconnecting WebSocket");
      client.deactivate();
    };
  }, [userId]);

  const value = useMemo(() => ({ alerts, status, clear: () => setAlerts([]) }), [alerts, status]);

  return <OverCtx.Provider value={value}>{children}</OverCtx.Provider>;
}

export function useOverconsumption() {
  const ctx = useContext(OverCtx);
  if (!ctx) throw new Error("useOverconsumption must be used inside OverconsumptionProvider");
  return ctx;
}