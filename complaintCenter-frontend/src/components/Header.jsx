import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';
import logoImage from '../assets/logotipo.png';

function Header() {
    return (
        <header className="header">
            <a href="/" className="logo-link">
                <img
                    src={logoImage}
                    alt="Logotipo - Central de Denúncias"
                    className="header-logo"
                />
            </a>
            <nav>
                <a href="/" className="link">Início</a>
                <Link to="/acompanhar" className="link">Acompanhe sua denúncia</Link>
            </nav>
        </header>
    );
}

export default Header;