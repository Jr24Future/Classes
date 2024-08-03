import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './components/MainPage';
import Login from './components/Login';
import Signup from './components/Signup';
import About from './components/About';
import Settings from './components/Settings';
import ChangeInfo from './components/ChangeInfo';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/main" element={<MainPage />} />
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/about" element={<About />} />
                <Route path="/settings" element={<Settings />} />
                <Route path="/change-info" element={<ChangeInfo />} />
            </Routes>
        </Router>
    );
}

export default App;
