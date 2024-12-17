import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import api, { getFavorites, removeFavorite } from "../../api/Api";
import { useAuth } from "../../context/AuthContext";
import "../styles/Favorites.css";

function Favorites() {
    const [favorites, setFavorites] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { token } = useAuth();

    useEffect(() => {
        const fetchFavorites = async () => {
            try {
                const data = await getFavorites(token);
                setFavorites(data);
                setError(null);
            } catch (err) {
                console.error("Favoriler alınırken hata oluştu:", err);
                setError("Favoriler alınamadı.");
            } finally {
                setLoading(false);
            }
        };

        fetchFavorites();
    }, [token]);

    const handleRemoveFavorite = async (type, id) => {
        try {
            await removeFavorite(type, id, token);
            setFavorites((prevFavorites) =>
                prevFavorites.filter(
                    (favorite) =>
                        !(type === "business" && favorite.businessId === id) &&
                        !(type === "product" && favorite.productId === id)
                )
            );
            alert("Favoriden kaldırıldı.");
        } catch (err) {
            console.error("Favoriden kaldırma işlemi sırasında hata:", err);
            alert("Favoriden kaldırma işlemi başarısız oldu.");
        }
    };

    if (loading) return <p>Yükleniyor...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="favorites-container">
            <h2>Favorilerim</h2>
            {favorites.length > 0 ? (
                <div className="favorites-list">
                    {favorites.map((favorite) =>
                        favorite.businessId ? (
                            <div key={`business-${favorite.businessId}`} className="favorite-card">
                                <h3>İşletme: {favorite.businessName}</h3>
                                <p>{favorite.businessDescription || "Açıklama yok"}</p>
                                <button
                                    className="remove-favorite-button"
                                    onClick={() => handleRemoveFavorite("business", favorite.businessId)}
                                >
                                    Favorilerden Kaldır
                                </button>
                                <Link
                                    to="/business-detail"
                                    state={{ id: favorite.businessId }}
                                    className="detail-link"
                                >
                                    Detay Gör
                                </Link>
                            </div>
                        ) : (
                            <div key={`product-${favorite.productId}`} className="favorite-card">
                                <h3>Ürün: {favorite.productName}</h3>
                                <p>{favorite.productDescription || "Açıklama yok"}</p>
                                <p>Fiyat: {favorite.productPrice} ₺</p>
                                <button
                                    className="remove-favorite-button"
                                    onClick={() => handleRemoveFavorite("product", favorite.productId)}
                                >
                                    Favorilerden Kaldır
                                </button>
                            </div>
                        )
                    )}
                </div>
            ) : (
                <p>Henüz favoriniz bulunmamaktadır.</p>
            )}
        </div>
    );
}

export default Favorites;
