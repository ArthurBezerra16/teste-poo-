import React, { useState } from "react";
import { Hotel, Mail, Lock, Eye, EyeOff } from "lucide-react";
import { api, mensagemErro } from "./api";
import "./login.css";

export default function Login({ onLogin }) {

    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [mostrarSenha, setMostrarSenha] = useState(false);
    const [erro, setErro] = useState("");
    const [carregando, setCarregando] = useState(false);

    async function entrar(event) {

        event.preventDefault();
        setErro("");
        setCarregando(true);

        try {

            const resposta = await api.post("/auth/login", {
                email: email.trim().toLowerCase(),
                senha: senha
            });

            const usuario = resposta.data;

            if (!usuario?.token) {
                throw new Error(
                    "O backend não retornou um token válido."
                );
            }

            console.log("Login realizado com sucesso.");

            onLogin(usuario);

        } catch (error) {

            const status = error.response?.status;

            console.error("Status do login:", status);

            if (status === 401) {

                setErro("E-mail ou senha inválidos.");

            } else {

                setErro(
                    status
                        ? `HTTP ${status}: ${mensagemErro(error)}`
                        : mensagemErro(error)
                );
            }

        } finally {

            setCarregando(false);
        }
    }

    return (
        <div className="login-page">

            <div className="login-container">

                <div className="login-logo">

                    <div className="login-logo-icon">
                        <Hotel size={30} />
                    </div>

                    <div>
                        <h2>Hotelaria</h2>
                        <span>PAINEL DE GESTÃO</span>
                    </div>

                </div>

                <form className="login-card" onSubmit={entrar}>

                    <h1>Bem-vindo!</h1>

                    <p>
                        Entre com suas credenciais para
                        acessar o sistema.
                    </p>

                    {erro && (
                        <div className="login-erro">
                            {erro}
                        </div>
                    )}

                    <label>E-mail</label>

                    <div className="login-input">

                        <Mail size={19} />

                        <input
                            type="email"
                            placeholder="Seu e-mail"
                            value={email}
                            onChange={e => setEmail(e.target.value)}
                            autoComplete="username"
                            required
                        />

                    </div>

                    <label>Senha</label>

                    <div className="login-input">

                        <Lock size={19} />

                        <input
                            type={mostrarSenha ? "text" : "password"}
                            placeholder="Sua senha"
                            value={senha}
                            onChange={e => setSenha(e.target.value)}
                            autoComplete="current-password"
                            required
                        />

                        <button
                            type="button"
                            aria-label="Mostrar ou ocultar senha"
                            onClick={() => setMostrarSenha(!mostrarSenha)}
                        >
                            {mostrarSenha
                                ? <EyeOff size={19} />
                                : <Eye size={19} />
                            }
                        </button>

                    </div>

                    <button
                        className="login-submit"
                        type="submit"
                        disabled={carregando}
                    >
                        {carregando
                            ? "Entrando..."
                            : "Entrar no sistema"}
                    </button>

                    <span className="login-aviso">
                        Acesso exclusivo para usuários autorizados
                    </span>

                </form>

                <footer>
                    Sistema de Gestão de Hotelaria
                </footer>

            </div>

        </div>
    );
}