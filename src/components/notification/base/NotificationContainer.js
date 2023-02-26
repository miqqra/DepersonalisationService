import React from "react";
import { ToastContainer } from "react-toastify";

const NotificationContainer = () => {
  return (
    <ToastContainer
      position="bottom-right"
      autoClose={30000}
      hideProgressBar={false}
      newestOnTop={false}
      closeOnClick={true}
      limit={4}
      rtl={false}
      pauseOnFocusLoss={false}
      draggable={true}
      pauseOnHover={false}
    />
  );
};

export default NotificationContainer;
