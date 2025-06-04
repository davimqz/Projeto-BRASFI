import React, { useState, useEffect } from 'react';
import axios, { AxiosError } from 'axios'; // Importar AxiosError
import { Link } from 'react-router-dom'; // Importar Link
import '../../styles/global.css'; // Pode ser útil para estilos globais
import '../../styles/profile.css'; // Criaremos este arquivo para os estilos da página de perfil

const API_URL = '/api'; // ATUALIZADO: Defina a URL base da sua API (usando o proxy)

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
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [description, setDescription] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const getAuthToken = () => localStorage.getItem('token'); // ATUALIZADO: chave do token
  const getUserId = () => localStorage.getItem('userId');

  // Carregar dados do usuário ao montar o componente
  useEffect(() => {
    const fetchUserData = async () => {
      const token = getAuthToken();
      const userId = getUserId();

      if (!token || !userId) {
        setError('Usuário não autenticado ou ID não encontrado.');
        setIsLoading(false);
        return;
      }

      try {
        setIsLoading(true);
        setSuccessMessage(null);
        setError(null);
        const response = await axios.get<{ name?: string; email?: string; description?: string }>(`${API_URL}/member/${userId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const userData = response.data;
        setName(userData.name || '');
        setEmail(userData.email || '');
        setDescription(userData.description || '');
      } catch (err: unknown) { // Tipar err como unknown
        console.error("Erro ao buscar dados do usuário:", err);
        // Pode-se adicionar uma verificação mais específica do erro se necessário
        // if (err instanceof AxiosError) { ... }
        setError('Falha ao carregar dados do perfil. Tente novamente mais tarde.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchUserData();
  }, []);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(null);
    setSuccessMessage(null);

    if (password && password !== confirmPassword) {
      setError('As senhas não coincidem!');
      return;
    }

    const token = getAuthToken();
    const userId = getUserId();

    if (!token || !userId) {
      setError('Usuário não autenticado. Não é possível salvar.');
      return;
    }

    // Definir um tipo para updatedData para maior clareza
    interface UpdatePayload {
      name: string;
      email: string;
      description: string;
      password?: string;
    }

    const updatedData: UpdatePayload = {
      name,
      email,
      description,
    };

    if (password) {
      updatedData.password = password;
    }

    try {
      setIsLoading(true);
      // A resposta do PUT pode não ser usada diretamente, mas podemos logá-la ou verificar o status
      /* const response = */ await axios.put(`${API_URL}/member/${userId}`, updatedData, {
        headers: { Authorization: `Bearer ${token}`,
                  'Content-Type': 'application/json' },
      });
      
      setSuccessMessage('Perfil atualizado com sucesso!');
      setPassword('');
      setConfirmPassword('');
    } catch (err: unknown) { // Tipar err como unknown
      console.error("Erro ao atualizar perfil:", err);
      if (axios.isAxiosError(err)) { // Usar type guard do Axios
        const axiosError = err as AxiosError<{ message?: string }>; // Especificar tipo do data do erro
        if (axiosError.response && axiosError.response.data && axiosError.response.data.message) {
          setError(`Falha ao atualizar perfil: ${axiosError.response.data.message}`);
        } else {
          setError('Falha ao atualizar perfil. Verifique os dados e tente novamente.');
        }
      } else {
        setError('Ocorreu um erro inesperado ao atualizar o perfil.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading && !name && !email) { // Ajustar condição de loading para ser mais robusta
    return (
      <div className="profile-page-container">
        <p className="loading-message">Carregando perfil...</p>
      </div>
    );
  }

  return (
    <div className="profile-page-container">
      <div className="back-button-wrapper">
        <Link to="/home" className="back-to-home-button top-left">
          &larr; Voltar para Home 
        </Link>
      </div>

      <div className="profile-card">
        <h1 className="profile-title">Editar Perfil</h1>
        
        {error && <p className="error-message">{error}</p>}
        {successMessage && <p className="success-message">{successMessage}</p>}
        
        <form onSubmit={handleSubmit} className="profile-form">
          <div className="form-group">
            <label htmlFor="name">Nome</label>
            <input
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              disabled={isLoading}
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
              disabled={isLoading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Nova Senha (deixe em branco para não alterar)</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={isLoading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirmar Nova Senha</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              disabled={!password || isLoading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Descrição</label>
            <textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={4}
              disabled={isLoading}
            />
          </div>

          <button type="submit" className="submit-button" disabled={isLoading}>
            {isLoading ? 'Salvando...' : 'Salvar Alterações'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default ProfilePage; 