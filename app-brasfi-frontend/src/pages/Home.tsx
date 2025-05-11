import logo from '../assets/round_logo.png';
import '../styles/global.css'; // Importando estilos globais
import '../styles/home.css';   // Importando estilos específicos da Home

export default function Home() {
  return (
    <div className="home-container">
      <div className="sidebar">
        {/* Cabeçalho: logo + nome */}
        <div className="logo-container">
          <img src={logo} alt="Logo da Empresa" className="logo" />
          <h2 className="logo-name">Brasfi</h2>
        </div>

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
  );
}
