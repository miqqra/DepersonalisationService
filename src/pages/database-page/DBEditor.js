import { MDBBtn, MDBTable, MDBTableBody, MDBTableHead } from "mdb-react-ui-kit";
import { useDispatch, useSelector } from "react-redux";
import { updateUser } from "./DatabasePageSlice";
import UserData from "../../enums/UserData";
import { getUsers, synchronizeUsers } from "./DatabasePageActions";
import styles from "./styles/Database.module.scss";
import LoadingStateBlock from "../../components/loading-state-block/LoadingStateBlock";

function DBEditor() {
  const dispatch = useDispatch();
  const users = useSelector((state) => state.databasePage.users);

  const columns = [
    UserData.NAME,
    UserData.SURNAME,
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

  const usersList = users.value.map((user) => {
    return (
      <tr key={user.id}>
        <th scope="row">{user.id}</th>
        {columns.map((column, index) => {
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
    <div>
      <div className={styles.fetch_buttons}>
        <MDBBtn onClick={() => dispatch(getUsers)} color={"dark"}>
          Импорт
        </MDBBtn>
        <MDBBtn color={"dark"}>Экспорт</MDBBtn>
        <MDBBtn
          onClick={() => dispatch(synchronizeUsers(users.value))}
          color={"dark"}
        >
          Синхронизация
        </MDBBtn>
      </div>
      <div className={styles}>
        <div>
          <LoadingStateBlock loadingState={users}>
            <MDBTable hover>
              <MDBTableHead>
                <tr>
                  <th scope="col">ID</th>
                  <th scope="col">Имя</th>
                  <th scope="col">Фамилия</th>
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
              </MDBTableHead>
              <MDBTableBody>{usersList}</MDBTableBody>
            </MDBTable>
          </LoadingStateBlock>
          <div>(//TODO sidebar)</div>
        </div>
      </div>
    </div>
  );
}

export default DBEditor;