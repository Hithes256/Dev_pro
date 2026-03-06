// MediCore — shared utilities
// All page logic is self-contained. This file is a shared stub.
function logout() {
  localStorage.clear();
  window.location.href = 'login.html';
}