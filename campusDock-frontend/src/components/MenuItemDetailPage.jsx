import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Switch } from '@headlessui/react';

const DEFAULT_IMG = 'https://cdn-icons-png.flaticon.com/512/3075/3075977.png';

const MenuItemDetailPage = () => {
  const { menuItemId } = useParams();
  const navigate = useNavigate();
  const [menuItem, setMenuItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [toast, setToast] = useState({ message: '', type: '' });
  const [updating, setUpdating] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [toggling, setToggling] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [editForm, setEditForm] = useState({ foodName: '', price: '', description: '', timeToCook: '' });
  // Remove the separate toggle switch and confirmation popup
  // Integrate toggle into the status pill with animation and confirmation
  const [showConfirmToggle, setShowConfirmToggle] = useState(false);
  const [pendingToggle, setPendingToggle] = useState(false);

  useEffect(() => {
    fetchMenuItem();
    // eslint-disable-next-line
  }, [menuItemId]);

  useEffect(() => {
    if (menuItem) {
      setEditForm({
        foodName: menuItem.foodName || menuItem.name || '',
        price: menuItem.price || '',
        description: menuItem.description || '',
        timeToCook: menuItem.timeToCook || '',
      });
    }
  }, [menuItem]);

  const fetchMenuItem = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`/api/v1/menuItems/${menuItemId}`);
      if (!res.ok) throw new Error('Failed to fetch menu item details');
      const data = await res.json();
      setMenuItem(data);
    } catch (err) {
      setError('Could not load menu item details.');
    }
    setLoading(false);
  };

  // Helper to get all media file URLs
  const getMediaFiles = (item) => {
    if (!item) return [];
    if (Array.isArray(item.mediaFile) && item.mediaFile.length > 0) return item.mediaFile;
    if (Array.isArray(item.MediaFile) && item.MediaFile.length > 0) return item.MediaFile;
    return [];
  };

  const mediaFiles = getMediaFiles(menuItem);

  // Management actions
  const handleToggleAvailability = async () => {
    setPendingToggle(true);
    try {
      const res = await fetch(`/api/v1/menuItems/${menuItemId}/toggle-availability`, { method: 'PATCH' });
      if (!res.ok) throw new Error('Failed to toggle availability');
      setToast({ message: 'Availability updated successfully!', type: 'success' });
      await fetchMenuItem();
    } catch (err) {
      setToast({ message: 'Failed to update availability.', type: 'error' });
    }
    setPendingToggle(false);
    setShowConfirmToggle(false);
    setTimeout(() => setToast({ message: '', type: '' }), 4000);
  };

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete this menu item?')) return;
    setDeleting(true);
    try {
      const res = await fetch(`/api/v1/menuItems/${menuItemId}`, { method: 'DELETE' });
      if (!res.ok) throw new Error('Failed to delete menu item');
      setToast({ message: 'Menu item deleted successfully!', type: 'success' });
      setTimeout(() => navigate(-1), 1200);
    } catch (err) {
      setToast({ message: 'Failed to delete menu item.', type: 'error' });
    }
    setDeleting(false);
    setTimeout(() => setToast({ message: '', type: '' }), 4000);
  };

  // For update, show inline form
  const handleUpdate = () => {
    setEditMode(true);
  };

  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setEditForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleEditSave = async () => {
    setUpdating(true);
    try {
      const res = await fetch(`/api/v1/menuItems/${menuItemId}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          foodName: editForm.foodName,
          price: parseFloat(editForm.price),
          description: editForm.description,
          timeToCook: editForm.timeToCook,
        }),
      });
      if (!res.ok) throw new Error('Failed to update menu item');
      setToast({ message: 'Menu item updated successfully!', type: 'success' });
      setEditMode(false);
      await fetchMenuItem();
    } catch (err) {
      setToast({ message: 'Failed to update menu item.', type: 'error' });
    }
    setUpdating(false);
    setTimeout(() => setToast({ message: '', type: '' }), 4000);
  };

  const handleEditCancel = () => {
    setEditMode(false);
    setEditForm({
      foodName: menuItem.foodName || menuItem.name || '',
      price: menuItem.price || '',
      description: menuItem.description || '',
      timeToCook: menuItem.timeToCook || '',
    });
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-300 flex flex-col">
      {/* Toast Popup */}
      {toast.message && (
        <div className={`fixed top-8 left-1/2 transform -translate-x-1/2 z-50 px-6 py-3 rounded shadow-lg text-white font-semibold transition-all duration-300 ${toast.type === 'success' ? 'bg-green-600' : toast.type === 'error' ? 'bg-red-600' : 'bg-blue-600'} dark:bg-opacity-90`}
        >
          {toast.message}
        </div>
      )}
      <div className="flex items-center justify-between px-8 pt-8 mb-4">
        <button
          className="flex items-center gap-2 px-4 py-2 rounded-full bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-700 shadow hover:bg-blue-100 dark:hover:bg-blue-900 text-blue-600 dark:text-blue-300 text-lg font-semibold focus:outline-none focus:ring-2 focus:ring-blue-400 transition"
          onClick={() => navigate(-1)}
          aria-label="Back"
          title="Back"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" /></svg>
          <span className="hidden sm:inline">Back</span>
        </button>
        <h1 className="text-3xl font-bold text-gray-900 dark:text-gray-100 flex-1 text-center">Menu Item Details</h1>
        <div className="w-32 flex justify-end gap-2 items-center">
          <button
            className="px-4 py-2 rounded-md bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200 font-semibold shadow hover:bg-yellow-200 dark:hover:bg-yellow-800 transition text-sm"
            onClick={handleUpdate}
            disabled={updating}
            title="Update (coming soon)"
          >
            Update
          </button>
          <button
            className={`px-4 py-2 rounded-md bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200 font-semibold shadow hover:bg-red-200 dark:hover:bg-red-800 transition text-sm ${deleting ? 'opacity-60' : ''}`}
            onClick={handleDelete}
            disabled={deleting}
            title="Delete"
          >
            Delete
          </button>
        </div>
      </div>
      <div className="flex flex-col md:flex-row gap-10 max-w-5xl w-full mx-auto mt-4 px-8 pb-12">
        {/* Media Section */}
        <div className="flex-1 flex flex-col items-center">
          <div className="w-full max-w-lg">
            <div className="overflow-x-auto flex space-x-4 pb-2">
              {mediaFiles.length > 0 ? (
                mediaFiles.map((url, idx) => (
                  <img
                    key={idx}
                    src={url}
                    alt={`Menu item media ${idx + 1}`}
                    className="rounded-xl object-cover h-72 w-72 min-w-[18rem] border border-gray-200 dark:border-gray-700 shadow-lg"
                  />
                ))
              ) : (
                <img
                  src={DEFAULT_IMG}
                  alt="Default menu item"
                  className="rounded-xl object-contain h-72 w-72 min-w-[18rem] border border-gray-200 dark:border-gray-700 shadow-lg opacity-70"
                />
              )}
            </div>
          </div>
        </div>
        {/* Details Section */}
        <div className="flex-1 flex flex-col justify-center">
          {loading ? (
            <div className="text-center text-gray-700 dark:text-gray-200 text-lg font-medium">Loading...</div>
          ) : error ? (
            <div className="text-center text-red-600 dark:text-red-400 text-lg font-medium">{error}</div>
          ) : menuItem ? (
            <div className="space-y-6">
              <h2 className="text-3xl font-extrabold text-gray-900 dark:text-gray-100 mb-2">
                {editMode ? (
                  <input
                    name="foodName"
                    value={editForm.foodName}
                    onChange={handleEditChange}
                    className="w-full px-3 py-2 rounded border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 text-2xl font-bold mb-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
                  />
                ) : (
                  menuItem.foodName || menuItem.name
                )}
              </h2>
              {editMode ? (
                <textarea
                  name="description"
                  value={editForm.description}
                  onChange={handleEditChange}
                  className="w-full px-3 py-2 rounded border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 text-lg mb-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
                  rows={2}
                />
              ) : (
                <p className="text-gray-700 text-lg dark:text-gray-200 mb-2 italic">{menuItem.description}</p>
              )}
              <div className="flex flex-wrap gap-4 mt-4 items-center">
                {editMode ? (
                  <input
                    name="price"
                    type="number"
                    step="0.01"
                    value={editForm.price}
                    onChange={handleEditChange}
                    className="px-4 py-2 rounded-full border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 font-semibold text-lg shadow w-32 focus:outline-none focus:ring-2 focus:ring-blue-400"
                  />
                ) : (
                  <span className="px-5 py-2 rounded-full bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200 font-semibold text-lg shadow">
                    ₹{menuItem.price}
                  </span>
                )}
                {/* Availability Toggle Switch */}
                <div className="flex items-center gap-2">
                  <Switch
                    checked={!!(menuItem && menuItem.available)}
                    onChange={() => setShowConfirmToggle(true)}
                    className={`${menuItem && menuItem.available ? 'bg-green-500' : 'bg-red-500'} relative inline-flex h-8 w-16 items-center rounded-full transition-colors focus:outline-none border-2 border-gray-300 dark:border-gray-700`}
                    disabled={pendingToggle}
                  >
                    <span className="sr-only">Toggle availability</span>
                    <span
                      className={`inline-block h-6 w-6 transform rounded-full bg-white shadow transition-transform duration-300 ${menuItem && menuItem.available ? 'translate-x-8' : 'translate-x-2'}`}
                    />
                  </Switch>
                  <span className={`text-base font-semibold transition-colors duration-300 ${menuItem && menuItem.available ? 'text-green-700 dark:text-green-200' : 'text-red-700 dark:text-red-200'}`}>{menuItem && menuItem.available ? 'Available' : 'Unavailable'}</span>
                </div>
                {editMode ? (
                  <input
                    name="timeToCook"
                    value={editForm.timeToCook}
                    onChange={handleEditChange}
                    className="px-4 py-2 rounded-full border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 font-semibold text-lg shadow w-40 focus:outline-none focus:ring-2 focus:ring-blue-400"
                  />
                ) : (
                  menuItem.timeToCook && (
                    <span className="px-5 py-2 rounded-full bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200 font-semibold text-lg shadow">
                      {menuItem.timeToCook}
                    </span>
                  )
                )}
              </div>
              <div className="mt-6 space-y-2">
                <p className="text-gray-500 dark:text-gray-400 text-base">ID: {menuItem.id}</p>
                <p className="text-gray-500 dark:text-gray-400 text-base">Canteen ID: {menuItem.canteenId}</p>
              </div>
              {editMode && (
                <div className="flex gap-4 mt-6">
                  <button
                    className="px-6 py-2 rounded-md bg-blue-600 text-white font-semibold shadow hover:bg-blue-700 transition"
                    onClick={handleEditSave}
                    disabled={updating}
                  >
                    Save
                  </button>
                  <button
                    className="px-6 py-2 rounded-md bg-gray-300 text-gray-800 font-semibold shadow hover:bg-gray-400 transition"
                    onClick={handleEditCancel}
                    disabled={updating}
                  >
                    Cancel
                  </button>
                </div>
              )}
            </div>
          ) : null}
        </div>
      </div>
      {/* Confirmation Popup for Toggle */}
      {showConfirmToggle && (
        <div className="fixed inset-0 z-60 flex items-center justify-center bg-black bg-opacity-30">
          <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg p-6 max-w-xs w-full flex flex-col items-center border border-gray-200 dark:border-gray-700">
            <p className="text-lg font-semibold mb-4 text-center text-gray-900 dark:text-gray-100">
              Are you sure you want to {menuItem && menuItem.available ? 'mark this menu item as Unavailable?' : 'mark this menu item as Available?'}
            </p>
            <div className="flex space-x-4">
              <button
                onClick={handleToggleAvailability}
                className={`px-4 py-2 rounded ${menuItem && menuItem.available ? 'bg-red-600 hover:bg-red-700' : 'bg-green-600 hover:bg-green-700'} text-white font-semibold`}
                disabled={pendingToggle}
              >
                Yes, {menuItem && menuItem.available ? 'Mark Unavailable' : 'Mark Available'}
              </button>
              <button
                onClick={() => setShowConfirmToggle(false)}
                className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400 text-gray-800 dark:bg-gray-700 dark:text-gray-100 dark:hover:bg-gray-600 font-semibold"
                disabled={pendingToggle}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MenuItemDetailPage; 