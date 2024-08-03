import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

function Signup() {
    const navigate = useNavigate();
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [address, setAddress] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    async function submit(e) {
        e.preventDefault();
        console.log("Signup form submitted");

        try {
            const response = await axios.post("http://localhost:8000/signup", {
                firstName,
                lastName,
                phoneNumber,
                address,
                email,
                password
            });

            console.log("Signup response:", response.data);

            if (response.data === "exist") {
                setMessage("User already exists");
            } else if (response.data === "notexist") {
                navigate("/main", { state: { id: email } });
            }
        } catch (e) {
            console.error('Error during signup:', e);
            setMessage("Signup failed");
        }
    }

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
            <h1 className="text-3xl font-bold mb-6">Signup</h1>
            <form className="bg-white p-6 rounded shadow-md w-full max-w-md" onSubmit={submit}>
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
                <button type="submit" className="w-full p-2 bg-blue-500 text-white rounded mt-4">Signup</button>
                {message && <p className="mt-4 text-center text-red-500">{message}</p>}
            </form>
            <p className="mt-4">Already have an account? <Link to="/login" className="text-blue-500 hover:underline">Login</Link></p>
        </div>
    );
}

export default Signup;
