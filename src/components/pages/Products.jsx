import React, { useState, useEffect, useCallback } from "react";
import api from "../../api/Api";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import "../styles/Products.css";

function Products() {
    const [products, setProducts] = useState([]); // Ürünleri tutan state
    const { token } = useAuth(); // Authorization için token
    const navigate = useNavigate();

    // Ürünleri backend'den çeken fonksiyon
    const fetchProducts = useCallback(async () => {
        try {
            const response = await api.get("/api/business/products", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            if (response.data.result) {
                setProducts(response.data.data); // Backend'den gelen ürünler
            } else {
                console.error("Ürünler alınamadı:", response.data.errorMessage);
            }
        } catch (error) {
            console.error("Ürünler alınırken hata oluştu:", error.response?.data || error);
        }
    }, [token]);

    // İlk renderda ürünleri çek
    useEffect(() => {
        fetchProducts();
    }, [fetchProducts]);

    // Ürün silme fonksiyonu
    const handleDelete = async (productId) => {
        if (!window.confirm("Bu ürünü silmek istediğinizden emin misiniz?")) return;
        try {
            const response = await api.delete(`/api/business/products/${productId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            if (response.data.result) {
                alert("Ürün başarıyla silindi!");
                fetchProducts(); // Ürünleri yeniden çek
            } else {
                console.error("Ürün silinemedi:", response.data.errorMessage);
            }
        } catch (error) {
            console.error("Ürün silinirken hata oluştu:", error.response?.data || error);
        }
    };

    // Güncelleme sayfasına yönlendirme
    const handleUpdate = (product) => {
        navigate("/update-product", { state: product });
    };

    return (
        <div className="product-container">
            <h2 className="product-header">Ürün Yönetimi</h2>
            <button
                className="button button-success"
                onClick={() => navigate("/add-product")} // Ürün ekleme sayfasına yönlendir
            >
                Yeni Ürün Ekle
            </button>

            {/* Ürün Kartları */}
            <div className="product-grid">
                {products.length > 0 ? (
                    products.map((product) => (
                        <div className="product-card" key={product.id}>
                            <div className="product-card-body">
                                {product.photoUrl ? (
                                    <img
                                        src={product.photoUrl}
                                        alt={product.name}
                                        className="product-card-image"
                                    />
                                ) : (
                                    <div className="product-card-placeholder">Fotoğraf Yok</div>
                                )}
                                <h5 className="product-card-title">{product.name}</h5>
                                <p className="product-card-description">{product.description}</p>
                                <p className="product-card-price">
                                    <strong>Fiyat:</strong> {product.price} ₺
                                </p>
                                <div className="product-card-buttons">
                                    <button
                                        className="button button-danger"
                                        onClick={() => handleDelete(product.id)}
                                    >
                                        Sil
                                    </button>
                                    <button
                                        className="button button-secondary"
                                        onClick={() => handleUpdate(product)}
                                    >
                                        Güncelle
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <p className="no-products-message">Henüz eklenmiş ürün yok.</p>
                )}
            </div>
        </div>
    );
}

export default React.memo(Products);
