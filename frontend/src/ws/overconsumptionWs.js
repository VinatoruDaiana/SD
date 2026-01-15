// src/ws/overconsumptionWs.js
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export function createOverconsumptionClient({ userId, onAlert, onStatus }) {
  const client = new Client({
    webSocketFactory: () => new SockJS("/ws"),

    reconnectDelay: 5000,
    heartbeatIncoming: 10000,
    heartbeatOutgoing: 10000,

    onConnect: () => {
      console.log("✅ WebSocket connected!");
      onStatus?.("connected");

      const destination = `/topic/overconsumption/${userId}`;
      console.log(`📡 Subscribing to: ${destination}`);

      client.subscribe(destination, (message) => {
        console.log("📨 Received alert:", message.body);
        const alert = JSON.parse(message.body);
        onAlert?.(alert);
      });
    },

    onStompError: (frame) => {
      console.error("❌ STOMP error:", frame.headers["message"], frame.body);
      onStatus?.("error");
    },

    onWebSocketClose: () => {
      console.log("🔌 WebSocket disconnected");
      onStatus?.("disconnected");
    },

    debug: (str) => {
      console.log("🐛 STOMP:", str);
    }
  });

  return client;
}