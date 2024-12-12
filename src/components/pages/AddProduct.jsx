import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/Api";
import "../styles/AddProduct.css";
import { useAuth } from "../../context/AuthContext";

function AddProduct() {
    const { token } = useAuth(); // Token çekmek için
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: "",
        description: "",
        price: "",
    });

    const [step, setStep] = useState(1); // Adımları kontrol eden state
    const [productId, setProductId] = useState(null); // Ürün ID'si
    const [selectedFile, setSelectedFile] = useState(null); // Fotoğraf dosyası
    const [errorMessage, setErrorMessage] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        setSelectedFile(file); // Fotoğraf dosyasını state'e ekle
    };

    const handleProductSubmit = async (e) => {
        e.preventDefault();
        try {
            // 1. Ürünü kaydet
            await api.post("/api/business/add-product", formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            // 2. Son eklenen ürünü al
            const latestProductResponse = await api.get("/api/products/latest-product", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            const id = latestProductResponse.data.data.id; // latest-product'tan gelen ID
            setProductId(id); // Ürün ID'sini state'e kaydet
            setStep(2); // Fotoğraf yükleme adımına geç
        } catch (error) {
            console.error("Ürün eklenirken bir hata oluştu:", error);
            setErrorMessage("Ürün eklenirken bir hata oluştu. Lütfen tekrar deneyin.");
        }
    };


    const handlePhotoSubmit = async (e) => {
        e.preventDefault();
        try {
            if (!selectedFile) {
                setErrorMessage("Lütfen bir dosya seçin.");
                return;
            }

            const photoData = new FormData();
            photoData.append("file", selectedFile);

            // Fotoğrafı yükle
            await api.post(`/api/business/products/${productId}/upload-photo`, photoData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "multipart/form-data",
                },
            });

            alert("Ürün fotoğrafı başarıyla yüklendi!");
            navigate("/products");
        } catch (error) {
            console.error("Fotoğraf yüklenirken bir hata oluştu:", error);
            setErrorMessage("Fotoğraf yüklenirken bir hata oluştu. Lütfen tekrar deneyin.");
        }
    };

    const handleCancel = () => {
        navigate("/products");
    };

    return (
        <div className="add-product-container">
            {step === 1 && (
                <>
                    <h2 className="add-product-title">Yeni Ürün Ekle</h2>
                    <form onSubmit={handleProductSubmit} className="add-product-form">
                        <div className="form-group">
                            <label htmlFor="name" className="form-label">Ürün Adı</label>
                            <input
                                type="text"
                                id="name"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="description" className="form-label">Açıklama</label>
                            <textarea
                                id="description"
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="price" className="form-label">Fiyat</label>
                            <input
                                type="number"
                                id="price"
                                name="price"
                                value={formData.price}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        {errorMessage && <p className="error-message">{errorMessage}</p>}
                        <div className="add-product-buttons">
                            <button type="submit" className="btn btn-primary">
                                Kaydet
                            </button>
                            <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                                İptal
                            </button>
                        </div>
                    </form>
                </>
            )}
            {step === 2 && (
                <>
                    <h2 className="add-product-title">Fotoğraf Ekle</h2>
                    <form onSubmit={handlePhotoSubmit} className="add-product-form">
                        <div className="form-group">
                            <label htmlFor="file" className="form-label">Ürün Fotoğrafı</label>
                            <input
                                type="file"
                                id="file"
                                name="file"
                                accept="image/*"
                                onChange={handleFileChange}
                                required
                            />
                        </div>
                        {errorMessage && <p className="error-message">{errorMessage}</p>}
                        <div className="add-product-buttons">
                            <button type="submit" className="btn btn-primary">
                                Fotoğraf Yükle
                            </button>
                            <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                                İptal
                            </button>
                        </div>
                    </form>
                </>
            )}
        </div>
    );
}

export default AddProduct;
