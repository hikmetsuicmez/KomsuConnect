import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Register.css';
import api from '../../api/Api';

const Register = () => {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        phoneNumber: '',
        neighborhood: '',
        role: 'ROLE_USER',
        businessName: '',
        businessDescription: '',
    });
    const navigate = useNavigate();
    const [isBusinessOwner, setIsBusinessOwner] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleRoleChange = (e) => {
        const role = e.target.value;
        setFormData({
            ...formData,
            role: role,
        });
        setIsBusinessOwner(role === "ROLE_BUSINESS_OWNER");
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            let response;
            if (formData.role === "ROLE_BUSINESS_OWNER") {
                const { firstName, lastName, email, password, phoneNumber, neighborhood, businessName, businessDescription } = formData;
                response = await api.post('/auth/register/business', { firstName, lastName, email, password, phoneNumber, neighborhood, businessName, businessDescription });
            } else {
                const { firstName, lastName, email, password, phoneNumber, neighborhood } = formData;
                response = await api.post('/auth/register/user', { firstName, lastName, email, password, phoneNumber, neighborhood });
            }

            alert(response?.data?.message || 'Kayıt başarılı.');
            navigate('/login');
        } catch (error) {
            console.error('Kayıt başarısız', error);
            const errorMessage = error.response?.data?.error || 'Kayıt sırasında bir hata oluştu.';
            alert(errorMessage);
        }
    };

    return (
        <div className="register-container">
            <div className="register-card">
                <h2 className="register-title">Kayıt Ol</h2>
                <form onSubmit={handleSubmit} className="register-form">
                    <input type="text" placeholder="Ad" name="firstName" value={formData.firstName} onChange={handleChange} required />
                    <input type="text" placeholder="Soyad" name="lastName" value={formData.lastName} onChange={handleChange} required />
                    <input type="email" placeholder="E-posta" name="email" value={formData.email} onChange={handleChange} required />
                    <input type="password" placeholder="Şifre" name="password" value={formData.password} onChange={handleChange} required />
                    <input type="text" placeholder="Telefon Numarası" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} required />
                    <input type="text" placeholder="Mahalle" name="neighborhood" value={formData.neighborhood} onChange={handleChange} required />

                    <div className="register-role">
                        <label>
                            <input type="radio" name="role" value="ROLE_USER" checked={formData.role === "ROLE_USER"} onChange={handleRoleChange} />
                            Kullanıcı
                        </label>
                        <label>
                            <input type="radio" name="role" value="ROLE_BUSINESS_OWNER" checked={formData.role === "ROLE_BUSINESS_OWNER"} onChange={handleRoleChange} />
                            İşletme Sahibi
                        </label>
                    </div>

                    {isBusinessOwner && (
                        <>
                            <input type="text" placeholder="İşletme Adı" name="businessName" value={formData.businessName} onChange={handleChange} required={isBusinessOwner} />
                            <textarea placeholder="İşletme Açıklaması" name="businessDescription" rows="3" value={formData.businessDescription} onChange={handleChange} required={isBusinessOwner}></textarea>
                        </>
                    )}

                    <button type="submit" className="register-button">Kayıt Ol</button>
                    <p className="register-footer">
                        Zaten bir hesabınız var mı? <a href="/login">Giriş Yap</a>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default Register;
