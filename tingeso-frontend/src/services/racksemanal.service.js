import httpClient from "../http-common";

const getAll = () => {
  return httpClient.get("/rack/");
};

const save = (rackData) => {
  return httpClient.post("/rack/", rackData);
};

const getWeeklyAvailability = (startDate, endDate) => {
  return httpClient.get("/rack/availability", {
    params: {
      startDate: startDate,
      endDate: endDate,     
    },
  });
};

const rackSemanalService = {
  getAll,
  save,
  getWeeklyAvailability,
};

export default rackSemanalService;
