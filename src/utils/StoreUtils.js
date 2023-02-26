import LoadingState from "../enums/LoadingState";

export class StateWithLoader {
  constructor(value, loading) {
    this.value = value;
    this.loading = loading;
  }
}

export const getStateWithLoader = (state) => {
  return new StateWithLoader(state, LoadingState.LOADING);
};
