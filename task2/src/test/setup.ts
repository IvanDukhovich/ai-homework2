import '@testing-library/jest-dom';
import { afterEach, vi } from 'vitest';

// Mock window.matchMedia
window.matchMedia = window.matchMedia || function() {
  return {
    matches: false,
    addListener: () => {},
    removeListener: () => {}
  };
};

// Reset mocks after each test
afterEach(() => {
  vi.restoreAllMocks();
}); 