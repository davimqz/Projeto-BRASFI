import logo from '../assets/round_logo.png';
import '../styles/global.css'; // Importando estilos globais
import '../styles/home.css';   // Importando estilos específicos da Home
import { Link } from 'react-router-dom'; // Importar Link

export default function Home() {
  return (
    <div className="home-container">
      {/* Header Superior */}
      <header className="top-header">
        <div className="logo-container-header">
          <img src={logo} alt="Logo Brasfi" className="logo-header" />
          <h2 className="logo-name-header">Brasfi</h2>
        </div>
        <Link to="/profile" className="avatar-link">
          <div className="avatar-placeholder">
            {/* Pode ser uma imagem de avatar ou iniciais do usuário no futuro */}
          </div>
        </Link>
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
