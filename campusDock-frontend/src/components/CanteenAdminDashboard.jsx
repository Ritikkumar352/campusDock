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
    category: 'main',
    available: true
  });

  // Remove hardcoded default and expect collegeId as a prop or from context
  // TODO: Pass collegeId as a prop or get from context/auth
  // Example:
  // const { collegeId } = props;
  const collegeId = 'YOUR_DEFAULT_COLLEGE_ID'; // Replace with actual or mock value
  // TODO: Replace with actual canteenId from logged-in owner session
  const fallbackCanteenId = 'cc1c29ab-b955-4b3f-b23f-9a58035ed8c0';
  const canteenId = window.canteenIdFromSession || fallbackCanteenId;

  useEffect(() => {
    // Fetch canteen info (if needed)
    fetchCanteens(collegeId).then((canteens) => {
      // Simulate: canteen admin manages only their own canteen
      const myCanteen = canteens && canteens.length > 0 ? canteens[0] : null;
      setCanteen(myCanteen);
      setMenuItems(myCanteen?.menuItems || []);
    }).catch(console.error);
    // Fetch menu items for the canteen
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
  }, [collegeId, canteenId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData();
      const menuItemPayload = {
        foodName: itemForm.name,
        price: parseFloat(itemForm.price),
        description: itemForm.description,
        isAvailable: itemForm.available,
        timeToCook: '15 min', // TODO: Add field to form if needed
        canteenId: canteenId,
      };
      formData.append('menuItem', new Blob([JSON.stringify(menuItemPayload)], { type: 'application/json' }));
      // If you want to support file upload, add a file input and append here:
      // formData.append('file', selectedFile);
      // For now, skip file if not present
      const response = await fetch(`/api/v1/menuItems/canteens/${canteenId}`, {
        method: 'POST',
        body: formData,
      });
      if (!response.ok) throw new Error('Failed to add menu item');
      // Optionally handle response
      // const data = await response.json();
      // Optionally show toast or update menuItems
      setMenuItems([...menuItems, { ...itemForm, id: Date.now(), price: parseFloat(itemForm.price) }]);
      resetForm();
    } catch (error) {
      console.error('Failed to save menu item:', error);
    }
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setItemForm({
      name: item.name,
      description: item.description,
      price: item.price.toString(),
      category: item.category,
      available: item.available
    });
    setShowForm(true);
  };

  const handleDelete = async (itemId) => {
    if (window.confirm('Are you sure you want to delete this item?')) {
      setMenuItems(menuItems.filter(item => item.id !== itemId));
    }
  };

  const toggleAvailability = async (itemId) => {
    try {
      const response = await fetch(`/api/v1/menuItems/${itemId}/toggle-availability`, {
        method: 'PATCH',
      });
      if (!response.ok) throw new Error('Failed to toggle availability');
      // Optionally, you can get the updated item from the response
      // const updatedItem = await response.json();
      setMenuItems(menuItems.map(item =>
        item.id === itemId ? { ...item, available: !item.available } : item
      ));
    } catch (error) {
      console.error('Failed to toggle availability:', error);
    }
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

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <h1 className="text-3xl font-bold mb-8 text-gray-800">Canteen Admin Dashboard</h1>
      
      <div className="bg-white rounded-lg shadow-lg p-6">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold flex items-center">
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
          <form onSubmit={handleSubmit} className="mb-6 p-6 bg-gray-50 rounded-lg">
            <h3 className="text-lg font-medium mb-4">
              {editingItem ? 'Edit Menu Item' : 'Add New Menu Item'}
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <input
                type="text"
                placeholder="Item Name"
                value={itemForm.name}
                onChange={(e) => setItemForm({...itemForm, name: e.target.value})}
                className="px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
                required
              />
              <input
                type="number"
                step="0.01"
                placeholder="Price"
                value={itemForm.price}
                onChange={(e) => setItemForm({...itemForm, price: e.target.value})}
                className="px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
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
                <label htmlFor="available">Available</label>
              </div>
            </div>
            <textarea
              placeholder="Description"
              value={itemForm.description}
              onChange={(e) => setItemForm({...itemForm, description: e.target.value})}
              className="w-full mt-4 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
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
            <div key={item.id} className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
              <div className="flex justify-between items-start mb-2">
                <h3 className="font-medium text-lg">{item.name}</h3>
                <div className="flex space-x-1">
                  <button
                    onClick={() => handleEdit(item)}
                    className="text-blue-600 hover:text-blue-800"
                  >
                    <Edit className="w-4 h-4" />
                  </button>
                  <button
                    onClick={() => handleDelete(item.id)}
                    className="text-red-600 hover:text-red-800"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
              
              <p className="text-gray-600 text-sm mb-3">{item.description}</p>
              
              <div className="flex justify-between items-center mb-2">
                <span className="text-lg font-semibold text-green-600">₹{item.price}</span>
              </div>
              
              <div className="flex justify-between items-center">
                <span className={`text-sm ${item.available ? 'text-green-600' : 'text-red-600'}`}>
                  {item.available ? 'Available' : 'Unavailable'}
                </span>
                <button
                  onClick={() => toggleAvailability(item.id)}
                  className={`px-3 py-1 text-xs rounded-full ${
                    item.available 
                      ? 'bg-red-100 text-red-800 hover:bg-red-200' 
                      : 'bg-green-100 text-green-800 hover:bg-green-200'
                  }`}
                >
                  {item.available ? 'Mark Unavailable' : 'Mark Available'}
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default CanteenAdminDashboard;