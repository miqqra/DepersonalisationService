import { call } from "redux-saga/effects";

import { createErrorToast } from "../models/ToastModel";

export function* execApiCall(args) {
  const {
    mainCall,
    onSuccess,
    onFail400,
    onFail403,
    onFailException,
    onAnyError,
    additionalAnyErrorHandling,
    withoutHandlingResponseStatus,
  } = args;

  try {
    const response = yield mainCall().catch((e) => e.response);

    if (withoutHandlingResponseStatus) {
      yield call(onSuccess, response);
    } else if (response.status === 200) {
      yield call(onSuccess, response);
    } else if (response.status === 400) {
      if (onAnyError) {
        yield call(onAnyError);
      } else if (onFail400) {
        yield call(onFail400, response.data);
      } else {
        createErrorToast(``);
        if (additionalAnyErrorHandling) {
          yield call(additionalAnyErrorHandling);
        }
      }
    } else if (response.status === 403) {
      if (selectPriority(onAnyError, onFail403)) {
        yield call(selectPriority(onAnyError, onFail403));
      } else {
        createErrorToast(
          `Возникла ошибка: ${response.data.message}. Повторите запрос`
        );
        if (additionalAnyErrorHandling) {
          yield call(additionalAnyErrorHandling);
        }
      }
    } else if (selectPriority(onAnyError, onFailException)) {
      yield call(selectPriority(onAnyError, onFailException));
    } else {
      createErrorToast(`Возникла неизвестная ошибка`);
      if (additionalAnyErrorHandling) {
        yield call(additionalAnyErrorHandling);
      }
    }
  } catch (e) {
    if (
      !withoutHandlingResponseStatus &&
      selectPriority(onAnyError, onFailException)
    ) {
      yield call(selectPriority(onAnyError, onFailException));
    } else if (!withoutHandlingResponseStatus) {
      createErrorToast(`Возникла неизвестная ошибка`);
      if (additionalAnyErrorHandling) {
        yield call(additionalAnyErrorHandling);
      }
    }
  }
}

function selectPriority(base, priority) {
  return priority ? priority : base;
}
