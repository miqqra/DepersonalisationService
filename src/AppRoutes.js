import { Route, Routes } from "react-router-dom";
import { paths } from "./routePaths";
import LoginPage from "./pages/login-page/LoginPage";
import Page from "./components/page/Page";

function AppRoutes() {
  return (
    <>
      <Routes>
        <Route path={paths.INDEX} element={<Page />}>
          <Route index element={<LoginPage />} />
          <Route path={paths.LOGIN} element={<LoginPage />} />
          <Route path={paths.DATABASE} element={undefined} />
          <Route path={paths.NOTFOUND} element={undefined} />
          <Route index element={undefined} />
        </Route>
      </Routes>
    </>
  );
}

export default AppRoutes;
