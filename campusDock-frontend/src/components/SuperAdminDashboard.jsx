import React, { useState, useEffect } from 'react';
import { fetchColleges, fetchCanteens, fetchUsers } from '../api/apiConfig';
import { Building, Plus, Store, Users, User } from 'lucide-react';

const SuperAdminDashboard = () => {
  const [colleges, setColleges] = useState([]);
  const [canteens, setCanteens] = useState([]);
  const [selectedCollege, setSelectedCollege] = useState(null);
  const [showCollegeForm, setShowCollegeForm] = useState(false);
  const [showCanteenForm, setShowCanteenForm] = useState(false);
  const [collegeForm, setCollegeForm] = useState({ name: '', domain: '' });
  const [canteenForm, setCanteenForm] = useState({ name: '', description: '', isOpen: true, mediaFile: null });
  const [view, setView] = useState('canteens'); // 'canteens', 'students', 'faculty'
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [toast, setToast] = useState({ message: '', type: '' });

  useEffect(() => {
    fetchColleges().then(setColleges).catch(console.error);
  }, []);

  useEffect(() => {
    if ((view === 'students' || view === 'faculty') && selectedCollege) {
      fetchUsers().then(setUsers).catch(console.error);
    }
  }, [view, selectedCollege]);

  const handleAddCollege = async (e) => {
    e.preventDefault();
    try {
      await fetch('http://localhost:8081/api/v1/colleges', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(collegeForm),
      });
      setShowCollegeForm(false);
      setCollegeForm({ name: '', domain: '' });
      fetchColleges().then(setColleges).catch(console.error);
      setToast({ message: 'College added successfully!', type: 'success' });
    } catch (error) {
      setToast({ message: 'Failed to add college.', type: 'error' });
    }
    setTimeout(() => setToast({ message: '', type: '' }), 3000);
  };

  const handleAddCanteen = async (e) => {
    e.preventDefault();
    // TODO: Implement real API call for adding canteen
    setShowCanteenForm(false);
  };

  const selectCollege = (college) => {
    setSelectedCollege(college);
    fetchCanteens(college.id || college.collegeId).then(setCanteens).catch(console.error);
  };

  // Helper functions for counts
  const canteenCount = selectedCollege && view === 'canteens' ? canteens.length : null;
  const studentCount = (view === 'students' && selectedCollege && users.length)
    ? users.filter(u => u.role === 'STUDENT' && u.email && u.email.endsWith(`@${selectedCollege.domain}`)).length
    : null;
  const facultyCount = (view === 'faculty' && selectedCollege && users.length)
    ? users.filter(u => u.role === 'FACULTY' && u.email && u.email.endsWith(`@${selectedCollege.domain}`)).length
    : null;
  // College count badge
  const collegeCount = colleges.length;

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <h1 className="text-3xl font-bold mb-8 text-gray-800">Super Admin Dashboard</h1>
      {/* Move view buttons to the top for best UX */}
      <div className="flex justify-center space-x-4 mb-8">
        <button
          className={`relative px-6 py-2 rounded-lg font-semibold shadow transition-colors duration-150 ${view === 'canteens' ? 'bg-blue-600 text-white' : 'bg-gray-100 hover:bg-blue-100 text-blue-700'}`}
          onClick={() => setView('canteens')}
        >
          <Store className="inline w-5 h-5 mr-2 align-text-bottom" /> Canteens
          {canteenCount !== null && (
            <span className="absolute -top-2 -right-2 bg-blue-600 text-white text-xs font-bold rounded-full px-2 py-0.5 shadow">{canteenCount}</span>
          )}
        </button>
        <button
          className={`relative px-6 py-2 rounded-lg font-semibold shadow transition-colors duration-150 ${view === 'students' ? 'bg-green-600 text-white' : 'bg-gray-100 hover:bg-green-100 text-green-700'}`}
          onClick={() => setView('students')}
        >
          <Users className="inline w-5 h-5 mr-2 align-text-bottom" /> Students
          {studentCount !== null && (
            <span className="absolute -top-2 -right-2 bg-green-600 text-white text-xs font-bold rounded-full px-2 py-0.5 shadow">{studentCount}</span>
          )}
        </button>
        <button
          className={`relative px-6 py-2 rounded-lg font-semibold shadow transition-colors duration-150 ${view === 'faculty' ? 'bg-purple-600 text-white' : 'bg-gray-100 hover:bg-purple-100 text-purple-700'}`}
          onClick={() => setView('faculty')}
        >
          <User className="inline w-5 h-5 mr-2 align-text-bottom" /> Faculty
          {facultyCount !== null && (
            <span className="absolute -top-2 -right-2 bg-purple-600 text-white text-xs font-bold rounded-full px-2 py-0.5 shadow">{facultyCount}</span>
          )}
        </button>
      </div>
      {toast.message && (
        <div className={`fixed top-6 left-1/2 transform -translate-x-1/2 z-50 px-6 py-3 rounded shadow-lg text-white font-semibold transition-all duration-300 ${toast.type === 'success' ? 'bg-green-600' : 'bg-red-600'}`}
        >
          {toast.message}
        </div>
      )}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Colleges Section */}
        <div className="bg-white rounded-lg shadow-lg p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-semibold flex items-center">
              <Building className="mr-2 text-blue-600" />
              Colleges
              <span className="ml-2 inline-flex items-center justify-center w-7 h-7 rounded-full bg-blue-600 text-white text-sm font-bold">{collegeCount}</span>
            </h2>
            <button
              onClick={() => setShowCollegeForm(true)}
              className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 flex items-center"
            >
              <Plus className="w-4 h-4 mr-2" />
              Add College
            </button>
          </div>
          {/* College List */}
          <div className="space-y-3">
            {colleges.map((college) => (
              <div
                key={college.id}
                onClick={() => selectCollege(college)}
                className={`p-4 border rounded-lg cursor-pointer transition-colors ${
                  selectedCollege?.id === college.id
                    ? 'border-blue-500 bg-blue-50'
                    : 'border-gray-200 hover:border-gray-300'
                }`}
              >
                <h3 className="font-medium">{college.name}</h3>
                <p className="text-sm text-gray-600">{college.domain}</p>
                {college.studentNames && (
                  <p className="text-xs text-gray-400">Students: {college.studentNames.length}</p>
                )}
              </div>
            ))}
          </div>
          {/* Add College Form */}
          {showCollegeForm && (
            <form onSubmit={handleAddCollege} className="mb-6 p-4 bg-gray-50 rounded-lg mt-4">
              <div className="space-y-4">
                <input
                  type="text"
                  placeholder="College Name"
                  value={collegeForm.name}
                  onChange={(e) => setCollegeForm({...collegeForm, name: e.target.value})}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
                <input
                  type="text"
                  placeholder="Domain (e.g. @abes.ac.in)"
                  value={collegeForm.domain}
                  onChange={(e) => setCollegeForm({...collegeForm, domain: e.target.value})}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
                <div className="flex space-x-2">
                  <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700">
                    Add
                  </button>
                  <button 
                    type="button" 
                    onClick={() => setShowCollegeForm(false)}
                    className="bg-gray-400 text-white px-4 py-2 rounded-md hover:bg-gray-500"
                  >
                    Cancel
                  </button>
                </div>
              </div>
            </form>
          )}
        </div>
        {/* Right Side Section: Dynamic based on view */}
        <div className="bg-white rounded-lg shadow-lg p-6">
          {view === 'canteens' && selectedCollege && (
            <>
              <div className="flex items-center mb-6 justify-between">
                <div className="flex items-center">
                  <Store className="mr-2 text-green-600" />
                  <h2 className="text-xl font-semibold">Canteens {selectedCollege && <span className="text-sm text-gray-500 ml-2">({selectedCollege.name})</span>}</h2>
                </div>
                <button
                  onClick={() => setShowCanteenForm(true)}
                  className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 flex items-center"
                >
                  <Plus className="w-4 h-4 mr-2" />
                  Add Canteen
                </button>
              </div>
              {showCanteenForm && (
                <form
                  onSubmit={async (e) => {
                    e.preventDefault();
                    try {
                      let response, data;
                      const canteenPayload = {
                        name: canteenForm.name,
                        description: canteenForm.description,
                        isOpen: canteenForm.isOpen,
                        college: selectedCollege.id
                      };
                      if (canteenForm.mediaFile) {
                        const formData = new FormData();
                        formData.append(
                          'canteen',
                          new Blob([JSON.stringify(canteenPayload)], { type: 'application/json' })
                        );
                        formData.append('media_file', canteenForm.mediaFile);
                        response = await fetch(`http://localhost:8081/api/v1/colleges/${selectedCollege.id}/canteens`, {
                          method: 'POST',
                          body: formData
                        });
                      } else {
                        const formData = new FormData();
                        formData.append(
                          'canteen',
                          new Blob([JSON.stringify(canteenPayload)], { type: 'application/json' })
                        );
                        response = await fetch(`http://localhost:8081/api/v1/colleges/${selectedCollege.id}/canteens`, {
                          method: 'POST',
                          body: formData
                        });
                      }
                      try {
                        data = await response.json();
                      } catch (e) {}
                      if (response.ok && data && (data.canetten_id || data.canteen_id)) {
                        setShowCanteenForm(false);
                        setCanteenForm({ name: '', description: '', isOpen: true, mediaFile: null });
                        fetchCanteens(selectedCollege.id).then(setCanteens).catch(console.error);
                        setToast({ message: 'Canteen added successfully!', type: 'success' });
                      } else {
                        setToast({ message: 'Failed to add canteen.', type: 'error' });
                      }
                    } catch (error) {
                      setToast({ message: 'Failed to add canteen.', type: 'error' });
                    }
                    setTimeout(() => setToast({ message: '', type: '' }), 3000);
                  }}
                  className="mb-6 p-4 bg-gray-50 rounded-lg"
                  encType="multipart/form-data"
                >
                  <div className="space-y-4">
                    <input
                      type="text"
                      placeholder="Canteen Name"
                      value={canteenForm.name}
                      onChange={e => setCanteenForm({ ...canteenForm, name: e.target.value })}
                      className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                      required
                    />
                    <input
                      type="text"
                      placeholder="Description"
                      value={canteenForm.description}
                      onChange={e => setCanteenForm({ ...canteenForm, description: e.target.value })}
                      className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <div className="flex items-center space-x-2">
                      <input
                        type="checkbox"
                        checked={canteenForm.isOpen}
                        onChange={e => setCanteenForm({ ...canteenForm, isOpen: e.target.checked })}
                        id="isOpen"
                      />
                      <label htmlFor="isOpen">Open</label>
                    </div>
                    <input
                      type="file"
                      accept="image/*"
                      onChange={e => setCanteenForm({ ...canteenForm, mediaFile: e.target.files[0] })}
                    />
                    <div className="flex space-x-2">
                      <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700">
                        Add
                      </button>
                      <button
                        type="button"
                        onClick={() => setShowCanteenForm(false)}
                        className="bg-gray-400 text-white px-4 py-2 rounded-md hover:bg-gray-500"
                      >
                        Cancel
                      </button>
                    </div>
                  </div>
                </form>
              )}
              <div className="space-y-3">
                {canteens.map((canteen) => (
                  <div key={canteen.id} className="p-4 border border-gray-200 rounded-lg flex justify-between items-center">
                    <div>
                      <h3 className="font-medium">{canteen.name}</h3>
                      <p className="text-sm text-gray-600">ID: {canteen.id}</p>
                    </div>
                    <span className={`px-2 py-1 text-xs rounded-full ${canteen.open ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>{canteen.open ? 'Open' : 'Closed'}</span>
                  </div>
                ))}
              </div>
            </>
          )}
          {view === 'students' && selectedCollege && (
            <>
              <div className="flex items-center mb-6">
                <Users className="mr-2 text-green-600" />
                <h2 className="text-xl font-semibold">Students {selectedCollege && <span className="text-sm text-gray-500 ml-2">({selectedCollege.name})</span>}</h2>
              </div>
              <div className="space-y-3">
                {users.filter(u => u.role === 'STUDENT' && u.email && u.email.endsWith(`@${selectedCollege.domain}`)).map((student) => (
                  <div key={student.id} className="p-4 border border-gray-200 rounded-lg cursor-pointer" onClick={() => setSelectedUser(student)}>
                    <div className="flex justify-between items-center">
                      <div>
                        <h3 className="font-medium">{student.name}</h3>
                        <p className="text-sm text-gray-600">{student.email}</p>
                      </div>
                      <span className="px-2 py-1 text-xs rounded-full bg-green-100 text-green-800">{student.role}</span>
                    </div>
                  </div>
                ))}
              </div>
            </>
          )}
          {view === 'faculty' && selectedCollege && (
            <>
              <div className="flex items-center mb-6">
                <User className="mr-2 text-purple-600" />
                <h2 className="text-xl font-semibold">Faculty {selectedCollege && <span className="text-sm text-gray-500 ml-2">({selectedCollege.name})</span>}</h2>
              </div>
              <div className="space-y-3">
                {users.filter(u => u.role === 'FACULTY' && u.email && u.email.endsWith(`@${selectedCollege.domain}`)).map((faculty) => (
                  <div key={faculty.id} className="p-4 border border-gray-200 rounded-lg cursor-pointer" onClick={() => setSelectedUser(faculty)}>
                    <div className="flex justify-between items-center">
                      <div>
                        <h3 className="font-medium">{faculty.name}</h3>
                        <p className="text-sm text-gray-600">{faculty.email}</p>
                      </div>
                      <span className="px-2 py-1 text-xs rounded-full bg-purple-100 text-purple-800">{faculty.role}</span>
                    </div>
                  </div>
                ))}
              </div>
            </>
          )}
          {selectedUser && (
            <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
              <div className="bg-white p-6 rounded-lg shadow-lg max-w-md w-full relative">
                <button className="absolute top-2 right-2 text-gray-500 hover:text-gray-700" onClick={() => setSelectedUser(null)}>&times;</button>
                <h2 className="text-xl font-bold mb-2">{selectedUser.name}'s Profile</h2>
                <p><b>Email:</b> {selectedUser.email}</p>
                <p><b>Role:</b> {selectedUser.role}</p>
                {/* Show all other details dynamically */}
                {Object.entries(selectedUser).map(([key, value]) => (
                  !['id', 'name', 'email', 'role'].includes(key) && (
                    <p key={key}><b>{key}:</b> {typeof value === 'object' ? JSON.stringify(value) : String(value)}</p>
                  )
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default SuperAdminDashboard;