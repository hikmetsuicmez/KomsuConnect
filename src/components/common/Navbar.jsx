import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import api from "../../api/Api";
import { useAuth } from '../../context/AuthContext';
import { useTheme } from '../../context/ThemeContext';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faHome,
    faEnvelope,
    faUser,
    faBell,
    faShoppingCart,
} from "@fortawesome/free-solid-svg-icons";
import "../styles/Navbar.css";


function Navbar() {
    const { user, logout, token } = useAuth();
    const navigate = useNavigate();
    const [unreadCount, setUnreadCount] = useState(0);
    const { isDarkMode, toggleTheme } = useTheme();
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    };

    useEffect(() => {
        fetchUnreadCount();
    }, []);

    const fetchUnreadCount = async () => {
        try {
            const response = await api.get("/api/notifications/unread-count", {
                headers: { Authorization: `Bearer ${token}` }
            });
            setUnreadCount(response.data || 0);
        } catch (err) {
            console.error("Unread notifications count fetch error:", err);
        }
    };

    return (
        <nav className={`navbar navbar-expand-lg ${isDarkMode ? 'navbar-dark bg-dark' : 'navbar-light bg-light'}`}>
            <div className="container">
                <Link className="navbar-brand" to={user ? '/dashboard' : '/'}>
                    KomsuConnect
                </Link>
                <button
                    className="navbar-toggler"
                    type="button"
                    onClick={toggleMenu}
                >
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className={`collapse navbar-collapse ${isMenuOpen ? 'show' : ''}`}>
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to={user ? '/dashboard' : '/'}>
                                <FontAwesomeIcon icon={faHome} /> Home
                            </Link>
                        </li>
                        {user && (
                            <>
                                <li className="nav-item">
                                    <Link className="nav-link" to="/messages">
                                        <FontAwesomeIcon icon={faEnvelope} /> Messages
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to="/profile">
                                        <FontAwesomeIcon icon={faUser} /> Profile
                                    </Link>
                                </li>
                                {user?.role?.toUpperCase() === "ROLE_BUSINESS_OWNER" && (
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/products">
                                            Products
                                        </Link>
                                    </li>
                                )}
                                <li className="nav-item position-relative">
                                    <Link
                                        className={`nav-link ${unreadCount > 0 ? 'unread-notifications' : ''}`}
                                        to="/notifications"
                                    >
                                        <FontAwesomeIcon icon={faBell} /> Notifications
                                        {unreadCount > 0 && (
                                            <span className="notification-badge">{unreadCount}</span>
                                        )}
                                    </Link>
                                </li>
                                <li className='nav-item'>
                                    <Link className='nav-link' to="/cart">
                                        <FontAwesomeIcon icon={faShoppingCart} /> Sepetim
                                    </Link>
                                </li>

                                <li className="nav-item ms-2">
                                    <button
                                        className="btn btn-outline-success"
                                        onClick={handleLogout}
                                    >
                                        Logout
                                    </button>
                                </li>
                            </>
                        )}
                        {!user && (
                            <>
                                <li className="nav-item">
                                    <Link className="nav-link" to="/login">
                                        Login
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to="/register">
                                        Register
                                    </Link>
                                </li>
                            </>
                        )}
                        <li className="nav-item ms-3">
                            <button
                                className="btn btn-outline-secondary"
                                onClick={toggleTheme}
                                style={{
                                    display: "flex",
                                    alignItems: "center",
                                    justifyContent: "center",
                                    padding: "10px",
                                    borderRadius: "50%",
                                    width: "40px",
                                    height: "40px",
                                }}
                            >
                                <i className={`fas ${isDarkMode ? 'fa-sun' : 'fa-moon'}`}></i>
                            </button>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    );
}

export default Navbar;
