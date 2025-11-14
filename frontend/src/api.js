// ==== BASE-uri relative (Traefik, same-origin) ====
const AUTH_BASE = import.meta.env.VITE_AUTH_BASE || "/auth";
console.log("AUTH_BASE =", AUTH_BASE); 
const USER_BASE  = import.meta.env.VITE_USERS_BASE || "/users";
const DEVICE_BASE = import.meta.env.VITE_DEVICES_BASE || "/devices";




// ==== LocalStorage keys ====
const TOKEN_KEY = "auth.token";
const USER_KEY  = "auth.user";

// ==== Helpers token & user ====
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

// ==== Authorization header (din TOKEN_KEY) ====
export function authHeaders() {
  const t = getToken();
  return t ? { Authorization: `Bearer ${t}` } : {};
}

// ---- JWT decode (optional) ----
function parseJwt(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64).split("").map(c => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2)).join("")
    );
    return JSON.parse(jsonPayload);
  } catch {
    return {};
  }
}

// ===== AUTH API =====
export async function apiLogin({ username, password }) {
  const res = await fetch(`${AUTH_BASE}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });
  if (!res.ok) throw new Error(`Login failed (${res.status}): ${await res.text()}`);
  const { token } = await res.json();
  if (!token) throw new Error("No token received from backend");

  // salvează tokenul pentru authHeaders()
  setToken(token);
  // opțional: deduce userul din token
  const payload = parseJwt(token);
  setUser(payload?.sub ? { username: payload.sub } : null);

  return { token };
}

export async function apiRegister({ username, email, password, role = "USER" }) {
  const res = await fetch(`${AUTH_BASE}/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, email, password, userRole: role }),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function apiValidate() {
  const t = getToken();
  if (!t) throw new Error("No token");
  const res = await fetch(`${AUTH_BASE}/validate?token=${encodeURIComponent(t)}`);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// ===== USERS API =====
export async function apiListUsers() {
  const res = await fetch(`${USER_BASE}`, { headers: { ...authHeaders() } });
  if (!res.ok) throw new Error(`List users failed: ${res.status}`);
  return res.json();
}
export async function apiCreateUser(body) {
  const res = await fetch(`${USER_BASE}`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error(`Create failed: ${res.status}`);
  return res.json();
}
export async function apiUpdateUser(id, body) {
  const res = await fetch(`${USER_BASE}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error(`Update failed: ${res.status}`);
  return res.json();
}
export async function apiDeleteUser(id) {
  const res = await fetch(`${USER_BASE}/${id}`, {
    method: "DELETE",
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Delete failed: ${res.status}`);
}
export async function apiGetUserByUsername(username) {
  const res = await fetch(`${USER_BASE}/by-username/${encodeURIComponent(username)}`, {
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Get user by username failed: ${res.status}`);
  return res.json();
}

// ===== DEVICES API =====
export async function apiListDevices() {
  const res = await fetch(`${DEVICE_BASE}`, { headers: { ...authHeaders() } });
  if (!res.ok) throw new Error(`List devices failed: ${res.status}`);
  return res.json();
}
export async function apiCreateDevice(body) {
  const res = await fetch(`${DEVICE_BASE}`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error(`Create device failed: ${res.status}`);
  return res.json();
}
export async function apiUpdateDevice(id, body) {
  const res = await fetch(`${DEVICE_BASE}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error(`Update device failed: ${res.status}`);
  return res.json();
}
export async function apiDeleteDevice(id) {
  const res = await fetch(`${DEVICE_BASE}/${id}`, {
    method: "DELETE",
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Delete device failed: ${res.status}`);
}
export async function apiAssignDevice(id, userId) {
  const res = await fetch(`${DEVICE_BASE}/${id}/assign/${userId}`, {
    method: "PATCH",
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Assign failed: ${res.status}`);
  return res.json();
}
export async function apiUnassignDevice(id) {
  const res = await fetch(`${DEVICE_BASE}/${id}/unassign`, {
    method: "PATCH",
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`Unassign failed: ${res.status}`);
  return res.json();
}
export async function apiListDevicesByUser(userId) {
  // DOAR această versiune, fără duplicat
  const res = await fetch(`${DEVICE_BASE}?userId=${encodeURIComponent(userId)}`, {
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(`List devices by user failed: ${res.status}`);
  return res.json();
}
