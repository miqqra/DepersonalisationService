import { getAccessToken, getRefreshToken, getUserRole } from "./Cookie";
import { apiAddress } from "./BackendSettings";
import { store } from "../store/Store";

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

const isDepersonalised = () => {
  const isDepersonalised = store.getState().databasePage.isDepersonalised;
  return isDepersonalised ? "/depersonalised" : "";
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

export async function getUsers() {
  const role = getUserRole();
  return fetch(apiAddress + `/${role}/users`, {
    method: "GET",
    headers: getAccessTokenHeader(),
  }).then((r) => r.json().then((data) => ({ status: r.status, data: data })));
}

export async function getDepersonalisedUsers() {
  const role = getUserRole();
  return fetch(apiAddress + `/depersonalised/${role}/users`, {
    method: "GET",
    headers: getAccessTokenHeader(),
  }).then((r) => r.json().then((data) => ({ status: r.status, data: data })));
}

export async function updatePeople(credentials) {
  return fetch(apiAddress + `/root/updatePeople`, {
    method: "POST",
    headers: getAccessTokenHeader(),
    body: JSON.stringify(credentials),
  }).then((r) => ({ status: r.status }));
}

export async function downloadXLSX() {
  return fetch(apiAddress + "/root/downloadFile?fileType=xlsx", {
    method: "GET",
    headers: getAccessTokenHeader(),
  }).then((r) =>
    r.arrayBuffer().then((data) => ({ status: r.status, data: data }))
  );
}

export async function updateDepersonalised() {
  const role = getUserRole();
  return fetch(apiAddress + `/${role}/updated`, {
    method: "GET",
    headers: getAccessTokenHeader(),
  }).then((r) => ({ status: r.status }));
}

export async function uploadSearchedUsers(param) {
  const role = getUserRole();
  return fetch(
    apiAddress + `${isDepersonalised()}/${role}/find?param=${param}`,
    {
      method: "GET",
      headers: getAccessTokenHeader(),
    }
  ).then((r) => r.json().then((data) => ({ status: r.status, data: data })));
}
