import React from 'react';
import '../../styles/global.css'; // Pode ser útil para estilos globais

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

const ProfilePage: React.FC = () => {
  return (
    <div style={profilePageStyle}>
      <h1 style={headingStyle}>Página de Perfil</h1>
      <p style={placeholderTextStyle}>
        Conteúdo do perfil do usuário será exibido aqui.
        <br />
        (Em construção)
      </p>
      {/* Adicionar mais elementos e lógica aqui no futuro */}
    </div>
  );
};

export default ProfilePage; 