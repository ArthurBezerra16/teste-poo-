import React, { useState } from "react";
import { Hotel, Mail, Lock, Eye, EyeOff } from "lucide-react";
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

            const resposta = await fetch("/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email,
                    senha
                })
            });

            if (resposta.status === 401) {
                throw new Error("E-mail ou senha inválidos.");
            }

            if (!resposta.ok) {
                throw new Error("Não foi possível realizar o login.");
            }

            const usuario = await resposta.json();

            onLogin(usuario);

        } catch (error) {

            setErro(error.message);

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
                            : "Entrar no sistema"
                        }
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
