import axios from 'axios';

export const api = axios.create({
    baseURL: '/api',
    timeout: 12000
});

api.interceptors.request.use(config => {

    const token = sessionStorage.getItem('hotelaria_token');

    if (token && config.url !== '/auth/login') {

        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

api.interceptors.response.use(
    resposta => resposta,
    erro => {

        if (
            erro.response?.status === 401 &&
            erro.config?.url !== '/auth/login'
        ) {

            sessionStorage.removeItem('hotelaria_token');
            sessionStorage.removeItem('hotelaria_usuario');

            window.location.reload();
        }

        return Promise.reject(erro);
    }
);

export function mensagemErro(erro) {

    const dados = erro.response?.data;

    if (erro.response?.status === 403) {
        return 'Você não possui permissão para esta operação.';
    }

    return (
        (typeof dados === 'string' && dados) ||
        dados?.message ||
        dados?.detail ||
        (erro.response
            ? `Erro HTTP ${erro.response.status}`
            : erro.message || 'Não foi possível conectar ao backend.')
    );
}