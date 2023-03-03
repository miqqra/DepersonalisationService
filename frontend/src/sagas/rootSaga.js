import { all } from "redux-saga/effects";
import { loginPageSagaWatcher } from "../pages/login-page/LoginPageSaga";
import { databasePageSagaWatcher } from "../pages/database-page/DatabasePageSaga";
import { profilePageSagaWatcher } from "../pages/profile-page/ProfilePageSaga";

function getSagas() {
  return [
    loginPageSagaWatcher(),
    databasePageSagaWatcher(),
    profilePageSagaWatcher(),
  ];
}

export default function* rootSaga() {
  yield all(getSagas().filter((saga) => !!saga));
}
