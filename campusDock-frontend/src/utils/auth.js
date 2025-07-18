// utils/auth.js
export const getCurrentRole = () => {
  // Change this manually to switch between roles
  return "SUPER_ADMIN"; // or "CANTEEN_ADMIN"
};

export const setRole = (role) => {
  // For demo purposes - in real app this would come from JWT token
  localStorage.setItem('userRole', role);
};

export const getStoredRole = () => {
  return localStorage.getItem('userRole') || "SUPER_ADMIN";
};

export function getCurrentUserRole() {
  return localStorage.getItem('userRole') || 'superadmin';
}

export function setCurrentUserRole(role) {
  localStorage.setItem('userRole', role);
}