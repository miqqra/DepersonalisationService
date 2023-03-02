import {
  MDBBtn,
  MDBDropdown,
  MDBDropdownItem,
  MDBDropdownMenu,
  MDBDropdownToggle,
  MDBInput,
  MDBInputGroup,
  MDBSwitch,
  MDBTable,
  MDBTableBody,
  MDBTableHead,
} from "mdb-react-ui-kit";
import { useDispatch, useSelector } from "react-redux";
import { updateUser } from "./DatabasePageSlice";
import UserData from "../../enums/UserData";
import {
  uploadDepersonalisedUsers,
  uploadUsers,
  synchronizeUsers,
  depersonaliseUsers,
  searchUsers,
  downloadFileType,
} from "./DatabasePageActions";
import styles from "./styles/Database.module.scss";
import LoadingStateBlock from "../../components/loading-state-block/LoadingStateBlock";
import { getUserRole } from "../../api/Cookie";
import { useState } from "react";
import { BiDownload, BiSearch, BiUser } from "react-icons/bi";
import { logoutUser } from "../login-page/LoginPageActions";
import { redirect } from "../../utils/BrowserUtils";
import { paths } from "../../routePaths";

function DatabasePageContent() {
  const dispatch = useDispatch();
  const users = useSelector((state) => state.databasePage.users);
  const isDepersonalised = useSelector(
    (state) => state.databasePage.isDepersonalised
  );
  const [searchQuery, setSearchQuery] = useState("");
  const userRole = getUserRole();

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

  const columnsList = (
    <tr className={styles.table_row}>
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
  );
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
    <div className={styles.root}>
      <div className={styles.navbar}>
        <div className={styles.fetch_buttons}>
          <MDBBtn
            onClick={() =>
              userRole === "user"
                ? dispatch(uploadDepersonalisedUsers)
                : dispatch(uploadUsers)
            }
            color={"dark"}
            size={"sm"}
          >
            Импорт
          </MDBBtn>
          <MDBBtn
            onClick={() => dispatch(synchronizeUsers)}
            color={"dark"}
            size={"sm"}
          >
            Экспорт
          </MDBBtn>
          <MDBDropdown className="btn-group">
            <MDBBtn
              onClick={() => dispatch(downloadFileType("xlsx"))}
              size={"sm"}
              outline
              color={"dark"}
            >
              <BiDownload size={"20"} />
            </MDBBtn>
            <MDBDropdownToggle
              size={"sm"}
              outline
              color={"dark"}
            ></MDBDropdownToggle>
            <MDBDropdownMenu>
              <MDBDropdownItem
                onClick={() => dispatch(downloadFileType("xlsx"))}
                link
              >
                XLSX
              </MDBDropdownItem>
              <MDBDropdownItem
                onClick={() => dispatch(downloadFileType("csv"))}
                link
              >
                CSV
              </MDBDropdownItem>
              <MDBDropdownItem
                onClick={() => dispatch(downloadFileType("json"))}
                link
              >
                JSON
              </MDBDropdownItem>
            </MDBDropdownMenu>
          </MDBDropdown>
        </div>
        <div className={styles.user_menu}>
          <MDBDropdown>
            <MDBDropdownToggle color={"dark"} outline>
              <BiUser size={"20"} />
            </MDBDropdownToggle>
            <MDBDropdownMenu appendToBody>
              <MDBDropdownItem onClick={() => redirect(paths.PROFILE)} link>
                Настройки
              </MDBDropdownItem>
              <MDBDropdownItem
                onClick={() => {
                  dispatch(logoutUser);
                }}
                link
              >
                Выйти
              </MDBDropdownItem>
            </MDBDropdownMenu>
          </MDBDropdown>
        </div>
      </div>
      <div className={styles.outlet}>
        <div className={styles.database}>
          <LoadingStateBlock loadingState={users}>
            <div className={styles.table}>
              <MDBInputGroup>
                <MDBInput
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  wrapperStyle={{ flexGrow: "2" }}
                  label={"Поиск"}
                ></MDBInput>
                <MDBBtn
                  onClick={() => dispatch(searchUsers(searchQuery))}
                  color={"dark"}
                  outline
                >
                  <BiSearch size={"17"}></BiSearch>
                </MDBBtn>
              </MDBInputGroup>
              <MDBTable responsive align={"middle"} small hover>
                <MDBTableHead dark>{columnsList}</MDBTableHead>
                <MDBTableBody>{usersList}</MDBTableBody>
              </MDBTable>
            </div>
            <div className={styles.sidebar}>
              <MDBSwitch
                label={isDepersonalised ? "Деперсонализованная" : "Исходная"}
                onClick={() => {
                  isDepersonalised
                    ? dispatch(uploadUsers)
                    : dispatch(uploadDepersonalisedUsers);
                }}
                defaultChecked={userRole === "user"}
                disabled={userRole === "user"}
              />
              <MDBBtn
                onClick={() => dispatch(depersonaliseUsers)}
                disabled={userRole === "user"}
                color={"dark"}
              >
                Деперсонализовать
              </MDBBtn>
            </div>
          </LoadingStateBlock>
        </div>
      </div>
    </div>
  );
}

export default DatabasePageContent;
