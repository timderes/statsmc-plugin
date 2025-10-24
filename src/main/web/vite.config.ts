import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  build: {
    emptyOutDir: true,
    // This path must match the resource path!
    // @See pom.xml -> ressource web
    outDir: "../resources/web",
  },
  root: __dirname,
  plugins: [react()],
});
