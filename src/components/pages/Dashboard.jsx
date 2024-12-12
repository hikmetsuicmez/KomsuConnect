import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import api from "../../api/Api";
import { useAuth } from "../../context/AuthContext";
import "../styles/Dashboard.css";

function Dashboard() {
    const [businesses, setBusinesses] = useState([]);
    const [queryNeighborhood, setQueryNeighborhood] = useState("");
    const [queryBusinessName, setQueryBusinessName] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const { token } = useAuth();

    useEffect(() => {
        const fetchBusinesses = async () => {
            try {
                const response = await api.get("/api/business/public-businesses", {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setBusinesses(response.data.data || []);
            } catch (error) {
                console.error("Hata:", error);
                setError("İşletmeler alınamadı.");
            }
        };

        fetchBusinesses();
    }, [token]);

    const handleSearch = async () => {
        setLoading(true);
        try {
            const params = {};
            if (queryNeighborhood.trim()) {
                params.neighborhood = queryNeighborhood;
            }
            if (queryBusinessName.trim()) {
                params.businessName = queryBusinessName;
            }
            const response = await api.get("/api/business/search", {
                params,
                headers: { Authorization: `Bearer ${token}` },
            });
            setBusinesses(response.data.data || []);
            setError(null);
        } catch (err) {
            console.error("Arama sırasında hata oluştu:", err);
            setError("Arama sırasında bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="dashboard-container">
            <h2 className="dashboard-title">Mahalledeki İşletmeler</h2>
            <div className="search-container">
                <input
                    type="text"
                    placeholder="Mahalle adı girin..."
                    value={queryNeighborhood}
                    onChange={(e) => setQueryNeighborhood(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="İşletme adı girin..."
                    value={queryBusinessName}
                    onChange={(e) => setQueryBusinessName(e.target.value)}
                />
                <button onClick={handleSearch} disabled={loading}>
                    Ara
                </button>
            </div>

            {loading && <p>Yükleniyor...</p>}
            {error && <p>{error}</p>}

            <div className="business-list">
                {businesses.length > 0 ? (
                    businesses.map((business) => (
                        <div key={business.id} className="business-card">
                            <div className="business-image">
                                <img
                                    src={business.photoUrl || 'https://via.placeholder.com/150'} // İşletme görseli
                                    alt={business.businessName || "İşletme"}
                                />
                            </div>
                            <div className="business-info">
                                <h3>{business.businessName || "İsimsiz İşletme"}</h3>
                                <p className="business-description">
                                    {business.businessDescription || "Açıklama bulunmamaktadır."}
                                </p>
                                <p className="business-rating">
                                    Puan:{" "}
                                    {business.rating != null
                                        ? `${business.rating.toFixed(1)} / 5`
                                        : "Henüz puanlanmamış"}
                                </p>
                                <Link
                                    to="/business-detail"
                                    state={{ id: business.id }}
                                    className="details-button"
                                >
                                    Detaylar
                                </Link>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>Henüz işletme bulunamadı.</p>
                )}
            </div>
        </div>
    );
}

export default Dashboard;
