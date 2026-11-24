import React, { useState } from 'react';
import Login from './Login';
import Dashboard from './Dashboard';
import Usuarios from './Usuarios';
import { LogOut, Users, LayoutDashboard } from 'lucide-react';

export default function App() {

    const [usuario, setUsuario] = useState(() => {
        try {
            const token = sessionStorage.getItem('hotelaria_token');

            if (!token) return null;

            return JSON.parse(
                sessionStorage.getItem('hotelaria_usuario')
            );
        } catch {
            return null;
        }
    });

    const [pagina, setPagina] = useState('dashboard');

    function realizarLogin(dados) {

        const { token, ...perfilUsuario } = dados;

        sessionStorage.setItem('hotelaria_token', token);

        sessionStorage.setItem(
            'hotelaria_usuario',
            JSON.stringify(perfilUsuario)
        );

        setUsuario(perfilUsuario);
        setPagina('dashboard');
    }

    function sair() {

        sessionStorage.removeItem('hotelaria_token');
        sessionStorage.removeItem('hotelaria_usuario');

        setUsuario(null);
        setPagina('dashboard');
    }

    if (!usuario) {
        return <Login onLogin={realizarLogin} />;
    }

    return (
        <>
            <div style={{
                background: '#152039',
                color: 'white',
                padding: '12px 24px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                flexWrap: 'wrap',
                gap: '12px'
            }}>

                <div>
                    <strong>Olá, {usuario.nome}!</strong>

                    <div style={{
                        fontSize: '12px',
                        marginTop: '4px'
                    }}>
                        {usuario.perfil === 'ADMIN'
                            ? 'Administrador'
                            : 'Funcionário'}
                    </div>
                </div>

                <div style={{
                    display: 'flex',
                    gap: '10px',
                    flexWrap: 'wrap'
                }}>

                    <button
                        className="botao secundario"
                        onClick={() => setPagina('dashboard')}
                    >
                        <LayoutDashboard size={16} />
                        Painel
                    </button>

                    {usuario.perfil === 'ADMIN' && (
                        <button
                            className="botao secundario"
                            onClick={() => setPagina('usuarios')}
                        >
                            <Users size={16} />
                            Usuários
                        </button>
                    )}

                    <button
                        className="botao primario"
                        onClick={sair}
                    >
                        <LogOut size={16} />
                        Sair
                    </button>

                </div>
            </div>

            {pagina === 'usuarios' &&
             usuario.perfil === 'ADMIN'
                ? <Usuarios />
                : <Dashboard />
            }
        </>
    );
}