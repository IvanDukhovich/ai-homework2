import { useState } from 'react';
import { User } from '../../types';
import styles from './UserTable.module.css';

interface UserTableProps {
  users: User[];
  onUserClick: (user: User) => void;
  onUserDelete: (userId: number) => void;
}

const UserTable = ({ users, onUserClick, onUserDelete }: UserTableProps) => {
  if (!users.length) {
    return <div className={styles.noUsers}>No users found</div>;
  }

  return (
    <div className={styles.tableContainer}>
      <table className={styles.table}>
        <thead>
          <tr>
            <th>NAME / EMAIL</th>
            <th>ADDRESS</th>
            <th>PHONE</th>
            <th>WEBSITE</th>
            <th>COMPANY</th>
            <th>ACTION</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.id} onClick={() => onUserClick(user)}>
              <td className={styles.nameCell}>
                <div className={styles.name}>{user.name}</div>
                <div className={styles.email}>{user.email}</div>
              </td>
              <td>
                {`${user.address.street}, ${user.address.suite}`}
              </td>
              <td>{user.phone}</td>
              <td>
                <a 
                  href={`https://${user.website}`} 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className={styles.websiteLink}
                  onClick={(e) => e.stopPropagation()}
                >
                  {user.website}
                </a>
              </td>
              <td>{user.company.name}</td>
              <td>
                <button 
                  className={styles.deleteButton}
                  onClick={(e) => {
                    e.stopPropagation();
                    onUserDelete(user.id);
                  }}
                  aria-label={`Delete ${user.name}`}
                >
                  ×
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default UserTable; 