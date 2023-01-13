import "./Register.css";

import InputField from "../../components/InputField/InputField";
import SubmitButton from "../../components/SubmitButton/SubmitButton";

import { useNavigate } from "react-router-dom";

const Register = () => {
    const navigate = useNavigate();

    const handleRedirect = () => {
        navigate("/login");
    };

    // TODO: Error handling and api call

    return (
        <div className="card login-container">
            <form method="post" acceptCharset="utf-8">
                <div className="card-content">
                    <span className="card-title center">
                        First time? Welcome!
                    </span>
                    <InputField text="Email" type="email" />
                    <InputField text="Username" />
                    <InputField text="Password" type="password" />
                    <InputField text="Confirm Password" type="password" />
                    <SubmitButton text="Register" />
                </div>
            </form>
            <h6 className="center">
                Already have an account?&nbsp;
                <a onClick={handleRedirect}>Login here!</a>
            </h6>
        </div>
    );
};

export default Register;
