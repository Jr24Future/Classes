import React from 'react';
import './about.css';

function About() {
    return (
        <div className="min-h-screen flex flex-col items-center bg-gray-900 text-white">
            <header className="w-full text-center py-4 bg-gray-800 fixed top-0 z-50">
                <div className="container mx-auto">
                    <h3 className="text-2xl">About</h3>
                    <nav className="flex justify-center space-x-4 mt-2">
                        <a className="hover:underline" href="/main">Home</a>
                        <a className="hover:underline" href="/">Log-out</a>
                    </nav>
                </div>
            </header>
            <main className="flex flex-col items-center mt-40">
                <div className="flex space-x-5">
                    <img class='left-image' src="/images/onur.jpg" alt="Onur Onal" className="w-60 h-auto rounded-md shadow-lg" />
                    <img class='right-image' src="/images/erroll.jpeg" alt="Erroll Barker" className="w-60 h-auto rounded-md shadow-lg" />
                </div>
                <h1 className="text-3xl font-bold mt-6">Onur Onal - Erroll Barker</h1>
                <div className="text-center mt-4 space-y-2">
                    <p><span className="underline">Course Name</span>: S E 319</p>
                    <p><span className="underline">Student Emails</span>: onur@iastate.edu - errollb@iastate.edu</p>
                    <p><span className="underline">Date</span>: 02/08/2024</p>
                    <p><span className="underline">Professor Name</span>: Abraham Aldaco</p>
                </div>
            </main>
        </div>
    );
}

export default About;
