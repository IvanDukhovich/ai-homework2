# Front - User Management Application

A responsive React application that displays and manages user data from an external API. The application provides a professional user interface with a table-like layout and modal interaction for user details.

## Technologies Used

- React 18 with TypeScript
- Vite for fast development and building
- CSS Modules for scoped styling
- Axios for API requests
- Vitest and React Testing Library for testing

## Features

- **User List Display:** Clean table-like layout showing user information in rows
- **User Detail Modal:** Detailed user information in a modal with smooth animations
- **User Management:** Delete users from the list (client-side only)
- **Responsive Design:** Works on all screen sizes from mobile to desktop
- **Accessibility:** Built with accessibility in mind

## Installation and Setup

1. Clone the repository
   ```
   git clone <repository-url>
   cd front
   ```

2. Install dependencies
   ```
   npm install
   ```

3. Start the development server
   ```
   npm run dev
   ```

4. Build for production
   ```
   npm run build
   ```

5. Run tests
   ```
   npm test
   ```

6. Run tests in watch mode
   ```
   npm run test:watch
   ```

7. Run tests with coverage report
   ```
   npm run test:coverage
   ```

## Project Structure

```
front/
├── public/            # Public assets
├── src/
│   ├── api/           # API service functions
│   │   └── __tests__/ # API tests
│   ├── components/    # React components
│   │   ├── UserTable/ # User table component and styles
│   │   │   └── __tests__/ # UserTable tests
│   │   └── UserModal/ # User modal component and styles
│   │       └── __tests__/ # UserModal tests
│   ├── styles/        # Global styles
│   ├── test/          # Test utilities and setup
│   ├── types/         # TypeScript type definitions
│   ├── __tests__/     # App-level integration tests
│   ├── App.tsx        # Main App component
│   └── main.tsx       # Entry point
├── index.html         # HTML template
├── package.json       # Dependencies and scripts
├── tsconfig.json      # TypeScript configuration
└── vite.config.ts     # Vite configuration
```

## API Integration

The application fetches user data from the JSONPlaceholder API:
```
https://jsonplaceholder.typicode.com/users
```

## Usage

- View all users in the table
- Click on a user row to see detailed information in a modal
- Click on the website link to visit the user's website
- Click on "View on map" in the modal to see the user's location on Google Maps
- Click the "×" button to delete a user from the list

## Development

### Adding New Features

1. Create new components in the `src/components` directory
2. Add corresponding CSS modules
3. Import and use components in the App or other components
4. Write tests for the new components

### Testing

The application uses Vitest with React Testing Library for testing:

- **Unit Tests**: Individual components and functions are tested in isolation
- **Integration Tests**: Testing component interactions and data flow
- **Test Structure**: Following the Arrange-Act-Assert pattern
- **Mocking**: External dependencies like API calls are mocked

Test files are located next to the components they test in `__tests__` directories.

### TypeScript Types

All types are defined in `src/types/index.ts` and are used throughout the application:

```typescript
export interface User {
  id: number;
  name: string;
  username: string;
  email: string;
  address: Address;
  phone: string;
  website: string;
  company: Company;
}
```

## Design Principles

- Clean, modern interface with proper spacing
- Responsive design that works on all devices
- Accessibility built-in
- Smooth transitions and animations
- Visual feedback for user interactions 