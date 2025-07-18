import React, { useState } from 'react';
import SuperAdminDashboard from './components/SuperAdminDashboard';
import CanteenAdminDashboard from './components/CanteenAdminDashboard';
import { getStoredRole, setRole } from './utils/auth';
import { UserCog, Store } from 'lucide-react';

function App() {
  const [currentRole, setCurrentRole] = useState(getStoredRole());

  const switchRole = (role) => {
    setRole(role);
    setCurrentRole(role);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Role Switcher */}
      <div className="bg-white shadow-sm border-b">
        <div className="max-w-6xl mx-auto px-6 py-4">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold text-gray-800">Campus Dock</h1>
            <div className="flex space-x-2">
              <button
                onClick={() => switchRole('SUPER_ADMIN')}
                className={`px-4 py-2 rounded-lg flex items-center ${
                  currentRole === 'SUPER_ADMIN'
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
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
                    : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
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