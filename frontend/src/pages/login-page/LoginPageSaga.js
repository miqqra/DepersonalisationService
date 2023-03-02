import { addNewUser, authorizeUser, logoutUser } from "./LoginPageActions";
import { call, takeEvery } from "redux-saga/effects";
import { execApiCall } from "../../utils/ApiUtils";
import { addUser, login, logout } from "../../api/ApiCalls";
import {
  deleteAccessToken,
  deleteRefreshToken,
  deleteUserRole,
  saveAccessToken,
  saveRefreshToken,
  saveUserRole,
} from "../../api/Cookie";
import { createErrorToast, createSuccessToast } from "../../models/ToastModel";
import { paths } from "../../routePaths";
import { redirect } from "../../utils/BrowserUtils";

export function* loginPageSagaWatcher() {
  yield takeEvery(authorizeUser, sagaLoginUser);
  yield takeEvery(logoutUser, sagaLogoutUser);
  yield takeEvery(addNewUser, sagaAddNewUser);
}

function* sagaLoginUser(action) {
  const data = {
    username: action.payload.username,
    password: action.payload.password,
  };

  yield call(execApiCall, {
    mainCall: () => login(data),
    onSuccess(response) {
      saveUserRole(response.data.roles);
      saveAccessToken(response.data.access_token);
      saveRefreshToken(response.data.refresh_token);
      createSuccessToast(`Вы успешно вошли в аккаунт`);
      redirect(paths.DATABASE);
    },
    onFail403() {
      createErrorToast(`Неверный логин или пароль`);
    },
    onAnyError() {
      createErrorToast(`Ошибка сервера`);
    },
  });
}

function* sagaLogoutUser() {
  yield call(execApiCall, {
    mainCall: () => logout(),
    onSuccess() {
      deleteUserRole();
      deleteAccessToken();
      deleteRefreshToken();
      redirect(paths.LOGIN);
    },
    withoutHandlingResponseStatus: true,
  });
}

function* sagaAddNewUser(action) {
  yield call(execApiCall, {
    mainCall: () => addUser(action.payload),
    onSuccess() {
      createSuccessToast(`Пользователь успешно добавлен`);
    },
    onAnyError() {
      createErrorToast(`Ошибка сервера`);
    },
  });
}
