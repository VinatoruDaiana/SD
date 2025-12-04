
import { useEffect, useMemo, useState } from "react";
import {
  apiListDevices,
  apiCreateDevice,
  apiUpdateDevice,
  apiDeleteDevice,
  apiAssignDevice,
  apiUnassignDevice,
} from "../api";

export default function AdminDevices() {
  const emptyForm = useMemo(() => ({ id: null, name: "", maxConsumption: "", description: "" }), []);
  const [devices, setDevices] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [assignMap, setAssignMap] = useState({}); 
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function load() {
    setLoading(true);
    setError("");
    try {
      const data = await apiListDevices();
      setDevices(data);
    } catch (e) {
      setError("Nu pot încărca devices.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  function onChange(e) {
    const { name, value } = e.target;
    setForm(f => ({ ...f, [name]: value }));
  }

  async function onCreate(e) {
    e.preventDefault();
    try {
      await apiCreateDevice({
        name: form.name.trim(),
        maxConsumption: form.maxConsumption ? Number(form.maxConsumption) : null,
        description: form.description?.trim() || ""
      });
      setForm(emptyForm);
      load();
    } catch (e) {
      setError(e.message);
    }
  }

  async function onUpdate(id) {
    const dev = devices.find(d => d.id === id);
    if (!dev) return;
    try {
      await apiUpdateDevice(id, {
        name: dev.name,
        maxConsumption: dev.maxConsumption,
        description: dev.description || ""
      });
      load();
    } catch (e) { setError(e.message); }
  }

  async function onDelete(id) {
    if (!confirm("Ștergi device-ul?")) return;
    try {
      await apiDeleteDevice(id);
      load();
    } catch (e) { setError(e.message); }
  }

  function editField(id, field, value) {
    setDevices(list => list.map(d => d.id === id ? { ...d, [field]: value } : d));
  }

  async function onAssign(id) {
    const userId = assignMap[id];
    if (!userId) return alert("Introdu un userId.");
    try {
      await apiAssignDevice(id, userId);
      setAssignMap(m => ({ ...m, [id]: "" }));
      load();
    } catch (e) { setError(e.message); }
  }

  async function onUnassign(id) {
    try {
      await apiUnassignDevice(id);
      load();
    } catch (e) { setError(e.message); }
  }

  return (
    <div style={{ padding: 24 }}>
      <h2>Devices – Admin</h2>

      <form onSubmit={onCreate} style={{ border: "1px solid #ddd", padding: 16, maxWidth: 600 }}>
        <h4>Create device</h4>
        <div style={{ display: "grid", gap: 8, gridTemplateColumns: "120px 1fr" }}>
          <label>Name</label>
          <input name="name" value={form.name} onChange={onChange} required />

          <label>Max consumption</label>
          <input name="maxConsumption" type="number" step="0.01" value={form.maxConsumption} onChange={onChange} />

          <label>Description</label>
          <input name="description" value={form.description} onChange={onChange} />
        </div>
        <button type="submit" style={{ marginTop: 12 }}>Create</button>
      </form>

      <hr />

      {loading ? <p>Loading…</p> : error ? <p style={{ color: "crimson" }}>{error}</p> : (
        <table style={{ borderCollapse: "collapse", width: "100%" }}>
          <thead>
            <tr>
              <th style={{ textAlign: "left" }}>ID</th>
              <th>Name</th>
              <th>Max</th>
              <th>Description</th>
              <th>UserId</th>
              <th>Actions</th>
              <th>Assign</th>
            </tr>
          </thead>
          <tbody>
            {devices.map(dev => (
              <tr key={dev.id}>
                <td style={{ fontFamily: "monospace" }}>{dev.id}</td>
                <td>
                  <input value={dev.name || ""} onChange={e => editField(dev.id, "name", e.target.value)} />
                </td>
                <td>
                  <input type="number" step="0.01"
                         value={dev.maxConsumption ?? ""}
                         onChange={e => editField(dev.id, "maxConsumption", e.target.value === "" ? null : Number(e.target.value))} />
                </td>
                <td>
                  <input value={dev.description || ""} onChange={e => editField(dev.id, "description", e.target.value)} />
                </td>
                <td style={{ fontFamily: "monospace" }}>{dev.userId || "-"}</td>
                <td>
                  <button onClick={() => onUpdate(dev.id)}>Save</button>{" "}
                  <button onClick={() => onDelete(dev.id)}>Delete</button>
                </td>
                <td>
                  <input
                    placeholder="userId"
                    value={assignMap[dev.id] || ""}
                    onChange={e => setAssignMap(m => ({ ...m, [dev.id]: e.target.value }))}
                    style={{ width: 280 }}
                  />
                  <button onClick={() => onAssign(dev.id)}>Assign</button>{" "}
                  <button onClick={() => onUnassign(dev.id)}>Unassign</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
