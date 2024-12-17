import React, { useState, useEffect } from "react";
import api from "../../api/Api";
import { useAuth } from "../../context/AuthContext";
import "../styles/CartPage.css";
import { useNavigate } from "react-router-dom";
import PaymentForm from "../pages/PaymentForm";
import AddressForm from "../pages/AddressForm";

function CartPage() {
    const { token, user } = useAuth();
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [orderStep, setOrderStep] = useState('cart'); // 'cart', 'address', 'payment'
    const [orderId, setOrderId] = useState(null);
    const [addressData, setAddressData] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCartItems = async () => {
            try {
                const response = await api.get(`/cart`, {
                    headers: { Authorization: `Bearer ${token}` },
                    params: { userId: user.id },
                });

                const items = response.data.data || [];

                const mergedItems = items.reduce((acc, item) => {
                    const existingItem = acc.find((i) => i.productId === item.productId);
                    if (existingItem) {
                        existingItem.quantity += item.quantity;
                    } else {
                        acc.push({ ...item });
                    }
                    return acc;
                }, []);

                setCartItems(mergedItems);
                
            } catch (err) {
                console.error("Error fetching cart items:", err);
                setError("Sepet öğeleri alınamadı.");
            } finally {
                setLoading(false);
            }
        };

        fetchCartItems();
    }, [token, user.id]);

    const handlePlaceOrder = async () => {
        try {
            const orderRequest = { userId: user.id };

            const response = await api.post(`/order`, orderRequest, {
                headers: { Authorization: `Bearer ${token}` },
            });

            setOrderId(response.data.data.orderId);
            setOrderStep('address');
        } catch (error) {
            console.error("Sipariş sırasında hata oluştu:", error);
            alert("Sipariş sırasında bir hata oluştu. Lütfen tekrar deneyin.");
        }
    };

    const handleAddressSubmit = (addressData) => {
        setAddressData(addressData);
        setOrderStep('payment');
    };

    const handlePayment = async (paymentData) => {
        try {
            // Adres bilgilerini de ekleyerek ödeme isteği gönder
            const paymentRequest = {
                ...paymentData,
                ...addressData
            };

            const response = await api.post(`/order/${orderId}/payment`, paymentRequest, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (response.data.result) {
                alert("Ödeme başarılı bir şekilde gerçekleşti!");
                setCartItems([]);
                navigate("/order-history");
            } else {
                throw new Error(response.data.errorMessage || "Ödeme başarısız oldu.");
            }
        } catch (error) {
            console.error("Ödeme sırasında hata oluştu:", error);
            alert("Ödeme sırasında bir hata oluştu. Lütfen tekrar deneyin.");
        }
    };

    const handleViewOrderHistory = () => {
        navigate("/order-history");
    };

    if (loading) return <p>Yükleniyor...</p>;
    if (error) return <p>{error}</p>;

    const totalAmount = cartItems.reduce((acc, item) => acc + item.quantity * item.price, 0);

    return (
        <div className="cart-page">
            <div className="cart-container">
                <h2 className="cart-title">Sepetiniz</h2>
                {cartItems.length === 0 ? (
                    <p className="empty-cart">Sepetinizde ürün bulunmamaktadır.</p>
                ) : (
                    <>
                        {orderStep === 'cart' && (
                            <>
                                <div className="cart-items">
                                    {cartItems.map((item) => (
                                        <div key={item.productId} className="cart-item">
                                            <img
                                                src={item.photoUrl || "placeholder.jpg"}
                                                alt={item.productName}
                                                className="cart-item-image"
                                            />
                                            <div className="cart-item-details">
                                                <h4>İşletme - {item.businessName}</h4>
                                                <h4>{item.productName}</h4>
                                                <p>Miktar: {item.quantity}</p>
                                                <p>Toplam Fiyat: {item.quantity * item.price} ₺</p>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                                <h3 className="cart-total">Genel Toplam: {totalAmount} ₺</h3>
                                <button onClick={handlePlaceOrder} className="btn btn-primary">
                                    Sipariş Ver
                                </button>
                            </>
                        )}
                        {orderStep === 'address' && (
                            <AddressForm onSubmit={handleAddressSubmit} />
                        )}
                        {orderStep === 'payment' && (
                            <PaymentForm onSubmit={handlePayment} totalAmount={totalAmount} />
                        )}
                    </>
                )}
                <button
                    onClick={handleViewOrderHistory}
                    className="btn btn-secondary order-history-button"
                >
                    Geçmiş Siparişlerini Görüntüle
                </button>
            </div>
        </div>
    );
}

export default CartPage;

