import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import UserModal from '../UserModal';
import { User } from '../../../types';

const mockUser: User = {
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
};

describe('UserModal', () => {
  it('renders user details when user is provided', () => {
    // Setup
    const onClose = vi.fn();
    
    // Execute
    render(<UserModal user={mockUser} onClose={onClose} />);
    
    // Verify user information is displayed
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('john@example.com')).toBeInTheDocument();
    expect(screen.getByText('Main St, Apt. Apt 123')).toBeInTheDocument();
    expect(screen.getByText('New York, 10001')).toBeInTheDocument();
    expect(screen.getByText('123-456-7890')).toBeInTheDocument();
    expect(screen.getByText('johndoe.com')).toBeInTheDocument();
    expect(screen.getByText('ABC Corp')).toBeInTheDocument();
    expect(screen.getByText('Catchphrase: Just do it')).toBeInTheDocument();
    expect(screen.getByText('Business: innovative solutions')).toBeInTheDocument();
    
    // Verify the map link
    const mapLink = screen.getByText('View on map');
    expect(mapLink).toBeInTheDocument();
    expect(mapLink.closest('a')).toHaveAttribute('href', 'https://www.google.com/maps?q=40.7128,-74.0060');
  });

  it('calls onClose when close button is clicked', () => {
    // Setup
    const onClose = vi.fn();
    
    // Execute
    render(<UserModal user={mockUser} onClose={onClose} />);
    
    // Click close button
    fireEvent.click(screen.getByLabelText('Close modal'));
    
    // Verify
    expect(onClose).toHaveBeenCalled();
  });

  it('calls onClose when overlay is clicked', () => {
    // Setup
    const onClose = vi.fn();
    
    // Execute
    const { container } = render(<UserModal user={mockUser} onClose={onClose} />);
    
    // Get the overlay div and click it
    const overlay = container.firstChild;
    if (overlay) {
      fireEvent.click(overlay);
    }
    
    // Verify
    expect(onClose).toHaveBeenCalled();
  });

  it('renders nothing when user is null', () => {
    // Setup
    const onClose = vi.fn();
    
    // Execute
    const { container } = render(<UserModal user={null} onClose={onClose} />);
    
    // Verify
    expect(container.firstChild).toBeNull();
  });
}); 