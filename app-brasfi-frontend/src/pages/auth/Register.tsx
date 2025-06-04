import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../../services/authService';
import type { RegisterData } from '../../types/auth';
import { AxiosError } from 'axios';
import './Auth.css';

interface ErrorResponse {
  message?: string;
  error?: string;
}

export function Register() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<RegisterData>({
    name: '',
    username: '',
    email: '',
    passwordHash: '',
    description: ''
  });
  const [error, setError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    
    try {
      await authService.register(formData);
      navigate('/login');
    } catch (error: unknown) {
      const axiosError = error as AxiosError<ErrorResponse>;
      const errorMessage = axiosError.response?.data?.message || 
                         axiosError.response?.data?.error ||
                         'Erro ao criar conta. Verifique os dados e tente novamente.';
      console.error('Detalhes do erro:', error);
      setError(errorMessage);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Criar Conta</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">Nome</label>
            <input
              type="text"
              id="name"
              name="name"
              className="form-control"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="username">Usuário</label>
            <input
              type="text"
              id="username"
              name="username"
              className="form-control"
              value={formData.username}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              className="form-control"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="passwordHash">Senha</label>
            <input
              type="password"
              id="passwordHash"
              name="passwordHash"
              className="form-control"
              value={formData.passwordHash}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="description">Descrição (opcional)</label>
            <textarea
              id="description"
              name="description"
              className="form-control"
              value={formData.description}
              onChange={handleChange}
            />
          </div>
          {error && <p className="error-message">{error}</p>}
          <button type="submit" className="btn btn-primary">
            Criar Conta
          </button>
        </form>
        <p className="auth-link">
          Já tem uma conta?{' '}
          <a href="/login" onClick={(e) => { e.preventDefault(); navigate('/login'); }}>
            Faça login
          </a>
        </p>
      </div>
    </div>
  );
} 