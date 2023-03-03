import Cookies from "js-cookie";

const USER_ROLE = "user_role";
const ACCESS_TOKEN = "access_token";
const REFRESH_TOKEN = "refresh_token";

export const saveUserRole = (roles) => {
  if (roles.includes("ROLE_ROOT")) {
    Cookies.set(USER_ROLE, "root");
  } else if (roles.includes("ROLE_ADMIN")) {
    Cookies.set(USER_ROLE, "admin");
  } else if (roles.includes("ROLE_USER")) {
    Cookies.set(USER_ROLE, "user");
  }
};
export const getUserRole = () => Cookies.get(USER_ROLE);
export const deleteUserRole = () => Cookies.remove(USER_ROLE);
export const saveAccessToken = (token) => Cookies.set(ACCESS_TOKEN, token);
export const getAccessToken = () => Cookies.get(ACCESS_TOKEN);
export const deleteAccessToken = () => Cookies.remove(ACCESS_TOKEN);
export const saveRefreshToken = (token) =>
  Cookies.set(REFRESH_TOKEN, token, { expires: 1 });
export const getRefreshToken = () => Cookies.get(REFRESH_TOKEN);
export const deleteRefreshToken = () => Cookies.remove(REFRESH_TOKEN);
