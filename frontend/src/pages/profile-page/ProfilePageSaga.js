import { addNewUser } from "./ProfilePageActions";
import { call, takeEvery } from "redux-saga/effects";
import { execApiCall } from "../../utils/ApiUtils";
import { addUser } from "../../api/ApiCalls";
import { createErrorToast, createSuccessToast } from "../../models/ToastModel";

export function* profilePageSagaWatcher() {
  yield takeEvery(addNewUser, sagaAddNewUser);
}

function* sagaAddNewUser(action) {
  yield call(execApiCall, {
    mainCall: () => addUser(action.payload),
    onSuccess() {
      createSuccessToast(`Пользователь успешно добавлен`);
    },
    onAnyError() {
      createErrorToast(`Такой пользователь уже существует`);
    },
  });
}
