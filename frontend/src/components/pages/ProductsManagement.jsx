import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import Products from "./Products";

function ProductsManagement() {
    const { user } = useAuth();
    const navigate = useNavigate();

    // Kullanıcı rol kontrolü
    if (user?.role !== "ROLE_BUSINESS_OWNER") {
        navigate("/"); // BusinessOwner değilse ana sayfaya yönlendir
        return null;
    }

    return (
        <div className="container mt-5">
            <Products />
        </div>
    );
}

export default ProductsManagement;
