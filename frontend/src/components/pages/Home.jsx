import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Home.css';

function Home() {
    const navigate = useNavigate();
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => setIsOpen(!isOpen);

    return (
        <div className="home-container">
            {/* Navbar */}
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">KomsuConnect</a>
                    <button
                        className="navbar-toggler"
                        type="button"
                        onClick={toggleMenu}
                    >
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className={`collapse navbar-collapse ${isOpen ? 'show' : ''}`}>
                        <ul className="navbar-nav ms-auto">
                            <li className="nav-item"><a className="nav-link" href="/login">Giriş Yap</a></li>
                            <li className="nav-item"><a className="nav-link" href="/register">Kayıt Ol</a></li>
                        </ul>
                    </div>
                </div>
            </nav>


            {/* Hero Bölümü */}
            <header className="home-hero">
                <h3 className="home-hero-title">Komşularınızla Bağ Kurun</h3>
                <h1 className='home-hero-text'>(Yerel işletmelerle bağlantı kurun)</h1>
                <h1></h1>
                <p className="home-hero-description">KomşuConnect ile mahallenizdeki işletmeleri keşfedin ve topluluğunuzla bağlarınızı güçlendirin.</p>
                <div className="home-hero-buttons">
                    <button className="home-button" onClick={() => navigate('/register')}>Kayıt Ol</button>
                    <button className="home-button home-button-secondary" onClick={() => navigate('/login')}>Giriş Yap</button>
                </div>
            </header>

            <footer className="bg-dark text-white text-center py-3 mt-5">
                <div className="container">
                    <p className="mb-0">&copy; 2024 KomşuConnect. Tüm hakları saklıdır.</p>
                    <div className="mt-2">
                        <a href="#" className="text-white mx-2"><i className="fab fa-facebook"></i></a>
                        <a href="#" className="text-white mx-2"><i className="fab fa-twitter"></i></a>
                        <a href="#" className="text-white mx-2"><i className="fab fa-instagram"></i></a>
                    </div>
                </div>
            </footer>

        </div>
    );
}

export default Home;
