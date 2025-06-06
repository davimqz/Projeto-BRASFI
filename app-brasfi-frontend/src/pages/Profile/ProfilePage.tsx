import React, { useState, useEffect } from 'react';
import axios, { AxiosError } from 'axios'; // Importar AxiosError
import { Link } from 'react-router-dom'; // Importar Link
import '../../styles/global.css'; // Pode ser útil para estilos globais
import '../../styles/profile.css'; // Criaremos este arquivo para os estilos da página de perfil

const API_URL = '/api'; // ATUALIZADO: Defina a URL base da sua API (usando o proxy)

// Estilos removidos pois não estavam sendo utilizados e causavam erros de linter.
// const profilePageStyle: React.CSSProperties = { ... };
// const headingStyle: React.CSSProperties = { ... };
// const placeholderTextStyle: React.CSSProperties = { ... };

const ProfilePage: React.FC = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [description, setDescription] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const getAuthToken = () => localStorage.getItem('token');
  const getUserId = () => localStorage.getItem('userId');

  // Carregar dados do usuário ao montar o componente
  useEffect(() => {
    const token = getAuthToken();
    const userId = getUserId();
    console.log("ProfilePage useEffect: token from localStorage:", token);
    console.log("ProfilePage useEffect: userId from localStorage:", userId);

    if (!token || !userId || token === "undefined") {
      let authError = "Usuário não autenticado ou ID não encontrado.";
      if (token === "undefined") {
        authError += " (Problema: token é a string 'undefined')";
        console.error("ProfilePage useEffect: Token é a string 'undefined'. Verifique a resposta da API de login.");
      }
      setError(authError);
      setIsLoading(false);
      return;
    }

    const fetchUserData = async () => {
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
      } catch (err: unknown) {
        console.error("Erro ao buscar dados do usuário:", err);
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

    if (!name.trim() || !email.trim()) {
      setError('Nome e Email são campos obrigatórios e não podem estar em branco.');
      return;
    }

    const token = getAuthToken();
    const userId = getUserId();

    console.log("ProfilePage handleSubmit: token from localStorage:", token);
    console.log("ProfilePage handleSubmit: userId from localStorage:", userId);

    if (!token || !userId || token === "undefined") {
      let authError = "Usuário não autenticado. Não é possível salvar.";
      if (token === "undefined") {
        authError += " (Problema: token é a string 'undefined')";
        console.error("ProfilePage handleSubmit: Token é a string 'undefined'. Verifique a resposta da API de login e como o token é setado.");
      }
      if (!userId) {
        console.error("ProfilePage handleSubmit: userId não encontrado no localStorage.");
      }
      setError(authError);
      return;
    }

    interface UpdatePayload {
      name: string;
      email: string;
      description: string;
      newPassword?: string;
    }

    const updatedData: UpdatePayload = {
      name,
      email,
      description,
    };

    if (password && password !== confirmPassword) {
      setError('As senhas não coincidem!');
      return;
    }
    
    if (password) {
        updatedData.newPassword = password;
    }

    console.log("Enviando para PUT /member/" + userId + ":", updatedData);

    try {
      setIsLoading(true);
      await axios.put(`${API_URL}/member/${userId}`, updatedData, {
        headers: { 
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json' 
        },
      });
      
      setSuccessMessage('Perfil atualizado com sucesso!');
      setPassword('');
      setConfirmPassword('');
    } catch (err: unknown) {
      console.error("Erro ao atualizar perfil:", err);
      if (axios.isAxiosError(err)) {
        const axiosError = err as AxiosError<{ message?: string; error?: string; path?: string; status?: number; timestamp?: string }>;
        console.error("Detalhes do erro Axios:", axiosError.response?.data);
        if (axiosError.response && axiosError.response.data && axiosError.response.data.message) {
          setError(`Falha ao atualizar perfil: ${axiosError.response.data.message}`);
        } else if (axiosError.response && axiosError.response.data && typeof axiosError.response.data.error === 'string'){
          setError(`Falha ao atualizar perfil: ${axiosError.response.data.error} (status: ${axiosError.response.status})`);
        } else if (axiosError.response && axiosError.response.status === 401) {
          setError('Sua sessão pode ter expirado ou o token é inválido. Por favor, faça login novamente.');
        }else {
          setError('Falha ao atualizar perfil. Verifique os dados e tente novamente.');
        }
      } else {
        setError('Ocorreu um erro inesperado ao atualizar o perfil.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading && (!name && !email && !description && !error)) { 
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
              onChange={(e) => {setPassword(e.target.value); setError(null); setSuccessMessage(null);}}
              disabled={isLoading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirmar Nova Senha</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => {setConfirmPassword(e.target.value); setError(null); setSuccessMessage(null);}}
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