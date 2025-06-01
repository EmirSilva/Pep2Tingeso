import httpClient from "../http-common";

const getAll = () => {
  return httpClient.get("/reservations/");
};

const get = (id) => {
  return httpClient.get(`/reservations/${id}`);
};

const create = (reservationData) => {
  return httpClient.post("/reservations/", reservationData);
};

const calculateTotalPrice = (reservationData) => {
  return httpClient.post("/reservations/calculate-total-price", reservationData);
};

const update = (reservationData) => {
  return httpClient.put("/reservations/", reservationData);
}

const cancel = (id) => {
  return httpClient.delete(`/reservations/${id}`);
};

const getReporteIngresosPorMesTipoTarifa = (params) => {
  return httpClient.get("/reports/ingresos-por-mes-tipo", { params });
};

const getReporteIngresosPorMesNumeroPersonas = (params) => {
  return httpClient.get("/reports/ingresos-personas", { params });
};

const reservationService = {
  getAll,
  get,
  create,
  calculateTotalPrice,
  update,
  cancel,
  getReporteIngresosPorMesTipoTarifa,
  getReporteIngresosPorMesNumeroPersonas,
};

export default reservationService;