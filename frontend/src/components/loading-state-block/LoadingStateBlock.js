import LoadingState from "../../enums/LoadingState";
import { HashLoader } from "react-spinners";
import styles from "./styles/LoadingStateBlock.module.scss";

function LoadingStateBlock({ loadingState, children }) {
  let block = null;

  if (loadingState.loading === LoadingState.LOADED) {
    block = children;
  } else if (loadingState.loading === LoadingState.LOADING) {
    block = (
      <div className={styles.root}>
        <HashLoader loading={true} size={75} color={"black"} />
      </div>
    );
  } else if (loadingState.loading === LoadingState.ERROR) {
    block = <>{"Server error"}</>;
  }

  return <>{block}</>;
}

export default LoadingStateBlock;
