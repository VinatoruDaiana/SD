
export default function RegisterForm({ onSubmit }) {
  return (
    <form onSubmit={onSubmit} style={{ display: "grid", gap: 12, maxWidth: 420 }}>
      <h2>Register</h2>

      <div>
        <label>Username</label>
        <input name="username" required />
      </div>

      <div>
        <label>Email (opțional)</label>
        <input name="email" type="email" placeholder="name@example.com" />
      </div>

      <div>
        <label>Parolă</label>
        <input name="password" type="password" required />
      </div>

      {}

      <button type="submit">Creează cont</button>
    </form>
  );
}
