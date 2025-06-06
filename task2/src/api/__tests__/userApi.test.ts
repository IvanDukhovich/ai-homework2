import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from 'axios';
import { fetchUsers } from '../userApi';
import { User } from '../../types';

// Mock axios
vi.mock('axios');
const mockedAxios = axios as unknown as {
  get: vi.Mock;
};

describe('userApi', () => {
  const mockUsers: User[] = [
    {
      id: 1,
      name: 'John Doe',
      username: 'johndoe',
      email: 'john@example.com',
      phone: '123-456-7890',
      website: 'johndoe.com',
      address: {
        street: 'Main St',
        suite: 'Apt 123',
        city: 'New York',
        zipcode: '10001',
        geo: { lat: '40.7128', lng: '-74.0060' }
      },
      company: {
        name: 'ABC Corp',
        catchPhrase: 'Just do it',
        bs: 'innovative solutions'
      }
    }
  ];

  beforeEach(() => {
    vi.resetAllMocks();
  });

  it('should fetch users successfully', async () => {
    // Setup
    mockedAxios.get.mockResolvedValueOnce({ data: mockUsers });
    
    // Execute
    const result = await fetchUsers();
    
    // Verify
    expect(axios.get).toHaveBeenCalledWith('https://jsonplaceholder.typicode.com/users');
    expect(result).toEqual(mockUsers);
  });

  it('should throw error when API call fails', async () => {
    // Setup
    mockedAxios.get.mockRejectedValueOnce(new Error('API error'));
    
    // Execute and Verify
    await expect(fetchUsers()).rejects.toThrow('API error');
    expect(axios.get).toHaveBeenCalledWith('https://jsonplaceholder.typicode.com/users');
  });
}); 