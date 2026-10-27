import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "vite";

const page = (name) => fileURLToPath(new URL(`./${name}.html`, import.meta.url));

export default defineConfig({
  server: {
    port: 8091,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        headers: {
          Connection: "keep-alive",
        },
      },
    },
  },
  preview: {
    port: 8091,
  },
  build: {
    rollupOptions: {
      input: {
        index: page("index"),
        product: page("product"),
        cart: page("cart"),
        orders: page("orders"),
        order: page("order"),
        account: page("account"),
        login: page("login"),
      },
    },
  },
});
