import { all } from "redux-saga/effects";
import { loginPageSagaWatcher } from "../pages/login-page/LoginPageSaga";

function getSagas() {
  return [loginPageSagaWatcher()];
}

export default function* rootSaga() {
  yield all(getSagas().filter((saga) => !!saga));
}
