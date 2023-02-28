import styles from "./styles/Database.module.scss";
import { useDispatch } from "react-redux";
import { getUsers } from "./DatabasePageActions";
import DBEditor from "./DBEditor";

function DatabasePage() {
  const dispatch = useDispatch();
  dispatch(getUsers);

  return (
    <div className={styles.root}>
      <DBEditor />
    </div>
  );
}

export default DatabasePage;
