import { User } from '../../types';
import styles from './UserModal.module.css';

interface UserModalProps {
  user: User | null;
  onClose: () => void;
}

const UserModal = ({ user, onClose }: UserModalProps) => {
  if (!user) return null;

  const { lat, lng } = user.address.geo;
  const mapUrl = `https://www.google.com/maps?q=${lat},${lng}`;

  return (
    <>
      <div className={styles.overlay} onClick={onClose} />
      <div 
        className={styles.modal} 
        role="dialog"
        aria-labelledby="user-modal-title"
      >
        <button 
          className={styles.closeButton}
          onClick={onClose}
          aria-label="Close modal"
        >
          ×
        </button>

        <div className={styles.header}>
          <h2 id="user-modal-title">{user.name}</h2>
          <div className={styles.email}>{user.email}</div>
        </div>

        <div className={styles.section}>
          <h3>Address</h3>
          <div className={styles.addressContent}>
            <p>{user.address.street}, Apt. {user.address.suite}</p>
            <p>{user.address.city}, {user.address.zipcode}</p>
          </div>
          <a 
            href={mapUrl}
            target="_blank"
            rel="noopener noreferrer"
            className={styles.mapLink}
          >
            <span className={styles.mapIcon}>📍</span> View on map
          </a>
        </div>

        <div className={styles.section}>
          <h3>Contact</h3>
          <div className={styles.contact}>
            <div className={styles.contactItem}>
              <div className={styles.contactLabel}>Phone:</div>
              <div>{user.phone}</div>
            </div>
            <div className={styles.contactItem}>
              <div className={styles.contactLabel}>Website:</div>
              <a 
                href={`https://${user.website}`}
                target="_blank"
                rel="noopener noreferrer"
                className={styles.websiteLink}
              >
                {user.website}
              </a>
            </div>
          </div>
        </div>

        <div className={styles.section}>
          <h3>Company</h3>
          <div className={styles.company}>
            <div className={styles.companyName}>{user.company.name}</div>
            <div className={styles.catchphrase}>Catchphrase: {user.company.catchPhrase}</div>
            <div className={styles.businessModel}>Business: {user.company.bs}</div>
          </div>
        </div>
      </div>
    </>
  );
};

export default UserModal; 