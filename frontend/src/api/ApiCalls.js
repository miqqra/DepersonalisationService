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
  return fetch(apiAddress + "/root/users", {
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

export async function downloadXlsx() {

  const download = (path, filename) => {
    // Create a new link
    const anchor = document.createElement('a');
    anchor.href = path;
    anchor.download = filename;

    // Append to the DOM
    document.body.appendChild(anchor);

    // Trigger `click` event
    anchor.click();

    // Remove element from DOM
    document.body.removeChild(anchor);
  };

  return fetch(apiAddress + "/root/downloadFile?fileType=xlsx", {
    method: "GET",
    headers: getAccessTokenHeader()
  })
      .then(obj => obj.arrayBuffer())
      .then(data => {
        const blob = new Blob([data], {type: "application/octet-stream"});

        const url = URL.createObjectURL(blob);

        // Download file
        download(url, 'lol.xlsx');

        // Release the object URL
        URL.revokeObjectURL(url);
      })
}
