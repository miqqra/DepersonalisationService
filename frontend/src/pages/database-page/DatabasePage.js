import {
  MDBBtn,
  MDBDropdown,
  MDBDropdownItem,
  MDBDropdownMenu,
  MDBDropdownToggle,
  MDBSwitch,
} from "mdb-react-ui-kit";
import { useDispatch, useSelector } from "react-redux";
import {
  depersonaliseUsers,
  downloadFileType,
  synchronizeUsers,
  uploadDepersonalisedUsers,
  uploadFileType,
  uploadUsers,
} from "./DatabasePageActions";
import styles from "./styles/Database.module.scss";
import LoadingStateBlock from "../../components/loading-state-block/LoadingStateBlock";
import { getUserRole } from "../../api/Cookie";
import { BiCloudUpload, BiDownload, BiUser } from "react-icons/bi";
import { logoutUser } from "../login-page/LoginPageActions";
import { redirect } from "../../utils/BrowserUtils";
import { paths } from "../../routePaths";
import { useRef } from "react";
import LoadingState from "../../enums/LoadingState";
import { store } from "../../store/Store";
import { DatabaseTable, DatabaseTableSearch } from "./DatabasePageContent";

function DatabasePage() {
  const dispatch = useDispatch();
  const users = useSelector((state) => state.databasePage.users);
  const isDepersonalised = useSelector(
    (state) => state.databasePage.isDepersonalised
  );
  const userRole = getUserRole();

  function SelectFileButton() {
    const fileInput = useRef();
    const selectFile = (event) => {
      event.preventDefault();
      fileInput.current?.click();
    };

    return (
      <div>
        <input
          type="file"
          style={{ display: "none" }}
          ref={fileInput}
          onChange={(e) => {
            dispatch(uploadFileType(e?.target.files[0]));
          }}
        />
        <MDBBtn onClick={selectFile} size={"sm"} outline color={"dark"}>
          <BiCloudUpload size={"20"} />
        </MDBBtn>
      </div>
    );
  }

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
          <SelectFileButton></SelectFileButton>
          <MDBDropdown className="btn-group">
            <MDBBtn
              onClick={() => dispatch(downloadFileType("xlsx" || null))}
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
                onClick={() => dispatch(downloadFileType("xlsx" || null))}
                link
              >
                XLSX
              </MDBDropdownItem>
              <MDBDropdownItem
                onClick={() => dispatch(downloadFileType("csv" || null))}
                link
              >
                CSV
              </MDBDropdownItem>
              <MDBDropdownItem
                onClick={() => dispatch(downloadFileType("json" || null))}
                link
              >
                JSON
              </MDBDropdownItem>
            </MDBDropdownMenu>
          </MDBDropdown>
          {store.getState().databasePage.users.loading ===
          LoadingState.LOADED ? (
            <MDBSwitch
              label={isDepersonalised ? "Деперсонализованная" : "Исходная"}
              onChange={(event) => {
                event.target.checked
                  ? dispatch(depersonaliseUsers)
                  : dispatch(uploadUsers);
              }}
              checked={isDepersonalised}
              disabled={userRole === "user"}
            />
          ) : (
            <></>
          )}
        </div>
        <div className={styles.user_menu}>
          <MDBDropdown>
            <MDBDropdownToggle size={"sm"} color={"dark"} outline>
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
              <DatabaseTableSearch />
              <DatabaseTable />
            </div>
          </LoadingStateBlock>
        </div>
      </div>
    </div>
  );
}

export default DatabasePage;
