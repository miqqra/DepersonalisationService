import { createSlice } from "@reduxjs/toolkit";
import { StateWithLoader } from "../../utils/StoreUtils";
import LoadingState from "../../enums/LoadingState";

const initialState = {
  users: new StateWithLoader([], LoadingState.LOADING),
  isDepersonalised: false,
};

export const databasePageSlice = createSlice({
  name: "DatabasePage",
  initialState,
  reducers: {
    updateItems: (state, action) => {
      state.users = new StateWithLoader(action.payload, LoadingState.LOADED);
    },
    updateUser: (state, action) => {
      state.users.value.forEach(function (user) {
        if (user.id === action.payload.id) {
          user[action.payload.key] = action.payload.value;
        }
      });
    },
    setIsLoadingItems: (state) => {
      state.users = new StateWithLoader([], LoadingState.LOADING);
    },
    setErrorLoadingItems: (state) => {
      state.users = new StateWithLoader([], LoadingState.ERROR);
    },
    setIsDepersonalised: (state, action) => {
      state.isDepersonalised = action.payload;
    },
  },
});

export const {
  updateItems,
  updateUser,
  setIsLoadingItems,
  setErrorLoadingItems,
  setIsDepersonalised,
} = databasePageSlice.actions;

export default databasePageSlice.reducer;
