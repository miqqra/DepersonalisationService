import { createAction } from "@reduxjs/toolkit";

export const authorizeUser = createAction("user/authorizeUser");
export const logoutUser = createAction("user/logoutUser");
export const addNewUser = createAction("user/addNewUser", function (data) {
  if (data.roles === "root") {
    return {
      payload: { ...data, roles: ["ROLE_USER", "ROLE_ROOT", "ROLE_ADMIN"] },
    };
  } else if (data.roles === "admin") {
    return { payload: { ...data, roles: ["ROLE_USER", "ROLE_ADMIN"] } };
  } else {
    return { payload: { ...data, roles: ["ROLE_USER"] } };
  }
});
