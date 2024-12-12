import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080", // Backend API Base URL
    headers: {
        "Content-Type": "application/json",
    },
});

// İşletme veya ürün favorilere ekleme
export const addFavorite = async (type, id, token) => {
    const endpoint =
        type === "business"
            ? `/api/favorites/business/${id}` // İşletme favorilere ekleme endpointi
            : `/api/favorites/product/${id}`; // Ürün favorilere ekleme endpointi

    return await api.post(
        endpoint,
        {},
        {
            headers: { Authorization: `Bearer ${token}` },
        }
    );
};

// İşletme veya ürün favorilerden kaldırma
export const removeFavorite = async (type, id, token) => {
    const endpoint =
        type === "business"
            ? `/api/favorites/business/${id}` // İşletme favorilerden kaldırma endpointi
            : `/api/favorites/product/${id}`; // Ürün favorilerden kaldırma endpointi

    return await api.delete(endpoint, {
        headers: { Authorization: `Bearer ${token}` },
    });
};

// Kullanıcının favorilerini listeleme
export const getFavorites = async (token) => {
    const endpoint = `/api/favorites`;

    const response = await api.get(endpoint, {
        headers: { Authorization: `Bearer ${token}` },
    });

    return response.data.data || [];
};

// İşletme veya ürünün favori olup olmadığını kontrol etme
export const checkFavorite = async (type, id, token) => {
    const favorites = await getFavorites(token);
    if (type === "business") {
        return favorites.some((favorite) => favorite.business?.id === id);
    } else if (type === "product") {
        return favorites.some((favorite) => favorite.product?.id === id);
    }
    return false;
};

export default api;
