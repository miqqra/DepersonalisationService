import {
  MDBBtn,
  MDBInput,
  MDBValidation,
  MDBValidationItem,
} from "mdb-react-ui-kit";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { authorizeUser } from "./LoginPageActions";
import styles from "./styles/LoginPage.module.scss";

function LoginPage() {
  const dispatch = useDispatch();

  const [formValue, setFormValue] = useState({
    username: "",
    password: "",
  });

  const onChange = (e) => {
    setFormValue({ ...formValue, [e.target.name]: e.target.value });
  };

  const onSubmit = () => {
    dispatch(
      authorizeUser({
        ...formValue,
      })
    );
  };

  return (
    <div className={styles.root}>
      <MDBValidation className={styles.form} onSubmit={onSubmit} isValidated>
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
        <MDBBtn type="submit" outline color="dark">
          Войти
        </MDBBtn>
      </MDBValidation>
    </div>
  );
}

export default LoginPage;
