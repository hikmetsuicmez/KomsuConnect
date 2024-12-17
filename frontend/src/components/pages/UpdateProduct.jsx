import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../../api/Api";
import { useAuth } from "../../context/AuthContext";
import "../styles/UpdateProduct.css";

function UpdateProduct() {
    const location = useLocation();
    const navigate = useNavigate();
    const { token } = useAuth();
    const [product, setProduct] = useState(location.state || { name: "", description: "", price: "", photoUrl: "" });
    const [errors, setErrors] = useState({}); // Hataları tutan state
    const [selectedFile, setSelectedFile] = useState(null); // Yeni fotoğraf dosyası

    // Form Validasyonu
    const validateForm = () => {
        const newErrors = {};
        if (!product.name.trim()) newErrors.name = "Ürün adı boş olamaz.";
        if (!product.description.trim()) newErrors.description = "Açıklama boş olamaz.";
        if (!product.price || product.price <= 0) newErrors.price = "Fiyat pozitif bir sayı olmalıdır.";
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0; // Hata yoksa true döner
    };

    // Ürün Güncelleme
    const handleUpdate = async (e) => {
        e.preventDefault();
        if (!validateForm()) return; // Form geçerli değilse devam etme

        try {
            const response = await api.put(`/api/business/products/${product.id}`, product, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (response.data.result) {
                alert("Ürün başarıyla güncellendi!");
                // Eğer fotoğraf seçilmişse, fotoğrafı da yükle
                if (selectedFile) {
                    await handlePhotoUpload();
                }
                navigate("/products");
            } else {
                alert(response.data.errorMessage || "Ürün güncellenirken hata oluştu.");
            }
        } catch (error) {
            console.error("Hata:", error.response?.data || error);
            alert("Bir hata oluştu. Lütfen tekrar deneyin.");
        }
    };

    // Fotoğraf Yükleme
    const handlePhotoUpload = async () => {
        try {
            const photoData = new FormData();
            photoData.append("file", selectedFile);

            const response = await api.post(`/api/business/products/${product.id}/upload-photo`, photoData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "multipart/form-data",
                },
            });

            if (response.data.result) {
                alert("Fotoğraf başarıyla güncellendi!");
            } else {
                alert(response.data.errorMessage || "Fotoğraf güncellenirken hata oluştu.");
            }
        } catch (error) {
            console.error("Fotoğraf yüklenirken hata oluştu:", error.response?.data || error);
            alert("Fotoğraf yüklenirken bir hata oluştu.");
        }
    };

    const handleChange = (e) => {
        setProduct({ ...product, [e.target.name]: e.target.value });
        setErrors({ ...errors, [e.target.name]: "" });
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        setSelectedFile(file);
    };

    return (
        <div className="update-product-container">
            <h2 className="update-product-title">Ürün Güncelle</h2>
            <form className="update-product-form" onSubmit={handleUpdate}>
                <div className="form-group">
                    <label htmlFor="name" className="form-label">Ürün Adı</label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        className={`form-control ${errors.name ? 'is-invalid' : ''}`}
                        value={product.name}
                        onChange={handleChange}
                    />
                    {errors.name && <small className="error-message">{errors.name}</small>}
                </div>
                <div className="form-group">
                    <label htmlFor="description" className="form-label">Açıklama</label>
                    <textarea
                        id="description"
                        name="description"
                        className={`form-control ${errors.description ? 'is-invalid' : ''}`}
                        value={product.description}
                        onChange={handleChange}
                    />
                    {errors.description && <small className="error-message">{errors.description}</small>}
                </div>
                <div className="form-group">
                    <label htmlFor="price" className="form-label">Fiyat</label>
                    <input
                        type="number"
                        id="price"
                        name="price"
                        className={`form-control ${errors.price ? 'is-invalid' : ''}`}
                        value={product.price}
                        onChange={handleChange}
                    />
                    {errors.price && <small className="error-message">{errors.price}</small>}
                </div>
                <div className="form-group">
                    <label htmlFor="file" className="form-label">Yeni Fotoğraf</label>
                    <input
                        type="file"
                        id="file"
                        name="file"
                        accept="image/*"
                        onChange={handleFileChange}
                    />
                    {product.photoUrl && (
                        <img
                            src={product.photoUrl}
                            alt="Ürün Fotoğrafı"
                            style={{ width: "200px", marginTop: "10px", borderRadius: "5px" }}
                        />
                    )}
                </div>
                <div className="update-product-buttons">
                    <button type="submit" className="btn btn-primary">Güncelle</button>
                    <button
                        type="button"
                        className="btn btn-secondary"
                        onClick={() => navigate("/products")}
                    >
                        İptal
                    </button>
                </div>
            </form>
        </div>
    );
}

export default UpdateProduct;
