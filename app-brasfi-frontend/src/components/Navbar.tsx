import { Link } from 'react-router-dom';
import '../styles/navbar.css'; // Importe o CSS da navbar

export default function Navbar() {
  return (
    <nav className="navbar">
      <ul className="navbar-list">
        <li className="navbar-item">
          <Link to="/">Home</Link>
        </li>
        <li className="navbar-item">
          <Link to="/members">Members</Link>
        </li>
      </ul>
    </nav>
  );
}
