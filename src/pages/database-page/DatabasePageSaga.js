import { call, put, takeEvery } from "redux-saga/effects";
import { execApiCall } from "../../utils/ApiUtils";
import { getUsersRoot } from "../../api/ApiCalls";
import { createErrorToast } from "../../models/ToastModel";
import { getUsers } from "./DatabasePageActions";
import { updateItems } from "./DatabasePageSlice";

export function* databasePageSagaWatcher() {
  yield takeEvery(getUsers, sagaGetUsers);
}

function* sagaGetUsers() {
  yield call(execApiCall, {
    mainCall: () => getUsersRoot(),
    *onSuccess(response) {
      yield put(updateItems(response.data));
    },
    onAnyError() {
      createErrorToast(`Ошибка сервера`);
    },
  });
}
