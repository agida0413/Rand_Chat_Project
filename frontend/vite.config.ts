import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import tsconfigPaths from 'vite-tsconfig-paths'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  server: {
    host: true,
    port: 3000
  },
  resolve: {
    alias: [
      { find: '@', replacement: '/src' },
      { find: 'node_modules', replacement: '/node_modules' }
    ]
  }
})
