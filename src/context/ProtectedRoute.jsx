import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from './AuthContext';

function ProtectedRoute() {
    const { user } = useAuth();

    if (!user) {
        return <Navigate to="/login" />;
    }

    return <Outlet />;
}

export default ProtectedRoute;
