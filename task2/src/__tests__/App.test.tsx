import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import axios from 'axios';
import App from '../App';
import { User } from '../types';

// Mock axios
vi.mock('axios');
const mockedAxios = axios as unknown as {
  get: vi.Mock;
};

describe('App', () => {
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
    },
    {
      id: 2,
      name: 'Jane Smith',
      username: 'janesmith',
      email: 'jane@example.com',
      phone: '987-654-3210',
      website: 'janesmith.com',
      address: {
        street: 'Second St',
        suite: 'Suite 456',
        city: 'Los Angeles',
        zipcode: '90001',
        geo: { lat: '34.0522', lng: '-118.2437' }
      },
      company: {
        name: 'XYZ Inc',
        catchPhrase: 'Think different',
        bs: 'cutting-edge technology'
      }
    }
  ];

  beforeEach(() => {
    vi.resetAllMocks();
  });

  it('renders loading state initially', () => {
    // Setup
    mockedAxios.get.mockResolvedValueOnce({ data: mockUsers });
    
    // Execute
    render(<App />);
    
    // Verify
    expect(screen.getByText('Loading users...')).toBeInTheDocument();
  });

  it('renders users after loading', async () => {
    // Setup
    mockedAxios.get.mockResolvedValueOnce({ data: mockUsers });
    
    // Execute
    render(<App />);
    
    // Wait for users to load
    await waitFor(() => {
      expect(screen.queryByText('Loading users...')).not.toBeInTheDocument();
    });
    
    // Verify
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('Jane Smith')).toBeInTheDocument();
  });

  it('shows error message when API call fails', async () => {
    // Setup
    mockedAxios.get.mockRejectedValueOnce(new Error('API error'));
    
    // Execute
    render(<App />);
    
    // Wait for error message
    await waitFor(() => {
      expect(screen.queryByText('Loading users...')).not.toBeInTheDocument();
    });
    
    // Verify
    expect(screen.getByText('Failed to fetch users. Please try again later.')).toBeInTheDocument();
  });

  it('opens modal when a user is clicked', async () => {
    // Setup
    mockedAxios.get.mockResolvedValueOnce({ data: mockUsers });
    
    // Execute
    render(<App />);
    
    // Wait for users to load
    await waitFor(() => {
      expect(screen.queryByText('Loading users...')).not.toBeInTheDocument();
    });
    
    // Click on a user
    fireEvent.click(screen.getByText('John Doe'));
    
    // Verify modal is open
    expect(screen.getByRole('dialog')).toBeInTheDocument();
    expect(screen.getByText('Catchphrase: Just do it')).toBeInTheDocument();
  });

  it('closes modal when close button is clicked', async () => {
    // Setup
    mockedAxios.get.mockResolvedValueOnce({ data: mockUsers });
    
    // Execute
    render(<App />);
    
    // Wait for users to load
    await waitFor(() => {
      expect(screen.queryByText('Loading users...')).not.toBeInTheDocument();
    });
    
    // Open modal
    fireEvent.click(screen.getByText('John Doe'));
    
    // Verify modal is open
    expect(screen.getByRole('dialog')).toBeInTheDocument();
    
    // Close modal
    fireEvent.click(screen.getByLabelText('Close modal'));
    
    // Verify modal is closed
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('deletes user when delete button is clicked', async () => {
    // Setup
    mockedAxios.get.mockResolvedValueOnce({ data: mockUsers });
    
    // Execute
    render(<App />);
    
    // Wait for users to load
    await waitFor(() => {
      expect(screen.queryByText('Loading users...')).not.toBeInTheDocument();
    });
    
    // Verify both users are present
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('Jane Smith')).toBeInTheDocument();
    
    // Delete first user
    fireEvent.click(screen.getByLabelText('Delete John Doe'));
    
    // Verify user is deleted
    expect(screen.queryByText('John Doe')).not.toBeInTheDocument();
    expect(screen.getByText('Jane Smith')).toBeInTheDocument();
  });
}); 