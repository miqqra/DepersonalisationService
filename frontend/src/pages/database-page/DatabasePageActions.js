import { createAction } from "@reduxjs/toolkit";

export const uploadUsers = createAction("database/uploadUsers");
export const uploadDepersonalisedUsers = createAction(
  "database/uploadDepersonalisedUsers"
);
export const synchronizeUsers = createAction("database/synchronizeUsers");
export const downloadXlsx = createAction("database/downloadXlsx");
export const downloadCsv = createAction("database/downloadCsv");
