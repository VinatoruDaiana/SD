
import { useEffect, useMemo, useState } from "react";
import {
  apiListUsers,
  apiCreateUser,
  apiUpdateUser,
  apiDeleteUser,
} from "../api";

export default function AdminUsers() {
const emptyForm = useMemo(() => ({ id: null, username: "", role: "USER", password: "" }), [])
  const [users, setUsers] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function load() {
    setLoading(true);
    setError("");
    try {
      const data = await apiListUsers();
      setUsers(data);
    } catch (e) {
      setError("Nu pot încărca utilizatorii.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  function startEdit(u) {
    setForm({
      id: u.id,
      username: u.username ?? "",
      role: u.role ?? "USER",
      password: "", 
    });
  }

  function cancelEdit() {
    setForm(emptyForm);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError("");

    const payload = {
      username: form.username,
      role: form.role,
      ...(form.password ? { password: form.password } : {}),
    };

    try {
      if (form.id == null) {
        await apiCreateUser(payload);
      } else {
        await apiUpdateUser(form.id, payload);
      }
      await load();
      setForm(emptyForm);
    } catch (e) {
      setError("Operația a eșuat. Verifică backend-ul și CORS.");
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(id) {
    if (!confirm("Ștergi acest utilizator?")) return;
    setLoading(true);
    setError("");
    try {
      await apiDeleteUser(id);
      await load();
    } catch (e) {
      setError("Nu pot șterge utilizatorul.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: 24 }}>
      <h2>Users – Admin</h2>

      {error && (
        <div style={{ background: "#ffe5e5", padding: 8, marginBottom: 12 }}>
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} style={{ marginBottom: 24 }}>
        <fieldset disabled={loading} style={{ border: "1px solid #ddd", padding: 12 }}>
          <legend>{form.id == null ? "Create user" : `Edit user #${form.id}`}</legend>

          <div style={{ marginBottom: 8 }}>
            <label style={{ width: 100, display: "inline-block" }}>Username</label>
            <input
              value={form.username}
              onChange={(e) => setForm((f) => ({ ...f, username: e.target.value }))}
              required
            />
          </div>

          <div style={{ marginBottom: 8 }}>
            <label style={{ width: 100, display: "inline-block" }}>Role</label>
            <select
              value={form.role}
              onChange={(e) => setForm((f) => ({ ...f, role: e.target.value }))}
            >
              <option value="ADMIN">ADMIN</option>
              <option value="USER">USER</option>
            </select>
          </div>

          <div style={{ marginBottom: 8 }}>
            <label style={{ width: 100, display: "inline-block" }}>
              Password{form.id != null ? " (leave blank to keep)" : ""}
            </label>
            <input
              type="password"
              value={form.password}
              onChange={(e) => setForm((f) => ({ ...f, password: e.target.value }))}
              placeholder={form.id != null ? "unchanged if blank" : ""}
            />
          </div>

          <div style={{ display: "flex", gap: 8 }}>
            <button type="submit" disabled={loading}>
              {form.id == null ? "Create" : "Save"}
            </button>
            {form.id != null && (
              <button type="button" onClick={cancelEdit}>
                Cancel
              </button>
            )}
          </div>
        </fieldset>
      </form>

      <hr />

      <h3>Lista utilizatori</h3>
      {loading && <p>Se încarcă…</p>}
      {!loading && (
        <table width="100%" border="1" cellPadding="6" style={{ borderCollapse: "collapse" }}>
          <thead style={{ background: "#f7f7f7" }}>
            <tr>
              <th align="left">ID</th>
              <th align="left">Username</th>
              <th align="left">Role</th>
              <th align="left" style={{ width: 160 }}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users?.length ? (
              users.map((u) => (
                <tr key={u.id}>
                  <td>{u.id}</td>
                  <td>{u.username}</td>
                  <td>{u.role}</td>
                  <td>
                    <button onClick={() => startEdit(u)} style={{ marginRight: 8 }}>
                      Edit
                    </button>
                    <button onClick={() => handleDelete(u.id)}>Delete</button>
                  </td>
                </tr>
              ))
            ) : (
              <tr><td colSpan="4">Niciun utilizator.</td></tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
