import { AiFillGitlab } from "react-icons/ai";
import { BiBoltCircle } from "react-icons/bi";

function Footer() {
  return (
    <>
      <>
        <BiBoltCircle color="white" size={100} />
        <h3>Сделано для хакатона Совкомбанка</h3>
      </>
      <AiFillGitlab color="gray" />
    </>
  );
}

export default Footer;
