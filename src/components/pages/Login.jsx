import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import api from '../../api/Api';
import '../styles/Login.css'; // CSS dosyasını dahil ediyoruz

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/auth/login', { email, password });
            const { token, message, userSummary } = response.data.data;
            if (token && userSummary) {
                login(userSummary, token);
                console.log("Giriş başarılı:", message);
                navigate('/dashboard');
            } else {
                alert("Giriş sırasında beklenmeyen bir sorun oluştu.");
            }
        } catch (error) {
            console.error("Giriş işlemi başarısız:", error);
            if (error.response) {
                alert(`Hata: ${error.response.data.message || "Giriş yapılamadı."}`);
            } else {
                alert("Bir bağlantı hatası oluştu.");
            }
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h2 className="login-title">Giriş Yap</h2>
                <form onSubmit={handleLogin}>
                    <div className="login-form-group">
                        <label htmlFor="email" className="login-label">Email</label>
                        <input
                            type="email"
                            id="email"
                            className="login-input"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="login-form-group">
                        <label htmlFor="password" className="login-label">Şifre</label>
                        <input
                            type="password"
                            id="password"
                            className="login-input"
                            placeholder="Şifre"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="login-button">Giriş Yap</button>
                </form>
                <p className="login-register">
                    Henüz hesabınız yok mu? <a href="/register" className="login-register-link">Kayıt Ol</a>
                </p>
                <p className="login-register">
                    <a href="/" className="login-register-link">Ana Sayfa </a>
                </p>
            </div>
        </div>
    );
}

export default Login;
