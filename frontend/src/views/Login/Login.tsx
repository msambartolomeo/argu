import "./Login.css";

import { useNavigate } from "react-router-dom";
import InputField from "../../components/InputField/InputField";
import SubmitButton from "../../components/SubmitButton/SubmitButton";

const Login = () => {
    const navigate = useNavigate();

    const handleRedirect = () => {
        navigate("/register");
    };

    return (
        <div className="card login-container">
            <form method="post" acceptCharset="utf-8">
                <div className="card-content container-with-space">
                    <span className="card-title center">
                        Welcome back :&#41;
                    </span>
                    <InputField text="Username" />
                    <InputField text="Password" type="password" />
                    <div className="left">
                        <label>
                            <input
                                type="checkbox"
                                className="filled-in"
                                name="rememberme"
                                id="rememberme"
                            />
                            <span>Remember me</span>
                        </label>
                    </div>
                    <SubmitButton text="Login" />
                </div>
            </form>
            <h6 className="center">
                Don&apos;t have an account?&nbsp;
                <a onClick={handleRedirect}>Register here!</a>
            </h6>
        </div>
    );
};

export default Login;
