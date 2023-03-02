import styles from "./styles/Profile.module.scss";
import { BiHappy, BiHappyBeaming, BiMeh } from "react-icons/bi";
import { getUserRole } from "../../api/Cookie";
import {
  MDBBtn,
  MDBBtnGroup,
  MDBInput,
  MDBRadio,
  MDBValidation,
  MDBValidationItem,
} from "mdb-react-ui-kit";
import { useState } from "react";
import { addNewUser } from "../login-page/LoginPageActions";
import { useDispatch } from "react-redux";

function ProfilePage() {
  const dispatch = useDispatch();
  const userRole = getUserRole();
  const [formValue, setFormValue] = useState({
    username: "",
    password: "",
    roles: "user",
  });
  const onChange = (e) => {
    setFormValue({ ...formValue, [e.target.name]: e.target.value });
  };
  const onSubmit = () => {
    dispatch(addNewUser(formValue));
  };

  const HeaderContent = () => {
    if (userRole === "user") {
      return (
        <div className={styles.header_content}>
          <BiMeh size={"100"} />
          <h2>Пользователь</h2>
        </div>
      );
    } else if (userRole === "admin") {
      return (
        <div className={styles.header_content}>
          <BiHappy size={"100"} />
          <h2>Админ</h2>
        </div>
      );
    } else if (userRole === "root") {
      return (
        <div className={styles.header_content}>
          <BiHappyBeaming size={"100"} />
          <h2>Суперпользователь</h2>
        </div>
      );
    }
  };

  return (
    <div className={styles.root}>
      <div className={styles.header}>{<HeaderContent />}</div>
      <div className={styles.outlet}>
        {userRole === "user" ? (
          <h1>У вас нет прав на добавление новых пользователей</h1>
        ) : (
          <MDBValidation
            className={styles.form}
            onSubmit={onSubmit}
            isValidated
          >
            <MDBValidationItem feedback="Введите логин" invalid>
              <MDBInput
                value={formValue.username}
                name="username"
                onChange={onChange}
                id="loginInput"
                required
                label="Логин"
              />
            </MDBValidationItem>
            <MDBValidationItem feedback="Введите пароль" invalid>
              <MDBInput
                value={formValue.password}
                name="password"
                type="password"
                onChange={onChange}
                id="passwordInput"
                required
                label="Пароль"
              />
            </MDBValidationItem>
            <MDBBtnGroup shadow={"0"}>
              <MDBRadio
                name="options"
                wrapperTag="span"
                label="Пользователь"
                onClick={() => setFormValue({ ...formValue, roles: "user" })}
                disabled={userRole === "user"}
                defaultChecked
              />
              <MDBRadio
                name="options"
                wrapperClass="mx-2"
                wrapperTag="span"
                label="Админ"
                disabled={userRole !== "root"}
                onClick={() => setFormValue({ ...formValue, roles: "admin" })}
              />
            </MDBBtnGroup>
            <MDBBtn type="submit" outline color="dark">
              Добавить пользователя
            </MDBBtn>
          </MDBValidation>
        )}
      </div>
    </div>
  );
}

export default ProfilePage;
