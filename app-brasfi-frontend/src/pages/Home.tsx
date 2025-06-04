import React, { useState, useEffect } from 'react';
import axios from 'axios';
import logo from '../assets/round_logo.png';
import profilePhoto from '../assets/profile_photo_sample.png';
import '../styles/global.css'; // Importando estilos globais
import '../styles/home.css';   // Importando estilos específicos da Home
import { Link } from 'react-router-dom'; // Importar Link

const API_URL = '/api'; // Consistente com ProfilePage

export default function Home() {
  const [userName, setUserName] = useState<string | null>(null);

  useEffect(() => {
    const fetchUserName = async () => {
      const token = localStorage.getItem('token');
      const userId = localStorage.getItem('userId');

      if (token && userId) {
        try {
          const response = await axios.get(`${API_URL}/member/${userId}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          if (response.data && response.data.name) {
            const fullName = response.data.name;
            const firstName = fullName.split(' ')[0]; // Pega o primeiro nome
            setUserName(firstName);
          }
        } catch (error) {
          console.error("Erro ao buscar nome do usuário para Home:", error);
          // Não precisa mostrar erro na UI da Home, apenas loga
        }
      }
    };
    fetchUserName();
  }, []);

  return (
    <div className="home-container">
      {/* Header Superior */}
      <header className="top-header">
        <div className="logo-container-header">
          <img src={logo} alt="Logo Brasfi" className="logo-header" />
          <h2 className="logo-name-header">Brasfi</h2>
        </div>
        <div className="user-greeting-avatar-container"> {/* Novo container para agrupar saudação e avatar */}
          {userName && <span className="user-greeting">Olá, {userName}</span>}
          <Link to="/profile" className="avatar-link">
            <img src={profilePhoto} alt="Avatar do usuário" className="avatar-placeholder" />
          </Link>
        </div>
      </header>

      <div className="main-content-area"> {/* Novo container para sidebar e content */}
        <div className="sidebar">
          {/* Cabeçalho: logo + nome - Removido daqui, movido para o top-header */}
          {/* 
          <div className="logo-container">
            <img src={logo} alt="Logo da Empresa" className="logo" />
            <h2 className="logo-name">Brasfi</h2>
          </div> 
          */}

          {/* Lista de comunidades */}
          <ul className="community-list">
            <li className="community-item">Comunidade 1</li>
            <li className="community-item">Comunidade 2</li>
            <li className="community-item">Comunidade 3</li>
            <li className="community-item">Comunidade 4</li>
            <li className="community-item">Comunidade 5</li>
          </ul>
        </div>

        {/* Conteúdo principal da página */}
        <div className="content">
          <h1 className="header">Feed?</h1>
        </div>
      </div>
    </div>
  );
}
