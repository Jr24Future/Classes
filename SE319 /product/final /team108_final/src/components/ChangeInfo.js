import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';

function ChangeInfo() {
    const location = useLocation();
    const navigate = useNavigate();
    const userId = location.state?.id || localStorage.getItem('userId') || '';

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [address, setAddress] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [showDeleteModal, setShowDeleteModal] = useState(false); // State to control modal visibility

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get(`http://localhost:8000/user/${userId}`);

                if (response.data !== "notexist" && response.data !== "fail") {
                    const { firstName, lastName, phoneNumber, address, email } = response.data;
                    setFirstName(firstName);
                    setLastName(lastName);
                    setPhoneNumber(phoneNumber);
                    setAddress(address);
                    setEmail(email);
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

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.put('http://localhost:8000/settings', {
                userId, firstName, lastName, phoneNumber, address, password
            });

            if (response.data === 'success') {
                setMessage('Information updated successfully');
            } else {
                setMessage('Update failed');
            }
        } catch (error) {
            console.error('Error updating information:', error);
            setMessage('Update failed');
        }
    };

    const handleDeleteAccount = async () => {
        try {
            const response = await axios.delete(`http://localhost:8000/user/${userId}`);
            if (response.data === 'success') {
                localStorage.removeItem('userId');
                navigate('/signup');
            } else {
                setMessage('Failed to delete account');
            }
        } catch (error) {
            console.error('Error deleting account:', error);
            setMessage('Failed to delete account');
        }
    };

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
            <h1 className="text-3xl font-bold mb-6">Change Info</h1>
            <form className="bg-white p-6 rounded shadow-md w-full max-w-md" onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label className="block text-gray-700">First Name</label>
                    <input
                        type="text"
                        className="w-full p-2 border border-gray-300 rounded mt-1"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                    />
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700">Last Name</label>
                    <input
                        type="text"
                        className="w-full p-2 border border-gray-300 rounded mt-1"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                    />
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700">Phone Number</label>
                    <input
                        type="text"
                        className="w-full p-2 border border-gray-300 rounded mt-1"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                    />
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700">Address</label>
                    <input
                        type="text"
                        className="w-full p-2 border border-gray-300 rounded mt-1"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                    />
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700">Email</label>
                    <input
                        type="email"
                        className="w-full p-2 border border-gray-300 rounded mt-1"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        disabled
                    />
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700">Password</label>
                    <input
                        type="password"
                        className="w-full p-2 border border-gray-300 rounded mt-1"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button type="submit" className="w-full p-2 bg-blue-500 text-white rounded mt-4">Update</button>
                {message && <p className="mt-4 text-center text-red-500">{message}</p>}
            </form>
            <button 
                onClick={() => setShowDeleteModal(true)} 
                className="w-full max-w-md p-2 bg-red-500 text-white rounded mt-4 hover:bg-red-600 transition duration-300"
            >
                Delete Account
            </button>

            {/* Delete Confirmation Modal */}
            {showDeleteModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded shadow-lg w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">Confirm Deletion</h2>
                        <p className="mb-4">Are you sure you want to delete your account? This action cannot be undone.</p>
                        <div className="flex justify-end space-x-4">
                            <button 
                                onClick={() => setShowDeleteModal(false)}
                                className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400 transition duration-300"
                            >
                                Cancel
                            </button>
                            <button 
                                onClick={handleDeleteAccount}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition duration-300"
                            >
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default ChangeInfo;
