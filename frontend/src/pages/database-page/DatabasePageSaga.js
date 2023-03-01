import { call, put, takeEvery } from "redux-saga/effects";
import { execApiCall } from "../../utils/ApiUtils";
import { getUsersRoot, updatePeople } from "../../api/ApiCalls";
import { createErrorToast, createSuccessToast } from "../../models/ToastModel";
import { getUsers, synchronizeUsers } from "./DatabasePageActions";
import { updateItems } from "./DatabasePageSlice";

export function* databasePageSagaWatcher() {
  yield takeEvery(getUsers, sagaGetUsers);
  yield takeEvery(synchronizeUsers, sagaSynchronizeUsers);
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

function* sagaSynchronizeUsers(action) {
  yield call(execApiCall, {
    mainCall: () => updatePeople(action.payload),
    onSuccess() {
      createSuccessToast(`Синхронизация данных прошла успешно`);
    },
    onAnyError() {
      createErrorToast(`Что-то пошло не так. Повторите попытку`);
    },
  });
}
