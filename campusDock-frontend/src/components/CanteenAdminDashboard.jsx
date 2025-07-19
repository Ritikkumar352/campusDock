import React, { useState, useEffect } from 'react';
import { fetchCanteens } from '../api/apiConfig';
import { UtensilsCrossed, Plus, Edit, Trash2 } from 'lucide-react';

const CanteenAdminDashboard = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [canteen, setCanteen] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [itemForm, setItemForm] = useState({
    name: '',
    description: '',
    price: '',
    available: true,
    timeToCook: '15 min',
    file: null,
  });
  const [toast, setToast] = useState({ message: '', type: '' });
  const [selectedMenuItem, setSelectedMenuItem] = useState(null);
  const [menuItemDetails, setMenuItemDetails] = useState(null);
  const [showMenuItemModal, setShowMenuItemModal] = useState(false);

  // Remove hardcoded default and expect collegeId as a prop or from context
  // TODO: Pass collegeId as a prop or get from context/auth
  // Example:
  // const { collegeId } = props;
  // const collegeId = 'YOUR_DEFAULT_COLLEGE_ID'; // Replace with actual or mock value
  // Use canteenId from session if available, otherwise fallback to default
  const fallbackCanteenId = 'cc1c29ab-b955-4b3f-b23f-9a58035ed8c0';
  const canteenId = window.canteenIdFromSession || fallbackCanteenId;

  useEffect(() => {
    // Only fetch menu items for the canteen
    const fetchMenuItems = async () => {
      try {
        const res = await fetch(`/api/v1/menuItems/canteens/${canteenId}`);
        if (!res.ok) throw new Error('Failed to fetch menu items');
        const data = await res.json();
        setMenuItems(Array.isArray(data) ? data : []);
      } catch (error) {
        console.error('Failed to fetch menu items:', error);
        setMenuItems([]);
      }
    };
    fetchMenuItems();
  }, [canteenId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData();
      const menuItemPayload = {
        foodName: itemForm.name,
        price: parseFloat(itemForm.price),
        description: itemForm.description,
        isAvailable: itemForm.available,
        timeToCook: itemForm.timeToCook,
        canteenId: canteenId,
      };
      formData.append('menuItem', new Blob([JSON.stringify(menuItemPayload)], { type: 'application/json' }));
      if (itemForm.file) {
        formData.append('file', itemForm.file);
      }
      const response = await fetch(`/api/v1/menuItems/canteens/${canteenId}`, {
        method: 'POST',
        body: formData,
      });
      if (!response.ok) throw new Error('Failed to add menu item');
      const data = await response.json();
      setToast({ message: 'Menu item added successfully!', type: 'success' });
      setMenuItems([...menuItems, { ...itemForm, id: data['menu Item Id:'] || Date.now(), price: parseFloat(itemForm.price) }]);
      resetForm();
    } catch (error) {
      setToast({ message: 'Failed to add menu item.', type: 'error' });
      console.error('Failed to save menu item:', error);
    }
    setTimeout(() => setToast({ message: '', type: '' }), 4000);
  };

  // Edit menu item (update fields)
  const handleEdit = async (item) => {
    // For now, just open the form for editing (implement PATCH if needed)
    setEditingItem(item);
    setItemForm({
      name: item.foodName || item.name || '',
      description: item.description || '',
      price: item.price ? String(item.price) : '',
      available: item.isAvailable !== undefined ? item.isAvailable : item.available,
      timeToCook: item.timeToCook || '15 min',
      file: null,
    });
    setShowForm(true);
  };

  // Delete menu item
  const handleDelete = async (itemId) => {
    try {
      const response = await fetch(`/api/v1/menuItems/${itemId}`, {
        method: 'DELETE',
      });
      if (!response.ok) throw new Error('Failed to delete menu item');
      setMenuItems(menuItems.filter(item => item.id !== itemId));
      setToast({ message: 'Menu item deleted successfully!', type: 'success' });
    } catch (error) {
      setToast({ message: 'Failed to delete menu item.', type: 'error' });
      console.error('Failed to delete menu item:', error);
    }
    setTimeout(() => setToast({ message: '', type: '' }), 4000);
  };

  // Toggle availability (PATCH)
  const toggleAvailability = async (itemId) => {
    try {
      const response = await fetch(`/api/v1/menuItems/${itemId}/toggle-availability`, {
        method: 'PATCH',
      });
      if (!response.ok) throw new Error('Failed to toggle availability');
      setMenuItems(menuItems.map(item =>
        item.id === itemId ? { ...item, available: !item.available, isAvailable: !item.isAvailable } : item
      ));
      setToast({ message: 'Availability updated successfully!', type: 'success' });
    } catch (error) {
      setToast({ message: 'Failed to update availability.', type: 'error' });
      console.error('Failed to toggle availability:', error);
    }
    setTimeout(() => setToast({ message: '', type: '' }), 4000);
  };

  const resetForm = () => {
    setItemForm({ name: '', description: '', price: '', category: 'main', available: true });
    setShowForm(false);
    setEditingItem(null);
  };

  const getCategoryColor = (category) => {
    const colors = {
      main: 'bg-blue-100 text-blue-800',
      salad: 'bg-green-100 text-green-800',
      beverage: 'bg-purple-100 text-purple-800',
      dessert: 'bg-pink-100 text-pink-800'
    };
    return colors[category] || 'bg-gray-100 text-gray-800';
  };

  // Fetch menu item details by ID
  const fetchMenuItemDetails = async (menuItemId) => {
    try {
      const res = await fetch(`/api/v1/menuItems/${menuItemId}`);
      if (!res.ok) throw new Error('Failed to fetch menu item details');
      const data = await res.json();
      setMenuItemDetails(data);
      setShowMenuItemModal(true);
    } catch (error) {
      setMenuItemDetails(null);
      setShowMenuItemModal(false);
    }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <h1 className="text-3xl font-bold mb-8 text-gray-800 dark:text-gray-100">Canteen Admin Dashboard</h1>
      
      <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg p-6">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold flex items-center text-gray-900 dark:text-gray-100">
            <UtensilsCrossed className="mr-2 text-orange-600" />
            Menu Items
          </h2>
          <button
            onClick={() => setShowForm(true)}
            className="bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 flex items-center"
          >
            <Plus className="w-4 h-4 mr-2" />
            Add Menu Item
          </button>
        </div>

        {showForm && (
          <form onSubmit={handleSubmit} className="mb-6 p-6 bg-gray-50 dark:bg-gray-800 rounded-lg">
            <h3 className="text-lg font-medium mb-4 text-gray-900 dark:text-gray-100">
              {editingItem ? 'Edit Menu Item' : 'Add New Menu Item'}
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <input
                type="text"
                placeholder="Item Name"
                value={itemForm.name}
                onChange={(e) => setItemForm({...itemForm, name: e.target.value})}
                className="px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
                required
              />
              <input
                type="number"
                step="0.01"
                placeholder="Price"
                value={itemForm.price}
                onChange={(e) => setItemForm({...itemForm, price: e.target.value})}
                className="px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
                required
              />
              <input
                type="text"
                placeholder="Time to Cook (e.g. 15 min)"
                value={itemForm.timeToCook}
                onChange={(e) => setItemForm({...itemForm, timeToCook: e.target.value})}
                className="px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
                required
              />
              <div className="flex items-center">
                <input
                  type="checkbox"
                  id="available"
                  checked={itemForm.available}
                  onChange={(e) => setItemForm({...itemForm, available: e.target.checked})}
                  className="mr-2"
                />
                <label htmlFor="available" className="text-gray-900 dark:text-gray-100">Available</label>
              </div>
              <input
                type="file"
                accept="image/*"
                onChange={e => setItemForm({ ...itemForm, file: e.target.files[0] })}
                className="col-span-1 md:col-span-2"
              />
            </div>
            <textarea
              placeholder="Description"
              value={itemForm.description}
              onChange={(e) => setItemForm({...itemForm, description: e.target.value})}
              className="w-full mt-4 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
              rows="3"
            />
            <div className="flex space-x-2 mt-4">
              <button type="submit" className="bg-orange-600 text-white px-4 py-2 rounded-md hover:bg-orange-700">
                {editingItem ? 'Update' : 'Add'} Item
              </button>
              <button 
                type="button" 
                onClick={resetForm}
                className="bg-gray-400 text-white px-4 py-2 rounded-md hover:bg-gray-500"
              >
                Cancel
              </button>
            </div>
          </form>
        )}

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {menuItems.map((item) => (
            <div key={item.id} className="border border-gray-200 dark:border-gray-700 rounded-lg p-4 hover:shadow-md transition-shadow bg-white dark:bg-gray-900 cursor-pointer" onClick={() => fetchMenuItemDetails(item.id)}>
              <div className="flex justify-between items-start mb-2">
                <h3 className="font-medium text-lg text-gray-900 dark:text-gray-100">{item.name || item.foodName}</h3>
                <div className="flex space-x-1">
                  <button
                    onClick={e => { e.stopPropagation(); handleEdit(item); }}
                    className="text-blue-600 hover:text-blue-800"
                  >
                    <Edit className="w-4 h-4" />
                  </button>
                  <button
                    onClick={e => { e.stopPropagation(); handleDelete(item.id); }}
                    className="text-red-600 hover:text-red-800"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
              <p className="text-gray-600 text-sm mb-3 dark:text-gray-300">{item.description}</p>
              <div className="flex justify-between items-center mb-2">
                <span className="text-lg font-semibold text-green-600 dark:text-green-300">₹{item.price}</span>
              </div>
              <div className="flex justify-between items-center">
                <span className={`text-sm ${item.available ? 'text-green-600 dark:text-green-300' : 'text-red-600 dark:text-red-300'}`}>
                  {item.available ? 'Available' : 'Unavailable'}
                </span>
                <button
                  onClick={e => { e.stopPropagation(); toggleAvailability(item.id); }}
                  className={`px-3 py-1 text-xs rounded-full ${
                    item.available 
                      ? 'bg-red-100 text-red-800 hover:bg-red-200 dark:bg-red-900 dark:text-red-200 dark:hover:bg-red-800' 
                      : 'bg-green-100 text-green-800 hover:bg-green-200 dark:bg-green-900 dark:text-green-200 dark:hover:bg-green-800'
                  }`}
                >
                  {item.available ? 'Mark Unavailable' : 'Mark Available'}
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Menu Item Details Modal */}
      {showMenuItemModal && menuItemDetails && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white dark:bg-gray-900 rounded-2xl shadow-2xl p-8 max-w-md w-full relative border border-gray-200 dark:border-gray-700">
            <button className="absolute top-4 right-4 text-3xl text-gray-400 hover:text-gray-700 dark:text-gray-300 dark:hover:text-white" onClick={() => setShowMenuItemModal(false)}>&times;</button>
            <h2 className="text-2xl font-bold mb-2 text-gray-900 dark:text-gray-100">{menuItemDetails.foodName || menuItemDetails.name}</h2>
            <p className="mb-2 text-gray-700 text-lg dark:text-gray-200">{menuItemDetails.description}</p>
            <p className="mb-2 text-gray-500 text-sm dark:text-gray-400">ID: {menuItemDetails.id}</p>
            <p className="mb-2 text-gray-500 text-sm dark:text-gray-400">Canteen ID: {menuItemDetails.canteenId}</p>
            <p className="mb-2 text-gray-500 text-sm dark:text-gray-400">Price: ₹{menuItemDetails.price}</p>
            <p className="mb-2 text-gray-500 text-sm dark:text-gray-400">Available: {menuItemDetails.isAvailable ? 'Yes' : 'No'}</p>
            {menuItemDetails.timeToCook && (
              <p className="mb-2 text-gray-500 text-sm dark:text-gray-400">Time to Cook: {menuItemDetails.timeToCook}</p>
            )}
            {/* Add more fields as needed */}
          </div>
        </div>
      )}

      {/* Toast Popup */}
      {toast.message && (
        <div className={`fixed top-8 left-1/2 transform -translate-x-1/2 z-50 px-6 py-3 rounded shadow-lg text-white font-semibold transition-all duration-300 ${toast.type === 'success' ? 'bg-green-600' : 'bg-red-600'} dark:bg-opacity-90`}
        >
          {toast.message}
        </div>
      )}
    </div>
  );
};

export default CanteenAdminDashboard;