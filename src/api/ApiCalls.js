import { getAccessToken, getRefreshToken } from "./Cookie";
import { apiAddress } from "./BackendSettings";

const getAccessTokenHeader = () => {
  const token = getAccessToken();

  return {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };
};

const getDefaultHeader = () => {
  return {
    "Content-Type": "application/json",
  };
};

export async function login(credentials) {
  const header = {
    ...getDefaultHeader(),
    ...credentials,
  };
  return fetch(apiAddress + "/login", {
    method: "POST",
    headers: header,
  }).then((r) => r.json().then((data) => ({ status: r.status, data: data })));
}

export async function updateToken() {
  const header = {
    ...getDefaultHeader(),
    Authorization: `Bearer ${getRefreshToken()}`,
  };
  return fetch(apiAddress + "/accounts/token/refresh", {
    method: "GET",
    headers: header,
  }).then((r) => r.json().then((data) => ({ status: r.status, data: data })));
}

export async function getUsersRoot() {
  return fetch(apiAddress + "/root/updated", {
    method: "GET",
    headers: getAccessTokenHeader(),
  }).then((r) => r.json().then((data) => ({ status: r.status, data: data })));
}
