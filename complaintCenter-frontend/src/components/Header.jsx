import React from 'react';
import './Header.css';

function Header() {
    return (
        <header className="header">
            <h1>Minha Página Inicial</h1>
            <nav>
                <a href="#home" className="link">Home</a>
                <a href="#form" className="link">Formulário</a>
            </nav>
        </header>
    );
}

export default Header;