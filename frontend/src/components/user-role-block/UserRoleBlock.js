import { getUserRole } from "../../api/Cookie";

function UserRoleBlock({ role, children }) {
  const userRole = getUserRole();
  return <>{userRole === role ? children : <></>}</>;
}

export default UserRoleBlock;
