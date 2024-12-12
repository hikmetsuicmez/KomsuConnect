import React, { useEffect, useState } from 'react';
import api from "../../api/Api";
import { useAuth } from "../../context/AuthContext";
import "../styles/Notification.css";

const Notifications = () => {
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { token } = useAuth();

    useEffect(() => {
        fetchNotifications();
    }, []);

    const fetchNotifications = async () => {
        try {
            const response = await api.get('/api/notifications', {
                headers: { Authorization: `Bearer ${token}` },
            });
            setNotifications(response.data.data || []);
            console.log(response.data.data)
        } catch (err) {
            setError('Bildirimler alınırken bir hata oluştu.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const markAsRead = async (id) => {
        try {
            await api.put(`/api/notifications/${id}/mark-as-read`, {}, {
                headers: { Authorization: `Bearer ${token}` },
            });
            fetchNotifications(); // Listeyi güncelle
        } catch (err) {
            console.error(err);
            alert('Bildirim okundu olarak işaretlenirken bir hata oluştu.');
        }
    };

    if (loading) return <p>Yükleniyor...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="notifications-container">
          <h2>Bildirimler</h2>
          {notifications.length === 0 ? (
            <p style={{ color: "#fff", textAlign: "center" }}>Henüz bir bildirim yok.</p>
          ) : (
            <div className="notification-list">
              {notifications.map((notification) => (
                <div
                  key={notification.id}
                  className={`notification-card ${notification.isRead ? 'read' : 'unread'}`}
                  onClick={() => markAsRead(notification.id)}
                >
                  <h3>{notification.message}</h3>
                  <p className="timestamp">{new Date(notification.timestamp).toLocaleString()}</p>
                </div>
              ))}
            </div>
          )}
        </div>
      );
      

};

export default Notifications;
