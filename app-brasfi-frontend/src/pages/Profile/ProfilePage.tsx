import React, { useState, useEffect } from 'react';
import '../../styles/global.css'; // Pode ser útil para estilos globais
import '../../styles/profile.css'; // Criaremos este arquivo para os estilos da página de perfil

// Define um estilo básico para o container da página de perfil
const profilePageStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  minHeight: 'calc(100vh - 60px)', // Ajustar '60px' se a altura do seu header for diferente
  padding: '2rem',
  backgroundColor: 'var(--dark, #1a1a1a)', // Usando variável de cor com fallback
  color: 'var(--white, #ffffff)', // Usando variável de cor com fallback
};

const headingStyle: React.CSSProperties = {
  fontSize: '2rem',
  marginBottom: '1rem',
  color: 'var(--primary, #3498db)', // Usando variável de cor com fallback
};

const placeholderTextStyle: React.CSSProperties = {
  fontSize: '1.2rem',
  textAlign: 'center',
};

// Simulação de dados do usuário - no futuro, viria de uma API/contexto
const mockUserData = {
  name: 'Usuário Exemplo',
  email: 'usuario@exemplo.com',
  description: 'Esta é uma descrição de exemplo sobre mim. Gosto de programar e tomar café.',
};

const ProfilePage: React.FC = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [description, setDescription] = useState('');

  // Carregar dados do usuário ao montar o componente (simulação)
  useEffect(() => {
    setName(mockUserData.name);
    setEmail(mockUserData.email);
    setDescription(mockUserData.description);
  }, []);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (password !== confirmPassword) {
      alert('As senhas não coincidem!');
      return;
    }
    // Lógica para enviar os dados atualizados para a API
    console.log('Dados do perfil atualizados:', {
      name,
      email,
      password, // Enviar a senha apenas se ela foi alterada e validada
      description,
    });
    alert('Perfil atualizado com sucesso! (Simulação)');
    // Limpar campos de senha após o envio
    setPassword('');
    setConfirmPassword('');
  };

  return (
    <div className="profile-page-container">
      <div className="profile-card">
        <h1 className="profile-title">Editar Perfil</h1>
        <form onSubmit={handleSubmit} className="profile-form">
          <div className="form-group">
            <label htmlFor="name">Nome</label>
            <input
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Nova Senha (deixe em branco para não alterar)</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirmar Nova Senha</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              disabled={!password} // Desabilita se a nova senha não estiver preenchida
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Descrição</label>
            <textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={4}
            />
          </div>

          <button type="submit" className="submit-button">
            Salvar Alterações
          </button>
        </form>
      </div>
    </div>
  );
};

export default ProfilePage; 