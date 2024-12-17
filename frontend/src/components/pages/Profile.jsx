import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import '../styles/Profile.css';
import { useAuth } from '../../context/AuthContext';
import api from '../../api/Api';

function Profile() {
    const { userId } = useParams(); // Başka bir kullanıcının profiline erişim için
    const { token } = useAuth(); // Token kullanarak API çağrıları yapılır
    const { logout } = useAuth();
    const [newEmail, setNewEmail] = useState('');
    const [profile, setProfile] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        neighborhood: '',
        businessId: null, // İşletme ID
        businessName: '',
        businessDescription: '',
        photoUrl: '', // İşletme fotoğraf URL'si
    });
    const [isEditing, setIsEditing] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [selectedFile, setSelectedFile] = useState(null); // Fotoğraf dosyası için state


   

    // Profil bilgisini getirme
    const fetchProfile = useCallback(async () => {
        const endpoint = userId ? `/api/users/${userId}` : `/api/users/me`;
        try {
            const response = await api.get(endpoint, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            if (response.data && response.data.result) {
                const profileData = response.data.data;
                setProfile({
                    firstName: profileData.firstName,
                    lastName: profileData.lastName,
                    email: profileData.email,
                    phoneNumber: profileData.phoneNumber,
                    neighborhood: profileData.neighborhood,
                    businessId: profileData.id, // ID'yi businessId olarak alıyoruz
                    businessName: profileData.businessName,
                    businessDescription: profileData.businessDescription,
                    photoUrl: profileData.photoUrl, // Henüz bir fotoğraf URL'si yoksa boş bırakıyoruz
                });
            } else {
                setErrorMessage('Profil verisi alınamadı.');
            }
        } catch (error) {
            setErrorMessage('Profil bilgileri alınırken bir hata oluştu.');
        }
    }, [token, userId]); // token ve userId bağımlılıklar olarak eklendi

    useEffect(() => {
        fetchProfile();
    }, [fetchProfile]); // fetchProfile güvenle bağımlılık dizisine eklendi

    // Profil bilgilerini güncelleme
    const handleSave = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                firstName: profile.firstName,
                lastName: profile.lastName,
                email: profile.email,
                phoneNumber: profile.phoneNumber,
                neighborhood: profile.neighborhood,
            };

            const response = await api.put('/api/users/me', payload, {
                headers: { Authorization: `Bearer ${token}` },
            });

            await fetchProfile();

            setProfile(response.data.data); // Güncel profil bilgilerini state'e aktar
            setIsEditing(false);
        } catch (error) {
            console.error('Profil güncellenirken bir hata oluştu:', error);
        }
    };

    const handleEmailChange = async () => {
        try {
            const response = await api.put('/api/users/update-email', { newEmail }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            if (response.data.result) {
                alert("Email başarıyla güncellendi.");
                logout();
            } else {
                alert("Email güncellenmedi.");
                logout();
            }
        } catch (error) {
            console.error('Email güncellenirken hata oluştu:', error);
        }
    };

    const handleCancel = () => {
        setIsEditing(false);
    };

    const isBusinessOwner = profile.businessId !== null; // İşletme sahibi olup olmadığını kontrol et

    // İşletme fotoğrafını yükleme
    const handlePhotoUpload = async () => {
        if (!selectedFile) {
            alert('Lütfen bir dosya seçin.');
            return;
        }

        const formData = new FormData();
        formData.append('file', selectedFile);


        try {
            const response = await api.post(`/api/business/${profile.businessId}/upload-photo`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'multipart/form-data',
                },
            });
            if (response.data.result) {
                alert('Fotoğraf başarıyla yüklendi!');
                setProfile((prev) => ({
                    ...prev,
                    photoUrl: response.data.data, // Yeni fotoğraf URL'sini güncelle
                }));
            }
        } catch (error) {
            alert('Fotoğraf yükleme işlemi başarısız oldu.');
        }
    };



    return (
        <div className="container mt-5" style={{ maxWidth: "800px", backgroundColor: "#F0EBE3", padding: "20px", borderRadius: "10px" }}>
            <div className="card">
                <div className="card-header text-white text-center">
                    <h2>Profil Bilgileri</h2>
                </div>
                <div className="card-body">
                    <div className="text-center position-relative">
                        <img
                            src={profile.photoUrl1 || 'https://via.placeholder.com/100'}  // PROFIL FOTOĞRAFI GELMELİ İŞLETME FOTOĞRAFI DEĞİL.
                            alt="Business Profile"
                            className="rounded-circle"
                        />
                        <h3>{profile.firstName} {profile.lastName}</h3>
                        <p style={{ color: "#9A8C98" }}>{profile.neighborhood}</p>
                    </div>

                    {errorMessage && (
                        <div className="alert alert-danger text-center">
                            {errorMessage}
                        </div>
                    )}

                    {!isEditing ? (
                        <>
                            <div className="row text-center mb-4">
                                <div className="col-md-6">
                                    <strong>Email:</strong> {profile.email}
                                </div>
                                <div className="col-md-6">
                                    <strong>Telefon:</strong> {profile.phoneNumber}
                                </div>
                            </div>

                            {profile.businessId && profile.businessName &&(
                                <div className="hizmet-profil text-center">
                                    <div className="text-center position-relative">
                                        <img
                                            src={profile.photoUrl || 'https://via.placeholder.com/100'}
                                            alt="Business Profile"
                                            className="rounded-circle"
                                        />
                                        <h3>{profile.businessName}</h3>
                                        <p style={{ color: "#9A8C98" }}>{profile.neighborhood}</p>
                                    </div>
                                    <h4>İşletme Bilgileri</h4>
                                    <p><strong>İşletme Adı:</strong> {profile.businessName}</p>
                                    <p><strong>Açıklama:</strong> {profile.businessDescription}</p>
                                </div>
                            )}

                            {!userId && (
                                <div className="text-center mt-4">
                                    <button onClick={() => setIsEditing(true)} className="btn btn-primary">
                                        Düzenle
                                    </button>
                                </div>
                            )}
                        </>
                    ) : (
                        <form>
                            <div className="row">
                                <div className="col-md-6 mb-3">
                                    <label htmlFor="firstName" className="form-label">Ad</label>
                                    <input type="text" className="form-control" id="firstName" value={profile.firstName || ''}
                                        onChange={(e) => setProfile({ ...profile, firstName: e.target.value })} />
                                </div>
                                <div className="col-md-6 mb-3">
                                    <label htmlFor="lastName" className="form-label">Soyad</label>
                                    <input type="text" className="form-control" id="lastName" value={profile.lastName || ''}
                                        onChange={(e) => setProfile({ ...profile, lastName: e.target.value })} />

                                </div>
                            </div>
                            <div className="mb-3">
                                <label htmlFor="newEmail" className="form-label">Yeni Email</label>
                                <input
                                    type="email"
                                    className="form-control"
                                    id="newEmail"
                                    placeholder="Yeni email adresinizi girin"
                                    value={newEmail}
                                    onChange={(e) => setNewEmail(e.target.value)}
                                />
                                <button
                                    type="button"
                                    className="btn btn-warning mt-2"
                                    onClick={handleEmailChange}
                                >
                                    Email Güncelle
                                </button>
                            </div>
                            <div className="mb-3">
                                <label htmlFor="phoneNumber" className="form-label">Telefon</label>
                                <input type="text" className="form-control" id="phoneNumber" value={profile.phoneNumber || ''}
                                    onChange={(e) => setProfile({ ...profile, phoneNumber: e.target.value })} />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="neigborhood" className="form-label">Mahalle</label>
                                <input type="text" className="form-control" id="neigborhood" value={profile.neighborhood || ''}
                                    onChange={(e) => setProfile({ ...profile, neighborhood: e.target.value })} />
                            </div>

                            {profile.businessId && profile.businessName && (

                                <div className="mb-3 text-center">
                                    <div className="text-center position-relative">
                                        <img
                                            src={profile.photoUrl || 'https://via.placeholder.com/100'}
                                            alt="Business Profile"
                                            className="rounded-circle"
                                        />
                                        <h3>{profile.businessName}</h3>
                                        <p style={{ color: "#9A8C98" }}>{profile.neighborhood}</p>
                                    </div>
                                    <label>İşletme Fotoğrafı</label>
                                    <h1></h1>
                                    <input
                                        type="file"
                                        className="form-control"
                                        onChange={(e) => setSelectedFile(e.target.files[0])}
                                    />
                                    <button
                                        type="button"
                                        className="btn btn-primary mt-2"
                                        onClick={handlePhotoUpload}
                                    >
                                        Fotoğraf Yükle
                                    </button>
                                </div>
                            )}

                            <div className="text-center mt-4">
                                <button type="submit" className="btn btn-primary" onClick={handleSave}>Kaydet</button>
                                <button type="button" className="btn btn-secondary ms-2" onClick={handleCancel}>İptal</button>
                            </div>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Profile;
