import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation, useNavigate, Link } from 'react-router-dom';

function Settings() {
    const location = useLocation();
    const navigate = useNavigate();
    const userId = location.state?.id || localStorage.getItem('userId') || '';

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [message, setMessage] = useState('');

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get(`http://localhost:8000/user/${userId}`);

                if (response.data !== "notexist" && response.data !== "fail") {
                    const { firstName, lastName } = response.data;
                    setFirstName(firstName);
                    setLastName(lastName);
                } else {
                    setMessage('Failed to fetch user data');
                }
            } catch (error) {
                console.error('Error fetching user data:', error);
                setMessage('Failed to fetch user data');
            }
        };

        if (userId) {
            fetchUserData();
        } else {
            setMessage('No user ID provided');
        }
    }, [userId]);

    const handleLogout = () => {
        localStorage.removeItem('userId');
        navigate('/login');
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
            <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-5xl flex">
                <div className="w-1/2 flex flex-col items-center justify-center border-r border-gray-300 pr-6">
                    <h1 className="text-5xl font-bold mb-2">Welcome</h1>
                    <h2 className="text-3xl font-bold mb-3">{firstName}</h2>
                    <p className="mt-2 text-center">
                        If you have any problems please reach out to us from <Link to="/about" className="text-blue-600 hover:text-blue-800">here</Link>.
                    </p>
                </div>
                <div className="w-1/2 pl-6">
                    <h2 className="text-3xl font-bold mb-4">Settings</h2>
                    <div className="flex flex-col space-y-4">
                        <button 
                            onClick={() => navigate("/change-info", { state: { id: userId } })}
                            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition duration-300"
                        >
                            Change Info
                        </button>
                        <button 
                            onClick={() => navigate("/main", { state: { id: userId } })}
                            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition duration-300"
                        >
                            Return to Shopping
                        </button>
                        <button 
                            onClick={handleLogout}
                            className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition duration-300"
                        >
                            Log Out
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Settings;
