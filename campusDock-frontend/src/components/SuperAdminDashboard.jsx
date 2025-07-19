import React, { useState, useEffect } from 'react';
import { fetchColleges, fetchCanteens, fetchUsers, BASE_URL } from '../api/apiConfig';
import { Building, Plus, Store, Users, User, UserCog } from 'lucide-react';
import { Switch } from '@headlessui/react';

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
  const [canteenDetails, setCanteenDetails] = useState(null);
  const [showCanteenModal, setShowCanteenModal] = useState(false);
  const [isToggling, setIsToggling] = useState(false);
  const [showConfirmToggle, setShowConfirmToggle] = useState(false);
  const [showOwnerForm, setShowOwnerForm] = useState(false);
  const [ownerForm, setOwnerForm] = useState({ name: '', email: '', password: '' });
  const [ownerLoading, setOwnerLoading] = useState(false);
  const [ownerMessage, setOwnerMessage] = useState(null);
  const [ownerInfo, setOwnerInfo] = useState(null);

  useEffect(() => {
    fetchColleges().then(setColleges).catch(console.error);
  }, []);

  useEffect(() => {
    if ((view === 'students' || view === 'faculty') && selectedCollege) {
      fetchUsers().then(setUsers).catch(console.error);
    }
  }, [view, selectedCollege]);

  // When canteen details modal is closed or a new canteen is opened, reset the owner form state
  useEffect(() => {
    if (!showCanteenModal) {
      setShowOwnerForm(false);
      setOwnerForm({ name: '', email: '', password: '' });
      setOwnerMessage(null);
      setOwnerInfo(null);
    }
  }, [showCanteenModal, canteenDetails?.id]);

  const handleAddCollege = async (e) => {
    e.preventDefault();
    try {
      await fetch(`${BASE_URL}/colleges`, {
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

  // Fetch canteen details by ID
  const fetchCanteenDetails = async (canteenId) => {
    try {
      const res = await fetch(`${BASE_URL}/colleges/canteens/${canteenId}`);
      if (!res.ok) throw new Error('Failed to fetch canteen details');
      const data = await res.json();
      setCanteenDetails(data);
      // Fetch owner info
      try {
        const ownerRes = await fetch(`${BASE_URL}/admins/getCanteenOwners/${canteenId}`);
        if (ownerRes.ok) {
          const owner = await ownerRes.json();
          if (owner && owner.name && owner.email) {
            setOwnerInfo(owner);
          } else {
            setOwnerInfo(null);
          }
        } else {
          setOwnerInfo(null);
        }
      } catch {
        setOwnerInfo(null);
      }
      setShowCanteenModal(true);
    } catch (error) {
      setToast({ message: 'Failed to load canteen details.', type: 'error' });
      setTimeout(() => setToast({ message: '', type: '' }), 3000);
    }
  };

  // Toggle open/close status
  const handleToggleOpen = async () => {
    if (!canteenDetails) return;
    setIsToggling(true);
    try {
      const res = await fetch(`${BASE_URL}/colleges/canteens/${canteenDetails.id}/toggle-open`, {
        method: 'PATCH',
      });
      if (!res.ok) throw new Error('Failed to toggle open status');
      const updated = await res.json();
      setCanteenDetails(updated);
      // Optionally update the canteens list
      setCanteens((prev) => prev.map(c => c.id === updated.id ? { ...c, open: updated.open } : c));
      setToast({ message: `Canteen is now ${updated.open ? 'Open' : 'Closed'}.`, type: 'success' });
    } catch (error) {
      setToast({ message: 'Failed to toggle open status.', type: 'error' });
    }
    setIsToggling(false);
    setTimeout(() => setToast({ message: '', type: '' }), 3000);
  };

  const handleRegisterOwner = async (e) => {
    e.preventDefault();
    setOwnerLoading(true);
    setOwnerMessage(null);
    try {
      const res = await fetch(`${BASE_URL}/admins/owners`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...ownerForm,
          canteenId: canteenDetails.id,
        }),
      });
      const data = await res.json();
      if (res.ok) {
        setOwnerMessage({ type: 'success', text: data.message || 'Owner registered successfully!' });
        setOwnerForm({ name: '', email: '', password: '' });
        setShowOwnerForm(false);
      } else {
        setOwnerMessage({ type: 'error', text: data.message || 'Failed to register owner.' });
      }
    } catch (error) {
      setOwnerMessage({ type: 'error', text: 'Failed to register owner.' });
    }
    setOwnerLoading(false);
    setTimeout(() => setOwnerMessage(null), 4000);
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <h1 className="text-3xl font-bold mb-8 text-gray-800 dark:text-white">Super Admin Dashboard</h1>
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
        <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg p-6 border border-gray-200 dark:border-gray-700">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-semibold flex items-center text-gray-900 dark:text-gray-100">
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
                    ? 'border-blue-500 bg-blue-50 dark:bg-gray-800 dark:border-blue-400'
                    : 'border-gray-200 hover:border-gray-300 dark:border-gray-700 dark:hover:border-gray-500 bg-white dark:bg-gray-900'
                }`}
              >
                <h3 className="font-medium text-gray-900 dark:text-gray-100">{college.name}</h3>
                <p className="text-sm text-gray-600 dark:text-gray-300">{college.domain}</p>
                {college.studentNames && (
                  <p className="text-xs text-gray-400 dark:text-gray-400">Students: {college.studentNames.length}</p>
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
        <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg p-6 border border-gray-200 dark:border-gray-700">
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
                        response = await fetch(`${BASE_URL}/colleges/${selectedCollege.id}/canteens`, {
                          method: 'POST',
                          body: formData
                        });
                      } else {
                        const formData = new FormData();
                        formData.append(
                          'canteen',
                          new Blob([JSON.stringify(canteenPayload)], { type: 'application/json' })
                        );
                        response = await fetch(`${BASE_URL}/colleges/${selectedCollege.id}/canteens`, {
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
                  <div key={canteen.id} className="p-4 border border-gray-200 rounded-lg flex justify-between items-center cursor-pointer" onClick={() => fetchCanteenDetails(canteen.id)}>
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
      {/* Canteen Details Modal */}
      {showCanteenModal && canteenDetails && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white dark:bg-gray-900 rounded-2xl shadow-2xl p-10 max-w-2xl w-full relative flex flex-col items-center border border-gray-200 dark:border-gray-700">
            <button className="absolute top-4 right-4 text-3xl text-gray-400 hover:text-gray-700 dark:text-gray-300 dark:hover:text-white" onClick={() => setShowCanteenModal(false)}>&times;</button>
            <div className="w-full flex flex-col md:flex-row gap-8 items-center">
              <div className="flex-shrink-0 w-64 h-64 bg-gray-100 dark:bg-gray-800 rounded-xl overflow-hidden flex items-center justify-center border border-gray-200 dark:border-gray-700">
                {canteenDetails.mediaUrl ? (
                  <img src={canteenDetails.mediaUrl} alt="Canteen" className="object-cover w-full h-full" />
                ) : (
                  <img src="https://cdn-icons-png.flaticon.com/512/3075/3075977.png" alt="Canteen Placeholder" className="object-contain w-32 h-32 opacity-60" />
                )}
              </div>
              <div className="flex-1">
                <h2 className="text-3xl font-bold mb-2 text-gray-900 dark:text-gray-100">{canteenDetails.name}</h2>
                <p className="mb-2 text-gray-700 text-lg dark:text-gray-200">{canteenDetails.description}</p>
                <p className="mb-2 text-gray-500 text-sm dark:text-gray-400">ID: {canteenDetails.id}</p>
                <p className="mb-2 text-gray-500 text-sm dark:text-gray-400">College ID: {canteenDetails.collegeId}</p>
                {/* Owner info or register button BELOW canteen details */}
                {ownerInfo ? (
                  <div className="mb-4 p-4 rounded-xl bg-white border-2 border-green-400 flex items-center gap-4 shadow-sm">
                    <div className="flex-shrink-0">
                      <div className="w-14 h-14 rounded-full bg-green-100 flex items-center justify-center text-2xl font-bold text-green-700 border-2 border-green-300">
                        {ownerInfo.name ? ownerInfo.name[0].toUpperCase() : <UserCog className="w-8 h-8" />}
                      </div>
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2 mb-1 flex-nowrap min-w-0">
                        <span className="font-semibold text-green-800 text-lg whitespace-nowrap">Canteen Owner</span>
                        <span className="ml-2 px-2 py-0.5 rounded bg-green-100 text-green-700 text-xs font-medium whitespace-nowrap">Verified</span>
                      </div>
                      <div className="truncate text-gray-900 font-medium text-base">{ownerInfo.name}</div>
                      <div className="truncate text-gray-600 text-sm">{ownerInfo.email}</div>
                    </div>
                  </div>
                ) : (
                  <button
                    className="mb-4 px-3 py-1.5 rounded bg-red-600 text-white font-bold shadow flex items-center justify-center gap-2 border border-red-700 hover:bg-red-700 transition text-base"
                    onClick={() => setShowOwnerForm((v) => !v)}
                  >
                    <UserCog className="w-4 h-4 mr-1" />
                    {showOwnerForm ? 'Cancel Owner Registration' : 'Register Canteen Owner'}
                  </button>
                )}
                {/* Owner Registration Form */}
                {showOwnerForm && (
                  <form onSubmit={handleRegisterOwner} className="mb-4 p-4 bg-gray-50 dark:bg-gray-800 rounded-lg border border-gray-200 dark:border-gray-700">
                    <div className="mb-2">
                      <input
                        type="text"
                        placeholder="Owner Name"
                        value={ownerForm.name}
                        onChange={e => setOwnerForm({ ...ownerForm, name: e.target.value })}
                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 mb-2 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
                        required
                      />
                      <input
                        type="email"
                        placeholder="Owner Email"
                        value={ownerForm.email}
                        onChange={e => setOwnerForm({ ...ownerForm, email: e.target.value })}
                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 mb-2 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
                        required
                      />
                      <input
                        type="password"
                        placeholder="Password"
                        value={ownerForm.password}
                        onChange={e => setOwnerForm({ ...ownerForm, password: e.target.value })}
                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
                        required
                      />
                    </div>
                    <button
                      type="submit"
                      className="w-full bg-indigo-600 text-white py-2 rounded font-semibold hover:bg-indigo-700 transition"
                      disabled={ownerLoading}
                    >
                      {ownerLoading ? 'Registering...' : 'Register Owner'}
                    </button>
                  </form>
                )}
                {/* Owner Registration Message */}
                {ownerMessage && (
                  <div className={`mb-2 px-4 py-2 rounded text-center font-semibold ${ownerMessage.type === 'success' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                    {ownerMessage.text}
                  </div>
                )}
                <div className="flex items-center space-x-4 mt-4">
                  <span className={`px-4 py-2 rounded-full text-base font-semibold ${canteenDetails.open ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' : 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'}`}>{canteenDetails.open ? 'Currently Open' : 'Currently Closed'}</span>
                  <Switch
                    checked={canteenDetails.open}
                    onChange={() => setShowConfirmToggle(true)}
                    className={`${canteenDetails.open ? 'bg-green-500' : 'bg-red-500'} relative inline-flex h-8 w-16 items-center rounded-full transition-colors focus:outline-none ml-4`}
                  >
                    <span className="sr-only">Toggle open/close</span>
                    <span
                      className={`inline-block h-6 w-6 transform rounded-full bg-white shadow transition-transform ${canteenDetails.open ? 'translate-x-8' : 'translate-x-2'}`}
                    />
                  </Switch>
                </div>
                {canteenDetails.createdAt && (
                  <p className="mt-4 text-xs text-gray-400 dark:text-gray-500">Created: {new Date(canteenDetails.createdAt).toLocaleString()}</p>
                )}
              </div>
            </div>
            {/* Confirmation Popup */}
            {showConfirmToggle && (
              <div className="fixed inset-0 z-60 flex items-center justify-center bg-black bg-opacity-30">
                <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg p-6 max-w-xs w-full flex flex-col items-center border border-gray-200 dark:border-gray-700">
                  <p className="text-lg font-semibold mb-4 text-center text-gray-900 dark:text-gray-100">
                    Are you sure you want to {canteenDetails.open ? 'mark this canteen as Closed?' : 'mark this canteen as Open?'}
                  </p>
                  <div className="flex space-x-4">
                    <button
                      onClick={() => { setShowConfirmToggle(false); handleToggleOpen(); }}
                      className={`px-4 py-2 rounded ${canteenDetails.open ? 'bg-red-600 hover:bg-red-700' : 'bg-green-600 hover:bg-green-700'} text-white font-semibold`}
                    >
                      Yes, {canteenDetails.open ? 'Close' : 'Open'}
                    </button>
                    <button
                      onClick={() => setShowConfirmToggle(false)}
                      className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400 text-gray-800 dark:bg-gray-700 dark:text-gray-100 dark:hover:bg-gray-600 font-semibold"
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
      {/* Owner Registration Toast Popup */}
      {ownerMessage && (
        <div className={`fixed top-8 left-1/2 transform -translate-x-1/2 z-50 px-6 py-3 rounded shadow-lg text-white font-semibold transition-all duration-300 ${ownerMessage.type === 'success' ? 'bg-green-600' : 'bg-red-600'} dark:bg-opacity-90`}
        >
          {ownerMessage.text}
        </div>
      )}
    </div>
  );
};

export default SuperAdminDashboard;