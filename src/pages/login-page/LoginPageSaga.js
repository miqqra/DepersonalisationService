import { authorizeUser } from "./LoginPageActions";
import { call, takeEvery } from "redux-saga/effects";
import { execApiCall } from "../../utils/ApiUtils";
import { login } from "../../api/ApiCalls";
import { saveToken } from "../../api/Cookie";
import { createErrorToast, createSuccessToast } from "../../models/ToastModel";

export function* loginPageSagaWatcher() {
  yield takeEvery(authorizeUser, sagaLoginUser);
}

function* sagaLoginUser(action) {
  const data = JSON.stringify({
    email: action.payload.email,
    password: action.payload.password,
  });

  yield call(execApiCall, {
    mainCall: () => login(data),
    onSuccess(response) {
      saveToken(response.data.token);
      createSuccessToast(`Вы успешно вошли в аккаунт`);
      //TODO redirect the user
    },
    onAnyError() {
      createErrorToast(`Неверный логин или пароль`);
    },
  });
}
