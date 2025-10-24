import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { tanstackRouter } from "@tanstack/router-plugin/vite";

// https://vite.dev/config/
export default defineConfig({
  build: {
    emptyOutDir: true,
    // This path must match the resource path!
    // @See pom.xml -> ressource web
    outDir: "../resources/web",
  },
  root: __dirname,
  // Make sure that '@tanstack/router-plugin' is passed before '@vitejs/plugin-react'
  plugins: [
    tanstackRouter({
      autoCodeSplitting: true,
      target: "react",
    }),
    react(),
  ],
});
