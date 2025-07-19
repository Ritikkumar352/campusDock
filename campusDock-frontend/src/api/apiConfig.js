// apiConfig.js
export const BASE_URL = "/api/v1";

export const endpoints = {
  colleges: `${BASE_URL}/colleges`,
  canteens: (collegeId) => `${BASE_URL}/colleges/${collegeId}/canteens`,
  canteenById: (canteenId) => `${BASE_URL}/colleges/canteens/${canteenId}`,
  menuItems: (canteenId) => `${BASE_URL}/menuItems/canteens/${canteenId}`,
  menuItemById: (id) => `${BASE_URL}/menuItems/${id}`,
};

async function safeJson(res) {
  const contentType = res.headers.get('content-type');
  if (contentType && contentType.includes('application/json')) {
    return await res.json();
  } else {
    return null;
  }
}

export async function fetchColleges() {
  const res = await fetch(`${BASE_URL}/colleges`);
  if (!res.ok) {
    let msg = 'Unknown error';
    try {
      const err = await safeJson(res);
      msg = err?.message || JSON.stringify(err);
    } catch {}
    throw new Error(msg);
  }
  return await safeJson(res);
}

export async function fetchCanteens(collegeId) {
  const res = await fetch(`${BASE_URL}/colleges/${collegeId}/canteens`);
  if (!res.ok) {
    let msg = 'Unknown error';
    try {
      const err = await safeJson(res);
      msg = err?.message || JSON.stringify(err);
    } catch {}
    throw new Error(msg);
  }
  return await safeJson(res);
}

export async function fetchUsers() {
  const res = await fetch(`${BASE_URL}/users`);
  if (!res.ok) {
    let msg = 'Unknown error';
    try {
      const err = await safeJson(res);
      msg = err?.message || JSON.stringify(err);
    } catch {}
    throw new Error(msg);
  }
  return await safeJson(res);
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
      let msg = 'Unknown error';
      try {
        const err = await safeJson(response);
        msg = err?.message || JSON.stringify(err);
      } catch {}
      throw new Error(msg);
    }
    return await safeJson(response);
  } catch (error) {
    console.error('API call failed:', error);
    throw error;
  }
};