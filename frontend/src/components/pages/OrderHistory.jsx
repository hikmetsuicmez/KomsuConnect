import React, { useEffect, useState } from "react";
import api from "../../api/Api";
import { useAuth } from "../../context/AuthContext";
import "../styles/OrderHistory.css";

function OrderHistory() {
    const { user, token } = useAuth();
    const [orderHistory, setOrderHistory] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchOrderHistory = async () => {
            try {
                const response = await api.get("/order/history", {
                    headers: { Authorization: `Bearer ${token}` },
                    params: { userId: user.id },
                });
                setOrderHistory(response.data.data || []);
                setLoading(false);
            } catch (err) {
                console.error("Error fetching order history:", err);
                setError("Sipariş geçmişi alınırken bir hata oluştu.");
                setLoading(false);
            }
        };

        fetchOrderHistory();
    }, [token, user.id]);

    if (loading) return <p>Yükleniyor...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="order-history-container">
            <h2 className="order-history-header">Sipariş Geçmişi</h2>
            {orderHistory.length > 0 ? (
                orderHistory.map((order) => (
                    <div key={order.orderId} className="order-item">
                        <p>
                            <span>Sipariş:</span> {order.orderId}
                        </p>
                        <p>
                            <span>Toplam Fiyat:</span> {order.totalPrice} ₺
                        </p>
                        <p>
                            <span>Tarih:</span>{" "}
                            {new Date(order.createdAt).toLocaleString()}
                        </p>
                        <ul className="order-products">
                            {order.orderItems.map((item, index) => (
                                <li key={index}>
                                    <span>Ürün:</span> {item.productName} -{" "}
                                    <span>Adet:</span> {item.quantity} -{" "}
                                    <span>Fiyat:</span> {item.price} ₺
                                </li>
                            ))}
                        </ul>
                    </div>
                ))
            ) : (
                <p className="order-history-empty">Henüz bir sipariş geçmişiniz bulunmamaktadır.</p>
            )}
        </div>
    );
}

export default OrderHistory;
