// ==== BASE-uri relative (Traefik, same-origin) ====
const AUTH_BASE = "/api/auth";    
const USER_BASE = "/api/users";
const DEVICE_BASE = "/api/devices";
const MONITORING_BASE = "/api/monitoring";



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

export async function apiLogin({ username, password }) {
  console.log("🟡 apiLogin called with", { username, password });

  const res = await fetch(`${AUTH_BASE}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });

  console.log("🟡 apiLogin response status:", res.status);

  const data = await res.json().catch((err) => {
    console.error("🟡 apiLogin: cannot parse JSON", err);
    throw new Error("Cannot parse login response JSON");
  });

  console.log("🟡 apiLogin response JSON:", data);

  if (!res.ok) {
    throw new Error(`Login failed (${res.status})`);
  }

  const token = data.token || data.accessToken;
  if (!token) {
    throw new Error("No token received from backend");
  }

  // salvează tokenul pentru authHeaders()
  setToken(token);

  // deduce user-ul din JWT (payload)
  const payload = parseJwt(token) || {};
  const user = payload.sub
    ? {
        username: payload.sub,
        role: payload.role || (Array.isArray(payload.roles) ? payload.roles[0] : undefined),
      }
    : null;

  setUser(user);

  console.log("🟡 apiLogin: built user from token:", user);

  // IMPORTANT: întoarcem și user, nu doar token
  return { token, user };
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



// ======= MONITORING API============
export async function apiGetDailyConsumption(deviceId, date) {
  // date trebuie să fie string de forma YYYY-MM-DD
  const res = await fetch(
    `${MONITORING_BASE}/consumption?deviceId=${encodeURIComponent(
      deviceId
    )}&date=${encodeURIComponent(date)}`,
    {
      headers: {
        ...authHeaders(),   // folosim JWT-ul existent
      },
    }
  );

  if (!res.ok) {
    throw new Error(`Get daily consumption failed: ${res.status}`);
  }

  // returnează o listă de obiecte: { hour: 0..23, energyKwh: number }
  return res.json();
}
