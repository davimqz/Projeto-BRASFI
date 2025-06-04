export interface LoginCredentials {
  username: string;
  password: string;
}

export interface RegisterData {
  name: string;
  username: string;
  email: string;
  passwordHash: string;
  description?: string;
}

export interface AuthResponse {
  id: number;
  name: string;
  username: string;
  email: string;
  description?: string;
} 