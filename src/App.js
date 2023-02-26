import { BrowserRouter } from "react-router-dom";
import AppRoutes from "./AppRoutes";
import "mdb-react-ui-kit/dist/css/mdb.min.css";
import "react-toastify/dist/ReactToastify.css";
import NotificationContainer from "./components/notification/base/NotificationContainer";
import React from "react";

function App() {
  return (
    <BrowserRouter>
      <AppRoutes />
      <NotificationContainer />
    </BrowserRouter>
  );
}

export default App;
