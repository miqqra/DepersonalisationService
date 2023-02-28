import { all } from "redux-saga/effects";
import { loginPageSagaWatcher } from "../pages/login-page/LoginPageSaga";
import { databasePageSagaWatcher } from "../pages/database-page/DatabasePageSaga";

function getSagas() {
  return [loginPageSagaWatcher(), databasePageSagaWatcher()];
}

export default function* rootSaga() {
  yield all(getSagas().filter((saga) => !!saga));
}
