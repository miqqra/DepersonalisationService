import {
  MDBBtn,
  MDBBtnGroup,
  MDBInput,
  MDBModal,
  MDBModalBody,
  MDBModalContent,
  MDBModalDialog,
  MDBModalHeader,
  MDBModalTitle,
  MDBRadio,
  MDBValidation,
  MDBValidationItem,
} from "mdb-react-ui-kit";
import styles from "./styles/Profile.module.scss";
import { addNewUser } from "./ProfilePageActions";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { getUserRole } from "../../api/Cookie";

export function AddUserModal(props) {
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
  const toggleShow = () => props.setIsActive(!props.isActive);
  return (
    <MDBModal show={props.isActive} setShow={props.setIsActive} tabIndex="-1">
      <MDBModalDialog>
        <MDBModalContent>
          <MDBModalHeader>
            <MDBModalTitle>Добавить нового пользователя</MDBModalTitle>
            <MDBBtn
              className="btn-close"
              color="none"
              onClick={toggleShow}
            ></MDBBtn>
          </MDBModalHeader>
          <MDBModalBody>
            <MDBValidation
              className={styles.form}
              onSubmit={() => {
                toggleShow();
                dispatch(addNewUser(formValue || null));
              }}
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
              <MDBBtn type="submit" color="dark">
                Готово
              </MDBBtn>
            </MDBValidation>
          </MDBModalBody>
        </MDBModalContent>
      </MDBModalDialog>
    </MDBModal>
  );
}
