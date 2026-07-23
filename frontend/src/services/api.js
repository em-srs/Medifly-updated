// ═══════════════════════════════════════════════════════════
// MediFly API Service Client
// Connects React SPA to Spring Boot 3.3.5 Backend (Port 5000)
// ═══════════════════════════════════════════════════════════

const API_BASE_URL = 'http://localhost:5000/api/v1';

async function fetchWithAuth(url, options = {}) {
  const token = localStorage.getItem('medifly_jwt_token');
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
    ...options.headers,
  };

  const response = await fetch(url, { ...options, headers });
  
  if (response.status === 401) {
    // Clear invalid token if unauthenticated
    localStorage.removeItem('medifly_jwt_token');
  }

  return response;
}

// ── Auth Services ──
export async function loginApi(email, password) {
  const res = await fetch(`${API_BASE_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  });
  if (!res.ok) {
    const errorData = await res.json().catch(() => ({}));
    throw new Error(errorData.message || 'Login failed');
  }
  return res.json();
}

export async function registerApi(name, email, phone, password, role = 'user') {
  const res = await fetch(`${API_BASE_URL}/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, email, phone, password, role }),
  });
  if (!res.ok) {
    const errorData = await res.json().catch(() => ({}));
    throw new Error(errorData.message || 'Registration failed');
  }
  return res.json();
}

export async function fetchProfileApi() {
  const res = await fetchWithAuth(`${API_BASE_URL}/auth/profile`);
  if (!res.ok) throw new Error('Failed to fetch profile');
  return res.json();
}

// ── Medicine & Salt Search Services ──
export async function searchMedicinesApi(query = '') {
  const res = await fetch(`${API_BASE_URL}/medicines?search=${encodeURIComponent(query)}`);
  if (!res.ok) throw new Error('Failed to fetch medicines');
  return res.json();
}

export async function fetchMedicineByIdApi(id) {
  const res = await fetch(`${API_BASE_URL}/medicines/${id}`);
  if (!res.ok) throw new Error('Medicine not found');
  return res.json();
}

export async function compareSaltsApi(medicineId) {
  const res = await fetch(`${API_BASE_URL}/medicines/salt-comparison/${medicineId}`);
  if (!res.ok) throw new Error('Failed to compare salts');
  return res.json();
}

// ── Order Services ──
export async function createOrderApi(orderData) {
  const res = await fetchWithAuth(`${API_BASE_URL}/orders`, {
    method: 'POST',
    body: JSON.stringify(orderData),
  });
  if (!res.ok) throw new Error('Failed to create order');
  return res.json();
}

export async function fetchUserOrdersApi() {
  const res = await fetchWithAuth(`${API_BASE_URL}/orders`);
  if (!res.ok) throw new Error('Failed to fetch orders');
  return res.json();
}
