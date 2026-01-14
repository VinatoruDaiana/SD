
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export function createOverconsumptionClient({ onAlert, onStatus }) {
  const client = new Client({
  
    webSocketFactory: () => new SockJS("/ws"),

    reconnectDelay: 5000,
    heartbeatIncoming: 10000,
    heartbeatOutgoing: 10000,

    onConnect: () => {
      onStatus?.("connected");

      client.subscribe(`/topic/overconsumption/${userId}`, (message) => {
        const alert = JSON.parse(message.body);
        onAlert?.(alert);
      });
    },



    onStompError: (frame) => {
      console.error("STOMP error:", frame.headers["message"], frame.body);
      onStatus?.("error");
    },

    onWebSocketClose: () => {
      onStatus?.("disconnected");
    },
  });

  return client;
}
