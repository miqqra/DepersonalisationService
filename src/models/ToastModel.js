import { toast } from "react-toastify";

export const createErrorToast = (message) => {
  return toast.error(message);
};

export const createInfoToast = (message) => {
  return toast.info(message);
};

export const createSuccessToast = (message) => {
  return toast.success(message);
};

export const createWarningToast = (message) => {
  return toast.warning(message);
};
