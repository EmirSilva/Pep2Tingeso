import axios from "axios";

const kartingBackendServer = import.meta.env.VITE_KARTING_BACKEND_SERVER;
const kartingBackendPort = import.meta.env.VITE_KARTING_BACKEND_PORT;

export default axios.create({
    baseURL: `http://${kartingBackendServer}:${kartingBackendPort}`,
    headers: {
        'Content-Type': 'application/json'
    }
});