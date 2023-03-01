import { combineReducers } from "redux";
import { databasePageSlice } from "../pages/database-page/DatabasePageSlice";

export const rootReducer = combineReducers({
  databasePage: databasePageSlice.reducer,
});
