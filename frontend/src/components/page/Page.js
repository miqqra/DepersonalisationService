import { Outlet } from "react-router-dom";
import Footer from "./Footer";
import TopBar from "./TopBar";
import styles from "./styles/Outlet.module.scss";

function Page(props) {
  return (
    <div className={styles.root}>
      <TopBar />
      <div className={styles.outlet}>
        <Outlet />
      </div>
      <div className={styles.footer}>
        <Footer />
      </div>
    </div>
  );
}

export default Page;
