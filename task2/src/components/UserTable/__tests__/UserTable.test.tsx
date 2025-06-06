import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import UserTable from '../UserTable';
import { User } from '../../../types';

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

describe('UserTable', () => {
  it('renders user data correctly', () => {
    // Setup
    const onUserClick = vi.fn();
    const onUserDelete = vi.fn();
    
    // Execute
    render(
      <UserTable 
        users={mockUsers} 
        onUserClick={onUserClick} 
        onUserDelete={onUserDelete} 
      />
    );
    
    // Verify
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('john@example.com')).toBeInTheDocument();
    expect(screen.getByText('Main St, Apt 123')).toBeInTheDocument();
    expect(screen.getByText('123-456-7890')).toBeInTheDocument();
    expect(screen.getByText('johndoe.com')).toBeInTheDocument();
    expect(screen.getByText('ABC Corp')).toBeInTheDocument();
  });

  it('calls onUserClick when row is clicked', () => {
    // Setup
    const onUserClick = vi.fn();
    const onUserDelete = vi.fn();
    
    // Execute
    render(
      <UserTable 
        users={mockUsers} 
        onUserClick={onUserClick} 
        onUserDelete={onUserDelete} 
      />
    );
    
    // Click on the row
    fireEvent.click(screen.getByText('John Doe'));
    
    // Verify
    expect(onUserClick).toHaveBeenCalledWith(mockUsers[0]);
  });

  it('calls onUserDelete when delete button is clicked', () => {
    // Setup
    const onUserClick = vi.fn();
    const onUserDelete = vi.fn();
    
    // Execute
    render(
      <UserTable 
        users={mockUsers} 
        onUserClick={onUserClick} 
        onUserDelete={onUserDelete} 
      />
    );
    
    // Click on the delete button
    fireEvent.click(screen.getByLabelText('Delete John Doe'));
    
    // Verify
    expect(onUserDelete).toHaveBeenCalledWith(1);
    // Ensure row click wasn't triggered
    expect(onUserClick).not.toHaveBeenCalled();
  });

  it('displays "No users found" when users array is empty', () => {
    // Setup
    const onUserClick = vi.fn();
    const onUserDelete = vi.fn();
    
    // Execute
    render(
      <UserTable 
        users={[]} 
        onUserClick={onUserClick} 
        onUserDelete={onUserDelete} 
      />
    );
    
    // Verify
    expect(screen.getByText('No users found')).toBeInTheDocument();
  });
}); 