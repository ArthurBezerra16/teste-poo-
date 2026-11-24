export function calcularReserva(
    quartos,
    servicos,
    dataEntrada,
    dataSaida
) {
    let diarias = 0;

    if (dataEntrada && dataSaida) {
        const entrada = new Date(dataEntrada);
        const saida = new Date(dataSaida);

        diarias = Math.max(
            0,
            Math.round(
                (saida - entrada) / (1000 * 60 * 60 * 24)
            )
        );
    }

    const valorDiarias = quartos.reduce(
        (total, quarto) =>
            total + Number(quarto.valorDiaria),
        0
    );

    const valorQuartos = valorDiarias * diarias;

    const valorServicos = servicos.reduce(
        (total, servico) =>
            total + Number(servico.preco),
        0
    );

    const valorTotal = valorQuartos + valorServicos;

    return {
        diarias,
        valorDiarias,
        valorQuartos,
        valorServicos,
        valorTotal
    };
}