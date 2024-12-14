import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../api/Api";
import { useAuth } from "../../context/AuthContext";
import "../styles/BusinessDetail.css";

function BusinessDetail() {
    const location = useLocation();
    const navigate = useNavigate();
    const { id } = location.state || {}; // İşletme ID'si
    const { token, user } = useAuth();
    const [business, setBusiness] = useState(null); // İşletme bilgileri
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [rating, setRating] = useState(0); // Kullanıcının puanı
    const [averageRating, setAverageRating] = useState(null); // Ortalama puan
    const [productQuantities, setProductQuantities] = useState({}); // Ürün miktarları

    useEffect(() => {
        if (!id) {
            setError("İşletme bilgisi bulunamadı.");
            setLoading(false);
            return;
        }

        const fetchBusinessDetails = async () => {
            try {
                const response = await api.get(`/api/business/${id}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setBusiness(response.data.data || null);
            } catch (err) {
                console.error("İşletme bilgileri alınırken hata:", err);
                setError("İşletme bilgileri alınamadı.");
            }
        };

        const fetchProducts = async () => {
            try {
                const response = await api.get(`/api/business/${id}/products`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setProducts(response.data.data || []);
            } catch (err) {
                console.error("Ürünler alınırken hata:", err);
                setError("Ürünler alınamadı.");
            }
        };

        const fetchAverageRating = async () => {
            try {
                const response = await api.get(`/api/business/${id}/average-rating`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setAverageRating(response.data.data || null);
            } catch (err) {
                console.error("Ortalama puan alınırken hata:", err);
            }
        };

        fetchBusinessDetails();
        fetchProducts();
        fetchAverageRating();
        setLoading(false);
    }, [id, token]);

    const handleSendMessage = async () => {
        try {
            const response = await api.get(`/api/business/${id}/owner`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const owner = response.data.data;

            navigate("/messages", {
                state: {
                    receiverId: owner.id,
                    receiverName: `${owner.firstName} ${owner.lastName}`,
                    isNewConversation: true, // Yeni konuşma başlatıldığını belirtmek için
                    businessId: id, // İşletme ID'sini de gönderiyoruz
                    businessName: business?.businessName || "Unknown Business", // İşletme adı
                },
            });
        } catch (error) {
            console.error("Error fetching business owner:", error);
            alert("Mesaj göndermek için işletme sahibi bilgileri alınamadı.");
        }
    };

    const handleAddToCart = async (productId) => {
        try {
            const quantity = productQuantities[productId] || 1;
            const cartItem = {
                productId, // Ürün ID'si
                userId: user.id, // Kullanıcı ID'si
                quantity, // Kullanıcıdan gelen miktar
            };

            await api.post("/cart/add", cartItem, {
                headers: { Authorization: `Bearer ${token}` },
            });

            alert("Ürün sepete eklendi!");
        } catch (err) {
            console.error("Sepete eklerken hata oluştu:", err);
            alert("Ürün sepete eklenemedi.");
        }
    };

    const handleRate = async () => {
        try {
            await api.post(
                `/api/business/rate`,
                {},
                {
                    headers: { Authorization: `Bearer ${token}` },
                    params: {
                        rating: rating,
                        businessId: id,
                    },
                }
            );

            alert("Puanınız kaydedildi!");
            setRating(0);

            const response = await api.get(`/api/business/${id}/average-rating`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setAverageRating(response.data.data || null);
        } catch (err) {
            if (err.response && err.response.data === 409) {
                alert("Bu işletmeye zaten puan verdiniz.");
            }
            console.error("Puanlama sırasında hata:", err);
            alert("Puanlama sırasında bir hata oluştu.");
        }
    };

    if (loading) return <p>Yükleniyor...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="business-detail-container">
            <div className="business-info">
                <h2>{business?.businessName || "İşletme Adı Yok"}</h2>
                <p>{business?.businessDescription || "Açıklama yok"}</p>
                <p>
                    Ortalama Puan:{" "}
                    {averageRating !== null ? averageRating.toFixed(1) : "Henüz puanlanmamış"}
                </p>
                <button onClick={handleSendMessage} className="btn btn-primary">
                    Mesaj Gönder
                </button>
            </div>

            <h2>Ürünler</h2>
            {products.length > 0 ? (
                <div className="products-grid">
                    {products.map((product) => (
                        <div key={product.id} className="product-card">
                            {product.photoUrl ? (
                                <img
                                    src={product.photoUrl}
                                    alt={product.name}
                                    className="product-image"
                                />
                            ) : (
                                <div className="product-placeholder">Fotoğraf Yok</div>
                            )}
                            <h4>{product.name}</h4>
                            <p>{product.description}</p>
                            <p>{product.price} ₺</p>
                            <div>
                                <label htmlFor={`quantity-${product.id}`}>Miktar:</label>
                                <input
                                    type="number"
                                    id={`quantity-${product.id}`}
                                    min="1"
                                    defaultValue="1"
                                    onChange={(e) =>
                                        setProductQuantities((prevState) => ({
                                            ...prevState,
                                            [product.id]: Number(e.target.value),
                                        }))
                                    }
                                />
                            </div>
                            <button
                                onClick={() => handleAddToCart(product.id)}
                                className="btn btn-secondary"
                            >
                                Sepete Ekle
                            </button>
                        </div>
                    ))}
                </div>
            ) : (
                <p>Bu işletmeye ait ürün bulunmamaktadır.</p>
            )}

            <div className="rate-container">
                <label htmlFor="rating">Puanınızı Verin:</label>
                <select
                    id="rating"
                    value={rating}
                    onChange={(e) => setRating(Number(e.target.value))}
                >
                    <option value={0}>Puan Seç</option>
                    <option value={1}>1</option>
                    <option value={2}>2</option>
                    <option value={3}>3</option>
                    <option value={4}>4</option>
                    <option value={5}>5</option>
                </select>
                <button onClick={handleRate} disabled={rating === 0}>
                    Puanla
                </button>
            </div>
        </div>
    );
}

export default BusinessDetail;
