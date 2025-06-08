import httpClient from "../http-common"; // Asegúrate de que la ruta a tu httpClient es correcta

const getAll = () => {
  return httpClient.get("/karts/").then(response => {
    return Array.isArray(response.data) ? response.data : [response.data];
  });
};

const get = (id) => {
  return httpClient.get(`/karts/${id}`).then(response => {
    return Array.isArray(response.data) ? response.data : [response.data];
  });
};

const create = (kartData) => {
  return httpClient.post("/karts/", kartData).then(response => {
    return Array.isArray(response.data) ? response.data : [response.data];
  });
};

const update = (kartData) => {
  return httpClient.put("/karts/", kartData).then(response => {
    return Array.isArray(response.data) ? response.data : [response.data];
  });
};

const remove = (id) => { // Usamos "remove" en lugar de "delete" para consistencia con httpClient
  return httpClient.delete(`/karts/${id}`).then(response => {
    return Array.isArray(response.data) ? response.data : [response.data];
  });
};

const getAvailableKarts = () => {
  return httpClient.get(`/reservations/available`).then(response => {
      return Array.isArray(response.data) ? response.data : [response.data];
  });
};

const kartService = {
  getAll,
  get,
  create,
  update,
  remove,
  getAvailableKarts,
};

export default kartService;

