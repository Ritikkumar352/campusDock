import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://api.campusdock.live:8082',
      '/api/v1': 'http://api.campusdock.live:8082',
    },
  },
});
