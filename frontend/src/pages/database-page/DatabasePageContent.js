import {
  MDBBtn,
  MDBInput,
  MDBInputGroup,
  MDBTable,
  MDBTableBody,
  MDBTableHead,
} from "mdb-react-ui-kit";
import styles from "./styles/Database.module.scss";
import { updateUser } from "./DatabasePageSlice";
import UserData from "../../enums/UserData";
import { useDispatch, useSelector } from "react-redux";
import { searchUsers } from "./DatabasePageActions";
import { BiSearch } from "react-icons/bi";
import { useState } from "react";

const COLUMNS = [
  UserData.SURNAME,
  UserData.NAME,
  UserData.PATRONYMIC,
  UserData.AGE,
  UserData.SEX,
  UserData.BIRTHDATE,
  UserData.PASSPORT_SERIES,
  UserData.PASSPORT_NUMBER,
  UserData.WHERE_PASSPORT_WAS_ISSUED,
  UserData.WHEN_PASSPORT_WAS_ISSUED,
  UserData.REGISTRATION,
  UserData.OCCUPATION,
  UserData.TAX_ID_NUMBER,
  UserData.INIPA,
];

const columnsList = (
  <tr className={styles.table_row}>
    <th scope="col">ID</th>
    <th scope="col">Фамилия</th>
    <th scope="col">Имя</th>
    <th scope="col">Отчество</th>
    <th scope="col">Возраст</th>
    <th scope="col">Пол</th>
    <th scope="col">Дата рождения</th>
    <th scope="col">Серия паспорта</th>
    <th scope="col">Номер паспорта</th>
    <th scope="col">Кем выдан</th>
    <th scope="col">Когда выдан</th>
    <th scope="col">Регистрация</th>
    <th scope="col">Работа</th>
    <th scope="col">ИНН</th>
    <th scope="col">СНИЛС</th>
  </tr>
);

export function DatabaseTable() {
  const dispatch = useDispatch();
  const users = useSelector((state) => state.databasePage.users);

  const usersList = users.value.map((user) => {
    return (
      <tr key={user.id}>
        <th scope="row">{user.id}</th>
        {COLUMNS.map((column, index) => {
          // noinspection JSUnresolvedVariable
          return (
            <td
              className={styles.table_row}
              onInput={(event) =>
                dispatch(
                  updateUser({
                    id: user.id,
                    key: column,
                    value: event.target.innerText,
                  })
                )
              }
              contentEditable={true}
              suppressContentEditableWarning={true}
              key={index}
            >
              {user[column]}
            </td>
          );
        })}
      </tr>
    );
  });
  return (
    <MDBTable responsive align={"middle"} small hover>
      <MDBTableHead dark>{columnsList}</MDBTableHead>
      <MDBTableBody>{usersList}</MDBTableBody>
    </MDBTable>
  );
}

export function DatabaseTableSearch() {
  const dispatch = useDispatch();
  const [searchQuery, setSearchQuery] = useState("");
  return (
    <MDBInputGroup>
      <MDBInput
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
        wrapperStyle={{ flexGrow: "2" }}
        label={"Поиск"}
      ></MDBInput>
      <MDBBtn
        onClick={() => dispatch(searchUsers(searchQuery || null))}
        color={"dark"}
        outline
      >
        <BiSearch size={"17"}></BiSearch>
      </MDBBtn>
    </MDBInputGroup>
  );
}
