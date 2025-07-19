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
  const canteenId = 'YOUR_DEFAULT_CANTEEN_ID'; // Replace with actual or mock value

  useEffect(() => {
    fetchCanteens(collegeId).then((canteens) => {
      // Simulate: canteen admin manages only their own canteen
      const myCanteen = canteens && canteens.length > 0 ? canteens[0] : null;
      setCanteen(myCanteen);
      setMenuItems(myCanteen?.menuItems || []);
    }).catch(console.error);
  }, [collegeId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingItem) {
        // Update item
        setMenuItems(menuItems.map(item => 
          item.id === editingItem.id 
            ? { ...item, ...itemForm, price: parseFloat(itemForm.price) }
            : item
        ));
      } else {
        // Add new item
        const newItem = { 
          id: Date.now(), 
          ...itemForm, 
          price: parseFloat(itemForm.price) 
        };
        setMenuItems([...menuItems, newItem]);
      }
      
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

  const toggleAvailability = (itemId) => {
    setMenuItems(menuItems.map(item => 
      item.id === itemId ? { ...item, available: !item.available } : item
    ));
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
              <select
                value={itemForm.category}
                onChange={(e) => setItemForm({...itemForm, category: e.target.value})}
                className="px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
              >
                <option value="main">Main Course</option>
                <option value="salad">Salad</option>
                <option value="beverage">Beverage</option>
                <option value="dessert">Dessert</option>
              </select>
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
            <div key={item.id} className="border border-gray-200 dark:border-gray-700 rounded-lg p-4 hover:shadow-md transition-shadow bg-white dark:bg-gray-900">
              <div className="flex justify-between items-start mb-2">
                <h3 className="font-medium text-lg text-gray-900 dark:text-gray-100">{item.name}</h3>
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
              <p className="text-gray-600 text-sm mb-3 dark:text-gray-300">{item.description}</p>
              <div className="flex justify-between items-center mb-2">
                <span className="text-lg font-semibold text-green-600 dark:text-green-300">${item.price}</span>
                <span className={`px-2 py-1 text-xs rounded-full ${getCategoryColor(item.category)} dark:bg-opacity-80`}>{item.category}</span>
              </div>
              <div className="flex justify-between items-center">
                <span className={`text-sm ${item.available ? 'text-green-600 dark:text-green-300' : 'text-red-600 dark:text-red-300'}`}>
                  {item.available ? 'Available' : 'Unavailable'}
                </span>
                <button
                  onClick={() => toggleAvailability(item.id)}
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
    </div>
  );
};

export default CanteenAdminDashboard;