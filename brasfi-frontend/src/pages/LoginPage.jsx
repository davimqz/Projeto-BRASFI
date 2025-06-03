import React, { useState } from 'react';
import ForgotPasswordModal from '../components/ForgotPasswordModal';

export default function LoginPage() {
  const [login, setLogin] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const [modalAberto, setModalAberto] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setErro('');

    try {
      const response = await fetch('http://localhost:3001/auth', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ login, senha }),
      });

      const data = await response.json();

      if (response.ok) {
        localStorage.setItem('token', data.token);
        alert('Login realizado!');
      } else {
        setErro(data.mensagem || 'Credenciais inválidas');
      }
    } catch (error) {
      setErro('Erro de conexão com o servidor.');
    }
  };

  return (
    <div style={estilos.container}>
      <h2>Entrar</h2>
      <form onSubmit={handleLogin} style={estilos.form}>
        <input
          type="text"
          placeholder="Login"
          value={login}
          onChange={(e) => setLogin(e.target.value)}
          required
          style={estilos.input}
        />
        <input
          type="password"
          placeholder="Senha"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
          required
          style={estilos.input}
        />
        <button type="submit" style={estilos.botao}>Entrar</button>
      </form>
      {erro && <p style={estilos.erro}>{erro}</p>}
      <p>
        <button onClick={() => setModalAberto(true)} style={estilos.link}>Esqueceu a senha?</button>
      </p>
      {modalAberto && <ForgotPasswordModal onClose={() => setModalAberto(false)} />}
    </div>
  );
}

const estilos = {
  container: { maxWidth: 320, margin: 'auto', padding: 20, textAlign: 'center' },
  form: { display: 'flex', flexDirection: 'column', gap: 10 },
  input: { padding: 10, borderRadius: 4, border: '1px solid #ccc' },
  botao: { padding: 10, background: '#0d6efd', color: '#fff', border: 'none', borderRadius: 4 },
  link: { background: 'none', border: 'none', color: '#0d6efd', cursor: 'pointer', textDecoration: 'underline' },
  erro: { color: 'red' },
};
