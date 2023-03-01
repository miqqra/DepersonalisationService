import { AiFillGitlab, AiFillTrophy } from "react-icons/ai";
import styles from "./styles/Footer.module.scss";

function Footer() {
  return (
    <>
      <div className={styles.links}>
        <a href="https://git.codenrock.com/adventure-league/cnrprod-team-27459/depersonalization">
          <AiFillGitlab color="gray" size={35} />
        </a>
      </div>
      <div className={styles.label}>
        <AiFillTrophy color="white" size={60} />
        <h3 style={{ fontSize: "25px", color: "gray" }}>
          Сделано для хакатона Совкомбанка
        </h3>
      </div>
    </>
  );
}

export default Footer;
