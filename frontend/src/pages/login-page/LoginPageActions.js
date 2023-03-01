import { createAction } from "@reduxjs/toolkit";

export const authorizeUser = createAction(
  "user/authorizeUser",
  function authorizeUser(data) {
    return {
      payload: { ...data },
    };
  }
);
