import { Outlet } from "react-router-dom";
import Footer from "./Footer";
import TopBar from "./TopBar";

function Page(props) {
  return (
    <>
      <TopBar />
      <Outlet />
      <Footer />
    </>
  );
}

export default Page;
