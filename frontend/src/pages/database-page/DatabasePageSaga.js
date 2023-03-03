import { call, put, takeEvery } from "redux-saga/effects";
import { execApiCall } from "../../utils/ApiUtils";
import {
  downloadSpecificType,
  getDepersonalisedUsers,
  getUsers,
  updateDepersonalised,
  updatePeople, uploadFile,
  uploadSearchedUsers,
} from "../../api/ApiCalls";
import { createErrorToast, createSuccessToast } from "../../models/ToastModel";
import {
  uploadDepersonalisedUsers,
  uploadUsers,
  synchronizeUsers,
  downloadFileType,
  depersonaliseUsers,
  searchUsers,
  uploadFileType
} from "./DatabasePageActions";
import { setIsDepersonalised, updateItems } from "./DatabasePageSlice";
import { store } from "../../store/Store";
import { downloadFile } from "../../utils/BrowserUtils";

export function* databasePageSagaWatcher() {
  yield takeEvery(uploadUsers, sagaGetUsers);
  yield takeEvery(uploadDepersonalisedUsers, sagaGetDepersonalisedUsers);
  yield takeEvery(synchronizeUsers, sagaSynchronizeUsers);
  yield takeEvery(depersonaliseUsers, sagaDepersonaliseUsers);
  yield takeEvery(searchUsers, sagaSearchUsers);
  yield takeEvery(uploadUsers, sagaGetUsers);
  yield takeEvery(uploadDepersonalisedUsers, sagaGetDepersonalisedUsers);
  yield takeEvery(synchronizeUsers, sagaSynchronizeUsers);
  yield takeEvery(downloadFileType, sagaDownloadFile);
  yield takeEvery(uploadFileType, sagaUploadFile)
}

function* sagaGetUsers() {
  yield call(execApiCall, {
    mainCall: () => getUsers(),
    *onSuccess(response) {
      yield put(setIsDepersonalised(false));
      yield put(updateItems(response.data));
    },
    onAnyError() {
      createErrorToast(`Ошибка сервера`);
    },
  });
}

function* sagaGetDepersonalisedUsers() {
  yield call(execApiCall, {
    mainCall: () => getDepersonalisedUsers(),
    *onSuccess(response) {
      yield put(setIsDepersonalised(true));
      yield put(updateItems(response.data));
    },
    onAnyError() {
      createErrorToast(`Что-то пошло не так`);
    },
  });
}

function* sagaSynchronizeUsers() {
  const users = store.getState().databasePage.users.value;
  yield call(execApiCall, {
    mainCall: () => updatePeople(users),
    onSuccess() {
      createSuccessToast(`Синхронизация данных прошла успешно`);
    },
    onAnyError() {
      createErrorToast(`Что-то пошло не так`);
    },
  });
}

function* sagaDownloadFile(action) {
  yield call(execApiCall, {
    mainCall: () => downloadSpecificType(action.payload),
    onSuccess(response) {
      downloadFile(response.data, `data.${action.payload}`);
    },
    onAnyError() {
      createErrorToast(`Не удалось скачать данные`);
    },
  });
}

function* sagaDepersonaliseUsers() {
  yield call(execApiCall, {
    mainCall: () => updateDepersonalised(),
    onSuccess() {
      createSuccessToast(`Данные деперсонализованы`);
    },
    onAnyError() {
      createErrorToast(`Что-то пошло не так`);
    },
  });
}

function* sagaSearchUsers(action) {
  yield call(execApiCall, {
    mainCall: () => uploadSearchedUsers(action.payload),
    *onSuccess(response) {
      yield put(updateItems(response.data));
    },
    onAnyError() {
      createErrorToast(`Ошибка сервера`);
    },
  });
}

function * sagaUploadFile(action) {
  yield call(execApiCall, {
    mainCall: () => uploadFile(action.payload),
    onSuccess() {
      createSuccessToast("Файл успешно загружен на сервер")
    },
    onAnyError() {
      createErrorToast(`Ошибка сервера`);
    },
  });
}
