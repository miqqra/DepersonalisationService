import styles from "./styles/Profile.module.scss";
import { BiNoEntry, BiUserPlus } from "react-icons/bi";
import { MDBBtn } from "mdb-react-ui-kit";
import { useState } from "react";
import { AddUserModal } from "./ProfilePageContent";
import UserRoleBlock from "../../components/user-role-block/UserRoleBlock";
import RootEmoji from "../../assets/superuser.png";
import AdminEmoji from "../../assets/admin.png";
import UserEmoji from "../../assets/user.png";

function HeaderContent() {
  return (
    <>
      <UserRoleBlock role="user">
        <div className={styles.header_content}>
          <img src={UserEmoji} alt="Пользователь"></img>
          <h2>Пользователь</h2>
        </div>
      </UserRoleBlock>
      <UserRoleBlock role="admin">
        <div className={styles.header_content}>
          <img src={AdminEmoji} alt="Админ"></img>
          <h2>Админ</h2>
        </div>
      </UserRoleBlock>
      <UserRoleBlock role="root">
        <div className={styles.header_content}>
          <img src={RootEmoji} alt="Суперпользователь"></img>
          <h2>Суперпользователь</h2>
        </div>
      </UserRoleBlock>
    </>
  );
}

function ProfilePage() {
  const [addUserModal, setAddUserModal] = useState(false);

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <HeaderContent />
      </div>
      <div className={styles.outlet}>
        <UserRoleBlock role="root">
          <MDBBtn
            className={styles.action_button}
            color={"dark"}
            outline
            onClick={() => setAddUserModal(!addUserModal)}
          >
            <BiUserPlus size={"50"} />
            Добавить пользователя
          </MDBBtn>
          <AddUserModal isActive={addUserModal} setIsActive={setAddUserModal} />
        </UserRoleBlock>
        <UserRoleBlock role="user">
          <MDBBtn
            className={styles.action_button}
            color={"dark"}
            disabled
            onClick={() => setAddUserModal(!addUserModal)}
          >
            <BiNoEntry size={"50"} />
            Нет доступных действий
          </MDBBtn>
        </UserRoleBlock>
      </div>
    </div>
  );
}

export default ProfilePage;
