import Cookies from 'js-cookie'

const USER_TOKEN = 'token';

export const saveToken = (token) => Cookies.set(USER_TOKEN, token, {expires: 1});
export const getToken = () => Cookies.get(USER_TOKEN);
export const deleteToken = () => Cookies.remove(USER_TOKEN);
