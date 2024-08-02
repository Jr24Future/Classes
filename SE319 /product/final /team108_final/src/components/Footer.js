import React from 'react';
import { useNavigate } from "react-router-dom";

function Footer({ userId }) {
    const navigate = useNavigate();

    return (
        <footer className="bg-gray-800 py-5 relative">
            <div className="container mx-auto text-center text-white relative">
                <p className="inline-block">
                    Learn more about us. We are{' '}
                    <a href="/about" className="underline text-blue-400 hover:text-blue-300">Engineers</a>, by{' '}
                    <a href="https://jr24future.github.io/personal/" className="underline text-blue-400 hover:text-blue-300">Erroll</a> and{' '}
                    <a href="https://onuronal7.github.io/personal/index.html" className="underline text-blue-400 hover:text-blue-300">Onur</a>
                </p>
                <button 
                    onClick={() => navigate("/settings", { state: { id: userId } })} 
                    className="fa fa-cog absolute top-1/2 right-5 transform -translate-y-1/2 text-white hover:text-blue-400"
                ></button>
            </div>
        </footer>
    );
}

export default Footer;
