import React, { useEffect, useState } from 'react';
import { api, mensagemErro } from './api';
import { Users, Plus } from 'lucide-react';

const formularioInicial = {
    nome: '',
    email: '',
    senha: '',
    perfil: 'FUNCIONARIO'
};

export default function Usuarios() {

    const [usuarios, setUsuarios] = useState([]);
    const [form, setForm] = useState(formularioInicial);
    const [erro, setErro] = useState('');
    const [sucesso, setSucesso] = useState('');
    const [carregando, setCarregando] = useState(false);

    async function listarUsuarios() {

        try {

            const resposta = await api.get('/usuarios');

            setUsuarios(resposta.data);

        } catch (error) {

            setErro(mensagemErro(error));
        }
    }

    useEffect(() => {
        listarUsuarios();
    }, []);

    function alterarCampo(event) {

        const { name, value } = event.target;

        setForm(anterior => ({
            ...anterior,
            [name]: value
        }));
    }

    async function cadastrar(event) {

        event.preventDefault();

        setErro('');
        setSucesso('');
        setCarregando(true);

        try {

            await api.post('/usuarios', form);

            setSucesso('Usuário cadastrado com sucesso!');

            setForm(formularioInicial);

            await listarUsuarios();

        } catch (error) {

            setErro(mensagemErro(error));

        } finally {

            setCarregando(false);
        }
    }

    return (
        <main style={{
            padding: '30px',
            maxWidth: '1200px',
            margin: 'auto'
        }}>

            <div className="cabecalho">

                <div>
                    <div className="eyebrow">
                        ADMINISTRAÇÃO DO HOTEL
                    </div>

                    <h1>Gerenciamento de usuários</h1>

                    <p>
                        Cadastre administradores e funcionários.
                    </p>
                </div>

                <Users size={32} />

            </div>

            {erro && (
                <div className="alerta erro">
                    {erro}
                </div>
            )}

            {sucesso && (
                <div className="alerta ok">
                    {sucesso}
                </div>
            )}

            <div className="painel">

                <div className="painelTopo">
                    <h2>Novo usuário</h2>
                </div>

                <form
                    className="formulario"
                    onSubmit={cadastrar}
                >

                    <label className="campo">
                        <span>Nome completo</span>

                        <input
                            name="nome"
                            value={form.nome}
                            onChange={alterarCampo}
                            required
                        />
                    </label>

                    <label className="campo">
                        <span>E-mail</span>

                        <input
                            type="email"
                            name="email"
                            value={form.email}
                            onChange={alterarCampo}
                            required
                        />
                    </label>

                    <label className="campo">
                        <span>Senha</span>

                        <input
                            type="password"
                            name="senha"
                            value={form.senha}
                            onChange={alterarCampo}
                            minLength={8}
                            maxLength={72}
                            autoComplete="new-password"
                            required
                        />
                    </label>

                    <label className="campo">
                        <span>Perfil de acesso</span>

                        <select
                            name="perfil"
                            value={form.perfil}
                            onChange={alterarCampo}
                        >
                            <option value="FUNCIONARIO">
                                Funcionário
                            </option>

                            <option value="ADMIN">
                                Administrador
                            </option>
                        </select>

                    </label>

                    <button
                        type="submit"
                        className="botao primario"
                        disabled={carregando}
                    >
                        <Plus size={18} />

                        {carregando
                            ? 'Cadastrando...'
                            : 'Cadastrar usuário'}
                    </button>

                </form>

            </div>

            <div
                className="painel"
                style={{ marginTop: '25px' }}
            >

                <div className="painelTopo">

                    <div>
                        <h2>Usuários cadastrados</h2>
                        <p>{usuarios.length} usuário(s)</p>
                    </div>

                </div>

                <div className="tabelaWrap">

                    <table>

                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>E-mail</th>
                                <th>Perfil</th>
                                <th>Status</th>
                            </tr>
                        </thead>

                        <tbody>

                            {usuarios.map(usuario => (

                                <tr key={usuario.id}>

                                    <td>#{usuario.id}</td>

                                    <td>{usuario.nome}</td>

                                    <td>{usuario.email}</td>

                                    <td>{usuario.perfil}</td>

                                    <td>
                                        <span className={
                                            'status ' +
                                            (usuario.ativo
                                                ? 'confirmada'
                                                : 'cancelada')
                                        }>
                                            {usuario.ativo
                                                ? 'Ativo'
                                                : 'Inativo'}
                                        </span>
                                    </td>

                                </tr>

                            ))}

                        </tbody>

                    </table>

                </div>

            </div>

        </main>
    );
}