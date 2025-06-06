import axios, { AxiosError } from 'axios';
import type { LoginCredentials, RegisterData, AuthResponse } from '../types/auth';

// Usando o proxy reverso do Nginx
const API_URL = '';

// Configuração global do axios
const axiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  withCredentials: true
});

// Interceptor para logs de desenvolvimento
axiosInstance.interceptors.request.use(request => {
  console.log('Enviando requisição:', {
    url: request.url,
    method: request.method,
    data: request.data,
    headers: request.headers,
    withCredentials: request.withCredentials
  });
  return request;
});

// Interceptor para logs de resposta
axiosInstance.interceptors.response.use(
  response => {
    console.log('Resposta recebida:', {
      status: response.status,
      statusText: response.statusText,
      data: response.data,
      headers: response.headers
    });
    return response;
  },
  error => {
    console.error('Erro na requisição:', error);
    if (error.response) {
      console.error('Detalhes do erro:', {
        status: error.response.status,
        statusText: error.response.statusText,
        data: error.response.data,
        headers: error.response.headers
      });
    }
    throw error;
  }
);

export const authService = {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    try {
      const response = await axiosInstance.post('/auth/login', credentials);
      return response.data;
    } catch (error: unknown) {
      const axiosError = error as AxiosError;
      console.error('Erro no login:', axiosError.response?.data || axiosError.message);
      throw error;
    }
  },

  async register(data: RegisterData): Promise<AuthResponse> {
    try {
      console.log('Tentando registrar com:', data);
      const response = await axiosInstance.post('/member', data);
      return response.data;
    } catch (error: unknown) {
      const axiosError = error as AxiosError;
      console.error('Erro no registro:', axiosError.response?.data || axiosError.message);
      if (axiosError.response) {
        console.error('Detalhes do erro:', {
          data: axiosError.response.data,
          status: axiosError.response.status,
          headers: axiosError.response.headers
        });
      }
      throw error;
    }
  },

  async logout(): Promise<void> {
    localStorage.removeItem('token');
    delete axiosInstance.defaults.headers.common['Authorization'];
  },

  getToken(): string | null {
    return localStorage.getItem('token');
  },

  setToken(token: string): void {
    localStorage.setItem('token', token);
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  }
}; 