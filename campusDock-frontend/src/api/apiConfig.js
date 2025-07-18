// apiConfig.js
export const BASE_URL = "http://localhost:8081";

export const endpoints = {
  colleges: `${BASE_URL}/api/v1/colleges`,
  canteens: (collegeId) => `${BASE_URL}/api/v1/colleges/${collegeId}/canteens`,
  canteenById: (canteenId) => `${BASE_URL}/api/v1/colleges/canteens/${canteenId}`,
  menuItems: (canteenId) => `${BASE_URL}/api/v1/menuItems/canteens/${canteenId}`,
  menuItemById: (id) => `${BASE_URL}/api/v1/menuItems/${id}`,
};

const API_BASE = 'http://localhost:8081/api/v1'; // Adjust if needed

export async function fetchColleges() {
  const res = await fetch(`${API_BASE}/colleges`);
  return res.json();
}

export async function fetchCanteens(collegeId) {
  const res = await fetch(`${API_BASE}/colleges/${collegeId}/canteens`);
  return res.json();
}

export async function fetchUsers() {
  const res = await fetch(`${API_BASE}/users`);
  return res.json();
}

// Simple fetch wrapper
export const apiCall = async (url, options = {}) => {
  try {
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    return await response.json();
  } catch (error) {
    console.error('API call failed:', error);
    throw error;
  }
};