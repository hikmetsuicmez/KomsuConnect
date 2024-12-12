import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from './components/pages/Home';
import Login from './components/pages/Login';
import Register from './components/pages/Register';
import Dashboard from './components/pages/Dashboard';
import Profile from './components/pages/Profile';
import Messages from './components/pages/Messages';
import Notification from './components/common/Notifications';
import ProductsManagement from './components/pages/ProductsManagement';
import AddProduct from './components/pages/AddProduct';
import UpdateProduct from './components/pages/UpdateProduct';
import Navbar from './components/common/Navbar';
import ProtectedRoute from './context/ProtectedRoute';
import { AuthProvider } from './context/AuthContext';
import { ThemeProvider } from './context/ThemeContext';
import './components/styles/App.css';
import BusinessDetail from './components/pages/BusinessDetail';
import Favorites from './components/pages/Favorites';
import CartPage from './components/pages/CartPage';
import OrderHistory from './components/pages/OrderHistory';

function App() {
    return (
        <ThemeProvider>
            <AuthProvider>
                <div className="app">
                    <Routes>
                        {/* Herkesin erişebileceği rotalar */}
                        <Route path="/" element={<Home />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />

                        {/* Korumalı rotalar */}
                        <Route element={<ProtectedRoute />}>
                            <Route path="/dashboard" element={<Layout component={<Dashboard />} />} />
                            <Route path="/profile" element={<Layout component={<Profile />} />} />
                            <Route path="/messages" element={<Layout component={<Messages />} />} />
                            <Route path="/notifications" element={<Layout component={<Notification />} />} />
                            <Route path="/products" element={<Layout component={<ProductsManagement />} />} />
                            <Route path="/add-product" element={<Layout component={<AddProduct />} />} />
                            <Route path="/update-product" element={<Layout component={<UpdateProduct />} />} />
                            <Route path="/business-detail" element={<Layout component={<BusinessDetail />} />} />
                            <Route path="/favorites" element={<Layout component={<Favorites />} />} />
                            <Route path="/cart" element={<Layout component={<CartPage />} />} />
                            <Route path="/order-history" element={<Layout component={<OrderHistory />} />} />
                        </Route>
                    </Routes>
                </div>
            </AuthProvider>
        </ThemeProvider>
    );
}

/**
 * Layout bileşeni
 * Her korumalı rota için tekrar eden Navbar'ı burada merkezi hale getiriyoruz.
 */
const Layout = ({ component }) => {
    return (
        <>
            <Navbar />
            {component}
        </>
    );
};

export default App;
