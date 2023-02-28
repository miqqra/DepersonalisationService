import axios from "axios";

export const apiAddress = "http://localhost:8080";
export const appAddress = "http://localhost:3000";

export function initAxios() {
  axios.defaults.baseURL = apiAddress;
  axios.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
  axios.defaults.headers.common["Access-Control-Allow-Credentials"] = "true";
}
