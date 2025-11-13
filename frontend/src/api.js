// Pentru Docker/Traefik, toate rutele sunt relative
const AUTH_BASE = import.meta.env.VITE_AUTH_BASE || "http://localhost:8083";
const USER_BASE = import.meta.env.VITE_USERS_BASE || "http://localhost:8081";
const DEVICE_BASE = import.meta.env.VITE_DEVICES_BASE || "http://localhost:8082";

console.log("AUTH_BASE =", AUTH_BASE);
console.log("USER_BASE =", USER_BASE);
console.log("DEVICE_BASE =", DEVICE_BASE);

const TOKEN_KEY = "auth.token";
const USER_KEY = "auth.user"; 

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}
export function setToken(token) {
  if (token) localStorage.setItem(TOKEN_KEY, token);
  else localStorage.removeItem(TOKEN_KEY);
}
export function getUser() {
  const raw = localStorage.getItem(USER_KEY);
  return raw ? JSON.parse(raw) : null;
}
export function setUser(u) {
  if (u) localStorage.setItem(USER_KEY, JSON.stringify(u));
  else localStorage.removeItem(USER_KEY);
}

export function logout() {
  setToken(null);
  setUser(null);
}

function parseJwt(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map(c => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    return JSON.parse(jsonPayload);
  } catch (err) {
    console.error("JWT decode error:", err);
    return {};
  }
}

export async function apiLogin({ username, password }) {
  const res = await fetch(`${AUTH_BASE}/api/auth/login`, {
    method: "POST",
    headers: { 
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ username, password }),
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(`Login failed (${res.status}): ${text}`);
  }

  const { token } = await res.json();
  if (!token) throw new Error("No token received from backend");

  const payload = parseJwt(token);

  const user = {
    username: payload.username || payload.sub || username,
    role: String(payload.role || payload.roles || "").toUpperCase(),
  };

  return { token, user };
}

export async function apiRegister({ username, email, password, role = "USER" }) {
  const res = await fetch(`${AUTH_BASE}/api/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, email, password, userRole: role }),
  });

  if (!res.ok) {
    let msg = "Registration failed";
    try {
      const j = await res.json();
      msg = j.error || j.message || JSON.stringify(j);
    } catch {}
    throw new Error(msg);
  }
  
  return await res.json();
}

export function authHeaders() {
  const token = getToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export async function apiListUsers() {
  const res = await fetch(`${USER_BASE}/api/users`, { headers: { ...authHeaders() } });
  if (!res.ok) throw new Error(`List users failed: ${res.status}`);
  return res.json();
}

export async function apiCreateUser({ username, role, password }) {
  const res = await fetch(`${USER_BASE}/api/users`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify({ username, role, password })
  });
  if (!res.ok) throw new Error(`Create failed: ${res.status}`);
  return res.json();
}

export async function apiUpdateUser(id, { username, role, password }) {
  const res = await fetch(`${USER_BASE}/api/users/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify({ username, role, password })
  });
  if (!res.ok) throw new Error(`Update failed: ${res.status}`);
  return res.json();
}

export async function apiDeleteUser(id) {
  const res = await fetch(`${USER_BASE}/api/users/${id}`, {
    method: "DELETE",
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Delete failed: ${res.status}`);
}

export async function apiListDevices() {
  const res = await fetch(`${DEVICE_BASE}/devices`, {
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`List devices failed: ${res.status}`);
  return res.json();
}

export async function apiCreateDevice({ name, maxConsumption, description }) {
  const res = await fetch(`${DEVICE_BASE}/devices`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify({ name, maxConsumption, description })
  });
  if (!res.ok) throw new Error(`Create device failed: ${res.status}`);
  return res.json();
}

export async function apiUpdateDevice(id, { name, maxConsumption, description }) {
  const res = await fetch(`${DEVICE_BASE}/devices/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify({ name, maxConsumption, description })
  });
  if (!res.ok) throw new Error(`Update device failed: ${res.status}`);
  return res.json();
}

export async function apiDeleteDevice(id) {
  const res = await fetch(`${DEVICE_BASE}/devices/${id}`, {
    method: "DELETE",
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Delete device failed: ${res.status}`);
}

export async function apiAssignDevice(id, userId) {
  const res = await fetch(`${DEVICE_BASE}/devices/${id}/assign/${userId}`, {
    method: "PATCH",
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Assign failed: ${res.status}`);
  return res.json();
}

export async function apiUnassignDevice(id) {
  const res = await fetch(`${DEVICE_BASE}/devices/${id}/unassign`, {
    method: "PATCH",
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Unassign failed: ${res.status}`);
  return res.json();
}

export async function apiGetUserByUsername(username) {
  const res = await fetch(`${USER_BASE}/api/users/by-username/${encodeURIComponent(username)}`, {
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Get user by username failed: ${res.status}`);
  return res.json(); 
}

export async function apiListDevicesByUser(userId) {
  const res = await fetch(`${DEVICE_BASE}/devices?userId=${encodeURIComponent(userId)}`, {
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`List devices by user failed: ${res.status}`);
  return res.json(); 
}