import { useEffect, useMemo, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useAuth } from "../auth/AuthContext";
import { Link } from "react-router-dom";

export default function SupportChatUser() {
  const { user } = useAuth(); // user.username trebuie să existe
  const userIdentifier = user?.username;

  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const [status, setStatus] = useState("disconnected");

  const client = useMemo(() => {
    const WS_BASE = import.meta.env.VITE_WS_BASE_URL || window.location.origin;

    return new Client({
      webSocketFactory: () => new SockJS(`${WS_BASE}/ws`),
      reconnectDelay: 3000,
      onConnect: () => setStatus("connected"),
      onWebSocketClose: () => setStatus("disconnected"),
    });
  }, []);

  useEffect(() => {
    if (!userIdentifier) return;

    client.onConnect = () => {
      setStatus("connected");

      client.subscribe(`/topic/chat/user/${userIdentifier}`, (frame) => {
        const msg = JSON.parse(frame.body);
        setMessages((prev) => [...prev, msg]);
      });
    };

    client.activate();
    return () => client.deactivate();
  }, [client, userIdentifier]);

  function send() {
    if (!text.trim()) return;

    const msg = {
      chatId: crypto.randomUUID?.() || String(Date.now()),
      userIdentifier,
      from: "USER",
      text,
      timestamp: Date.now(),
    };

    client.publish({
      destination: "/app/chat/send",
      body: JSON.stringify(msg),
    });

    setMessages((prev) => [...prev, msg]); // echo local
    setText("");
  }

  return (
    <div style={{ padding: 24 }}>
      <h2>Customer Support Chatbot</h2>
      <p>
        Te-ai conectat ca <b>USER</b>: <b>{user?.username}</b> — WS: <b>{status}</b>
      </p>

      <p><Link to="/app">← Back</Link></p>

      <div style={{ border: "1px solid #ddd", padding: 12, height: 360, overflow: "auto" }}>
        {messages.map((m, i) => (
          <div key={i} style={{ marginBottom: 10 }}>
            <b>{m.from}</b>: {m.text}
          </div>
        ))}
      </div>

      <div style={{ display: "flex", gap: 8, marginTop: 12 }}>
        <input
          style={{ flex: 1, padding: 10 }}
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && send()}
          placeholder="Type your message..."
        />
        <button onClick={send}>Send</button>
      </div>
    </div>
  );
}
