import LoadingState from "../../enums/LoadingState";
import { HashLoader } from "react-spinners";

function LoadingStateBlock({ loadingState, children }) {
  let block = null;

  if (loadingState.loading === LoadingState.LOADED) {
    block = children;
  } else if (loadingState.loading === LoadingState.LOADING) {
    block = (
      <>
        <HashLoader loading={true} size={150} color={"#487487"} />
      </>
    );
  } else if (loadingState.loading === LoadingState.ERROR) {
    block = <>{"Server error"}</>;
  }

  return <>{block}</>;
}

export default LoadingStateBlock;
