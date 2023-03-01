import styles from "./styles/Database.module.scss";
import DBEditor from "./DBEditor";

function DatabasePage() {
  return (
    <div className={styles.root}>
      <DBEditor />
    </div>
  );
}

export default DatabasePage;
