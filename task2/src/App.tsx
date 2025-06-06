import { useEffect, useState } from 'react';
import { User } from './types';
import { fetchUsers } from './api/userApi';
import UserTable from './components/UserTable/UserTable';
import UserModal from './components/UserModal/UserModal';

const App = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);

  useEffect(() => {
    const getUsers = async () => {
      try {
        setLoading(true);
        setError(null);
        const fetchedUsers = await fetchUsers();
        setUsers(fetchedUsers);
      } catch (err) {
        setUsers([]);
        setError('Failed to fetch users. Please try again later.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    getUsers();
  }, []);

  const handleUserClick = (user: User) => {
    setSelectedUser(user);
  };

  const handleUserDelete = (userId: number) => {
    setUsers(users.filter(user => user.id !== userId));
    if (selectedUser && selectedUser.id === userId) {
      setSelectedUser(null);
    }
  };

  const handleCloseModal = () => {
    setSelectedUser(null);
  };

  return (
    <div className="container">
      <h1 className="heading">Users</h1>
      
      {loading && <p>Loading users...</p>}
      
      {error && !loading && <p style={{ color: 'var(--error-color)' }}>{error}</p>}
      
      {!loading && !error && (
        <UserTable 
          users={users}
          onUserClick={handleUserClick}
          onUserDelete={handleUserDelete}
        />
      )}
      
      <UserModal 
        user={selectedUser}
        onClose={handleCloseModal}
      />
    </div>
  );
};

export default App; 