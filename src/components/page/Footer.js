import { AiFillGitlab } from "react-icons/ai";
import { BiBoltCircle } from "react-icons/bi";
import styles from "./styles/Footer.module.scss";

function Footer() {
  return (
    <div className={styles.root}>
      <div className={styles.links}>
        <a href="https://git.codenrock.com/adventure-league/cnrprod-team-27459/depersonalization">
          <AiFillGitlab color="gray" size={35} />
        </a>
      </div>
      <div className={styles.label}>
        <BiBoltCircle color="white" size={70} />
        <h3 style={{ fontSize: "25px", color: "gray" }}>
          Сделано для хакатона Совкомбанка
        </h3>
      </div>
    </div>
  );
}

export default Footer;
