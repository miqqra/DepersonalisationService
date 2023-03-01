import { appAddress } from "../api/BackendSettings";

export const redirect = (path) =>
  (window.location.href = `${appAddress}/${path}`);

export const downloadFile = (data, fileName) => {
  //Make file URL
  const blob = new Blob([data], { type: "application/octet-stream" });
  const url = URL.createObjectURL(blob);

  //Make a new link
  const anchor = document.createElement("a");
  anchor.href = url;
  anchor.download = fileName;

  // Append to the DOM
  document.body.appendChild(anchor);

  // Trigger `click` event
  anchor.click();

  // Remove element from DOM
  document.body.removeChild(anchor);

  //Release the object URL
  URL.revokeObjectURL(url);
};
