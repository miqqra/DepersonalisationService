import {apiAddress} from "./BackendSettings";
import {getToken} from "./Cookie";

const getUserTokenHeader = () => {
  const token = getToken();

  return {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    }
}

const getDefaultHeader = () => {
  return {
      'Content-Type': 'application/json',
    }
}

export async function login(credentials) {
  return fetch(apiAddress + "/login", {
    method: "POST",
    headers: getDefaultHeader(),
    body: JSON.stringify(credentials)
  }).then((res) => res.json());
}
