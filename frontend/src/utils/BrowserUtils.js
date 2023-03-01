import { appAddress } from "../api/BackendSettings";

export const redirect = (path) =>
  (window.location.href = `${appAddress}/${path}`);
