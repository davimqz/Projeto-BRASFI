import React, { useState } from 'react';

export default function ForgotPasswordModal({ onClose }) {
  const [email, setEmail] = useState('');
  const [mensagem, setMensagem] = useState('');

  const handleEnviar = () => {
    // Aqui simula envio. Pode usar fetch se tiver backend.
    setMensagem(`Se houver uma conta associada a ${email}, você receberá instruções em instantes.`);
  };

  return (
    <div style={estilos.overlay}>
      <div style={estilos.modal}>
        <h3>Recuperar senha</h3>
        <input
          type="email"
          placeholder="Digite seu e-mail"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          style={estilos.input}
        />
        <button onClick={handleEnviar} style={estilos.botao}>Enviar</button>
        <button onClick={onClose} style={estilos.fechar}>Fechar</button>
        {mensagem && <p>{mensagem}</p>}
      </div>
    </div>
  );
}

const estilos = {
  overlay: {
    position: 'fixed', top: 0, left: 0, width: '100%', height: '100%',
    backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center',
  },
  modal: {
    background: '#fff', padding: 20, borderRadius: 10, width: 300, textAlign: 'center',
  },
  input: { padding: 10, margin: '10px 0', width: '100%' },
  botao: { padding: 10, width: '100%', background: '#0d6efd', color: '#fff', border: 'none', borderRadius: 4 },
  fechar: { marginTop: 10, background: 'transparent', color: '#555', border: 'none', cursor: 'pointer' },
};
