import React, { useState, useEffect, useRef } from "react";
import { useAuth } from "../../context/AuthContext";
import { useLocation } from "react-router-dom";
import api from "../../api/Api";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import "../styles/Messages.css";

function Messages() {
    const { user, token } = useAuth();
    const location = useLocation();
    const [receiverId, setReceiverId] = useState(location.state?.receiverId || null);
    const [receiverName, setReceiverName] = useState(location.state?.receiverName || null);
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState("");
    const [businesses, setBusinesses] = useState([]);
    const [conversationList, setConversationList] = useState([]);
    const [activeConversation, setActiveConversation] = useState(receiverId);
    const stompClient = useRef(null);
    const messagesEndRef = useRef(null);
    const [conversationListWithBusinesses, setConversationListWithBusinesses] = useState([]);

    useEffect(() => {
        const updatedList = conversationList.map((conv) => {
            const matchingBusiness = businesses.find(
                (business) => business.id === conv.sender.id
            );
            return {
                ...conv,
                businessName: matchingBusiness?.businessName || null, // Eğer eşleşme yoksa null
            };
        });
        setConversationListWithBusinesses(updatedList);
    }, [conversationList, businesses]);

    // Fetch inbox conversations
    useEffect(() => {
        const fetchInbox = async () => {
            try {
                const response = await api.get(`/api/messages/inbox`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                let inbox = response.data.data || [];
                setConversationList((prevList) => {
                    const uniqueList = [...prevList, ...inbox].reduce((unique, item) => {
                        if (!unique.some((conv) => conv.sender.id === item.sender.id)) {
                            unique.push(item);
                        }
                        return unique;
                    }, []);
                    return uniqueList;
                });
            } catch (error) {
                console.error("[Messages.jsx] Error fetching inbox:", error);
            }
        };

        fetchInbox();
    }, [token]);

    useEffect(() => {
        const fetchBusinesses = async () => {
            try {
                const response = await api.get("/api/business/public-businesses", {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setBusinesses(response.data.data || []);
            } catch (error) {
                console.error("Hata:", error);
            }
        };
        fetchBusinesses();
    }, [token]);

    // Fetch or create a conversation
    const getOrCreateConversation = async (receiverId) => {
        try {
            const response = await api.get(`/api/conversation-or-create/${user.id}/${receiverId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const conversation = response.data.data;

            // Konuşmayı listeye ekle
            setConversationList((prevList) => {
                const isExistingConversation = prevList.some((conv) => conv.sender.id === receiverId);
                if (!isExistingConversation) {
                    return [conversation, ...prevList];
                }
                return prevList;
            });

            // Aktif konuşmayı ayarla
            setReceiverId(conversation.sender.id === user.id ? conversation.receiver.id : conversation.sender.id);
            setReceiverName(conversation.businessName || conversation.sender.name);
            setMessages([]); // Mesajları sıfırla
        } catch (error) {
            console.error("[Messages.jsx] Error fetching or creating conversation:", error);
        }
    };

    // Fetch messages of the selected conversation
    useEffect(() => {
        if (!receiverId) return;

        const fetchMessages = async () => {
            try {
                const response = await api.get(
                    `/api/messages/conversation/${user.id}/${receiverId}`,
                    { headers: { Authorization: `Bearer ${token}` } }
                );
                setMessages(response.data.data || []);
            } catch (error) {
                console.error("[Messages.jsx] Error fetching messages:", error);
            } finally {
                scrollToBottom();
            }
        };
        fetchMessages();
    }, [receiverId, user.id, token]);

    // WebSocket setup
    useEffect(() => {
        if (!receiverId) return;

        const socket = new SockJS("http://localhost:8080/ws");
        stompClient.current = Stomp.client(socket);

        stompClient.current.connect({}, () => {
            stompClient.current.subscribe(`/topic/conversation/${user.id}/${receiverId}`, (msg) => {
                const newMessage = JSON.parse(msg.body);

                // Sadece diğer kullanıcının gönderdiği mesajları ekle
                if (newMessage.senderId !== user.id) {
                    setMessages((prevMessages) => [...prevMessages, newMessage]);
                }
            });
        });

        return () => {
            if (stompClient.current) {
                stompClient.current.disconnect();
            }
        };
    }, [receiverId, user.id]);

    const sendMessage = async () => {
        if (!message.trim()) return;

        // Yeni konuşma veya mevcut konuşmayı getir
        await getOrCreateConversation(receiverId);

        try {
            const messageData = {
                content: message,
                senderId: user.id,
                receiverId,
            };

            // Mesajı backend'e gönder
            const response = await api.post(`/api/messages/send/${receiverId}`, messageData.content, {
                headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
            });

            // Konuşmayı listeye ekle
            const isExistingConversation = conversationList.some(
                (conv) => conv.sender.id === receiverId
            );
            if (!isExistingConversation) {
                const newConversation = {
                    sender: {
                        id: receiverId,
                        firstName: receiverName || "Unknown Business",
                    },
                    receiver: { id: user.id },
                };

                setConversationList((prevList) => [newConversation, ...prevList]);
            }

            // Mesajı listeye ekle
            setMessages((prevMessages) => [
                ...prevMessages,
                { ...messageData, sender: { id: user.id } },
            ]);

            setMessage(""); // Mesaj alanını temizle
            scrollToBottom();
        } catch (error) {
            console.error("[Messages.jsx] Error sending message:", error);
        }
    };

    const scrollToBottom = () => {
        setTimeout(() => {
            messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
        }, 100);
    };

    const handleConversationClick = (conv) => {
        setReceiverId(conv.sender.id);
        setReceiverName(conv.businessName || conv.sender.firstName);
        setActiveConversation(conv.sender.id);

        // Konuşmaya ait mesajları yükleyin
        const fetchMessages = async () => {
            try {
                const response = await api.get(
                    `/api/messages/conversation/${user.id}/${conv.sender.id}`,
                    { headers: { Authorization: `Bearer ${token}` } }
                );
                setMessages(response.data.data || []);
            } catch (error) {
                console.error("[Messages.jsx] Error fetching messages for conversation:", error);
            } finally {
                scrollToBottom();
            }
        };
        fetchMessages();
    };

    return (
        <div className="messages-container">
            <div className="conversation-list">
                <h4>Mesajlar</h4>
                <ul>
                    {conversationListWithBusinesses.map((conv) => (
                        <li
                            key={conv.sender.id}
                            className={activeConversation === conv.sender.id ? "active" : ""}
                            onClick={() => handleConversationClick(conv)}
                        >
                            {conv.businessName} {/* İşletme adı öncelikli */}
                        </li>
                    ))}

                </ul>
            </div>
            <div className="chat-window">
                <div className="messages-header">
                    <h4>{receiverName || "Mesajlar"}</h4>
                </div>
                <div className="messages-body">
                    {messages.map((msg, index) => (
                        <div
                            key={index}
                            className={`message-bubble ${msg.sender?.id === user.id ? "me" : "other"}`}
                        >
                            {msg.content.replace(/(^"|"$)/g, "")}
                        </div>
                    ))}
                    <div ref={messagesEndRef}></div>
                </div>
                <div className="messages-footer">
                    <input
                        type="text"
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        placeholder="Mesaj yazın..."
                    />
                    <button onClick={sendMessage}>Gönder</button>
                </div>
            </div>
        </div>
    );
}

export default Messages;
