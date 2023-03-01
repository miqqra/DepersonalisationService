import { createAction } from "@reduxjs/toolkit";

export const getUsers = createAction("database/getUsers");
export const synchronizeUsers = createAction("database/synchronizeUsers");
