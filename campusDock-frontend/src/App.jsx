import React, { useState, useEffect } from 'react';
import SuperAdminDashboard from './components/SuperAdminDashboard';
import CanteenAdminDashboard from './components/CanteenAdminDashboard';
import { getStoredRole, setRole } from './utils/auth';
import { UserCog, Store, Moon, Sun } from 'lucide-react';

function App() {
  const [currentRole, setCurrentRole] = useState(getStoredRole());
  const [darkMode, setDarkMode] = useState(() => {
    return localStorage.getItem('darkMode') === 'true';
  });

  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
    localStorage.setItem('darkMode', darkMode);
  }, [darkMode]);

  const switchRole = (role) => {
    setRole(role);
    setCurrentRole(role);
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
      {/* Role Switcher and Dark Mode Toggle */}
      <div className="bg-white dark:bg-gray-800 shadow-sm border-b border-gray-200 dark:border-gray-700">
        <div className="max-w-6xl mx-auto px-6 py-4">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold text-gray-800 dark:text-gray-100">Campus Dock</h1>
            <div className="flex space-x-2 items-center">
              <button
                onClick={() => setDarkMode((d) => !d)}
                className="rounded-full p-2 bg-gray-100 dark:bg-gray-700 hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors border border-gray-200 dark:border-gray-600 focus:outline-none"
                aria-label="Toggle dark mode"
                title={darkMode ? 'Switch to light mode' : 'Switch to dark mode'}
              >
                {darkMode ? <Sun className="w-5 h-5 text-yellow-400" /> : <Moon className="w-5 h-5 text-gray-700 dark:text-gray-200" />}
              </button>
              <button
                onClick={() => switchRole('SUPER_ADMIN')}
                className={`px-4 py-2 rounded-lg flex items-center ${
                  currentRole === 'SUPER_ADMIN'
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-200 text-gray-700 hover:bg-gray-300 dark:bg-gray-700 dark:text-gray-100 dark:hover:bg-gray-600'
                }`}
              >
                <UserCog className="w-4 h-4 mr-2" />
                Super Admin
              </button>
              <button
                onClick={() => switchRole('CANTEEN_ADMIN')}
                className={`px-4 py-2 rounded-lg flex items-center ${
                  currentRole === 'CANTEEN_ADMIN'
                    ? 'bg-orange-600 text-white'
                    : 'bg-gray-200 text-gray-700 hover:bg-gray-300 dark:bg-gray-700 dark:text-gray-100 dark:hover:bg-gray-600'
                }`}
              >
                <Store className="w-4 h-4 mr-2" />
                Canteen Admin
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Dashboard Content */}
      {currentRole === 'SUPER_ADMIN' ? (
        <SuperAdminDashboard />
      ) : (
        <CanteenAdminDashboard />
      )}
    </div>
  );
}

export default App;